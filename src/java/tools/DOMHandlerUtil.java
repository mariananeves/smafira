package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMHandlerUtil {
	private static final String encoding = "UTF-8";
	
	
//	##### get XML Document #####
	
	public static Document httpResponseToXMLDocument(HttpResponse response){		
		Document doc = null;		
		
		try {
			doc = getXMLDocumentFromInputStream(response.getEntity().getContent());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	
	public static Document getXMLDocumentFromSource(String sourcePath){
		return getXMLDocumentFromFile(new File(sourcePath));
	}
	
	public static Document getXMLDocumentFromFile(File source){
		Document doc = null;
		try {			
			doc = getXMLDocumentFromInputStream(new FileInputStream(source));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return doc;
	}
		
	public static Document getXMLDocumentFromInputStream(InputStream is){
		Document doc = null;
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
					
			doc = docBuilder.parse(is);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	
	public static ArrayList<Document> getXMLDocumentsFromDirectory(String dirPath){
		ArrayList<Document> docs = new ArrayList<Document>();
		
		
		String[] xmlPaths = getXMLFilePaths(dirPath);
		for(String path: xmlPaths){
			path = dirPath + path;
			docs.add(getXMLDocumentFromFile(new File(path)));
		}
		
		return docs;
	}
	
	
	public static String[] getXMLFilePaths(String dirPath){
		File file = new File(dirPath);
		
		if(file.exists() && file.isDirectory()){
			return file.list(new XMLFilter());
		}
		
		return null;
	}
	
//	##########
	
	
//	##### write XML Document #####
	
	
	
	/*
	 * source: http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
	 */
	public static void writeXMLFile(Document doc, String dest){
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(dest));
 
			transformer.transform(source, result);
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
//	##########
	
	
//	##### Handle XML Document #####
	/*
	 * source: http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
	 */
	public static Document createEmptyXMLDocument(){
		Document doc = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			doc = docBuilder.newDocument();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	
	public static NodeList findElementsByXPath(Node node, String xpathExprStr) throws XPathExpressionException{
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(xpathExprStr);
		return (NodeList) expr.evaluate(node, XPathConstants.NODESET);
	}
	
	
	public static String getNodeValue(Node root, String nodePath) throws TransformerException{
		Node node = XPathAPI.selectSingleNode(root, nodePath);
		
		if(node != null)
			return node.getTextContent();
		
		return "";
	}
	
	
	public static ArrayList<String> getChildNodesValues(Node parent){
		ArrayList<String> childNodesValues = new ArrayList<String>();
		
		if(parent.hasChildNodes()){
			NodeList childList = parent.getChildNodes();
			
			for(int i = 0; i < childList.getLength(); i++){
				childNodesValues.add(childList.item(i).getTextContent());
			}
			
			return childNodesValues;
		}
		
		
		return null;
	}
	
	public static ArrayList<String> getChildNodesValuesWithAttribute(Node parent, String attrName){
		ArrayList<String> childNodesValues = new ArrayList<String>();
		
		if(parent.hasChildNodes()){
			NodeList childList = parent.getChildNodes();
			Node child, attribute;
			String content;
			
			for(int i = 0; i < childList.getLength(); i++){
				content = "";
				child = null;
				attribute = null;
				child = childList.item(i);
				if(child != null){
					NamedNodeMap mp = child.getAttributes();
					
					if(mp != null)
						attribute = mp.getNamedItem(attrName);
					
					content = child.getTextContent();
					
					if(attribute != null)
						content += " +++ " + attribute.getTextContent();
					
					childNodesValues.add(content);					
				}
				
			}
			
			return childNodesValues;
		}
		
		
		return null;
	}
	
	
//	##########
	
	
	public static class XMLFilter implements FilenameFilter{
		@Override
		public boolean accept(File dir, String name) {
			if(name.endsWith(".xml"))
				return true;
			return false;
		}
		
	}
}
