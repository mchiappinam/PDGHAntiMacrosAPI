package net.eq2online.macros.scripting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.eq2online.console.Log;
import net.eq2online.macros.scripting.api.IMacroEventProvider;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.api.IVariableProvider;

/**
 * Essentially a struct which stores information about loaded modules
 *
 * @author Adam Mummery-Smith
 */
public class LoadedModuleInfo
{
	/**
	 * Module file
	 */
	public final File module;
	
	/**
	 * Name of the module
	 */
	public final String name;
	
	/**
	 * Number of custom actions provided by this module
	 */
	private int customActionCount = 0;
	
	/**
	 * Number of custom variable providers
	 */
	private int customVariableProviderCount = 0;
	
	/**
	 * Number of custom iterators
	 */
	private int customIteratorCount = 0;
	
	/**
	 * Number of custom event providers
	 */
	private int customEventProviderCount = 0;
	
	/**
	 * Actions
	 */
	private List<String> actions = new ArrayList<String>();
	
	/**
	 * Variable Providers
	 */
	private List<String> providers = new ArrayList<String>();
	
	/**
	 * Iterators
	 */
	private List<String> iterators = new ArrayList<String>();
	
	/**
	 * Event providers
	 */
	private List<String> eventProviders = new ArrayList<String>();

	/**
	 * @param module
	 */
	public LoadedModuleInfo(File module)
	{
		this.module = module;
		this.name = module.getName();
	}
	
	/**
	 * @param action
	 */
	public IScriptAction addAction(IScriptAction action)
	{
		if (action != null && !this.actions.contains(action.toString()))
		{
			this.customActionCount++;
			this.actions.add(action.toString());
		}
		
		return action;
	}

	/**
	 * @param provider
	 */
	public IVariableProvider addProvider(IVariableProvider provider)
	{
		if (provider != null && !this.providers.contains(provider.getClass().getSimpleName()))
		{
			this.customVariableProviderCount++;
			this.providers.add(provider.getClass().getSimpleName());
		}
		
		return provider;
	}

	/**
	 * @param iterator
	 */
	public IScriptedIterator addIterator(IScriptedIterator iterator)
	{
		if (iterator != null && !this.iterators.contains(iterator.getClass().getSimpleName()))
		{
			this.customIteratorCount++;
			this.iterators.add(iterator.getClass().getSimpleName());
		}
		
		return iterator;
	}
	
	/**
	 * @param provider
	 */
	public IMacroEventProvider addEventProvider(IMacroEventProvider provider)
	{
		if (provider != null && !this.eventProviders.contains(provider.getClass().getSimpleName()))
		{
			this.customEventProviderCount++;
			this.eventProviders.add(provider.getClass().getSimpleName());
		}
		
		return provider;
	}

	/**
	 * 
	 */
	public void printStatus()
	{
		if (this.customActionCount + this.customVariableProviderCount > 0)
			Log.info("API Loaded module {0} found {1} custom action(s) {2} new variable provider(s) {3} new iterator(s), {4} event provider(s)", this.name, this.customActionCount, this.customVariableProviderCount, this.customIteratorCount, this.customEventProviderCount);
	}

}
