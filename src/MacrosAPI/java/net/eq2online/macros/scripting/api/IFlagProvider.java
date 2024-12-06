package net.eq2online.macros.scripting.api;

/**
 * Interface for objects which provide flags
 *
 * @author Adam Mummery-Smith
 */
public interface IFlagProvider
{
	public static final boolean EMPTY = false;

	/**
	 * Get the value of the specified flag
	 * 
	 * @param flag Flag name to retrieve
	 * @return
	 */
	public abstract boolean getFlag(String flag);
	
	/**
	 * Get the value of the specified flag
	 * 
	 * @param flag Flag name to retrieve
	 * @return
	 */
	public abstract boolean getFlag(String flag, int offset);
	
	/**
	 * Set the value of the specified flag
	 * 
	 * @param flag Flag to set
	 * @param value Value for the flag
	 */
	public abstract void setFlag(String flag, boolean value);
	
	/**
	 * Set the value of the specified flag
	 * 
	 * @param flag Flag to set
	 * @param value Value for the flag
	 */
	public abstract void setFlag(String flag, int offset, boolean value);
	
	/**
	 * Set the specified flag
	 * 
	 * @param flag Flag to set
	 */
	public abstract void setFlag(String flag);
	
	/**
	 * Set the specified flag
	 * 
	 * @param flag Flag to set
	 */
	public abstract void setFlag(String flag, int offset);
	
	/**
	 * Unset (clear) the specified flag
	 * 
	 * @param flag Flag to unset
	 */
	public abstract void unsetFlag(String flag);
	
	/**
	 * Unset (clear) the specified flag
	 * 
	 * @param flag Flag to unset
	 */
	public abstract void unsetFlag(String flag, int offset);
	
}