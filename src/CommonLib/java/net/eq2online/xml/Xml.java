package net.eq2online.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.eq2online.console.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML helper functions to make working with XML in Java less of a nuisance
 * 
 * @author Adam Mummery-Smith
 */
public class Xml
{
	/**
	 * xpath evaluator
	 */
	private static XPath xpath;
	
	/**
	 * Namespace context helper for resolving namespaces
	 */
	private static Xmlns staticNamespaceContext;
	
	/**
	 * Factory for document builders
	 */
	private static DocumentBuilderFactory documentBuilderFactory;

	/**
	 * Document builder
	 */
	private static DocumentBuilder documentBuilder;
	
	/**
	 * Factory for transformers (not the big robotic kind, the small boring kind)
	 */
	private static TransformerFactory transformerFactory;
	
	/**
	 * Robots in disguise! (Wait, no...) :)
	 */
	private static Transformer transformer;

	static
	{
		// XML namespace manager which is used if a context is not specified
		staticNamespaceContext = new Xmlns();

		// Create the xpath evaluator
		xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(staticNamespaceContext);
		
		// Create the document builder factory and enable namespaces
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		createDocumentBuilder(documentBuilderFactory);

		// Create the transformer factory
		transformerFactory = TransformerFactory.newInstance();
		createTransformer(transformerFactory);
	}
	
	/**
	 * Attempts to create the document builder if one is not initialised already. This may
	 * fail in which case the function will return FALSE
	 * 
	 * @param factory
	 * @return
	 */
	protected static boolean createDocumentBuilder(DocumentBuilderFactory factory)
	{
		if (documentBuilder == null)
		{
			try
			{
				// Create the document builder
				documentBuilder = documentBuilderFactory.newDocumentBuilder();
			}
			catch (ParserConfigurationException e) { return false; }
		}
		
		return true;
	}
	
	/**
	 * Attempts to create the dom transformer if one is not initialised already. This may
	 * fail in which case the function will return FALSE
	 * 
	 * @param factory
	 * @return
	 */
	protected static boolean createTransformer(TransformerFactory factory)
	{
		if (transformer == null)
		{
			try
			{
				transformer = transformerFactory.newTransformer();
			}
			catch (TransformerConfigurationException e) { return false; }

			// Set transformer properties for the desired output layout that we need for transformation 
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		}
		
		return true;
	}
	
	/**
	 * Create and return a new, empty document. Returns null if the document builder cannot be initialised
	 * @return new document
	 */
	public static Document createDocument()
	{
		return (createDocumentBuilder(documentBuilderFactory)) ? documentBuilder.newDocument() : null;
	}
	
	/**
	 * Loads a document from the specified URI. Returns null if the document builder cannot be initialised
	 * @param uri URI to load the xml from
	 * @return new document
	 */
	public static Document getDocument(String uri) throws SAXException, IOException
	{
		return (createDocumentBuilder(documentBuilderFactory)) ? documentBuilder.parse(uri) : null;
	}
	
	/**
	 * Loads a document from the specified file. Returns null if the document builder cannot be initialised
	 * @param file File to load from
	 * @return new document
	 */
	public static Document getDocument (File file) throws SAXException, IOException
	{
		return (createDocumentBuilder(documentBuilderFactory)) ? documentBuilder.parse(file) : null;
	}

	public static Document getDocument (InputStream stream) throws SAXException, IOException
	{
		return (createDocumentBuilder(documentBuilderFactory)) ? documentBuilder.parse(stream) : null;
	}
	
	/**
	 * Attempts to save the specified document to the specified file
	 * 
	 * @param file File to save to
	 * @param document DOM Document to save 
	 * @return True if the file was saved successfully, false if saving the file failed
	 */
	public static boolean saveDocument(File file, Document document)
	{
		if (createTransformer(transformerFactory))
		{
			try
			{
				transformer.transform(new DOMSource(document), new StreamResult(file));
				return true;
			}
			catch (TransformerException e) {}
		}
		
		return false;
	}

	/**
	 * Add a namespace prefix to the current static namespace manager
	 * 
	 * @param prefix
	 * @param namespaceURI
	 */
	public static void addNS(String prefix, String namespaceURI)
	{
		staticNamespaceContext.addPrefix(prefix, namespaceURI);
	}
	
