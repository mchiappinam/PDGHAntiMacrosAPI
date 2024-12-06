package net.eq2online.macros.scripting;

/**
 * Interpolator object which interpolates float values
 *
 * @author Adam Mummery-Smith
 */
public class FloatInterpolator
{
	/**
	 * Type of interpolation to apply
	 *
	 * @author Adam Mummery-Smith
	 */
	public enum InterpolationType
	{
		Linear,
		Smooth
	}
	
	/**
	 * Type of interpolation for this interpolator instance
	 */
	protected InterpolationType interpolationType;

	/**
	 * Interpolation value
	 */
	public float start, target;
	
	/**
	 * Interpolation start time
	 */
	public long startTime, duration;
	
	/**
	 * True once interpolations has completed
	 */
	public boolean finished = false;
	
	/**
	 * Multipler controls the direction of interpolation, 1 for positive interpolation, -1 for negative interpolation
	 */
	protected float interpolationMultiplier;
	
	public FloatInterpolator(float startValue, float targetValue, long duration, InterpolationType interpolationType)
	{
		this.interpolationType = interpolationType;
		
		this.startTime = System.currentTimeMillis();
		this.duration = duration; 
		
		this.start = startValue;
		this.target = targetValue;
		
		if (startValue == targetValue || duration == 0)
			this.finished = true;
		else if (startValue > targetValue)
			this.interpolationMultiplier = -1;
		else
			this.interpolationMultiplier = 1;
	}
	
	/**
	 * Interpolate the internal value and return the new value
	 * 
	 * @return current interpolated value
	 */
	public Float interpolate()
	{
		if (this.finished)
		{
			return this.target;
		}
		
		float deltaTime = (float)(System.currentTimeMillis() - this.startTime) / (float)this.duration;
		
		if (deltaTime >= 1.0F)
		{
			this.finished = true;
			return this.target;
		}

		if (this.interpolationType == InterpolationType.Smooth)
		{
			deltaTime = (float)(Math.sin((deltaTime - 0.5) * Math.PI) * 0.5) + 0.5F;
		}
		
		float value = this.start + (deltaTime * ((this.target - this.start)));
		
		if ((this.interpolationMultiplier > 0 && value >= this.target) || (this.interpolationMultiplier < 0 && value <= this.target))
		{
			value = this.target;
			this.finished = true;
		}

		return value;
	}
}
