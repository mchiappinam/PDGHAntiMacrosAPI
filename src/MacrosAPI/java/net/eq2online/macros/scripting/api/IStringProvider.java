package net.eq2online.macros.scripting.api;

public interface IStringProvider
{
	public static final String EMPTY = "";
	
	/**
	 * Get the value of the specified string
	 * 
	 * @param stringName String name to retrieve
	 * @return
	 */
	public abstract String getString(String stringName);
	
	/**
	 * Get the value of the specified string
	 * 
	 * @param stringName String name to retrieve
	 * @return
	 */
	public abstract String getString(String stringName, int offset);
	
	/**
	 * Set the value of the specified string
	 * 
	 * @param stringName String name to set
	 * @param value Value for the string
	 */
	public abstract void setString(String stringName, String value);
	
	/**
	 * Set the value of the specified string
	 * 
	 * @param stringName String name to set
	 * @param value Value for the string
	 */
	public abstract void setString(String stringName, int offset, String value);
	
	/**
	 * Unset (clear) the specified string
	 * 
	 * @param stringName String to unset
	 */
	public abstract void unsetString(String stringName);
	
	/**
	 * Unset (clear) the specified string
	 * 
	 * @param stringName String to unset
	 */
	public abstract void unsetString(String stringName, int offset);
}
