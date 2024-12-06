package net.eq2online.macros.scripting;

/**
 * Struct which encapsulates a yaw and pitch direction of view  
 *
 * @author Adam Mummery-Smith
 */
public class Direction
{
	/**
	 * Yaw, check the IncludesYaw member to see whether this value has been set or not 
	 */
	public float yaw;
	
	/**
	 * Pitch, check the IncludesPitch member to see whether this value has been set or not
	 */
	public float pitch;
	
	/**
	 * True if the yaw was set
	 */
	public boolean includesYaw;
	
	/**
	 * True if the pitch was set
	 */
	public boolean includesPitch;
	
	/**
	 * Interpolation time if interpolating to this value
	 */
	public long duration;
	
	public Direction()
	{
	}
	
	/**
	 * Create a new direction with the specified pitch and yaw
	 * 
	 * @param yaw
	 * @param pitch
	 */
	public Direction(float yaw, float pitch)
	{
		while (yaw < 0F) yaw += 360F;
		while (yaw > 360F) yaw -= 360F;

		while (pitch < 0F) pitch += 360F;
		while (pitch > 360F) pitch -= 360F;

		this.yaw   = yaw;
		this.pitch = pitch;
	}

	/**
	 * Set and include the yaw value
	 * 
	 * @param yaw
	 */
	public void setYaw(float yaw)
	{
		this.yaw = yaw;
		this.includesYaw = true;
	}
	
	/**
	 * Set and include the pitch value
	 * 
	 * @param pitch
	 */
	public void setPitch(float pitch)
	{
		this.pitch = pitch;
		this.includesPitch = true;
	}

	/**
	 * Set and include both the yaw and pitch values
	 * 
	 * @param yaw New yaw value
	 * @param pitch New pitch value
	 */
	public void setYawAndPitch(float yaw, float pitch)
	{
		this.setPitch(pitch);
		this.setYaw(yaw);
	}
	
	/**
	 * Set the interpolation duration (in milliseconds)
	 * @param duration
	 */
	public void setDuration(long duration)
	{
		this.duration = duration;
	}
	
	/**
	 * Check whether either pitch or yaw were set
	 * @return
	 */
	public boolean isEmpty()
	{
		return (!this.includesPitch && !this.includesYaw);
	}

	/**
	 * Create a copy of this direction
	 * 
	 * @return
	 */
	public Direction cloneDirection()
	{
		return new Direction(this.yaw, this.pitch);
	}
}