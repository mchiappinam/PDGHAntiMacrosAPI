package net.eq2online.macros.scripting;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.eq2online.macros.scripting.api.IMacroActionContext;
import net.eq2online.macros.scripting.api.IMacroEventManager;
import net.eq2online.macros.scripting.api.IMessageFilter;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptParser;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.api.IVariableProvider;

/**
 *
 * @author Adam Mummery-Smith
 */
public class ScriptContext
{
	private static final Map<String, ScriptContext> contexts = new HashMap<String, ScriptContext>();
	
	public static final ScriptContext MAIN = new ScriptContext("main");
	
	public static final ScriptContext CHATFILTER = new ScriptContext("chatfilter");
	
	/**
	 * The name of this context
	 */
	private final String name;
	
	/**
	 * Registered script core for this context
	 */
	private ScriptCore core;
	
	private Constructor<? extends IMacroActionContext> actionContextCtor;
	
	private IErrorLogger logger;

	private IActionFilter actionFilter;
	
	/**
	 * @param name
	 */
	private ScriptContext(String name)
	{
		if (ScriptContext.contexts.containsKey(name))
		{
			throw new IllegalArgumentException("Context with name \"" + name + "\" already exists, use getContext() instead");
		}
		
		this.name = name;
		
		ScriptContext.contexts.put(this.name, this);
	}
	
	/**
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * @param core
	 */
	void setCore(ScriptCore core)
	{
		this.core = core;
	}
	
	/**
	 * @return
	 */
	public ScriptCore getCore()
	{
		return this.core;
	}
	
	public void setActionContextClass(Class<? extends IMacroActionContext> actionContextClass)
	{
		try
		{
			this.actionContextCtor = actionContextClass.getDeclaredConstructor(ScriptContext.class, IScriptActionProvider.class, IVariableProvider.class);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Get the currently registered script action provider in this context
	 */
	public IScriptActionProvider getScriptActionProvider()
	{
		return this.core.getScriptActionProvider();
	}

	/**
	 * Gets the default script parser
	 * 
	 * @return
	 */
	public IScriptParser getParser()
	{
		return this.core.getParser();
	}

	/**
	 * Get the currently bound documentation provider
	 * 
	 * @return
	 */
	public IDocumentor getDocumentor()
	{
		return this.core.getDocumentor();
	}

	public IMessageFilter getMessageFilter()
	{
		return this.core.getMessageFilter();
	}

	/**
	 * @param iteratorName
	 * @return
	 */
	public Class<? extends IScriptedIterator> getIterator(String iteratorName)
	{
		return this.core.getIterator(iteratorName);
	}
	
	/**
	 * @return
	 */
	public Map<String, IScriptAction> getActions()
	{
		return this.core.getActions();
	}
	
	/**
	 * @return
	 */
	public List<IScriptAction> getActionsList()
	{
		return this.core.getActionsList();
	}
	
	public IScriptAction getAction(String actionName)
	{
		return this.core.getAction(actionName);
	}
	
	public IMacroActionContext createActionContext(IVariableProvider contextVariableProvider)
	{
		try
		{
			return this.actionContextCtor.newInstance(this, this.getScriptActionProvider(), contextVariableProvider);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * @param macrosPath
	 * @param provider
	 * @param eventManager
	 * @param defaultParser
	 * @param logger
	 * @param documentor
	 * @return
	 */
	public boolean create(IScriptActionProvider provider, IMacroEventManager eventManager, IScriptParser defaultParser, IErrorLogger logger, IDocumentor documentor, IActionFilter actionFilter, Class<? extends IMacroActionContext> actionContextClass)
	{
		this.logger = logger;
		this.actionFilter = actionFilter;
		this.setActionContextClass(actionContextClass);
		
		return ScriptCore.createCoreForContext(this, provider, eventManager, defaultParser, logger, documentor);
	}
	
	public void initActions()
	{
		this.core.initActions(this.actionFilter, this.logger);
	}
	
	/**
	 * @return
	 */
	public boolean isCreated()
	{
		return this.core != null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return this == obj || this.name.equals(obj);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	public static ScriptContext getContext(String name)
	{
		if (!ScriptContext.contexts.containsKey(name))
		{
			return new ScriptContext(name);
		}
		
		return ScriptContext.contexts.get(name);
	}

	public static Collection<ScriptContext> getAvailableContexts()
	{
		return Collections.unmodifiableCollection(ScriptContext.contexts.values());
	}
}
