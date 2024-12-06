package net.eq2online.macros.scripting.api;

/**
 * Interface for providers that specifically provide array functionality 
 * 
 * @author Adam Mummery-Smith
 */
public interface IArrayProvider extends IVariableProvider
{
	public static final int MISSING = -1;
	
	/**
	 * Push a variable onto the end of the array
	 * 
	 * @param arrayName
	 * @param value
	 * @return 
	 */
	public abstract boolean push(String arrayName, String value);
	
	/**
	 * Get a variable from the end of the array and remove it
	 * 
	 * @param arrayName
	 * @return
	 */
	public abstract String pop(String arrayName);

	/**
	 * Put a variable into the array at the first free location
	 * 
	 * @param arrayName
	 * @param variableValue
	 * @return 
	 */
	public abstract boolean put(String arrayName, String value);

	/**
	 * Get the location of the specified element or -1 if not found
	 * 
	 * @param arrayName
	 * @param value
	 * @param caseSensitive
	 * @return
	 */
	public abstract int indexOf(String arrayName, String value, boolean caseSensitive);
	
	/**
	 * Delete the specified entry from the array
	 * 
	 * @param arrayName
	 * @param offset
	 */
	public abstract void delete(String arrayName, int offset);
	
	/**
	 * Delete (clear) the specified array
	 *  
	 * @param arrayName
	 */
	public abstract void clear(String arrayName);

	/**
	 * Get the length of the specified array (max index + 1)
	 * 
	 * @param arrayName
	 * @return
	 */
	public abstract int getMaxArrayIndex(String arrayName);
	
	/**
	 * Check whether this provider has an array with the specified name
	 * 
	 * @param arrayName
	 * @return
	 */
	public abstract boolean checkArrayExists(String arrayName);

	/**
	 * Like GetVariable, but for arrays
	 * 
	 * @param variableName
	 * @param offset
	 * @return
	 */
	public abstract Object getArrayVariableValue(String variableName, int offset);
}
