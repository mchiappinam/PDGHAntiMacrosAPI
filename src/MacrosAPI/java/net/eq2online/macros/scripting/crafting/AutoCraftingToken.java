package net.eq2online.macros.scripting.crafting;

/**
 * Token returned from an auto-crafting request submission, used as a callback delegate from the auto-crafting
 * manager to notify the caller that a job has succeeded or failed.
 * 
 * @author Adam Mummery-Smith
 */
public class AutoCraftingToken
{
	/**
	 * Next available token ID
	 */
	private static int nextId = 0;
	
	/**
	 * Token ID
	 */
	public final int id;
	
	/**
	 * Initiator to callback with crafting progress
	 */
	private IAutoCraftingInitiator initiator;
	
	/**
	 * Reason for completion or failure
	 */
	private String reason = null;
	
	/**
	 * @param initiator
	 */
	public AutoCraftingToken(IAutoCraftingInitiator initiator)
	{
		this.id = nextId++;
		this.initiator = initiator;
	}
	
	/**
	 * @param initiator
	 * @param reason
	 */
	public AutoCraftingToken(IAutoCraftingInitiator initiator, String reason)
	{
		this.id = nextId++;
		this.initiator = initiator;
		this.notifyCompleted(reason);
	}
	
	/**
	 * Called by the auto-crafting manager when crafting is completed or fails
	 * 
	 * @param reason
	 * @return
	 */
	public AutoCraftingToken notifyCompleted(String reason)
	{
		this.reason = reason;
		
		if (this.initiator != null)
		{
			this.initiator.notifyTokenProcessed(this, reason);
		}
		
		return this;
	}
	
	/**
	 * Check whether the token's job is completed
	 * 
	 * @return
	 */
	public boolean isCompleted()
	{
		return this.reason != null;
	}
	
	public String getReason()
	{
		return this.reason;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (!(obj instanceof AutoCraftingToken)) return false;
		return ((AutoCraftingToken)obj).id == this.id;
	}

	@Override
	public int hashCode()
	{
		return this.id;
	}
}
