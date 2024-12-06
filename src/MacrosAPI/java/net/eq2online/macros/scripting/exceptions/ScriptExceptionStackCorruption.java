package net.eq2online.macros.scripting.exceptions;

public class ScriptExceptionStackCorruption extends ScriptException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3613874587143317694L;

	public ScriptExceptionStackCorruption()
	{
		super("Stack corruption in script");
	}
	
	public ScriptExceptionStackCorruption(String arg0)
	{
		super(arg0);
	}
	
	public ScriptExceptionStackCorruption(Throwable arg0)
	{
		super(arg0);
	}
	
	public ScriptExceptionStackCorruption(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
