package net.eq2online.macros.scripting;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.eq2online.console.Log;
import net.eq2online.macros.compatibility.Reflection;
import net.eq2online.macros.scripting.api.*;

/**
 * Manages loading and registration of script modules and acts as a central repository for some
 * script engine resources.
 * 
 * @author Adam Mummery-Smith
 */
public final class ScriptCore
{
	private static final Map<ScriptContext, ScriptCore> contexts = new HashMap<ScriptContext, ScriptCore>();
	
	/**
	 * Context for this core 
	 */
	private final ScriptContext context;
	
	/**
	 * Active Script Action Provider module
	 */
	private IScriptActionProvider activeProvider;
	
	/**
	 * Active event manager
	 */
	private IMacroEventManager eventManager;
	
	/**
	 * Default script parser
	 */
	private IScriptParser parser;
	
	/**
	 * 
	 */
	private IMessageFilter messageFilter;
	
	/**
	 * Mapping of action names to action providers
	 */
	private final Map<String, IScriptAction> actions = new HashMap<String, IScriptAction>();

	/**
	 * All actions in a list, for speedy iteration
	 */
	private final List<IScriptAction> actionsList = new LinkedList<IScriptAction>();
	
	/**
	 * Iterators
	 */
	private Map<String, Class<? extends IScriptedIterator>> iterators = new HashMap<String, Class<? extends IScriptedIterator>>();

	/**
	 * Regex used to match actions when highlighting, each registered action is added to this regex so that
	 * it can be matched when required by the GUI. Currently only used by the text editor. 
	 */
	private Pattern actionRegex = Pattern.compile("");

	/**
	 * Documentation provider
	 */
	private IDocumentor documentor;
	
