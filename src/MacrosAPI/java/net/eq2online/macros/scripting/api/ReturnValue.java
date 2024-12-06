package net.eq2online.macros.scripting.api;

import net.eq2online.macros.scripting.ScriptCore;

/**
 * Concrete implementation of a IReturnValue for script actions  
 * 
 * @author Adam Mummery-Smith
 */
public class ReturnValue implements IReturnValue
{
	/**
	 * Actual or inferred boolean value to return
	 */
	private boolean booleanValue;
	
	/**
	 * Actual or inferred integer value to return
	 */
	private int integerValue;
	
	/**
	 * Actual or inferred string value to return
	 */
	private String stringValue;
	
	/**
	 * Create a return value with the specified string value, infers the integer and boolean values 
	 * 
	 * @param value
	 */
	public ReturnValue(String value)
	{
		this.setString(value);
	}

	/**
	 * Create a return value with the specified integer value, infers the string and boolean values 
	 * 
	 * @param value
	 */
	public ReturnValue(int value)
	{
		this.setInt(value);
	}

	/**
	 * Create a return value with the specified boolean value, infers the integer and string values 
	 * 
	 * @param value
	 */
	public ReturnValue(boolean value)
	{
		this.setBool(value);
	}

	/**
	 * @param value
	 */
	public void setString(String value)
	{
		this.stringValue = value;
		this.integerValue = ScriptCore.tryParseInt(value, 0);
		this.booleanValue = value == null || value.toLowerCase().equals("true") || this.integerValue != 0;
	}
	
	/**
	 * @param value
	 */
	public void setInt(int value)
	{
		this.stringValue = String.valueOf(value);
		this.integerValue = value;
		this.booleanValue = this.integerValue != 0;
	}
	
	/**
	 * @param value
	 */
	public void setBool(boolean value)
	{
		this.stringValue = value ? "True" : "False";
		this.integerValue = value ? 1 : 0;
		this.booleanValue = value;
	}
	
	/* (non-Javadoc)
	 * @see net.eq2online.macros.scripting.api.IReturnValue#hasValues()
	 */
	@Override
	public boolean isVoid()
	{
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.eq2online.macros.scripting.api.IReturnValue#getBoolean()
	 */
	@Override
	public boolean getBoolean()
	{
		return this.booleanValue;
	}
	
	/* (non-Javadoc)
	 * @see net.eq2online.macros.scripting.api.IReturnValue#getInteger()
	 */
	@Override
	public int getInteger()
	{
		return this.integerValue;
	}
	
	/* (non-Javadoc)
	 * @see net.eq2online.macros.scripting.api.IReturnValue#getString()
	 */
	@Override
	public String getString()
	{
		return this.stringValue != null ? this.stringValue : "";
	}
	
	/* (non-Javadoc)
	 * @see net.eq2online.macros.scripting.api.IReturnValue#getLocalMessage()
	 */
	@Override
	public String getLocalMessage()
	{
		return null;
	}
	
	/* (non-Javadoc)
	 * @see net.eq2online.macros.scripting.api.IReturnValue#getRemoteMessage()
	 */
	@Override
	public String getRemoteMessage()
	{
		return null;
	}
}
