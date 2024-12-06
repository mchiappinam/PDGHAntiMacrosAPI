package net.eq2online.macros.scripting.exceptions;

public class ScriptExceptionStackOverflow extends ScriptException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8589588416236445843L;

	public ScriptExceptionStackOverflow()
	{
		super("Stack overflow in script");
	}
	
	public ScriptExceptionStackOverflow(String arg0)
	{
		super(arg0);
	}
	
	public ScriptExceptionStackOverflow(Throwable arg0)
	{
		super(arg0);
	}
	
	public ScriptExceptionStackOverflow(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
