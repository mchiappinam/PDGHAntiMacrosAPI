package net.eq2online.macros.scripting;

/**
 * Wraps two float interpolators to facilitate interpolating both components of a direction (pitch and yaw)
 * 
 * @author Adam Mummery-Smith
 *
 */
public class DirectionInterpolator
{
	/**
	 * Inner FloatInterpolator objects which interpolate the yaw and pitch
	 */
	protected FloatInterpolator yawInterpolator, pitchInterpolator;
	
	/**
	 * Interpolator id, used to ensure only the latest interpolator is used
	 */
	public final int id; 
	
	public DirectionInterpolator(Direction initialDirection, Direction targetDirection, FloatInterpolator.InterpolationType interpolationType, int id)
	{
		this.id = id;
		
		if (initialDirection.yaw - targetDirection.yaw >= 180F)
			targetDirection.yaw += 360F;
		else if (targetDirection.yaw - initialDirection.yaw > 180F)
			initialDirection.yaw += 360F;
		
		if (initialDirection.pitch - targetDirection.pitch >= 180F)
			targetDirection.pitch += 360F;
		else if (targetDirection.pitch - initialDirection.pitch > 180F)
			initialDirection.pitch += 360F;

		this.yawInterpolator = new FloatInterpolator(initialDirection.yaw, targetDirection.yaw, targetDirection.duration, interpolationType);
		this.pitchInterpolator = new FloatInterpolator(initialDirection.pitch, targetDirection.pitch, targetDirection.duration, interpolationType);
	}
	
	/**
	 * Perform interpolation
	 * 
	 * @return
	 */
	public Direction interpolate()
	{
		return new Direction(this.yawInterpolator.interpolate(), this.pitchInterpolator.interpolate());
	}
	
	/**
	 * Returns true if this interpolator has completed
	 * 
	 * @return
	 */
	public boolean isFinished()
	{
		return this.yawInterpolator.finished && this.pitchInterpolator.finished;
	}
}