package pubmedSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dataCollection.PubMedDocument.ContentXPath;

import tools.DOMHandlerUtil;
import tools.ToolsUtil;

public class PubMedDataHandler {
	private HttpSearcher pubMedSearcher;
	private NcbiEntrezURIBuilder uriBuilder;

	public PubMedDataHandler(){
		pubMedSearcher = new HttpSearcher();
		uriBuilder = new NcbiEntrezURIBuilder();
	}
	
	
	public ArrayList<String> getResultPubMedIDs(Document doc){
		ArrayList<String> resultPubMedIDs = new ArrayList<String>();
		
		String xPath = ContentXPath.ID_LIST_SEARCH.path + "/" + ContentXPath.ID_SEARCH.path;
		Node pubMedId;
		try {
			NodeList idList = DOMHandlerUtil.findElementsByXPath(doc.getDocumentElement(), xPath);
			if(idList != null && idList.getLength() > 0){
				for(int i = 0; i < idList.getLength(); i++){
					pubMedId = idList.item(i);
					resultPubMedIDs.add(pubMedId.getTextContent());
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		
		return resultPubMedIDs;
	}
	
	
	public ArrayList<String> getRandomResultPubMedDocIDList(Document doc, int numDocs){
		ArrayList<String> resultPubMedIDs = getResultPubMedIDs(doc);
		ArrayList<String> randomPubMedIDs = new ArrayList<String>();
		Random random = new Random();		
		random.setSeed(System.currentTimeMillis());
		Integer randomNumber;
		int maxRandom = resultPubMedIDs.size();
		
		HashSet<Integer> generatedNumbers = new HashSet<Integer>();
		
		for(int i = 0; i < numDocs; i++){
			randomNumber = random.nextInt(maxRandom);
			
			while(generatedNumbers.contains(randomNumber))
				randomNumber = random.nextInt(maxRandom);
			
			generatedNumbers.add(randomNumber);
			randomPubMedIDs.add(resultPubMedIDs.get(randomNumber) + ":" + (randomNumber+1));
		}
		
		return randomPubMedIDs;
	}
	
	public ArrayList<String> getIDsFromTextFile(String path){
		ArrayList<String> idList = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
			String id;
			
			while((id = reader.readLine()) != null){
				id = id.trim();
				if(!id.isEmpty())
					idList.add(id);
			}
			
			reader.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return idList;
	}
	
	public void getPubMedDocData(ArrayList<String> pubMedIDs){
		String query, result;
		URI searchUri;
		try {
			searchUri = uriBuilder.buildPubMedIDPostURI(pubMedIDs);
			result = pubMedSearcher.httpResponseAsText(pubMedSearcher.httpGetRequest(searchUri));
//			for(String id:pubMedIDs){
//				query = id + "[uid]";
//				searchUri = pubMedSearcher.buildSearchNCBIEntrezURI(query, null);
//				result = pubMedSearcher.httpResponseToString(pubMedSearcher.sendHttpRequest(searchUri));
//			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	
	public ArrayList<String> getPubmedIDsFromSummary(String summaryPath){
		ArrayList<String> pmIDs = new ArrayList<String>();
		
		Document doc = DOMHandlerUtil.getXMLDocumentFromSource(summaryPath);
		
		Node root;
		NodeList pmDocumentList;
		Node pmDocument;
		String pmid;
		
		
		try {
			root = doc.getDocumentElement();			
			pmDocumentList = XPathAPI.selectNodeList(root, ContentXPath.DOCROOT_SUM.path + "/" + ContentXPath.ID_SUM.path);
			System.out.println("length: " + pmDocumentList.getLength());
			if(pmDocumentList != null){
				for(int i = 0; i < pmDocumentList.getLength(); i++){
					pmDocument = pmDocumentList.item(i);
					
					if(pmDocument != null){						
						pmid = pmDocument.getTextContent();
						pmIDs.add(pmid);						
					}
				}
			}				
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		return pmIDs;
	}
	
//	##### get Search Parameters From XML Response Documents #####
	
	public NameValuePair[] getWebEnvAndQueryKey(Document doc){
		NameValuePair[] parameters = new NameValuePair[2];
		
		try {
			String webEnv = DOMHandlerUtil.getNodeValue(doc.getDocumentElement(), "WebEnv");
			String queryKey = DOMHandlerUtil.getNodeValue(doc.getDocumentElement(), "QueryKey");
			
			parameters[0] = new BasicNameValuePair("WebEnv", webEnv);
			parameters[1] = new BasicNameValuePair("query_key", queryKey);
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		return parameters;
	}
	
	
	public NameValuePair getWebEnvFromElinkResponse(Document responseDoc){		
		return getParameterFromResponse(responseDoc, "WebEnv", "LinkSet/WebEnv");
	}
	
	public NameValuePair getQueryKeyFromElinkResponse(Document responseDoc){		
		return getParameterFromResponse(responseDoc, "query_key", "LinkSet/LinkSetDbHistory/QueryKey");
	}
	
	public NameValuePair getParameterFromResponse(Document responseDoc, String name, String valuePath){
		NameValuePair pair = null;
		String value;
		
		try {
			value = DOMHandlerUtil.getNodeValue(responseDoc.getDocumentElement(), valuePath);
			pair = new BasicNameValuePair(name, value);
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		return pair;
	}
	
	
//	##########
	
	
//	##########
	
	public static void testSearch(){
		PubMedDataHandler litSearcher = new PubMedDataHandler();
		HttpSearcher testSearcher = new HttpSearcher();
		NcbiEntrezURIBuilder uriBuilder = new NcbiEntrezURIBuilder();
		
		try {
			URI uri = uriBuilder.buildSearchNCBIEntrezURI(null, null);
			Document doc = DOMHandlerUtil.httpResponseToXMLDocument(testSearcher.httpGetRequest(uri));
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static void testPost(){
		PubMedDataHandler litSearcher = new PubMedDataHandler();
		HttpSearcher testSearcher = new HttpSearcher();
		NcbiEntrezURIBuilder uriBuilder = new NcbiEntrezURIBuilder();
		
		try {
			URI uri = uriBuilder.buildSearchNCBIEntrezURI(null, null);
			Document doc = DOMHandlerUtil.httpResponseToXMLDocument(testSearcher.httpGetRequest(uri));
			litSearcher.getPubMedDocData(litSearcher.getResultPubMedIDs(doc));
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
//		testSearch();
		testPost();
		
	}
}
