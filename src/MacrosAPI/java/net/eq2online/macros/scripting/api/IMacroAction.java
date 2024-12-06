package net.eq2online.macros.scripting.api;

import net.eq2online.macros.scripting.exceptions.ScriptExceptionStackOverflow;

/**
 * Upstream interface for macro actions
 *
 * @author Adam Mummery-Smith
 */
public interface IMacroAction
{
	/**
	 * Check whether this action can be executed now
	 * 
	 * @param provider Script Action Provider for script actions to use
	 * @param macro Macro which owns this action
	 * @return True if this action can be executed now
	 */
	public abstract boolean canExecuteNow(IMacroActionContext context, IMacro macro);
	
	/**
	 * Return true if the action only executes once per tick
	 * 
	 * @return
	 */
	public abstract boolean isClocked();

	/**
	 * Execute the action
	 * @param provider Script Action Provider for script actions to use
	 * @param macro Macro which owns this action
	 * @param stop True if the stop flag is set on the macro
	 * 
	 * @throws ScriptExceptionStackOverflow 
	 */
	public abstract boolean execute(IMacroActionContext context, IMacro macro, boolean stop, boolean allowLatent) throws ScriptExceptionStackOverflow;
	
	/**
	 * Executes a stack pop at this action, the action's ScriptAction actually carries out the pop
	 * 
	 * @param processor
	 * @param provider
	 * @param macro
	 * @param popAction
	 * @return
	 */
	public abstract boolean executeStackPop(IMacroActionProcessor processor, IMacroActionContext context, IMacro macro, IMacroAction popAction);
	
	public abstract boolean canBreak(IMacroActionProcessor processor, IScriptActionProvider provider, IMacro macro, IMacroAction breakAction);

	/**
	 * Get the script action which this macro action is hosting
	 * 
	 * @return
	 */
	public abstract IScriptAction getAction();
	
	/**
	 * Get the state data for this action
	 * 
	 * @param <T>
	 * @return
	 */
	public abstract <T> T getState();
	
	/**
	 * Set the state data for this action
	 * @param state
	 */
	public abstract void setState(Object state);
	
	/**
	 * Get the parameters passed to this action
	 * 
	 * @return
	 */
	public abstract String[] getParams();

	/**
	 * Get the original parameter string passed to this action
	 * 
	 * @return
	 */
	public abstract String getRawParams();
	
	/**
	 * Get the action processor for this action
	 * 
	 * @return
	 */
	public abstract IMacroActionProcessor getActionProcessor();

	/**
	 * @param parser 
	 * 
	 */
	public abstract void refreshPermissions(IScriptParser parser);

	public abstract void onStopped(IMacroActionProcessor macroActionProcessor, IMacroActionContext context, IMacro macro);
	
	public abstract boolean hasOutVar();
	
	public abstract String getOutVarName();
}