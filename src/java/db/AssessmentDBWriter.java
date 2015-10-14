package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dataCollection.PubMedDocument;
import dataCollection.PubMedDocument.RelatedDocumentXPath;
import tools.DOMHandlerUtil;
import tools.ToolsUtil;

import db.SQLConnector.DBType;

public class AssessmentDBWriter {

	public enum RankColumnName{
		PUBMED_SIMILAR_ARTICLES, SMO
	};

	private Connection conn;
	private Statement assessmentUpdate;



	public AssessmentDBWriter(){
		init();
	}

	private void init(){
		this.conn = (new SQLConnector(DBType.MYSQL)).getConnection();
		try {
			assessmentUpdate = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRankColumn(String refDocPmid, String resultDocPmid, String rankColumnName, int rankColumnValue){
		String refDocID = getRefDocID(refDocPmid);
		String sqlQuery = "UPDATE pubmed_result_document SET " + rankColumnName + "=" + rankColumnValue
				+ " WHERE ref_doc_id='" + refDocID + "' AND pmid='" + resultDocPmid + "'";

		try {
			assessmentUpdate.executeUpdate(sqlQuery);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getRefDocID(String refDocPmid){
		String refDocID;
		AssessmentDBReader reader = new AssessmentDBReader();

		refDocID = reader.getRefDocID(refDocPmid);
		reader.closeConnection();

		return refDocID;
	}


	public void writeNewResultDocumentsToDB(String documentsFilePath, String refDocPmid){

		Document sourceXMLDoc = DOMHandlerUtil.getXMLDocumentFromSource(documentsFilePath);
		Node sourceRoot = sourceXMLDoc.getDocumentElement();

		try {
			NodeList documents = XPathAPI.selectNodeList(sourceRoot, RelatedDocumentXPath.RELDOC_ROOT.getPath());
			Node document;
			String pmid, title, docAbstract, pmcid;
			PubMedDocument newPmDoc;
			String rank;


			if(documents != null){
				for(int i = 0; i < documents.getLength(); i++){
					document = documents.item(i);

					if(document != null){
						pmid = XPathAPI.selectSingleNode(document, RelatedDocumentXPath.ID.getPath()).getTextContent();


						title = XPathAPI.selectSingleNode(document, RelatedDocumentXPath.TITLE.getPath()).getTextContent();
						docAbstract = XPathAPI.selectSingleNode(document, RelatedDocumentXPath.ABSTRACT.getPath()).getTextContent();
						pmcid = XPathAPI.selectSingleNode(document, RelatedDocumentXPath.PMCID.getPath()).getTextContent();
						rank = XPathAPI.selectSingleNode(document, RelatedDocumentXPath.RANK.getPath()).getTextContent();


						newPmDoc = new PubMedDocument();
						newPmDoc.setDocPubMedID(pmid);
						newPmDoc.setDocTitle(title);
						newPmDoc.setDocAbstract(docAbstract);
						newPmDoc.setDocPMCID(pmcid);
						newPmDoc.setRank(rank);

						insertPubmedResultDocument(newPmDoc, refDocPmid);

					}
				}
			}


		}  catch (DOMException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public void insertPubmedResultDocument(PubMedDocument pmDoc, String refDocPmid){
		String refDocDbID = getRefDocID(refDocPmid);
		String sqlQuery = "INSERT INTO pubmed_result_document "
				+ "(version, doc_abstract, is_animal_test, last_change, pmcid, pmid, rank, ref_doc_id, relevance, similarity, title, ranksmo) "
				+ "VALUES (0, " + getNullOrQuotes(pmDoc.getDocAbstract()) + ", 99, now(), " + getNullOrQuotes(getCheckedPMCID(pmDoc.getDocPMCID()))
				+ ", '" + pmDoc.getDocPubMedID() + "', " + pmDoc.getRank() + ", " + refDocDbID + ", 99, 99, " + getNullOrQuotes(pmDoc.getDocTitle()) + ", 0);";


		try {
			assessmentUpdate.executeUpdate(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getNullOrQuotes(String value){
		if(value == null || value.isEmpty())
			return "NULL";
		else
			return ("'" + removeQuotesFromText(value) + "'");
	}

	private String removeQuotesFromText(String txt){
		txt = txt.replaceAll("'", "");
		txt = txt.replaceAll("\"", "");

		return txt;
	}

	public static String getCheckedPMCID(String pmcid){
		if(pmcid == null || pmcid.isEmpty())
			return null;
		else if(pmcid.startsWith("PMC") && !pmcid.startsWith("pmc-id:"))
			return pmcid;
		else{
			String newPmcid = pmcid.substring(pmcid.indexOf("PMC")).trim();
			for(int i = "PMC".length(); i < newPmcid.length(); i++){
				if(!ToolsUtil.isNumeric(newPmcid.charAt(i))){
					newPmcid = newPmcid.substring(0, i);
					break;
				}
			}

			return newPmcid;
		}
	}

	public void closeConnection(){
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
