package dataCollection;

import htmlSearch.PubmedFulltextReference;
import htmlSearch.HtmlWrapper.HtmlContentString;
import htmlSearch.wrapper.PubmedWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import properties.PropertiesHandler;
import properties.PropertiesHandler.PropertiesHanderMode;
import properties.PropertiesHandler.PropertiesIDs;
import tools.DOMHandlerUtil;
import dataCollection.PubMedDocument.ContentXPath;
import dataCollection.PubMedDocument.MeshEntry;
import dataCollection.PubMedDocument.RelatedDocumentXPath;
import db.AssessmentDBReader;

public class PubMedDataCollector {
	
	PubmedWrapper pubmedWrapper;
	
	public PubMedDataCollector(){
		pubmedWrapper = new PubmedWrapper();
	}	
	
	public LinkedHashMap<String, PubMedDocument> getContentFromESummaryOrFetchXML(Document resultDoc){
		LinkedHashMap<String, PubMedDocument> eSummaryDocs = new LinkedHashMap<String, PubMedDocument>();
		PubMedDocument pubMedDoc;
		Element root = resultDoc.getDocumentElement();
		NodeList retrievedDocs;
		Node pubmedDocNode;
		
		
		String docRootPath = "", idPath = "", titlePath = "", referencesPath = "", hasAbstractPath = "", docAbstractPath = "", pmcidPath = "", 
				doiPath = "", piiPath = "";
		boolean isSummaryResult = PubMedDocument.isSummaryResultDoc(root.getNodeName());
//		resultDoc is ESummary Result XML
		if(isSummaryResult){
			docRootPath = ContentXPath.DOCROOT_SUM.getPath();
			idPath = ContentXPath.ID_SUM.getPath();
			titlePath = ContentXPath.TITLE_SUM.getPath();
			referencesPath = ContentXPath.REFERENCES_SUM.getPath();
			hasAbstractPath = ContentXPath.HASABSTRACT_SUM.getPath();
			pmcidPath = ContentXPath.PMCID_SUM.getPath();
			doiPath = ContentXPath.DOI_SUM.getPath();
			piiPath = ContentXPath.PII_SUM.getPath();
			
		}
//		resultDoc is EFetch Result XML
		else{
			docRootPath = ContentXPath.DOCROOT_FET.getPath();
			idPath = ContentXPath.ID_FET.getPath();
			titlePath = ContentXPath.TITLE_FET.getPath();
			docAbstractPath = ContentXPath.ABSTRACT_FET.getPath();
			pmcidPath = ContentXPath.PMCID_FET.getPath();			
		}
		
		try {
			retrievedDocs = XPathAPI.selectNodeList(root, docRootPath);
			
			if(retrievedDocs != null){
				String pubMedID;
				for(int i = 0; i < retrievedDocs.getLength(); i++){
					pubMedDoc = new PubMedDocument();
					
					pubmedDocNode = retrievedDocs.item(i);
					pubMedID = DOMHandlerUtil.getNodeValue(pubmedDocNode, idPath);
					pubMedDoc.setDocPubMedID(pubMedID);
					pubMedDoc.setDocTitle(DOMHandlerUtil.getNodeValue(pubmedDocNode, titlePath));
					
//					only in eSummary result
					if(isSummaryResult){
						pubMedDoc.setReferences(getReferences(XPathAPI.selectSingleNode(pubmedDocNode, referencesPath)));
						pubMedDoc.setHasAbstract((DOMHandlerUtil.getNodeValue(pubmedDocNode, hasAbstractPath).equals("1")));
//						TODO: pmcid needs to be filtered from text (only summary)
						pubMedDoc.setDocPMCID(DOMHandlerUtil.getNodeValue(pubmedDocNode, pmcidPath));
						pubMedDoc.setDoi(DOMHandlerUtil.getNodeValue(pubmedDocNode, doiPath));
						pubMedDoc.setPii(DOMHandlerUtil.getNodeValue(pubmedDocNode, piiPath));
					}
//					only in eFetch result
					else{
						pubMedDoc.setDocAbstract(DOMHandlerUtil.getNodeValue(pubmedDocNode, docAbstractPath));
						pubMedDoc.setDocPMCID(DOMHandlerUtil.getNodeValue(pubmedDocNode, pmcidPath));											
						pubMedDoc.setMeshTerms(getMeshTerms(pubMedDoc, pubmedDocNode));
					}
					
					eSummaryDocs.put(pubMedID, pubMedDoc);
				}
			}
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		return eSummaryDocs;
	}
	
	public ArrayList<MeshEntry> getMeshTerms(PubMedDocument pubMedDoc, Node pubmedDocNode) throws TransformerException{
		String meshPath = ContentXPath.MESHTERMS_FET.getPath();
		ArrayList<MeshEntry> meshTerms = new ArrayList<MeshEntry>();
		NodeList meshDocs;
		Node meshHeading, meshContent;
		MeshEntry mEntry;
		
		meshDocs = XPathAPI.selectNodeList(pubmedDocNode, meshPath);
		
		if(meshDocs != null){
			for(int k = 0; k < meshDocs.getLength(); k++){
				meshHeading = meshDocs.item(k);
				if(meshHeading != null){
					mEntry = pubMedDoc.new MeshEntry();
//					Descriptor Content
					meshContent = XPathAPI.selectSingleNode(meshHeading, ContentXPath.MESHDESCRIPTOR.getPath());
					if(meshContent != null){
						mEntry.setDescriptorName(meshContent.getTextContent());
						mEntry.setDescriptorMajorTopic(meshContent.getAttributes().getNamedItem(ContentXPath.MESH_ATTR_MAJORTOPIC.getPath()).getTextContent());
						mEntry.setDescriptorUI(meshContent.getAttributes().getNamedItem(ContentXPath.MESH_ATTR_UI.getPath()).getTextContent());
					}
//					Qualifier Content
					meshContent = XPathAPI.selectSingleNode(meshHeading, ContentXPath.MESHQUALIFIER.getPath());
					if(meshContent != null){
						mEntry.setQualifierName(meshContent.getTextContent());
						mEntry.setQualifierMajorTopic(meshContent.getAttributes().getNamedItem(ContentXPath.MESH_ATTR_MAJORTOPIC.getPath()).getTextContent());
						mEntry.setQualifierUI(meshContent.getAttributes().getNamedItem(ContentXPath.MESH_ATTR_UI.getPath()).getTextContent());
					}
					meshTerms.add(mEntry);
				}
			}
		}
		
		return meshTerms;
	}
	
	private ArrayList<String> getReferences(Node parent){		
		return DOMHandlerUtil.getChildNodesValuesWithAttribute(parent, "Name");
	}
	
	
	public HashMap<String, String> getAbstractsFromEFetch(String abstractCollection){
		StringReader sr = new StringReader(abstractCollection);
		return getAbstractsFromEFetchTextFile(sr);
	}
	
	public HashMap<String, String> getAbstractsFromEFetch(File abstractCollectionFile){
		
		try {
			FileReader fr = new FileReader(abstractCollectionFile);			
			return getAbstractsFromEFetchTextFile(fr);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		
		return new HashMap<String, String>();
	}
	
	
	
	public HashMap<String, String> getAbstractsFromEFetchTextFile(Reader abstractCollectionReader){
		HashMap<String, String> abstracts = new HashMap<String, String>();
		
		
		BufferedReader br = new BufferedReader(abstractCollectionReader);
		String line;
		String docAbstract = "";
		String pubMedID;
		
		try {
			while((line = br.readLine()) != null){
				if(line.startsWith("PMID: ")){
					pubMedID = getPubMedIDFromAbstract(line);
					abstracts.put(pubMedID, docAbstract.trim());
					docAbstract = "";
				}
				else{
					docAbstract += line + "\n";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return abstracts;
	}
	
	
	private String getPubMedIDFromAbstract(String idLine){
		String id = idLine.substring(idLine.indexOf(" ")).trim();
		id = id.substring(0, id.indexOf(" ")).trim();
		
		return id;
	}
	
	
	
	public void writeDocsToXML(String targetPath, String referenceDocID, LinkedHashMap<String, PubMedDocument> summaryResultDocs, LinkedHashMap<String, PubMedDocument> fetchResultDocs){
		Document doc = createResultXMLDoc(referenceDocID, summaryResultDocs, fetchResultDocs);
		
		String resultDirPath = targetPath + referenceDocID + ".xml";
				
		DOMHandlerUtil.writeXMLFile(doc, resultDirPath);
	}
	
	private Document createResultXMLDoc(String referenceDocID, LinkedHashMap<String, PubMedDocument> summaryResultDocs, LinkedHashMap<String, PubMedDocument> fetchResultDocs){
		Document doc = DOMHandlerUtil.createEmptyXMLDocument();
		LinkedHashMap<String, PubMedDocument> resultDocs = mergeSummaryAndFetchResults(summaryResultDocs, fetchResultDocs);
		addFulltextInfo(resultDocs);
		
//		Set<String> resultKeySet = resultDocs.keySet();
		PubMedDocument resultDoc;
		
		Element root = doc.createElement(RelatedDocumentXPath.ROOT.getPath());
		Attr searchDocPMID = doc.createAttribute(RelatedDocumentXPath.SEARCHDOC_ID_ATTR.getPath());
		searchDocPMID.setValue(referenceDocID);
		root.setAttributeNode(searchDocPMID);
		doc.appendChild(root);
		
		
		int rankCounter = 0;
		for(Map.Entry<String, PubMedDocument>entry: resultDocs.entrySet()){
			rankCounter++;
			resultDoc = entry.getValue();
			
			Element resultRoot = doc.createElement(RelatedDocumentXPath.RELDOC_ROOT.getPath());
			root.appendChild(resultRoot);
			
			
//			doc rank
			createAndAddTextElement(RelatedDocumentXPath.RANK.getPath(), String.valueOf(rankCounter), resultRoot, doc);
			
//			doc id	
			createAndAddTextElement(RelatedDocumentXPath.ID.getPath(), resultDoc.getDocPubMedID(), resultRoot, doc);
			
//			doc title
			createAndAddTextElement(RelatedDocumentXPath.TITLE.getPath(), resultDoc.getDocTitle(), resultRoot, doc);
			
//			doc abstract
			createAndAddTextElement(RelatedDocumentXPath.ABSTRACT.getPath(), resultDoc.getDocAbstract(), resultRoot, doc);
			
			
			
//			Element references = doc.createElement(RelatedDocumentXPath.REFERENCES.getPath());
////			TODO: schauen, wie references aussehen, um hinzuzufuegen
//			resultRoot.appendChild(references);
			
//			pmcid
			createAndAddTextElement(RelatedDocumentXPath.PMCID.getPath(), resultDoc.getDocPMCID(), resultRoot, doc);
			
			
//			Fulltext Links
			createFulltextLinks(doc, resultRoot, resultDoc);
			
			
//			Mesh Terms
			createMeshTerms(doc, resultRoot, resultDoc);
			
		}		
		
		return doc;
	}
	
	private void createFulltextLinks(Document doc, Element parent, PubMedDocument resultDoc){
		Element fulltextLinks = createAndAddTextElement(RelatedDocumentXPath.FULLTEXTLINKS_ROOT.getPath(), null, parent, doc);
		String freeStatusValue;
		
		if(resultDoc.getFulltextReferences() != null){
			for(PubmedFulltextReference reference: resultDoc.getFulltextReferences()){
				Element fulltextLink = createAndAddTextElement(RelatedDocumentXPath.FULLTEXTLINK.getPath(), null, fulltextLinks, doc);
				
				createAndAddTextElement(RelatedDocumentXPath.LINK_URL_NODE.getPath(), reference.getFullTextLink(), fulltextLink, doc);
				
				if(reference.getFreeStatus() != null)
					freeStatusValue = Boolean.toString(reference.getFreeStatus());
				else
					freeStatusValue = null;				
				createAndAddTextElement(RelatedDocumentXPath.LINK_FREE_STATUS_NODE.getPath(), freeStatusValue, fulltextLink, doc);
				
				createAndAddTextElement(RelatedDocumentXPath.LINK_JOURNAL_NODE.getPath(), reference.getJournal(), fulltextLink, doc);
			}
		}
	}
	
	private void createMeshTerms(Document doc, Element parent, PubMedDocument resultDoc){
		Element meshTerms = createAndAddTextElement(RelatedDocumentXPath.MESHTERMS_ROOT.getPath(), null, parent, doc);
		
		if(resultDoc.getMeshTerms() != null){
			for(MeshEntry meshEntry: resultDoc.getMeshTerms()){
				Element meshTerm = createAndAddTextElement(RelatedDocumentXPath.MESHTERM.getPath(), null, meshTerms, doc);
				createAndAddTextElement(RelatedDocumentXPath.DESCRIPTORNAME.getPath(), meshEntry.getDescriptorName(), meshTerm, doc);
				createAndAddTextElement(RelatedDocumentXPath.DESCRIPTORMAJORTOPIC.getPath(), meshEntry.getDescriptorMajorTopic(), meshTerm, doc);
				createAndAddTextElement(RelatedDocumentXPath.DESCRIPTORUI.getPath(), meshEntry.getDescriptorUI(), meshTerm, doc);
				
				createAndAddTextElement(RelatedDocumentXPath.QUALIFIERNAME.getPath(), meshEntry.getQualifierName(), meshTerm, doc);
				createAndAddTextElement(RelatedDocumentXPath.QUALIFIERMAJORTOPIC.getPath(), meshEntry.getQualifierMajorTopic(), meshTerm, doc);
				createAndAddTextElement(RelatedDocumentXPath.QUALIFIERUI.getPath(), meshEntry.getQualifierUI(), meshTerm, doc);
			}
		}
		
	}
	
//	returns new Element
	private Element createAndAddTextElement(String newElementXPath, String newElementContent, Element parent, Document doc){
		Element newElement = doc.createElement(newElementXPath);
		if(newElementContent != null)
			newElement.appendChild(doc.createTextNode(newElementContent));
		parent.appendChild(newElement);
		
		return newElement;
	}
	
	public void addFulltextInfo(LinkedHashMap<String, PubMedDocument> resultDocs){
		PubMedDocument resultDoc;
		ArrayList<PubmedFulltextReference> fulltextReferences;
		String referenceUrl;
		
		for(Map.Entry<String, PubMedDocument>entry: resultDocs.entrySet()){
			resultDoc = entry.getValue();
			
			referenceUrl = HtmlContentString.HTML_PAGE_BASEURL.getPath() + resultDoc.getDocPubMedID();
			fulltextReferences = pubmedWrapper.getFulltextReferences(referenceUrl);
			
			resultDoc.setFulltextReferences(fulltextReferences);
		}
	}
	
	public LinkedHashMap<String, PubMedDocument> mergeSummaryAndFetchResults(LinkedHashMap<String, PubMedDocument> summaryResultDocs, LinkedHashMap<String, PubMedDocument> fetchResultDocs){
//		no merge needed, if at least one list is empty
		if(summaryResultDocs == null || summaryResultDocs.isEmpty())
			return fetchResultDocs;
		if(fetchResultDocs == null || fetchResultDocs.isEmpty())
			return summaryResultDocs;
		
//		merge
		LinkedHashMap<String, PubMedDocument> mergeDocs = new LinkedHashMap<String, PubMedDocument>();
		Set<String> summaryKeySet = summaryResultDocs.keySet();
		Set<String> fetchKeySet = fetchResultDocs.keySet();
		
		PubMedDocument summaryResultDoc, fetchResultDoc;
		for(String summaryKey: summaryKeySet){
			summaryResultDoc = summaryResultDocs.get(summaryKey);
			fetchResultDoc = fetchResultDocs.get(summaryKey);
			
			if(fetchResultDoc != null){
				summaryResultDoc.setDocAbstract(fetchResultDoc.getDocAbstract());
				summaryResultDoc.setDocPMCID(fetchResultDoc.getDocPMCID());
				summaryResultDoc.setMeshTerms(fetchResultDoc.getMeshTerms());
				fetchKeySet.remove(summaryKey);
			}
			
			
			mergeDocs.put(summaryKey, summaryResultDoc);			
		}
		
		
//		add poss. missing documents from efetch
		if(!fetchKeySet.isEmpty()){
			for(String fetchKey: fetchKeySet){
				mergeDocs.put(fetchKey, fetchResultDocs.get(fetchKey));
			}
		}
		
		return mergeDocs;
	}
	
	
	public void writeRelatedCitationsContentToXML(String targetPath, String pubmedUid, String summaryPath, String fetchPath){
//		get content from ESummary and EFetch XML Result Files
		LinkedHashMap<String, PubMedDocument> summaryContent = getContentFromESummaryOrFetchXML(DOMHandlerUtil.getXMLDocumentFromSource(summaryPath));
		LinkedHashMap<String, PubMedDocument> fetchContent = getContentFromESummaryOrFetchXML(DOMHandlerUtil.getXMLDocumentFromSource(fetchPath));
		
//		write content to XML
//		get targetPath from Properties file if not specified
		if(targetPath == null || targetPath.isEmpty()){
			PropertiesHandler ph = new PropertiesHandler(PropertiesHanderMode.ENTREZ);
			targetPath = ph.getPropertyValue(PropertiesIDs.PROJECT_PATH);
			targetPath += ph.getPropertyValue(PropertiesIDs.DATA_PMRELATEDSEARCH_TARGET_PATH);			
		}
		
		writeDocsToXML(targetPath, pubmedUid, summaryContent, fetchContent);
	}
	
	
//	#########################################
//	get Content from Collection-XML and DB
	
	public LinkedHashMap<String, PubMedDocument> getResultDocs(String refDocPmid){
		LinkedHashMap<String, PubMedDocument> resultDocs = new LinkedHashMap<String, PubMedDocument>();
		PubMedDocument resultDoc;
		
		String relatedSearchCollectionDirPath = getCollectionDirPath();
				
		Document doc = DOMHandlerUtil.getXMLDocumentFromSource(relatedSearchCollectionDirPath + refDocPmid + ".xml");
		
		Node root;
		NodeList relatedDocumentList;
		Node relatedDocument;
		String pmid;
		
		
		try {
			root = doc.getDocumentElement();			
			relatedDocumentList = XPathAPI.selectNodeList(root, RelatedDocumentXPath.RELDOC_ROOT.getPath());
			
			if(relatedDocumentList != null){
				for(int i = 0; i < relatedDocumentList.getLength(); i++){
					relatedDocument = relatedDocumentList.item(i);
					
					if(relatedDocument != null){
						resultDoc = new PubMedDocument();
						
						pmid = DOMHandlerUtil.getNodeValue(relatedDocument, RelatedDocumentXPath.ID.path);
						resultDoc.setDocPubMedID(pmid);
						
						resultDoc.setRank(DOMHandlerUtil.getNodeValue(relatedDocument, RelatedDocumentXPath.RANK.path));
						resultDoc.setDocTitle(DOMHandlerUtil.getNodeValue(relatedDocument, RelatedDocumentXPath.TITLE.path));
						resultDoc.setDocAbstract(DOMHandlerUtil.getNodeValue(relatedDocument, RelatedDocumentXPath.ABSTRACT.path));
						resultDoc.setDocPMCID(DOMHandlerUtil.getNodeValue(relatedDocument, RelatedDocumentXPath.PMCID.path));
						
						resultDoc.setMeshTerms(getMeshTermsFromResultDoc(relatedDocument));
						resultDoc.setFulltextReferences(getFulltextReferencesFromResultDoc(relatedDocument));
						
						resultDocs.put(pmid, resultDoc);
					}
				}
			}				
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		
		return resultDocs;
	}
	
	
	private ArrayList<MeshEntry> getMeshTermsFromResultDoc(Node relatedDoc) throws TransformerException{
		ArrayList<MeshEntry> meshTerms = new ArrayList<MeshEntry>();
		NodeList entryList = XPathAPI.selectNodeList(relatedDoc, RelatedDocumentXPath.MESHTERMS_ROOT.path + "/" + RelatedDocumentXPath.MESHTERM.path);
		MeshEntry entry;
		PubMedDocument pmDoc = new PubMedDocument();
		Node entryNode;
		
		if(entryList != null){
			for(int i = 0; i < entryList.getLength(); i++){
				entryNode = entryList.item(i);
				entry = pmDoc.new MeshEntry();
				
				entry.setDescriptorName(DOMHandlerUtil.getNodeValue(entryNode, RelatedDocumentXPath.DESCRIPTORNAME.path));
				entry.setDescriptorMajorTopic(DOMHandlerUtil.getNodeValue(entryNode, RelatedDocumentXPath.DESCRIPTORMAJORTOPIC.path));
				entry.setDescriptorUI(DOMHandlerUtil.getNodeValue(entryNode, RelatedDocumentXPath.DESCRIPTORUI.path));
				entry.setQualifierName(DOMHandlerUtil.getNodeValue(entryNode, RelatedDocumentXPath.QUALIFIERNAME.path));
				entry.setQualifierMajorTopic(DOMHandlerUtil.getNodeValue(entryNode, RelatedDocumentXPath.QUALIFIERMAJORTOPIC.path));
				entry.setQualifierUI(DOMHandlerUtil.getNodeValue(entryNode, RelatedDocumentXPath.QUALIFIERUI.path));
				
				meshTerms.add(entry);
			}
		}		
		
		return meshTerms;
	}
	
	private ArrayList<PubmedFulltextReference> getFulltextReferencesFromResultDoc(Node resultDoc) throws TransformerException{
		ArrayList<PubmedFulltextReference> references = new ArrayList<PubmedFulltextReference>();
		NodeList refList = XPathAPI.selectNodeList(resultDoc, RelatedDocumentXPath.FULLTEXTLINKS_ROOT.path + "/" + RelatedDocumentXPath.FULLTEXTLINK.path);
		PubmedFulltextReference reference;
		Node refNode;
		
		if(refList != null){
			for(int i = 0; i < refList.getLength(); i++){
				refNode = refList.item(i);
				reference = new PubmedFulltextReference();
				
				reference.setFullTextLink(DOMHandlerUtil.getNodeValue(refNode, RelatedDocumentXPath.LINK_URL_NODE.path));
				reference.setFreeStatus(DOMHandlerUtil.getNodeValue(refNode, RelatedDocumentXPath.LINK_FREE_STATUS_NODE.path));
				reference.setJournal(DOMHandlerUtil.getNodeValue(refNode, RelatedDocumentXPath.LINK_JOURNAL_NODE.path));
				
				references.add(reference);
			}
		}
		
		
		return references;
	}
	
	
	
	
	private String getCollectionDirPath(){
		PropertiesHandler ph = new PropertiesHandler(PropertiesHanderMode.ENTREZ);
		String relatedSearchCollectionDirPath = ph.getPropertyValue(PropertiesIDs.PROJECT_PATH);
		relatedSearchCollectionDirPath += ph.getPropertyValue(PropertiesIDs.DATA_PMRELATEDSEARCH_TARGET_PATH);
		
		if(!relatedSearchCollectionDirPath.trim().endsWith("/")){
			relatedSearchCollectionDirPath = relatedSearchCollectionDirPath.trim() + "/";
		}
		
		return relatedSearchCollectionDirPath;
	}
	
	
	public void addAssessmentsToDocuments(HashMap<String, PubMedDocument> relCiteDocs){
		AssessmentDBReader dbReader = new AssessmentDBReader();
		
		HashMap<String, String> similarities = dbReader.getSimilarityAssessments(null);
		HashMap<String, String> relevances = dbReader.getRelevanceAssessments(null);
		HashMap<String, String> animalTests = dbReader.getAnimalTestAssessments(null);
		
		Set<String> pmids = relCiteDocs.keySet();
		PubMedDocument relCiteDoc;
		
		for(String pmid: pmids){
			relCiteDoc = relCiteDocs.get(pmid);
			
			if(relCiteDoc != null){
				relCiteDoc.setAnimalTest(animalTests.get(pmid));
				relCiteDoc.setSimilarity(similarities.get(pmid));
				relCiteDoc.setRelevance(relevances.get(pmid));
			}
		}
	}
	
	
	
	
	
	

//	public void testSummary(){
//		Document doc = DOMHandlerUtil.getXMLDocumentFromFile(new File("C:/Users/du/Projekte/SMAFIRA/git/testruns/esummary/20150126_092300.xml"));
//		ArrayList<PubMedDocument> eSummaryDocs = getContentFromESummaryOrFetchXML(doc);
//		PubMedDocument pubDoc;
//		
//		for(int i = 0; i < eSummaryDocs.size(); i++){
//			pubDoc = eSummaryDocs.get(i);
//						
//			System.out.println("DocNum: " + i);
//			System.out.println("id: " + pubDoc.getDocPubMedID());
//			System.out.println("title: " + pubDoc.getDocTitle());
//			System.out.println("hasAbstract: " + pubDoc.hasAbstract());
//			System.out.println("hasReferences: " + (pubDoc.getReferences() != null));
//			
//			System.out.println("######\n");
//		}		
//	}
//	
//	public void testFetch(){
//		Document doc = DOMHandlerUtil.getXMLDocumentFromFile(new File("C:/Users/du/Projekte/SMAFIRA/git/testruns/esummary/20150128_111222_fetch.xml"));
//		ArrayList<PubMedDocument> eFetchDocs = getContentFromESummaryOrFetchXML(doc);
//		PubMedDocument pubDoc;
//		
//		for(int i = 0; i < eFetchDocs.size(); i++){
//			pubDoc = eFetchDocs.get(i);
//						
//			System.out.println("DocNum: " + i);
//			System.out.println("id: " + pubDoc.getDocPubMedID());
//			System.out.println("title: " + pubDoc.getDocTitle());
//			System.out.println("abstract: " + pubDoc.getDocAbstract());
//			
//			System.out.println("######\n");
//		}		
//	}
	
	public void testAbstracts(){
		File abstractFile = new File("C:/Users/du/Projekte/SMAFIRA/git/testruns/esummary/20150126_092300.txt");
		HashMap<String, String> abstracts = getAbstractsFromEFetch(abstractFile);
		
		Set<String> keys = abstracts.keySet();
		
		for(String key: keys){
			System.out.println("PubMedID: " + key);
			System.out.println("Abstract: " + abstracts.get(key));
			System.out.println("###########\n");
		}
		
	}
	
	
	public static void main(String[] args){
		PubMedDataCollector pmdc = new PubMedDataCollector();

//		pmdc.testSummary();
//		pmdc.testAbstracts();
//		pmdc.testFetch();
	}
}
