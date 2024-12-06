package net.eq2online.console;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import com.mumfrey.liteloader.util.log.LiteLoaderLogger;

/**
 * Macros log writer
 * 
 * @author Adam Mummery-Smith
 */
public final class Log
{
	/**
	 * Write an entry to the log
	 * 
	 * @param info
	 */
	public static void info(final String info)
	{
		LiteLoaderLogger.info(info.replace("%", "%%"));
	}
    
	/**
	 * Write an object value to the log
	 * 
	 * @param info
	 */
	public static void info(final Object data)
	{
		if (data == null) return;
		LiteLoaderLogger.info(("[" + data.getClass().getSimpleName() + "] " + data.toString()).replace("%", "%%"));
	}

	/**
	 * Write a formatted entry to the log
	 * 
	 * @param format
	 * @param args
	 */
	public static void info(final String format, final Object... args)
	{
		LiteLoaderLogger.info(MessageFormat.format(format, args).replace("%", "%%"));
	}
	
	/**
	 * Print a stack trace for the specified throwable to the log
	 * 
	 * @param th
	 */
	public static void printStackTrace(final Throwable th)
	{
		StringWriter writer = new StringWriter();
		th.printStackTrace(new PrintWriter(writer));
		String stackTrace = writer.toString();
		Log.info(stackTrace);
	}
}
