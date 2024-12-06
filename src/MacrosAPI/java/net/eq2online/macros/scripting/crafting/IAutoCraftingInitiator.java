package net.eq2online.macros.scripting.crafting;

/**
 * Interfaces for objects which can initiatate an auto-crafting task and recieve a callback via the
 * AutoCraftingToken delegate.
 * 
 * @author Adam Mummery-Smith
 */
public interface IAutoCraftingInitiator
{
	public abstract void notifyTokenProcessed(AutoCraftingToken token, String reason);
}
