package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import db.SQLConnector.DBType;


public class AssessmentDBReader {
	
	
	
	private Connection conn;
	private Statement selectAssessments;
	
	public AssessmentDBReader() {
		init();
	}
	
	public HashMap<String, String> getRelevanceAssessments(String assessmentValue){
		return getColumnAssessment(ResultDbColNames.RELEVANCE.path, assessmentValue);
	}
	
	public HashMap<String, String> getSimilarityAssessments(String assessmentValue){
		return getColumnAssessment(ResultDbColNames.SIMILARITY.path, assessmentValue);
	}
	
	public HashMap<String, String> getAnimalTestAssessments(String assessmentValue){
		return getColumnAssessment(ResultDbColNames.ANIMALTEST.path, assessmentValue);
	}
	
	private HashMap<String, String> getColumnAssessment(String targetColumnName, String assessmentValue){
		String sqlQuery = "SELECT " + ResultDbColNames.PMID.path + ", " + targetColumnName + " FROM pubmed_result_document";
		HashMap<String, String> columnAssess = new HashMap<String, String>();
		String pmid, targetAttribute;
		
		if(assessmentValue != null && !assessmentValue.isEmpty()){
			sqlQuery += " WHERE " + targetColumnName + "='" + assessmentValue + "'";
		}
		
		try {
			ResultSet assessments = selectAssessments.executeQuery(sqlQuery);
			
			while(assessments.next()){
				pmid = assessments.getString(ResultDbColNames.PMID.path);
				targetAttribute = assessments.getString(targetColumnName);
//				System.out.println("pmid: " + pmid + "; " + targetColumnName + ": " + targetAttribute);
				columnAssess.put(pmid, targetAttribute);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return columnAssess;
	}
	
	public String getRefDocID(String refDocPmid){
		String refDocID = null;
		String sqlQuery = "SELECT " + ReferenceDbColNames.ID.path + " FROM pubmed_reference_document WHERE "
				+ ReferenceDbColNames.PMID.path + " = '" + refDocPmid + "'";
		
		try {
			Statement selectRefDoc = conn.createStatement();
			
			ResultSet assessments = selectRefDoc.executeQuery(sqlQuery);
			
			if(assessments.next()){
				refDocID = assessments.getString(ResultDbColNames.ID.path);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		return refDocID;
	}
	
	private void init(){
		this.conn = (new SQLConnector(DBType.MYSQL)).getConnection();
		try {
			selectAssessments = conn.createStatement();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection(){
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		AssessmentDBReader adbr = new AssessmentDBReader();
//		adbr.getRelevanceAssessments(null);
//		adbr.getSimilarityAssessments(null);
		adbr.getAnimalTestAssessments(null);
	}

}
