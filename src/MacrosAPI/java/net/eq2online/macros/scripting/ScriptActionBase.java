package net.eq2online.macros.scripting;

import net.eq2online.macros.scripting.api.IScriptAction;

/**
 * Base class for script actions
 * 
 * @author Adam Mummery-Smith
 */
public abstract class ScriptActionBase implements IScriptAction
{
	/**
	 * The context of this script action
	 */
	protected final ScriptContext context;
	
	/**
	 * Name of this action
	 */
	protected final String actionName;
	
	/**
	 * Protected ctor, used for child instance singletons
	 * @param context 
	 * 
	 * @param actionName
	 */
	protected ScriptActionBase(ScriptContext context, String actionName)
	{
		this.context = context;
		this.actionName = actionName.toLowerCase(); 
	}
	
	/* (non-Javadoc)
	 * @see net.eq2online.macros.scripting.api.IScriptAction#getContext()
	 */
	@Override
	public ScriptContext getContext()
	{
		return this.context;
	}
	
	/* (non-Javadoc)
	 * @see net.eq2online.macros.scripting.api.IScriptAction#getName()
	 */
	@Override
	public String getName()
	{
		return this.actionName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.actionName;
	}
}