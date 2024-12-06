package net.eq2online.macros.scripting.api;


/**
 * Variable provider supplied by macro events to event-triggered macros.  
 *
 * @author Adam Mummery-Smith
 */
public interface IMacroEventVariableProvider extends IVariableProvider
{
	public abstract void initInstance(String[] instanceVariables);
}
