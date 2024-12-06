package net.eq2online.macros.scripting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

/**
 * This class is used to provide variable replacement inside strings, it accepts a text template and a variable provider
 * and uses the variable provider to replace strings in the template in the form %varname%. Owners can then call toString()
 * to retrieve the resultant text
 *
 * @author Adam Mummery-Smith
 */
public class VariableExpander
{
	/**
	 * Provider for the variables
	 */
	private final IScriptActionProvider provider;
	
	/**
	 * Original (unparsed) string
	 */
	private final String templateString;
	
	/**
	 * String processed with variable values
	 */
	private String innerString;

	/**
	 * Quote strings that get replaced in the expression
	 */
	private final boolean quoteStrings;

	private final String defaultStringValue;
	
	private static Pattern variablePattern = Pattern.compile("%(" + Variable.PREFIX_SHARED + "?[" + Variable.PREFIX_TYPES + "]?[a-z~]([a-z0-9_\\-]*?)(\\[[0-9]{1,5}\\])?)%", Pattern.CASE_INSENSITIVE);

	/**
	 * Create a new variable expander
	 * 
	 * @param provider Provider which will provide variables
	 * @param macro Macro which is the in-context object
	 * @param text
	 */
	public VariableExpander(IScriptActionProvider provider, IMacro macro, String text, boolean quoteStrings)
	{
		this(provider, macro, text, quoteStrings, "");
	}
	
	public VariableExpander(IScriptActionProvider provider, IMacro macro, String text, boolean quoteStrings, String defaultStringValue)
	{
		this.provider = provider;
		this.templateString = text;
		this.quoteStrings = quoteStrings;
		this.defaultStringValue = defaultStringValue;
		
		this.apply(macro);
	}
	
	/**
	 * Refresh the variables in this holder with the current values using the specified context provider
	 * 
	 * @param macro In-context provider
	 */
	public void apply(IMacro macro)
	{
		this.innerString = this.templateString;
		
		Matcher variablePatternMatcher = variablePattern.matcher(this.innerString);
		int replacements = 0;
		
		while (variablePatternMatcher.find() && replacements < 0x100)
		{
			replacements++;
			
			this.innerString = VariableExpander.replaceVariable(this.provider, macro, this.innerString, this.quoteStrings, variablePatternMatcher.group(), variablePatternMatcher.group(1), this.defaultStringValue);
			variablePatternMatcher.reset(this.innerString);
		}
	}
	
	/**
	 * Expand a single variable using the supplied provider and macro as context
	 * 
	 * @param provider
	 * @param macro
	 * @param variableName
	 * @return
	 */
	public static String expand(IScriptActionProvider provider, IMacro macro, String variableName)
	{
		String variable = "%" + variableName + "%";
		return VariableExpander.replaceVariable(provider, macro, variable, false, variable, variableName, "");
	}
	
	/**
	 * Replace all variables in the supplied string
	 * 
	 * @param provider
	 * @param macro
	 * @param subject
	 * @param quoteStrings
	 * @param variable
	 * @param variableName
	 * @return
	 */
	private static String replaceVariable(IScriptActionProvider provider, IMacro macro, String subject, boolean quoteStrings, String variable, String variableName, String defaultStringValue)
	{
		Object oVariableValue = provider != null ? provider.getVariable(variableName, macro) : null;
		
		if (oVariableValue == null)
		{
			if (Variable.couldBeInt(variableName)) return subject.replace(variable, "0");
			if (Variable.couldBeString(variableName)) return subject.replace(variable, defaultStringValue);
			if (Variable.couldBeBoolean(variableName)) return subject.replace(variable, "False");
			return subject.replace(variable, variableName);
		}
		
		if (oVariableValue instanceof Integer)
		{
			int iVariableValue = (Integer)oVariableValue;
			return subject.replace(variable, String.valueOf(iVariableValue));
		}
		else if (oVariableValue instanceof Boolean)
		{
			return subject.replace(variable, ((Boolean)oVariableValue) ? "True" : "False");
		}
		
		String variableValue = oVariableValue.toString();
		
		if (quoteStrings)
		{
			variableValue = "\"" + variableValue + "\"";
		}
		
		return subject.replace(variable, variableValue.replace(variable, variableName));
	}

	@Override
	public String toString()
	{
		return this.innerString;
	}
}
