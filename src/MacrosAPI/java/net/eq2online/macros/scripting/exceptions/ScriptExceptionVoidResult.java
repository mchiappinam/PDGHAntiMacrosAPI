package net.eq2online.macros.scripting.exceptions;

public class ScriptExceptionVoidResult extends ScriptException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3687316087979193782L;

	public ScriptExceptionVoidResult(String arg0)
	{
		super("Attempted to assign a void result of " + arg0);
	}
	
	public ScriptExceptionVoidResult(String arg0, Throwable arg1)
	{
		super("Attempted to assign a void result of " + arg0, arg1);
	}
}
