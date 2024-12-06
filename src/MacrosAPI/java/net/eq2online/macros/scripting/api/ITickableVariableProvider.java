package net.eq2online.macros.scripting.api;

/**
 * Variable providers can implement this interface if they wish to receive OnTick notifications
 * from the ScriptActionProvider. Not relevant for variable providers which are not registered
 * with the ScriptActionProvider.
 * 
 * @author Adam Mummery-Smith
 */
public interface ITickableVariableProvider extends IVariableProvider
{
	/**
	 * Called once per tick 
	 */
	public abstract void onTick();
}
