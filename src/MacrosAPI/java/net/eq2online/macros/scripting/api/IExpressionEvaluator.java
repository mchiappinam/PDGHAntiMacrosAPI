package net.eq2online.macros.scripting.api;

/**
 * Interface for objects which can evaluate expressions dynamically
 *
 * @author Adam Mummery-Smith
 */
public interface IExpressionEvaluator extends IVariableListener
{
	/**
	 * Dump variables to the console
	 */
	public abstract void dumpVariables();
	
	/**
	 * Add a string literal
	 * 
	 * @param literalString
	 * @return
	 */
	public abstract int addStringLiteral(String literalString);
	
	/**
	 * Evaluate the expression and return the result
	 * 
	 * @return result of evaluating the expression
	 */
	public abstract int evaluate();
	
	/**
	 * Get the result of the last evaluation
	 * 
	 * @return
	 */
	public abstract int getResult();
}