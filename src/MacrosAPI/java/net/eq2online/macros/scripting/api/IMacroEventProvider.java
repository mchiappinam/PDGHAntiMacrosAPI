package net.eq2online.macros.scripting.api;

/**
 * Interface for event providers
 *
 * @author Adam Mummery-Smith
 */
public interface IMacroEventProvider extends IMacrosAPIModule
{
	/**
	 * Get the dispatcher for this provider's events, the dispatcher receives onTick events every frame
	 * and is expected to dispatch events as required by the provider, the provider can implement its own
	 * dispatcher in simple scenarios
	 */
	public abstract IMacroEventDispatcher getDispatcher();

	/**
	 * Callback from the event manager whenever configuration changes or a new provider is added, the provider
	 * should call manager.registerEvent with ALL of the events it provides
	 */
	public abstract void registerEvents(IMacroEventManager manager);

	/**
	 * Gets a line of help to display in the edit GUI, called by the default event implementation, lines are
	 * in the range 0-5
	 * 
	 * @param macroEvent Event being displayed
	 * @param eventId ID of the event
	 * @param line Help line number to display, in the range 0-5
	 * @return
	 */
	public abstract String getHelp(IMacroEvent macroEvent, int eventId, int line);
}
