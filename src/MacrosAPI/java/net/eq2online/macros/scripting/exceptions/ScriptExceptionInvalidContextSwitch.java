package net.eq2online.macros.scripting.exceptions;

public class ScriptExceptionInvalidContextSwitch extends ScriptException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2725499246595774741L;

	public ScriptExceptionInvalidContextSwitch()
	{
		super("Invalid context switch in script");
	}
	
	public ScriptExceptionInvalidContextSwitch(String arg0)
	{
		super(arg0);
	}
	
	public ScriptExceptionInvalidContextSwitch(Throwable arg0)
	{
		super(arg0);
	}
	
	public ScriptExceptionInvalidContextSwitch(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
