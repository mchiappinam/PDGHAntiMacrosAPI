package net.eq2online.macros.scripting.api;

import java.util.HashMap;

/**
 * Upstream interface for Macro objects
 *
 * @author Adam Mummery-Smith
 */
public interface IMacro extends IVariableProvider, IVariableListener
{
	/**
	 * Get the key ID of the macro
	 * 	
	 * @return
	 */
	public abstract int getID();
	
	public abstract String getDisplayName();

	/**
	 * Get the backing flag provider for this macro, probably the template
	 * 
	 * @return
	 */
	public abstract IFlagProvider getFlagProvider();
	
	/**
	 * Get the backing counter provider for this macro, probably the template
	 * 
	 * @return
	 */
	public abstract ICounterProvider getCounterProvider();
	
	/**
	 * Get the backing string variable provider for this macro, probably the template
	 * 
	 * @return
	 */
	public abstract IStringProvider getStringProvider();
	
	/**
	 * Get the backing array provider for this macro, most likely the template 
	 * 
	 * @return
	 */
	public abstract IArrayProvider getArrayProvider();
	
	/**
	 * @return
	 */
	public abstract IMacroActionContext getContext();
	
	/**
	 * Get the execution state data
	 * 
	 * @return
	 */
	public abstract HashMap<String, Object> getStateData();
	
	/**
	 * @param key
	 * @return
	 */
	public abstract Object getState(String key);
	
	/**
	 * @param key
	 * @param value
	 */
	public abstract void setState(String key, Object value);

	/**
	 * 
	 */
	public abstract void markDirty();

	/**
	 * @return
	 */
	public abstract boolean isDirty();

	/**
	 * 
	 */
	public abstract void kill();

	/**
	 * @return
	 */
	public abstract boolean isDead();
	
	/**
	 * Register a new variable provider
	 * 
	 * @param variableProvider
	 */
	public abstract void registerVariableProvider(IVariableProvider variableProvider);
	
	/**
	 * Unregister a variable provider
	 * 
	 * @param variableProvider
	 */
	public abstract void unregisterVariableProvider(IVariableProvider variableProvider);
}
