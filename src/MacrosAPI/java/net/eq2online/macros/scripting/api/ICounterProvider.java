package net.eq2online.macros.scripting.api;

/**
 * Interface for objects which provide counter storage functionality
 *
 * @author Adam Mummery-Smith
 */
public interface ICounterProvider
{
	public static final int EMPTY = 0;

	/**
	 * Get a counter value
	 * 
	 * @param counter
	 * @return
	 */
	public abstract int getCounter(String counter);
	
	/**
	 * Get a counter array value
	 * 
	 * @param counter
	 * @return
	 */
	public abstract int getCounter(String counter, int offset);
	
	/**
	 * Set a counter value
	 * 
	 * @param counter
	 * @param value
	 */
	public abstract void setCounter(String counter, int value);
	
	/**
	 * Set a counter array value
	 * 
	 * @param counter
	 * @param value
	 */
	public abstract void setCounter(String counter, int offset, int value);

	/**
	 * Unset a counter value
	 * 
	 * @param variableName
	 * @param offset
	 */
	public abstract void unsetCounter(String counter);

	/**
	 * Unset a counter array value
	 * 
	 * @param variableName
	 * @param offset
	 */
	public abstract void unsetCounter(String counter, int offset);

	/**
	 * Increment the named counter
	 * 
	 * @param counter
	 * @param increment
	 */
	public abstract void incrementCounter(String counter, int increment);
	
	/**
	 * Increment the named array counter
	 * 
	 * @param counter
	 * @param increment
	 */
	public abstract void incrementCounter(String counter, int offset, int increment);
	
	/**
	 * Decrement the named counter
	 * 
	 * @param counter
	 * @param decrement
	 */
	public abstract void decrementCounter(String counter, int decrement);
	
	/**
	 * Decrement the named array counter
	 * 
	 * @param counter
	 * @param decrement
	 */
	public abstract void decrementCounter(String counter, int offset, int decrement);
}