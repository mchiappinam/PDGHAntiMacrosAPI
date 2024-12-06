package net.eq2online.macros.scripting.api;

import net.eq2online.macros.scripting.exceptions.ScriptException;
import net.eq2online.macros.scripting.exceptions.ScriptExceptionStackOverflow;

public interface IMacroActionProcessor
{
	/**
	 * Process the script, script parsing halts on latent functions or whenever a stack pop operation occurs so
	 * that the the game isn't halted by intensive macros with tight loops 
	 * 
	 * @param macro
	 * @param provider
	 * @param stop
	 * @param allowLatent
	 * @param clock
	 * @return
	 * @throws ScriptException
	 */
	public abstract boolean execute(IMacro macro, IMacroActionContext context, boolean stop, boolean allowLatent, boolean clock) throws ScriptException;
	
	/**
	 * Push an entry onto the stack
	 * 
	 * @param action Action which is pushing the stack
	 * @throws ScriptExceptionStackOverflow 
	 */
	public abstract void pushStack(IMacroAction action, boolean conditional) throws ScriptExceptionStackOverflow;
	
	/**
	 * Push an entry onto the stack with a specified pointer value
	 * 
	 * @param ptr Pointer value
	 * @param action Action which is pushing the stack
	 * @throws ScriptExceptionStackOverflow 
	 */
	public abstract void pushStack(int ptr, IMacroAction action, boolean conditional) throws ScriptExceptionStackOverflow;
	
	/**
	 * Pop an entry off the stack, sets the instruction pointer to the location indicated by the stack
	 * entry and returns the action 
	 * 
	 * @return
	 */
	public abstract boolean popStack();
	
	/**
	 * Get the stack entry on the top of the stack
	 * 
	 * @return
	 */
	public abstract IMacroActionStackEntry getTopStackEntry();

	/**
	 * Examines the entire stack to determine whether this action is executed or not, if any entries below this on the stack
	 * report a FALSE conditional state then the macro action currently at the pointer will be evaluated but not executed.
	 * 
	 * @return
	 */
	public abstract boolean getConditionalExecutionState();
	
	/**
	 * Break the topmost loop 
	 */
	public abstract void breakLoop(IScriptActionProvider provider, IMacro macro, IMacroAction breakAction);

	public abstract void beginUnsafeBlock(IScriptActionProvider provider, IMacro macro, IMacroAction instance, int maxActions);

	public abstract void endUnsafeBlock(IScriptActionProvider provider, IMacro macro, IMacroAction instance);

	public abstract boolean isUnsafe();
}