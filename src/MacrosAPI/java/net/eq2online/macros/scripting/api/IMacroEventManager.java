package net.eq2online.macros.scripting.api;

import java.util.List;

/**
 * Interface for the macro event manager
 * 
 * @author Adam Mummery-Smith
 */
public interface IMacroEventManager
{
	/**
	 * Register a new event provider with the manager
	 * 
	 * @param provider Provider to register
	 */
	public abstract void registerEventProvider(IMacroEventProvider provider);
	
	/**
	 * Creates and returns a new event, this method should be called from within an event provider's
	 * registerEvents() method
	 * 
	 * @param provider Provider registering the event
	 * @param name Name of the event to register, normally starting with "on"
	 * @return
	 */
	public abstract IMacroEvent registerEvent(IMacroEventProvider provider, String name);

	/**
	 * Creates and returns a new event, this method should be called from within an event provider's
	 * registerEvents() method
	 * 
	 * @param provider Provider registering the event
	 * @param name Name of the event to register, normally starting with "on"
	 * @param permissionGroup Permission group for the event
	 * @return
	 */
	public abstract IMacroEvent registerEvent(IMacroEventProvider provider, String name, String permissionGroup);

	/**
	 * Registers or updates an event, this can be used if a custom implementation of IMacroEvent is required
	 * 
	 * @param provider Provider registering the event
	 * @param name Name of the event to register, normally starting with "on"
	 * @return
	 */
	public abstract IMacroEvent registerEvent(IMacroEvent event);
	
	/**
	 * Get an event reference from the specified mapping ID
	 * 
	 * @param mappingId
	 * @return
	 */
	public abstract IMacroEvent getEvent(int mappingId);

	/**
	 * Get an event reference by name
	 * 
	 * @param eventName
	 * @return
	 */
	public abstract IMacroEvent getEvent(String eventName);
	
	/**
	 * Get a list of all registered events
	 */
	public abstract List<IMacroEvent> getEvents();

	/**
	 * Get the ID of an event by name
	 * 
	 * @param name
	 * @return
	 */
	public abstract int getEventID(String name);
	
	/**
	 * Get the ID of an event
	 * 
	 * @param event
	 * @return
	 */
	public abstract int getEventID(IMacroEvent event);
	
	/**
	 * Raise an event
	 * 
	 * @param event
	 * @param eventArgs
	 */
	public abstract void sendEvent(IMacroEvent event, String... eventArgs);
	
	/**
	 * Raise an event by name with the specified priority
	 * 
	 * @param eventName
	 * @param priority
	 * @param eventArgs
	 */
	public abstract void sendEvent(String eventName, int priority, String... eventArgs);
}
