package net.eq2online.macros.scripting.exceptions;

public class ScriptExceptionInvalidModeSwitch extends ScriptException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 295492412608506053L;

	public ScriptExceptionInvalidModeSwitch()
	{
		super("Invalid mode switch");
	}
	
	public ScriptExceptionInvalidModeSwitch(String arg0)
	{
		super(arg0);
	}
	
	public ScriptExceptionInvalidModeSwitch(Throwable arg0)
	{
		super(arg0);
	}
	
	public ScriptExceptionInvalidModeSwitch(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
