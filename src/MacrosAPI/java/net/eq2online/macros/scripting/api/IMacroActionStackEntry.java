package net.eq2online.macros.scripting.api;

public interface IMacroActionStackEntry
{
	public abstract boolean isStackPushOperator();
	
	/**
	 * Check whether the specified action can pop this stack entry
	 * 
	 * @param action
	 * @return
	 */
	public abstract boolean canBePoppedBy(IMacroAction action);
	
	public abstract void executeStackPop(IMacroActionProcessor processor, IMacroActionContext context, IMacro macro, IMacroAction popAction);
	
	public abstract boolean isConditionalOperator();
	
	public abstract boolean isConditionalElseOperator(IMacroAction action);
	
	public abstract boolean matchesConditionalOperator(IMacroAction action);

	public abstract boolean getConditionalFlag();
	
	public abstract void setConditionalFlag(boolean newFlag);

	public abstract boolean getIfFlag();
	
	public abstract void setIfFlag(boolean newFlag);
	
	public abstract boolean getElseFlag();

	public abstract void setElseFlag(boolean newFlag);
	
	public abstract IMacroAction getAction();
	
	public abstract int getStackPointer();
}