	static boolean createCoreForContext(ScriptContext context, IScriptActionProvider provider, IMacroEventManager eventManager, IScriptParser defaultParser, IErrorLogger logger, IDocumentor documentor)
	{
		if (!ScriptCore.contexts.containsKey(context) && !context.isCreated())
		{
			ScriptCore core = new ScriptCore(context, provider, eventManager, defaultParser, logger, documentor);
			ScriptCore.contexts.put(context, core);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Initialise the script core and load the addon modules 
	 * 
	 * @param macrosPath
	 * @param provider
	 */
	private ScriptCore(ScriptContext context, IScriptActionProvider provider, IMacroEventManager eventManager, IScriptParser parser, IErrorLogger logger, IDocumentor documentor)
	{
		this.context = context;
		this.context.setCore(this);
		
		this.registerScriptActionProvider(provider);
		this.eventManager = eventManager;
		this.parser = parser;
		this.documentor = documentor;
	}
	
	/**
	 * Register a new script action with the script core
	 * 
	 * @param action
	 */
	public void registerScriptAction(IScriptAction action)
	{
		try
		{
			this.registerAction(action);
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
		}
	}

	public IScriptAction getAction(String actionName)
	{
		return this.actions.get(actionName.toLowerCase());
	}

	/**
	 * Registration callback for script core
	 * 
	 * @param newAction
	 */
	private boolean registerAction(IScriptAction newAction)
	{
		if (this.actions.containsKey(newAction.toString()))
			return false;
		
		this.actions.put(newAction.toString(), newAction);
		this.actionsList.add(newAction);
		this.documentor.setDocumentation(newAction);
		this.updateScriptActionRegex();
		
		return true;
	}

	/**
	 * Updates the regex used to recognise script actions in the text editor
	 */
	private void updateScriptActionRegex()
	{
		String actionList = "";
		char separator = '(';
		
		for (IScriptAction action : this.actions.values())
		{
			actionList += separator + action.toString();
			separator = '|';
		}
		
		actionList += ")";
		
		this.actionRegex = Pattern.compile(actionList + "([\\(;]|$)", Pattern.CASE_INSENSITIVE);
	}
	
	/**
	 * @return
	 */
	public Map<String, IScriptAction> getActions()
	{
		return Collections.unmodifiableMap(this.actions);
	}
	
	/**
	 * @return
	 */
	public List<IScriptAction> getActionsList()
	{
		return this.actionsList;
	}

	/**
	 * Register a script action provider with the core
	 * 
	 * @param provider
	 */
	public void registerScriptActionProvider(IScriptActionProvider provider)
	{
		this.activeProvider = provider;
	}
	
	/**
	 * Get the currently registered script action provider
	 * 
	 * @return
	 */
	public IScriptActionProvider getScriptActionProvider()
	{
		return this.activeProvider;
	}

	/**
	 * Gets the default script parser
	 * 
	 * @return
	 */
	public IScriptParser getParser()
	{
		return this.parser;
	}

	/**
	 * Get the currently bound documentation provider
	 * 
	 * @return
	 */
	public IDocumentor getDocumentor()
	{
		return this.documentor;
	}
	
	/**
	 * Register a new variable provider with the active script action provider
	 * 
	 * @param variableProvider
	 */
	public void registerVariableProvider(IVariableProvider variableProvider)
	{
		if (this.activeProvider != null)
		{
			this.activeProvider.registerVariableProvider(variableProvider);
		}
	}
	
	/**
	 * Register a new variable listener with the active script action provider
	 * 
	 * @param variableListener
	 */
	public void registerVariableListener(IVariableListener variableListener)
	{
		if (this.activeProvider != null)
		{
			this.activeProvider.registerVariableListener(variableListener);
		}
	}
	
	/**
	 * Set a variable value
	 * 
	 * @param provider
	 * @param macro
	 * @param variableName
	 * @param variableValue
	 */
	public static void setVariable(IScriptActionProvider provider, IMacro macro, String variableName, String variableValue)
	{
		int intValue = ScriptCore.tryParseInt(variableValue, 0);
		boolean boolValue = ScriptCore.parseBoolean(variableValue, intValue);
		provider.setVariable(macro, variableName, variableValue, intValue, boolValue);
	}
	
	/**
	 * @param provider
	 * @param macro
	 * @param variableName
	 * @param variableValue
	 */
	public static void setVariable(IScriptActionProvider provider, IMacro macro, String variableName, int variableValue)
	{
		if (variableName.length() > 0)
		{
			provider.setVariable(macro, variableName, String.valueOf(variableValue), variableValue, variableValue != 0);
		}
	}
	
	/**
	 * Parse variables in the supplied string using the supplied context objects
	 * 
	 * @param provider
	 * @param macro
	 * @param text
	 * @param quoteStrings
	 * @return
	 */
	public static String parseVars(IScriptActionProvider provider, IMacro macro, String text, boolean quoteStrings)
	{
		return new VariableExpander(provider, macro, text, quoteStrings).toString();
	}

	/**
	 * Register a new iterator
	 * 
	 * @param iteratorName
	 * @param iterator
	 */
	public void registerIterator(String iteratorName, Class<? extends IScriptedIterator> iterator)
	{
		if (this.iterators.containsKey(iteratorName))
		{
			return;
//			throw new IllegalArgumentException("Attempted to register a duplicate iterator for key '" + iteratorName + "'");
		}
		
		this.iterators.put(iteratorName, iterator);
	}
	
	/**
	 * Get an iterator by key
	 * 
	 * @param iteratorName
	 * @return
	 */
	public Class<? extends IScriptedIterator> getIterator(String iteratorName)
	{
		return this.iterators.get(iteratorName);
	}
	
	public void registerEventProvider(IMacroEventProvider eventProvider)
	{
		if (this.eventManager != null)
		{
			this.eventManager.registerEventProvider(eventProvider);
		}
	}
	
	public void registerMessageFilter(IMessageFilter messageFilter)
	{
		this.messageFilter = messageFilter;
	}
	
	public IMessageFilter getMessageFilter()
	{
		return this.messageFilter;
	}

	/**
	 * Initialise the script core with available script actions by reflection
	 * 
	 * @param logger
	 */
	@SuppressWarnings("unchecked")
	void initActions(IActionFilter actionFilter, IErrorLogger logger)
	{
		try
		{
			Class<? extends ScriptActionBase> packageClass = (Class<? extends ScriptActionBase>)Class.forName("net.eq2online.macros.scripting.actions.lang.ScriptActionAssign");
			LinkedList<Class<? extends ScriptActionBase>> actions = Reflection.getSubclassesFor(ScriptActionBase.class, packageClass, "ScriptAction", logger);
			int loadedActions = 0;
			
			for (Class<? extends IScriptAction> action : actions)
			{
				try
				{
					IScriptAction newAction = null;
					Constructor<IScriptAction> ctor = (Constructor<IScriptAction>)action.getDeclaredConstructor(ScriptContext.class);
					if (ctor != null)
					{
						newAction = ctor.newInstance(this.context);

						if (newAction != null)
						{
							if (actionFilter.pass(this.context, this, newAction))
							{
								if (this.registerAction(newAction))
									loadedActions++;
							}
						}
					}
				}
				catch (Exception ex) { ex.printStackTrace(); }
			}

			Log.info("Script engine initialised, registered {0} script action(s)", loadedActions);
//			this.documentor.writeXml(new File(MacroModCore.getMacrosDirectory(), "scriptactions.xml"));
			
			if (loadedActions == 0)
			{
				logger.logError("Script engine initialisation error");
			}
		}
		catch (ClassNotFoundException ex)
		{
			logger.logError("Script engine initialisation error, package not found");
		}
	}
	
	/**
	 * @param variableValue
	 * @param intValue
	 * @return
	 */
	public static boolean parseBoolean(String variableValue)
	{
		int intValue = ScriptCore.tryParseInt(variableValue, 0);
		return ScriptCore.parseBoolean(variableValue, intValue);
	}

	/**
	 * @param variableValue
	 * @param intValue
	 * @return
	 */
	public static boolean parseBoolean(String variableValue, int intValue)
	{
		return variableValue == null || variableValue.toLowerCase().equals("true") || intValue != 0;
	}

	/**
	 * Safe function to parse an integer, returns 0 on failure
	 * 
	 * @param value String parameter to parse
	 * @return Parsed value or 0 if parsing failed
	 */
	public static int tryParseInt(String value, int defaultValue)
	{
		if (value == null) return defaultValue;
		try { return Integer.parseInt(value.trim().replaceAll(",", "")); } catch (NumberFormatException ex) {} return defaultValue;
	}
	
	/**
	 * Safe function to parse a long, returns 0 on failure
	 * 
	 * @param value String parameter to parse
	 * @return Parsed value or 0 if parsing failed
	 */
	public static long tryParseLong(String value, long defaultValue)
	{
		try { return Long.parseLong(value.trim()); } catch (NumberFormatException ex) {} return defaultValue;
	}
	
	/**
	 * Safe function to parse a float, returns 0 on failure
	 * 
	 * @param value String parameter to parse
	 * @return Parsed value or 0 if parsing failed
	 */
	public static float tryParseFloat(String value, float defaultValue)
	{
		try { return Float.parseFloat(value.trim()); } catch (NumberFormatException ex) {} return defaultValue;
	}
	
	public static int tryParseIntOffset(String value, int defaultValue, int source)
	{
		int offsetDirection = 1;
		
		if (value.startsWith("+"))
		{
			value = value.substring(1);
		}
		else if (value.startsWith("-"))
		{
			offsetDirection = -1;
			value = value.substring(1);
		}
		else
		{
			source = 0;
		}
		
		return source + (ScriptCore.tryParseInt(value, defaultValue) * offsetDirection);
	}
	
	/**
	 * Tokenise a delimited string into multiple strings
	 * 
	 * @param text
	 * @param separator
	 * @return
	 */
	public static final String[] tokenize(String text, char separator, char firstParamQuote, char otherParamsQuote, char escape, StringBuilder rawString)
	{
		// Array which will contain params as we parse them
		ArrayList<String> params = new ArrayList<String>();
		
		// Current parameter as we build it
		StringBuilder currentParam = new StringBuilder();
		boolean escaped = false;
		boolean quoted = false;
		boolean emptyParam = true;
		char quote = firstParamQuote;
		
		// This looks horrible but is quicker and more reliable than regex for this purpose
		for (int charPos = 0; charPos < text.length(); charPos++)
		{
			char currentChar = text.charAt(charPos);
			
			if (currentChar == escape)
			{
				escaped = true;
			}
			else if (currentChar == quote)
			{
				if (escaped)
				{
					escaped = false;
					currentParam.append(currentChar);
					emptyParam = false;
				}
				else if ((currentParam.length() == 0 || currentParam.toString().matches("^\\s+$")) && !quoted)
				{
					currentParam = new StringBuilder();
					quoted = true;
					emptyParam = false;
				}
				else if (quoted)
				{
					quoted = false;
				}
				else
				{
					currentParam.append(currentChar);
					emptyParam = false;
				}
			}
			else if (currentChar == separator)
			{
				if (escaped)
				{
					escaped = false;
					currentParam.append(currentChar);
					emptyParam = false;
				}
				else if (quoted)
				{
					currentParam.append(currentChar);
					emptyParam = false;
				}
				else
				{
					quote = otherParamsQuote; // follow on params
					params.add(currentParam.toString());
					if (rawString != null) rawString.append(" ").append(currentParam);
					currentParam = new StringBuilder();
					emptyParam = true;
				}
			}
			else
			{
				if (escaped)
				{
					escaped = false;
					currentParam.append(escape);
				}
				
				currentParam.append(currentChar);
				emptyParam = false;
			}
		}
		
		// Append any remaining characters to the params array, if the param ends with a separator then add an empty entry
		if (!emptyParam)
		{
			params.add(currentParam.toString());
			if (rawString != null) rawString.append(" ").append(currentParam);
		}
		else if (text.length() > 0 && text.endsWith(String.valueOf(separator)))
		{
			params.add("");
		}
		
		return params.toArray(new String[0]);
	}

	public String highlight(String text)
	{
		return highlight(text, String.valueOf(((char)-3)), String.valueOf(((char)-4)));
	}
	
	public String highlight(String text, String prefix, String suffix)
	{
		return this.actionRegex.matcher(text).replaceAll(prefix + "$1" + suffix + "$2");
	}
}
