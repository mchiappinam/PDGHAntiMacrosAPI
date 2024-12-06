package net.eq2online.macros.scripting.api;

/**
 * Interface for macro action return values, currently only getRemoteMessage and getLocalMessage are actually used
 * 
 * @author Adam Mummery-Smith
 */
public interface IReturnValue
{
	/**
	 * @return true if this return value has no values (only messages)
	 */
	public abstract boolean isVoid();
	
	/**
	 * @return boolean value of the expression, casting from the original type if needed
	 */
	public abstract boolean getBoolean();
	
	/**
	 * @return integer value of the expression, casting from the original type if needed
	 */
	public abstract int getInteger();
	
	/**
	 * @return string value of the expression, casting from the original type if needed
	 */
	public abstract String getString();
	
	/**
	 * @return message to display locally (log message)
	 */
	public abstract String getLocalMessage();
	
	/**
	 * @return message to send as a chat message
	 */
	public abstract String getRemoteMessage();
}
