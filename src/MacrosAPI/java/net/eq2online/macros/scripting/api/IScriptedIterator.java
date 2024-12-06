package net.eq2online.macros.scripting.api;

/**
 * Interface for scripted iterators
 * 
 * @author Adam Mummery-Smith
 */
public interface IScriptedIterator extends IVariableProvider
{
	/**
	 * Should return true while values are still available and execution is not broken, effectively the same
	 * as Iterator.hasNext().
	 * 
	 * @return
	 */
	public abstract boolean continueLooping();
	
	/**
	 * Effectively the same as Iterator.next()
	 */
	public abstract void increment();
	
	/**
	 * Rewind this iterator
	 */
	public abstract void reset();
	
	/**
	 * Break out of this loop
	 */
	public abstract void breakLoop();
}