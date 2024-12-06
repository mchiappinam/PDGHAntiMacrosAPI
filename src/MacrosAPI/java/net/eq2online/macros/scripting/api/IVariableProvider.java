package net.eq2online.macros.scripting.api;

import java.util.Set;

/**
 * Interface for objects which provide variables

 * @author Adam Mummery-Smith
 * 
 */
public interface IVariableProvider extends IMacrosAPIModule
{
	/**
	 * Provide all variables to the supplied listener
	 * @param clock true if this is a new tick
	 * @param variableListener Listener to provide variables to
	 */
	public abstract void updateVariables(boolean clock);
	
	/**
	 * Get a named variable from the cache, functionality contract stipulates that this function should
	 * return NULL if the specified variable cannot be provided by this provider
	 * 
	 * @param variableName
	 * @return
	 */
	public abstract Object getVariable(String variableName);

	/**
	 * Get the names of all variables in this provider 
	 * 
	 * @return
	 */
	public abstract Set<String> getVariables();
}
