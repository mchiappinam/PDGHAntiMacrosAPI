package net.eq2online.macros.scripting;

import net.eq2online.macros.scripting.api.IScriptAction;

/**
 * Interface for action filters
 * 
 * @author Adam Mummery-Smith
 */
public interface IActionFilter
{
	boolean pass(ScriptContext context, ScriptCore scriptCore, IScriptAction action);
}
