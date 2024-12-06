package net.eq2online.macros.scripting;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.eq2online.macros.scripting.api.IArrayProvider;
import net.eq2online.macros.scripting.api.ICounterProvider;
import net.eq2online.macros.scripting.api.IFlagProvider;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IStringProvider;
import net.eq2online.macros.scripting.api.IVariableProvider;

/**
 * Doesn't actually encapsulate a variable, but is used as part of the script parser to decode a variable
 * in the form of a string and work out what it actually means as far as the engine is concerned. Consumers
 * should call getVariable() or getArrayVariable with appropriate arguments in order to instance this class.
 * 
 * Instances of the class form an abstraction layer between the script action provider and the relevant
 * variable providers which store the variable values.
 * 
 * @author Adam Mummery-Smith
 */
public class Variable
{
	/**
	 * Variable type
	 */
	public enum Type
	{
		Flag,
		Counter,
		String
	}

	public static final String PREFIX_SHARED = "@";
	public static final String PREFIX_STRING = "&";
	public static final String PREFIX_INT = "#";
	public static final String PREFIX_BOOL = "";
	
	public static final String SUFFIX_ARRAY = "[]";
	
	public static final String PREFIX_TYPES = "#\046";
	
	/**
	 * Regex for validating variable names
	 */
	public static final Pattern variableNamePattern = Pattern.compile("^(" + PREFIX_SHARED + "?)([" + PREFIX_TYPES + "]?)([a-z~]([a-z0-9_\\-]*))(\\[([0-9]{1,5})\\])?$", Pattern.CASE_INSENSITIVE);
	
	/**
	 * Matcher for the array index part
	 */
	public static final Pattern arrayVariablePattern = Pattern.compile("\\[([0-9]{1,5})\\]$");
	
	/**
	 * If this variable is an array, this is the reference to the array provider
	 */
	private IArrayProvider arrayProvider;
	
	/**
	 * Reference to the macro  
	 */
	private IMacro macro;
	
	/**
	 * If this variable is shared, this is the shared variable provider 
	 */
	private IVariableProvider variableProvider;
	
	/**
	 * Type of variable
	 */
	public Variable.Type type = Type.Flag;
	
	/**
	 * True if this variable is a shared (global) variable
	 */
	public boolean isShared;
	
	/**
	 * Variable prefix
	 */
	public String prefix;
	
	/**
	 * Name of the variable
	 */
	public String variableName;
	
	/**
	 * Fully qualified name including prefix
	 */
	public String qualifiedName;
	
	/**
	 * If this variable is an array, this is the array index
	 */
	public int arrayOffset = -1;

