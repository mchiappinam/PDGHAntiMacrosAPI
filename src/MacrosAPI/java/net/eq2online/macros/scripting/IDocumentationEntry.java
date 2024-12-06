package net.eq2online.macros.scripting;

import net.minecraft.client.gui.FontRenderer;

/**
 * Interface for script action documentation entries
 * 
 * @author Adam Mummery-Smith
 */
public interface IDocumentationEntry
{
	/**
	 * True if this documentation entry should NOT be displayed as a context-sensitive help popup
	 */
	public abstract boolean isHidden();

	/**
	 * Gets the name of the action
	 */
	public abstract String getName();

	/**
	 * Gets the action usage string
	 */
	public abstract String getUsage();

	/**
	 * Gets the plain-text action description
	 */
	public abstract String getDescription();

	/**
	 * Gets a description of what the action returns
	 */
	public abstract String getReturnType();

	/**
	 * Callback for use by the text editor, renders this entry as a pop up
	 * 
	 * @param fontRenderer
	 * @param xPosition
	 * @param yPosition
	 */
	public abstract void drawAt(FontRenderer fontRenderer, int xPosition, int yPosition);
}
