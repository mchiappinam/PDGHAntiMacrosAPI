package net.eq2online.macros.scripting.api;

public interface IMacrosAPIModule
{
	/**
	 * API function, initialise this module, only called if API version matches
	 */
	public abstract void onInit();
}
