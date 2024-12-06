package net.eq2online.macros.scripting;

import java.util.regex.Pattern;

import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;

public abstract class ActionParser
{
	protected final ScriptContext context;
	
	protected ActionParser(ScriptContext context)
	{
		this.context = context;
	}

	/**
	 * Pattern for matching individual script actions
	 */
	protected static Pattern scriptActionPattern = Pattern.compile("^([a-z\\_]+)£?\\((.*)\\)$", Pattern.CASE_INSENSITIVE);
	
	/**
	 * Pattern for matching script directives (functions without parameters such as LOOP)
	 */
	protected static Pattern scriptDirectivePattern = Pattern.compile("^([a-z\\_]+)$", Pattern.CASE_INSENSITIVE);

	public abstract IMacroAction parse(IMacroActionProcessor actionProcessor, String scriptEntry);
}
