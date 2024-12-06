package net.eq2online.macros.scripting.api;

import java.util.Set;

import net.eq2online.macros.scripting.ScriptContext;
import net.eq2online.macros.scripting.crafting.AutoCraftingToken;
import net.eq2online.macros.scripting.crafting.IAutoCraftingInitiator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

/**
 * Main script action callback interface, provides most of the action-facing API. This interface is mis-named for
 * historic reasons and really ought to be called ScriptActionActionProvider or ScriptActionCallbackProvider since it
 * PROVIDE actions TO ScriptActions it *doesn't* PROVIDE ScriptActions.
 * 
 * Script Actions should treat this as their primary interface with the game 
 * 
 * @author Adam Mummery-Smith
 */
public interface IScriptActionProvider
{
	/**
	 * Register a new variable provider
	 * 
	 * @param variableProvider
	 */
	public abstract void registerVariableProvider(IVariableProvider variableProvider);
	
	/**
	 * Unregister a variable provider
	 * 
	 * @param variableProvider
	 */
	public abstract void unregisterVariableProvider(IVariableProvider variableProvider);

	/**
	 * Update all variable providers
	 */
	public abstract void updateVariableProviders(boolean clock);

	/**
	 * Get the provider which provides the variable specified, if any
	 * 
	 * @param variableName Name of the variable to search for
	 * @return Provider which provides the specified variable, or null if not found
	 */
	public abstract IVariableProvider getProviderForVariable(String variableName);
	
	/**
	 * Register a new variable provider
	 * 
	 * @param variableListener
	 */
	public abstract void registerVariableListener(IVariableListener variableListener);
	
	/**
	 * Unregister a variable provider
	 * 
	 * @param variableListener
	 */
	public abstract void unregisterVariableListener(IVariableListener variableListener);
	
	/**
	 * Get the shared variable provider
	 * 
	 * @return
	 */
	public abstract IVariableProviderShared getSharedVariableProvider();
	
	/**
	 * Get all environment variable names
	 * 
	 * @return
	 */
	public abstract Set<String> getEnvironmentVariables();

	/**
	 * Get the value of the specified variable using the supplied context provider
	 * 
	 * @param variableName
	 * @param inContextProvider
	 * @return
	 */
	public abstract Object getVariable(String variableName, IVariableProvider inContextProvider);
	
	/**
	 * Get the value of the specified variable using the specified macro as the context provider
	 * 
	 * @param variableName
	 * @param macro
	 * @return
	 */
	public abstract Object getVariable(String variableName, IMacro macro);
	
	/**
	 * Get an evaluator object for the supplied expression
	 * 
	 * @param macro
	 * @param expression
	 * @return
	 */
	public abstract IExpressionEvaluator getExpressionEvaluator(IMacro macro, String expression);
	
	/**
	 * Send a chat message
	 * 
	 * @param message
	 */
	public abstract void actionSendChatMessage(IMacro macro, IMacroAction instance, String message);
	
	/**
	 * Add a chat message to the local display
	 * 
	 * @param message
	 */
	public abstract void actionAddChatMessage(String message);
	
	/**
	 * Disconnect from the current server
	 */
	public abstract void actionDisconnect();
	
	/**
	 * Display a GUI screen
	 * 
	 * @param guiScreenName
	 * @param context
	 */
	public abstract void actionDisplayGuiScreen(String guiScreenName, ScriptContext context);

	public abstract void actionDisplayCustomScreen(String screenName, String backScreenName);
	
	public abstract void actionBindScreenToSlot(String slotName, String screenName);

	/**
	 * Switch configuration
	 * 
	 * @param configName Name of config to switch to
	 */
	public abstract String actionSwitchConfig(String configName, boolean verbose);
	
	/**
	 * Overlay a configuration into the current config
	 *  
	 * @param configName Configuration name to overlay
	 * @param toggle Set to true if the configuration should be toggled instead of always applied
	 * @param verbose Set to true to display a confirmation message when the overlay is successful  
	 * @return
	 */
	public abstract String actionOverlayConfig(String configName, boolean toggle, boolean verbose);
	
	/**
	 * Toggle the render distance
	 */
	public abstract void actionRenderDistance();
	
	/**
	 * Select an inventory slot based on the specified item ID
	 * 
	 * @param itemId
	 */
	public abstract boolean actionInventoryPick(String itemId, int damage);
	
	/**
	 * Select an inventory slot by ID
	 * 
	 * @param slotId ID of the slot to select
	 */
	public abstract void actionInventorySlot(int slotId);
	
	/**
	 * Move by offset through the inventory
	 * 
	 * @param offset
	 */
	public abstract void actionInventoryMove(int offset);
	
	/**
	 * Set or clear the player's sprinting state (provided they would normally be able to sprint)
	 * 
	 * @param sprint
	 */
	public abstract void actionSetSprinting(boolean sprint);
	
