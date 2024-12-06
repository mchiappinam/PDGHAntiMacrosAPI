package net.eq2online.macros.scripting.api;

import java.util.List;

import net.eq2online.macros.scripting.ActionParser;
import net.eq2online.macros.scripting.ScriptContext;

/**
 * Interface for script parser objects
 *
 * @author Adam Mummery-Smith
 */
public interface IScriptParser
{
	public abstract void addActionParser(ActionParser parser);
	
	/**
	 * Parses actions inside a script
	 * 
	 * @param script
	 */
	public abstract List<IMacroAction> parseScript(IMacroActionProcessor actionProcessor, String script);

	public abstract ScriptContext getContext();
}