package net.eq2online.macros.scripting.exceptions;

public class ScriptException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1410715644061148289L;

	public ScriptException()
	{
	}
	
	public ScriptException(String arg0)
	{
		super(arg0);
	}
	
	public ScriptException(Throwable arg0)
	{
		super(arg0);
	}
	
	public ScriptException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
