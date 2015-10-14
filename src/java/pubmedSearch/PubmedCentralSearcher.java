package pubmedSearch;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dataCollection.PubMedDocument.RelatedDocumentXPath;

import properties.PropertiesHandler;
import properties.PropertiesHandler.PropertiesHanderMode;
import properties.PropertiesHandler.PropertiesIDs;

import tools.DOMHandlerUtil;
import tools.HtmlDocumentUtil;
import tools.ToolsUtil;

public class PubmedCentralSearcher {
	
	private String PMC_ID_PLACEHOLDER = "$$$";
	private String pubmedCentralUri = "http://www.pubmedcentral.nih.gov/oai/oai.cgi?verb=GetRecord&identifier=oai:pubmedcentral.nih.gov:"
			+ PMC_ID_PLACEHOLDER + "&metadataPrefix=pmc";
	private String relatedSearchCollectionDirPath;
	
	public PubmedCentralSearcher(){
		PropertiesHandler ph = new PropertiesHandler(PropertiesHanderMode.ENTREZ);
		relatedSearchCollectionDirPath = ph.getPropertyValue(PropertiesIDs.PROJECT_PATH);
		relatedSearchCollectionDirPath += ph.getPropertyValue(PropertiesIDs.DATA_PMRELATEDSEARCH_TARGET_PATH);
	}
	
	
	public URI getPMCFulltextURI(String pmcId){
		String url = pubmedCentralUri.replace(PMC_ID_PLACEHOLDER, pmcId);
		URI uri = null;
		try {
			uri = new URI(url);			
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return uri;
	}
	
	public String getPMCFulltextXMLAsString(String pmcId){		
		String fullTextXml = "";
		URI uri = getPMCFulltextURI(pmcId);		
		
		fullTextXml = HtmlDocumentUtil.getHtmlDocumentAsString(uri);
		return fullTextXml;
	}
	
	
	public HashMap<String, String> getAllPMCIds(String refdocPmid){
		HashMap<String, String> pmcIds = new HashMap<String, String>();
		
		String sourceFilePath = relatedSearchCollectionDirPath + refdocPmid + ".xml";		
		Document doc = DOMHandlerUtil.getXMLDocumentFromSource(sourceFilePath);
		Node root = doc.getDocumentElement();
		
		String relDocNodeXPath = RelatedDocumentXPath.RELDOC_ROOT.getPath();
		String pmidNodeXPath = RelatedDocumentXPath.ID.getPath();
		String pmcIdNodeXPath = RelatedDocumentXPath.PMCID.getPath();
		
		String pmid, pmcId;
		
try {
			
			NodeList relDocList = XPathAPI.selectNodeList(root, relDocNodeXPath);
			if(relDocList != null){
				for(int i = 0; i < relDocList.getLength(); i++){
					Node relDoc = relDocList.item(i);
					
					if(relDoc != null){
						
						Node pmidNode = XPathAPI.selectSingleNode(relDoc, pmidNodeXPath);
						pmid = pmidNode.getTextContent();
						
						Node pmcIdNode = XPathAPI.selectSingleNode(relDoc, pmcIdNodeXPath);
						
						if(pmcIdNode != null){
							pmcId = pmcIdNode.getTextContent();
							if(!pmcId.isEmpty()){
								pmcIds.put(pmid, pmcId);
							}
						}
						
					}
				}
			}
			
			
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		
		return pmcIds;
	}
	
	
	
	public static void test(){
		PubmedCentralSearcher searcher = new PubmedCentralSearcher();
		HashMap<String, String> pmcIds = searcher.getAllPMCIds("16192371");
		String destDir = "C:/Users/du/Projekte/SMAFIRA/git/data/html/pmcFulltexts/";
		String destFilePath;
		String pmcId;
		
		Set<String> pmidList = pmcIds.keySet();
		
		for(String pmid: pmidList){
			pmcId = pmcIds.get(pmid);
			String htmlPage = searcher.getPMCFulltextXMLAsString(getPMCIDNumbers(pmcId));
			destFilePath = destDir + pmid + "_" + pmcId + ".xml";
			ToolsUtil.saveText(destFilePath, htmlPage);
		}		
	}
	
	private static String getPMCIDNumbers(String pmcId){
		String numbers = "";
		
		numbers = pmcId.substring(pmcId.indexOf("PMC") + "PMC".length());
		
		return numbers;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test();

	}

}