	/**
	 * Clear namespace prefixes in the current static namespace context
	 */
	public static void clearNS()
	{
		staticNamespaceContext.clear();
	}

	/**
	 * Replace the current static namespace context with the supplied namespace context
	 *  
	 * @param namespaceContext
	 */
	public static void setNamespaceContext(Xmlns namespaceContext)
	{
		if (namespaceContext != null)
		{
			staticNamespaceContext = namespaceContext;
		}
	}
	
	/**
	 * 
	 * @param xml
	 * @param xPath
	 * @return
	 */
	public static NodeList query(Node xml, String xPath)
	{
		return query(xml, xPath, staticNamespaceContext);
	}
	
	public static NodeList query(Node xml, String xPath, NamespaceContext namespaceContext)
	{
		try
		{
			xpath.setNamespaceContext(namespaceContext);

//    		Log.info("Result is: " + xpath.evaluate(xPath, xml));
			return (NodeList)xpath.evaluate(xPath, xml, XPathConstants.NODESET);
		}
		catch (XPathExpressionException ex)
		{
			Log.printStackTrace(ex);
			return null;
		}
	}
	
	public static ArrayList<Node> queryAsArray(Node xml, String xPath)
	{
		return queryAsArray(xml, xPath, staticNamespaceContext);
	}

	public static ArrayList<Node> queryAsArray(Node xml, String xPath, NamespaceContext namespaceContext)
	{
		ArrayList<Node> nodes = new ArrayList<Node>();
		NodeList result = query(xml, xPath, namespaceContext);
		
		if (result != null)
		{
			for (int nodeIndex = 0; nodeIndex < result.getLength(); nodeIndex++)
				nodes.add(result.item(nodeIndex));
		}
		
		return nodes;
	}
	
	public static Node getNode(Node node, String nodeName)
	{
		return getNode(node, nodeName, staticNamespaceContext);
	}

	public static Node getNode(Node node, String nodeName, NamespaceContext namespaceContext)
	{
    	try
		{
    		xpath.setNamespaceContext(namespaceContext);
			return (Node)xpath.evaluate(nodeName, node, XPathConstants.NODE);
		}
		catch (XPathExpressionException e) {}

		return null;
	}
	
	public static String getNodeValue(Node node, String nodeName, String defaultValue)
	{
		return getNodeValue(node, nodeName, defaultValue, staticNamespaceContext);
	}

	public static String getNodeValue(Node node, String nodeName, String defaultValue, NamespaceContext namespaceContext)
	{
    	try
		{
    		xpath.setNamespaceContext(namespaceContext);
			NodeList nodes = (NodeList)xpath.evaluate(nodeName, node, XPathConstants.NODESET);
			
			if (nodes.getLength() > 0)
				return nodes.item(0).getTextContent();
		}
		catch (XPathExpressionException ex)
		{
			Log.printStackTrace(ex);
		}

		return defaultValue;
	}
	
	public static int getNodeValue(Node node, String nodeName, int defaultValue)
	{
		return getNodeValue(node, nodeName, defaultValue, staticNamespaceContext);
	}
	
	public static int getNodeValue(Node node, String nodeName, int defaultValue, NamespaceContext namespaceContext)
	{
		String nodeValue = getNodeValue(node, nodeName, "" + defaultValue, namespaceContext);
		
		try
		{
			return Integer.parseInt(nodeValue);
		}
		catch (NumberFormatException ex)
		{
			return defaultValue;
		}
	}

	public static String getAttributeValue(Node node, String attributeName, String defaultValue)
	{
		Node attribute = node.getAttributes().getNamedItem(attributeName);
		
		if (attribute != null)
		{
			return attribute.getTextContent();
		}

		return defaultValue;
	}
	
	public static int getAttributeValue(Node node, String attributeName, int defaultValue)
	{
		String attributeValue = getAttributeValue(node, attributeName, "" + defaultValue);
		
		try
		{
			return Integer.parseInt(attributeValue);
		}
		catch (NumberFormatException ex)
		{
			return defaultValue;
		}
	}
}


