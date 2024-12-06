package net.eq2online.macros.scripting.api;

import net.minecraft.util.IIcon;

/**
 * Interface for event handles returned by the event manager, when registering an event with the event
 * manager a concrete class implementing IMacroEvent will be returned. 
 * 
 * @author Adam Mummery-Smith
 */
public interface IMacroEvent
{
	/**
	 * Get the name of the event
	 */
	public abstract String getName();

	/**
	 * Get the reference to 
	 */
	public abstract IMacroEventProvider getProvider();
	
	/**
	 * Set the class of the variable provider which will be spawned as a context provider when this event is raised.
	 * 
	 * @param providerClass
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public abstract void setVariableProviderClass(Class<? extends IMacroEventVariableProvider> providerClass);

	/**
	 * Internal method, this is called by the executive to instatiate the defined provider whenever an event is dispatched
	 * 
	 * @param args
	 * @return
	 */
	public abstract IMacroEventVariableProvider getVariableProvider(String[] args);
	
	/**
	 * Called when the event is dispatched 
	 */
	public abstract void onDispatch();

	/**
	 * Internal method, returns true if the event is permissible (supports permissions)
	 */
	public abstract boolean isPermissible();
	
	/**
	 * Internal method, returns the permission group for this event
	 */
	public abstract String getPermissionGroup();
	
	/**
	 * Internal method, returns the permission name for this event
	 */
	public abstract String getPermissionName();
	
	/**
	 * Get the icon to display in the UI
	 */
	public abstract IIcon getIcon();
	
	/**
	 * Set the icon to display in the UI
	 */
	public abstract void setIcon(IIcon icon);

	/**
	 * Get a help line for display in the macro edit GUI, lines are in the range 0-5
	 * @param eventId
	 * @param line
	 * 
	 * @return
	 */
	public abstract String getHelpLine(int eventId, int line);
}
