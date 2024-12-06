package net.eq2online.macros.scripting.api;

import net.eq2online.macros.scripting.ScriptContext;

/**
 * Interface for macro action context, this allows a certain execution context some level of control
 * over the actions that can execute in a given context
 * 
 * @author Adam Mummery-Smith
 */
public interface IMacroActionContext
{
	public abstract ScriptContext getScriptContext();
	
	public abstract IScriptActionProvider getProvider();
	
	public abstract IVariableProvider getVariableProvider();
}
