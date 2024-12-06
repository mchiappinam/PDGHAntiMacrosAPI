package net.eq2online.macros.scripting.api;

import net.eq2online.macros.scripting.ScriptContext;

/**
 * Interface for script actions
 * 
 * @author Adam Mummery-Smith
 */
public interface IScriptAction extends IMacrosAPIModule
{
	/**
	 * @return
	 */
	public abstract ScriptContext getContext();

	/**
	 * Get the name of the action, always lower-case
	 * 
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * Get whether this action can be safely executed in a different thread (eg. the network thread) 
	 * 
	 * @return
	 */
	public abstract boolean isThreadSafe();
	
	/**
	 * Return true if this is a stack push operator
	 * 
	 * @return
	 */
	public abstract boolean isStackPushOperator();
	
	/**
	 * Return true if this is a stack pop operator, stack pop operators can have no other behaviour.
	 * 
	 * @return
	 */
	public abstract boolean isStackPopOperator();
	
	/**
	 * Return true if this action can be popped by the supplied action when this action is the topmost entry in the stack.
	 * 
	 * @param action
	 * @return
	 */
	public abstract boolean canBePoppedBy(IScriptAction action);
	
	/**
	 * Execute a stack push operator, this function should return a boolean indicating the state for the conditional execution flag
	 * for this pass. If the return value is true the conditional flag is set, the stack pointer is pointed at the current instruction and
	 * execution continues. If the return value is false the conditional flag is cleared, and the stack pointer is left empty and the corresponding
	 * stack pop operation will not resume execution at the current location
	 * 
	 * @param provider
	 * @param macro
	 * @param instance
	 * @param params
	 * @return
	 */
	public abstract boolean executeStackPush(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params);
	
	/**
	 * Executes a stack pop operation. Somewhat counter-intuitively this function is called on the original action which pushed the stack when a matching
	 * stack pop operator (canBePoppedBy returns true) is found. The stack pop operator simply causes this function to be called on the topmost stack
	 * entry. The return value is not currently used.
	 * 
	 * @param provider
	 * @param macro
	 * @param instance
	 * @param rawParams
	 * @param params
	 * @param popAction
	 * @return
	 */
	public abstract boolean executeStackPop(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params, IMacroAction popAction);

	/**
	 * This method should return true if this action (at any location in the stack) can BREAK. When a BREAK action is encountered, the executive walks the
	 * stack from the top down to find the first action which returns true to this call. If no action in the stack returns true then the BREAK action has
	 * no effect. If returning true, the conditional flag is cleared and the remainder of the script is processed until the corresponding stack pop is
	 * encountered.
	 * 
	 * @param processor
	 * @param provider
	 * @param macro
	 * @param instance
	 * @param breakAction
	 * @return
	 */
	public abstract boolean canBreak(IMacroActionProcessor processor, IScriptActionProvider provider, IMacro macro, IMacroAction instance, IMacroAction breakAction);
	
	/**
	 * True if this is a conditional operator
	 * 
	 * @return
	 */
	public abstract boolean isConditionalOperator();
	
	public abstract boolean isConditionalElseOperator(IScriptAction action);
	
	/**
	 * True if this matches the most recent conditional operator
	 * 
	 * @param action
	 * @return
	 */
	public abstract boolean matchesConditionalOperator(IScriptAction action);
	
	/**
	 * Execute a conditional operator
	 * 
	 * @param provider
	 * @param macro
	 * @param instance
	 * @param params
	 * 
	 * @return True if the conditional suceeded
	 */
	public abstract boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params);
	
	public abstract void executeConditionalElse(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params, IMacroActionStackEntry top);
	
	/**
	 * Execute this script action, the action should make callbacks against the ScriptActionProvider rather than
	 * executing tasks itself, the action should validate and process the parameters and pass control back to the
	 * ScriptActionProvider which will handle processing the command.
	 * 
	 * @param provider Script Action Provider which will provide callback functions for the class
	 * @param macro Macro which owns the action instance
	 * @param instance Action instance
	 * @param params Action instance parameters
	 * @return Return value
	 */
	public abstract IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params);
	
	/**
	 * Query whether this action can be executed yet, if it can't then the processor will pause until the action
	 * can be executed, continually polling this function until it returns true. 
	 * 
	 * @param context 
	 * @param macro Macro which owns the action instance
	 * @param instance Action instance
	 * @param params Action instance parameters
	 * @return True if the action can be executed now or false to execute it latently (pause for this tick)
	 */
	public abstract boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params);
	
	/**
	 * Tick this action
	 * 
	 * @param provider
	 */
	public abstract int onTick(IScriptActionProvider provider);

	/**
	 * *** In general ALL actions which DON'T perform per-frame interpolation should return TRUE *** This determines whether
	 * an action is executed by tick or by frame.
	 * 
	 * @return
	 */
	public abstract boolean isClocked();
	
	/**
	 * This action can be controlled by permissions
	 * 
	 * @return
	 */
	public abstract boolean isPermissable();
	
	/**
	 * The permission group controlling this action's ability to execute
	 * 
	 * @return
	 */
	public abstract String getPermissionGroup();

	/**
	 * The action should register permissions with the permissions provider
	 * 
	 * @param actionName
	 * @param actionGroup
	 */
	public abstract void registerPermissions(String actionName, String actionGroup);
	
	/**
	 * The action should check its permission with the permission provider and return true if permission is available
	 * 
	 * @return
	 */
	public abstract boolean checkExecutePermission();
	
	/**
	 * Checks an arbitrary permission, in general checkExecutePermission should call this function with permission set to "execute"
	 * 
	 * @param actionName
	 * @param permission
	 * @return
	 */
	public abstract boolean checkPermission(String actionName, String permission);

	/**
	 * Called when the macro is stopped while this action is in a latent state
	 * 
	 * @param provider
	 * @param macro
	 * @param instance
	 */
	public abstract void onStopped(IScriptActionProvider provider, IMacro macro, IMacroAction instance);
}