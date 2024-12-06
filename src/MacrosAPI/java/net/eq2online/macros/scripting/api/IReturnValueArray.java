package net.eq2online.macros.scripting.api;

import java.util.List;

/**
 * Interface for macro action return values which return array values
 * 
 * @author Adam Mummery-Smith
 */
public interface IReturnValueArray extends IReturnValue
{
	/**
	 * @return Number of values in this array
	 */
	public abstract int size();
	
	/**
	 * @return TRUE if the values in this return value should be appended to any array type or FALSE
	 * if the array should be over-written with the values in this return value
	 */
	public abstract boolean shouldAppend();
	
	/**
	 * @return Get the return value as a list of booleans, MUST have the same number of elements reported by size()
	 */
	public abstract List<Boolean> getBooleans();
	
	/**
	 * @return Get the return value as a list of integers, MUST have the same number of elements reported by size()
	 */
	public abstract List<Integer> getIntegers();
	
	/**
	 * @return Get the return value as a list of strings, MUST have the same number of elements reported by size()
	 */
	public abstract List<String> getStrings();
}
