package net.eq2online.macros.scripting.api;

import net.minecraft.client.Minecraft;

/**
 * Event dispatchers are responsible for raising the events, in general a dispatcher will function by
 * watching variable values in the game, keeping track of their current value and raising events when
 * changes occur.
 *
 * @author Adam Mummery-Smith
 */
public interface IMacroEventDispatcher
{
	/**
	 * Called every frame, the dispatcher should check for events and raise events where necessary
	 * 
	 * @param manager manager to raise events on
	 * @param minecraft
	 */
	public abstract void onTick(IMacroEventManager manager, Minecraft minecraft);
}
