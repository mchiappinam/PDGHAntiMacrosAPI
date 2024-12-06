package net.eq2online.macros.scripting.api;

import java.util.Map;

/**
 * Interface for objects which accept variables
 * 
 * @author Adam Mummery-Smith
 *
 */
public interface IVariableListener
{
	/**
	 * Provide a boolean variable to this listener
	 * 
	 * @param variableName
	 * @param variableValue
	 */
	public abstract void setVariable(String variableName, boolean variableValue);

	/**
	 * Provide an integer variable to this listener
	 * 
	 * @param variableName
	 * @param variableValue
	 */
	public abstract void setVariable(String variableName, int variableValue);

	/**
	 * Provide a string variable to this listener
	 *  
	 * @param variableName
	 * @param variableValue
	 */
	public abstract void setVariable(String variableName, String variableValue);
	
	/**
	 * Provide multiple (abstract type) variables to this listener
	 * 
	 * @param variables
	 */
	public abstract void setVariables(Map<String, Object> variables);
}
