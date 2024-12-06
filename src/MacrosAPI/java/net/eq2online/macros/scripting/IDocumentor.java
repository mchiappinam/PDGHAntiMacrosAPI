package net.eq2online.macros.scripting;

import net.eq2online.macros.scripting.api.IScriptAction;

/**
 * Interface for the documentor
 * 
 * @author Adam Mummery-Smith
 */
public interface IDocumentor
{
	/**
	 * Read documentor settings from the specified language file (if found)
	 * 
	 * @param language
	 * @return fluent interface
	 */
	public abstract IDocumentor loadXml(String language);

	/**
	 * Write current documentation entries to XML, used only for initial population of the XML file
	 * 
	 * @param xmlFile
	 */
	public abstract void writeXml(java.io.File xmlFile);

	/**
	 * Get the documentation entry for the named script action
	 * 
	 * @param scriptActionName
	 * @return
	 */
	public abstract IDocumentationEntry getDocumentation(String scriptActionName);
	
	/**
	 * Get the documentation entry for the supplied script action
	 * 
	 * @param scriptAction
	 * @return
	 */
	public abstract IDocumentationEntry getDocumentation(IScriptAction scriptAction);
	
	/**
	 * @param scriptAction
	 */
	public abstract void setDocumentation(IScriptAction scriptAction);

	/**
	 * @param scriptAction
	 */
	public abstract void appendScriptActionNode(IScriptAction scriptAction);
}