	private Variable(IVariableProvider variableProvider, List<IArrayProvider> arrayProviders, IMacro macro, String sharedPrefix, String typePrefix, String variableName, String arrayFormat, String arrayIndex, boolean array)
	{
		this.variableProvider = variableProvider;
		this.macro            = macro;
		this.isShared         = sharedPrefix.equals(PREFIX_SHARED);
		this.prefix           = typePrefix;
		this.variableName     = variableName;
		this.qualifiedName    = typePrefix + variableName;
		
		if (this.prefix.equals(PREFIX_INT)) this.type = Type.Counter;
		else if (this.prefix.equals(PREFIX_STRING)) this.type = Type.String;
		
		if (arrayFormat != null || array)
		{
			if (this.isShared)
			{
				if (this.variableProvider instanceof IArrayProvider)
				{
					this.arrayProvider = (IArrayProvider)this.variableProvider;
				}
			}
			else
			{
				this.arrayProvider = this.macro.getArrayProvider();
			}
			
			if (this.arrayProvider == null || !this.arrayProvider.checkArrayExists(this.qualifiedName))
			{
				for (IArrayProvider otherProvider : arrayProviders)
				{
					if (otherProvider.checkArrayExists(this.qualifiedName))
					{
						this.arrayProvider = otherProvider;
						break;
					}
				}
			}
		}

		if (arrayFormat != null && arrayIndex != null)
		{
			this.arrayOffset = Math.max(0, Integer.parseInt(arrayIndex));
		}
		else if (array)
		{
			this.arrayOffset = 0;
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("Variable(%s)[%s] %s",  this.qualifiedName, this.arrayOffset, this.arrayProvider);
	}
	
	/**
	 * Gets whether the variable is an array or scalar
	 */
	public boolean isArray()
	{
		return this.arrayProvider != null && this.arrayOffset > -1;
	}
	
	/**
	 * Get the counter provider for this variable (from context) 
	 */
	public ICounterProvider getCounterProvider()
	{
		return this.isShared ? (this.variableProvider instanceof ICounterProvider ? (ICounterProvider)this.variableProvider : null) : this.macro.getCounterProvider();
	}
	
	/**
	 * Get the flag (boolean) provider for this variable (from context)
	 */
	public IFlagProvider getFlagProvider()
	{
		return this.isShared ? (this.variableProvider instanceof IFlagProvider ? (IFlagProvider)this.variableProvider : null) : this.macro.getFlagProvider();
	}
	
	/**
	 * Get the string provider for this variable (from context)
	 */
	public IStringProvider getStringProvider()
	{
		return this.isShared ? (this.variableProvider instanceof IStringProvider ? (IStringProvider)this.variableProvider : null) : this.macro.getStringProvider();
	}
	
	/**
	 * Get the variable value as a flag
	 */
	public boolean getFlag()
	{
		if (this.type != Variable.Type.Flag) return false;
		IFlagProvider flagProvider = this.getFlagProvider();
		return flagProvider != null ? flagProvider.getFlag(this.variableName, this.arrayOffset) : IFlagProvider.EMPTY;
	}
	
	/**
	 * Get the variable value as a counter (integer)
	 */
	public int getCounter()
	{
		if (this.type != Variable.Type.Counter) return 0;
		ICounterProvider counterProvider = this.getCounterProvider();
		return counterProvider != null ? counterProvider.getCounter(this.variableName, this.arrayOffset) : ICounterProvider.EMPTY;
	}
	
	/**
	 * Get the variable value as a string
	 */
	public String getString()
	{
		if (this.type != Variable.Type.String) return "";
		IStringProvider stringProvider = this.getStringProvider();
		return stringProvider != null ? stringProvider.getString(this.variableName, this.arrayOffset) : IStringProvider.EMPTY;
	}
	
	/**
	 * Set the variable value as a flag
	 */
	public void setFlag(boolean newValue)
	{
		if (this.type == Variable.Type.Flag)
		{
			IFlagProvider flagProvider = this.getFlagProvider();
			if (flagProvider != null) flagProvider.setFlag(this.variableName, this.arrayOffset, newValue);
		}
	}
	
	/**
	 * Set the variable value as a counter
	 */
	public void setCounter(int newValue)
	{
		if (this.type == Variable.Type.Counter)
		{
			ICounterProvider counterProvider = this.getCounterProvider();
			if (counterProvider != null) counterProvider.setCounter(this.variableName, this.arrayOffset, newValue);
		}
	}
	
	/**
	 * Set the variable value as a string
	 */
	public void setString(String newValue)
	{
		if (this.type == Variable.Type.String)
		{
			IStringProvider stringProvider = this.getStringProvider();
			if (stringProvider != null) stringProvider.setString(this.variableName, this.arrayOffset, newValue);
		}
	}

	/**
	 * Unset a flag 
	 */
	public void unSetFlag()
	{
		if (this.type == Variable.Type.Flag)
		{
			IFlagProvider flagProvider = this.getFlagProvider();
			if (flagProvider != null) flagProvider.unsetFlag(this.variableName, this.arrayOffset);
		}
	}

	/**
	 * Unset a counter
	 */
	public void unSetCounter()
	{
		if (this.type == Variable.Type.Counter)
		{
			ICounterProvider counterProvider = this.getCounterProvider();
			if (counterProvider != null) counterProvider.unsetCounter(this.variableName, this.arrayOffset);
		}
	}

	/**
	 * Unset a string
	 */
	public void unSetString()
	{
		if (this.type == Variable.Type.String)
		{
			IStringProvider stringProvider = this.getStringProvider();
			if (stringProvider != null) stringProvider.unsetString(this.variableName, this.arrayOffset);
		}
	}
	
	/**
	 * Gets a variable from the specified name using the supplied providers as context
	 * 
	 * @param variableProvider
	 * @param macro
	 * @param variableName
	 * @return
	 */
	public static Variable getVariable(IVariableProvider variableProvider, List<IArrayProvider> arrayProviders, IMacro macro, String variableName)
	{
		return getVariable(variableProvider, arrayProviders, macro, variableName, false);
	}
	
	/**
	 * Gets an array variable from the specified name using the supplied providers as context
	 * 
	 * @param variableProvider
	 * @param macro
	 * @param variableName
	 * @return
	 */
	public static Variable getArrayVariable(IVariableProvider variableProvider, List<IArrayProvider> arrayProviders, IMacro macro, String variableName)
	{
		return getVariable(variableProvider, arrayProviders, macro, variableName, true);
	}
	
	/**
	 * @param variableProvider
	 * @param macro
	 * @param variableName
	 * @param assumeArray
	 * @return
	 */
	private static Variable getVariable(IVariableProvider variableProvider, List<IArrayProvider> arrayProviders, IMacro macro, String variableName, boolean assumeArray)
	{
		Matcher var = variableNamePattern.matcher(variableName);
		
		if (var.matches())
		{                                                                // shared     // type       // name       // array []   // array index
			return new Variable(variableProvider, arrayProviders, macro, var.group(1), var.group(2), var.group(3), var.group(5), var.group(6), assumeArray);
		}
		else if (assumeArray && Variable.couldBeArraySpecifier(variableName))
		{
			var = variableNamePattern.matcher(getValidVariableOrArraySpecifier(variableName));
			
			if (var.matches())
			{                                                                // shared     // type       // name       // array []   // array index
				return new Variable(variableProvider, arrayProviders, macro, var.group(1), var.group(2), var.group(3), var.group(5), var.group(6), assumeArray);
			}
		}
		
		return null;
	}

	/**
	 * Check whether a variable name is valid
	 * 
	 * @param variableName
	 * @return
	 */
	public static final boolean isValidVariableName(String variableName)
	{
		return variableNamePattern.matcher(variableName).matches();
	}

	/**
	 * Check whether a names is a valid scalar name
	 * 
	 * @param variableName
	 * @return
	 */
	public static final boolean isValidScalarVariableName(String variableName)
	{
		Matcher matcher = variableNamePattern.matcher(variableName);
		return matcher.matches() && matcher.group(5) == null;
	}

	public static boolean isValidVariableOrArraySpecifier(String variableName)
	{
		String name = Variable.getValidVariableOrArraySpecifier(variableName);
		if (name != null) return true;
		
		String expandedVariableName = new VariableExpander(null, null, variableName, false, "var").toString();
		return Variable.getValidVariableOrArraySpecifier(expandedVariableName) != null;
	}

	public static String getValidVariableOrArraySpecifier(String variableName)
	{
		if (isValidVariableName(variableName)) return variableName;
		if (couldBeArraySpecifier(variableName) && Variable.isValidVariableName(variableName.substring(0, variableName.length() - 2)))
		{
			return variableName.substring(0, variableName.length() - 2);
		}
		
		return null;
	}
	
	/**
	 * @param variableName
	 * @return
	 */
	public static boolean couldBeArraySpecifier(String variableName)
	{
		return variableName != null && variableName.endsWith(SUFFIX_ARRAY) && variableName.length() > 2;
	}

	/**
	 * Makes an educated guess whether the specified (already assumed to be valid) variable name could be a string
	 * 
	 * @param variableName
	 * @return
	 */
	public static boolean couldBeString(String variableName)
	{
		if (variableName != null && variableName.startsWith(PREFIX_SHARED)) variableName = variableName.substring(1);
		return variableName != null && variableName.startsWith(PREFIX_STRING);
	}

	/**
	 * @param variableName
	 * @return
	 */
	public static boolean couldBeInt(String variableName)
	{
		if (variableName != null && variableName.startsWith(PREFIX_SHARED)) variableName = variableName.substring(1);
		return variableName != null && variableName.startsWith(PREFIX_INT);
	}

	public static boolean couldBeBoolean(String variableName)
	{
		if (variableName != null && variableName.startsWith(PREFIX_SHARED)) variableName = variableName.substring(1);
		return variableName != null && variableName.matches("^[a-z]([a-z0-9_\\-]*)$");
	}

	public void arrayPush(String value)
	{
		if (this.arrayProvider != null) this.arrayProvider.push(this.qualifiedName, value);
	}

	public void arrayPut(String value)
	{
		if (this.arrayProvider != null) this.arrayProvider.put(this.qualifiedName, value);
	}

	public String arrayPop()
	{
		return (this.arrayProvider != null) ? this.arrayProvider.pop(this.qualifiedName) : IStringProvider.EMPTY;
	}

	public int arrayIndexOf(String search, boolean caseSensitive)
	{
		return (this.arrayProvider != null) ? this.arrayProvider.indexOf(this.qualifiedName, search, caseSensitive) : IArrayProvider.MISSING;
	}

	public void arrayClear()
	{
		if (this.arrayProvider != null) this.arrayProvider.clear(this.qualifiedName);
	}

	public Object arrayGetValue(int offset)
	{
		return (this.arrayProvider != null) ? this.arrayProvider.getArrayVariableValue(this.qualifiedName, offset) : null;
	}

	public int arrayGetMaxIndex()
	{
		return (this.arrayProvider != null) ? this.arrayProvider.getMaxArrayIndex(this.qualifiedName) : -1;
	}

	public boolean arrayExists()
	{
		return (this.arrayProvider != null) ? this.arrayProvider.checkArrayExists(this.qualifiedName) : false;
	}
}