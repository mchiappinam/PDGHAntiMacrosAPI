package net.eq2online.macros.scripting.api;

/**
 * Interface for the extra methods provided by the shared variable provider 
 *
 * @author Adam Mummery-Smith
 */
public interface IVariableProviderShared extends IArrayProvider, IFlagProvider, ICounterProvider, IStringProvider
{
	/**
	 * Set a shared variable value
	 * 
	 * @param variableName Name of the shared variable to set
	 * @param variableValue New value for the shared variable
	 */
	public abstract void setSharedVariable(String variableName, String variableValue);
	
	/**
	 * Get a shared variable's raw value
	 * 
	 * @param variableName
	 * @return
	 */
	public abstract String getSharedVariable(String variableName);
	
	/**
	 * Get a shared variable as an integer value
	 * 
	 * @param variableName
	 * @param defaultValue
	 * @return
	 */
	public abstract int getSharedVariable(String variableName, int defaultValue);
}