	/**
	 * Stop all active macros
	 */
	public abstract void actionStopMacros();
	
	/**
	 * Stop all active macros which match the specified keycode
	 * 
	 * @param keyCode
	 */
	public abstract void actionStopMacros(IMacro macro, int keyCode);
	
	/**
	 * Get a macro flag value
	 * 
	 * @param macro
	 * @param flag
	 * @return
	 */
	public abstract boolean getFlagValue(IMacro macro, String flag);
	
	/**
	 * @param macro
	 * @param flag
	 * @param value
	 */
	public abstract void setFlagVariable(IMacro macro, String flag, boolean value);
	
	/**
	 * @param macro
	 * @param variableName
	 * @param variableValue
	 */
	public abstract void setVariable(IMacro macro, String variableName, String variableValue, int intValue, boolean boolValue);

	/**
	 * @param macro
	 * @param variableName
	 * @param returnValue
	 */
	public abstract void setVariable(IMacro macro, String variableName, IReturnValue returnValue);
	
	/**
	 * @param macro
	 * @param variableName
	 */
	public abstract void unsetVariable(IMacro macro, String variableName);
	
	/**
	 * @param macro
	 * @param counter
	 * @param increment
	 */
	public abstract void incrementCounterVariable(IMacro macro, String counter, int increment);
	
	/**
	 * 
	 * @param macro
	 * @param variableName
	 * @param variableValue
	 */
	public abstract void pushValueToArray(IMacro macro, String arrayName, String variableValue);
	
	/**
	 * @param macro
	 * @param variableName
	 * @param variableValue
	 * @return
	 */
	public abstract String popValueFromArray(IMacro macro, String arrayName);
	
	/**
	 * 
	 * @param macro
	 * @param arrayName
	 * @param variableValue
	 */
	public void putValueToArray(IMacro macro, String arrayName, String variableValue);

	/**
	 * @param macro
	 * @param arrayName
	 */
	public abstract void clearArray(IMacro macro, String arrayName);
	
	/**
	 * @param macro
	 * @param arrayName
	 * @param offset
	 */
	public abstract void deleteArrayElement(IMacro macro, String arrayName, int offset);
	
	/**
	 * 
	 * @param macro
	 * @param arrayName
	 * @param offset
	 */
	public abstract Object getArrayElement(IMacro macro, String arrayName, int offset);
	
	/**
	 * @param macro
	 * @param arrayName
	 * @param search
	 * @param caseSensitive
	 * @return
	 */
	public abstract int getArrayIndexOf(IMacro macro, String arrayName, String search, boolean caseSensitive);
	
	/**
	 * Get the size of the specified array
	 * 
	 * @param macro
	 * @param arrayName
	 * @return
	 */
	public abstract int getArraySize(IMacro macro, String arrayName);
	
	public abstract boolean getArrayExists(IMacro macro, String arrayName);
	
	/**
	 * @param chars
	 */
	public abstract void actionPumpCharacters(String chars);
	
	/**
	 * @param keyCode
	 * @param deep
	 */
	public abstract void actionPumpKeyPress(int keyCode, boolean deep);
	
	/**
	 * @param resourcePackNames
	 */
	public abstract void actionSelectResourcePacks(String[] resourcePackNames);
	
	/**
	 * @param minecraft
	 * @param thePlayer
	 * @param itemstack
	 * @param slotID
	 */
	public abstract void actionUseItem(Minecraft minecraft, EntityClientPlayerMP thePlayer, ItemStack itemstack, int slotID);
	
	/**
	 * @param keyBindId
	 * @param keyCode
	 */
	public abstract void actionBindKey(int keyBindId, int keyCode);

	/**
	 * @param entity
	 * @param yaw
	 * @param pitch
	 */
	public abstract void actionSetEntityDirection(Entity entity, float yaw, float pitch);

	/**
	 * 
	 */
	public abstract void actionRespawnPlayer();

	/**
	 * @param distance
	 */
	public abstract void actionSetRenderDistance(String distance);

	/**
	 * 
	 */
	public abstract void onTick();

	/**
	 * @param targetName
	 * @param logMessage
	 */
	public abstract void actionAddLogMessage(String targetName, String logMessage);
	
	public abstract void actionSetLabel(String targetName, String text, String binding);

	public abstract AutoCraftingToken actionCraft(IAutoCraftingInitiator initiator, EntityClientPlayerMP thePlayer, String itemId, int damageValue, int amount, boolean shouldThrowResult, boolean verbose);

	public abstract void actionBreakLoop(IMacro macro, IMacroAction breakAction);
	
	public abstract void actionBeginUnsafeBlock(IMacro macro, IMacroAction instance, int maxActions);
	
	public abstract void actionEndUnsafeBlock(IMacro macro, IMacroAction instance);

	public abstract void actionScheduleResChange(int width, int height);

	public abstract String getSoundResourceNamespace();
}