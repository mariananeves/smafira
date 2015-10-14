package dataMining;

import htmlSearch.PubmedFulltextReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import properties.PropertiesHandler;
import properties.PropertiesHandler.PropertiesHanderMode;
import properties.PropertiesHandler.PropertiesIDs;

import dataCollection.PubMedDataCollector;
import dataCollection.PubMedDocument;
import dataCollection.PubMedDocument.MeshEntry;

import tools.ToolsUtil;
import weka.classifiers.Classifier;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.Id3;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaArffCreator {
	
	private String arffPath;
	private boolean leaveOutNotAssessed = false;
	private boolean leaveOutNotDecidable = false;
	private LinkedHashMap<String, PubMedDocument> resultDocs;
	private LinkedHashMap<String, String> instanceID_pmid_mapping;

	public WekaArffCreator(){
		setArffPath();
	}
	
	public void loadResultDocs(String refDocPmid){
		resultDocs = retrieveResultDocumentsWithAssessments(refDocPmid);
	}
	
	public void loadResultDocsWithoutAssessments(String refDocPmid){
		resultDocs = retrieveResultDocuments(refDocPmid);
	}
	
	public void setLeaveOutNotAssessed(boolean leaveOutNotAssessed){
		this.leaveOutNotAssessed = leaveOutNotAssessed;
	}
	
	public enum DocumentAttributes{
		TITLE("title"),
		ABSTRACT("abstract"),
		PMCID("pmcid"),
		FULLTEXTURL("fulltexturl"), FULLTEXTJOURNAL("fulltextjournal"), FULLTEXTFREESTATUS("fulltextfreestatus"),
		MESHTERM("meshterm"), MESHMAJORTOPIC("meshmajortopic"), MESHUI("meshui"),
		QUALIFIER("qualifier"), QUALIFIERMAJORTOPIC("qualifiermajortopic"), QUALIFIERUI("qualifierui");
		
		String name;
		
		private DocumentAttributes(String name) {
			this.name = name;
		}		
	};
	
	public enum ClassAttributes{
		ANIMALTEST("animalTest"),
		SIMILARITY("similarity"),
		RELEVANCE("documentRelevance");
		
		String name;
		private ClassAttributes(String name) {
			this.name = name;
		}
	}
	
	private void setArffPath(){
		PropertiesHandler ph = new PropertiesHandler(PropertiesHanderMode.ENTREZ);
		this.arffPath = ph.getPropertyValue(PropertiesIDs.PROJECT_PATH) + ph.getPropertyValue(PropertiesIDs.DATA_ARFF_PATH);
	}
	
	public LinkedHashMap<String, PubMedDocument> retrieveResultDocumentsWithAssessments(String refDocPmid){
		PubMedDataCollector pdc = new PubMedDataCollector();
		
		LinkedHashMap<String, PubMedDocument> resultDocs = pdc.getResultDocs(refDocPmid);
		pdc.addAssessmentsToDocuments(resultDocs);
		
		return resultDocs;
	}
	
	public LinkedHashMap<String, PubMedDocument> retrieveResultDocuments(String refDocPmid){
		PubMedDataCollector pdc = new PubMedDataCollector();
		
		LinkedHashMap<String, PubMedDocument> resultDocs = pdc.getResultDocs(refDocPmid);
		
		return resultDocs;
	}
	
	
	public void testNominal(){
		FastVector atts = new FastVector();
		FastVector nominalAtts = new FastVector(2);
		nominalAtts.addElement("1");
		nominalAtts.addElement("2");
		atts.addElement(new Attribute("test", nominalAtts));		
	}
	
	
	
	public FastVector getAttributeVector(DocumentAttributes[] attributes){
		FastVector atts = new FastVector();
		
		for(DocumentAttributes attribute: attributes){
			atts.addElement(new Attribute(attribute.name, (FastVector) null));
		}
		
		return atts;
	}
	
	public void addClassAttribute(FastVector atts, ClassAttributes classAttribute){
		FastVector classValues = getClassValues(classAttribute);
		
		atts.addElement(new Attribute(classAttribute.name, classValues));
	}
	
	private FastVector getClassValues(ClassAttributes classAttribute){
		FastVector classValues = null;
		
		switch(classAttribute){
		case ANIMALTEST: classValues = getAnimalTestAttributeValues(); break;
		case SIMILARITY: classValues = getSimilarityAttributeValues(); break;
		case RELEVANCE: classValues = getRelevanceAttributeValues(); break;
		}
		
		return classValues;
	}
	
	
	public Instances getDataSet(DocumentAttributes[] attributeList, FastVector atts){
		Instances data = new Instances("document-features", atts, 0);
		double[] vals;
		
//		3. fill with data
		Set<String> pmids = resultDocs.keySet();
		PubMedDocument resultDoc;
		instanceID_pmid_mapping = new LinkedHashMap<String, String>();
		
		for(String pmid: pmids){
			resultDoc = resultDocs.get(pmid);
			vals = new double[data.numAttributes()];
			
			int i = 0;
			for(DocumentAttributes documentAttribute: attributeList){
				switch(documentAttribute){
				case TITLE: vals[i] = getValue(data, i, ToolsUtil.escapeString(resultDoc.getDocTitle(), "'")); break;
				case ABSTRACT: vals[i] = getValue(data, i, ToolsUtil.escapeString(resultDoc.getDocAbstract(), "'")); break;
				case PMCID: vals[i] = getValue(data, i, resultDoc.getDocPMCID()); break;
				case FULLTEXTURL: vals[i] = getValue(data, i, getFullTextUrls(resultDoc.getFulltextReferences()));; break;
				case FULLTEXTJOURNAL: vals[i] = getValue(data, i, getFullTextJournals(resultDoc.getFulltextReferences()));; break;
				case FULLTEXTFREESTATUS: vals[i] = getValue(data, i, getFullTextFreeStatus(resultDoc.getFulltextReferences()));; break;
				case MESHTERM: vals[i] = getValue(data, i, getMeshDescriptor(resultDoc.getMeshTerms())); break;
				case MESHMAJORTOPIC: vals[i] = getValue(data, i, getMeshDescMT(resultDoc.getMeshTerms())); break;
				case MESHUI: vals[i] = getValue(data, i, getMeshDescUi(resultDoc.getMeshTerms())); break;
				case QUALIFIER: vals[i] = getValue(data, i, getMeshQualifier(resultDoc.getMeshTerms())); break;
				case QUALIFIERMAJORTOPIC: vals[i] = getValue(data, i, getMeshQualifierMT(resultDoc.getMeshTerms())); break;
				case QUALIFIERUI: vals[i] = getValue(data, i, getMeshQualifierUi(resultDoc.getMeshTerms())); break;
				}
				i++;
			}
				
			vals[i] = Instance.missingValue();
			
			data.add(new Instance(1.0, vals));
			instanceID_pmid_mapping.put(String.valueOf(data.lastInstance().value(0)), pmid);
		}
				
		return data;
	}
	
	public Instances getDataSet(DocumentAttributes[] attributeList, ClassAttributes classAttribute, FastVector atts){
		Instances data = new Instances("document-features", atts, 0);
		double[] vals;
		
//		3. fill with data
		Set<String> pmids = resultDocs.keySet();
		PubMedDocument resultDoc;
		
		for(String pmid: pmids){
			resultDoc = resultDocs.get(pmid);
			if((!leaveOutNotAssessed || isAssessed(resultDoc, classAttribute)) && (!leaveOutNotDecidable || isDecidable(resultDoc, classAttribute))){
				vals = new double[data.numAttributes()];
				
				int i = 0;
				for(DocumentAttributes documentAttribute: attributeList){
					switch(documentAttribute){
					case TITLE: vals[i] = getValue(data, i, ToolsUtil.escapeString(resultDoc.getDocTitle(), "'")); break;
					case ABSTRACT: vals[i] = getValue(data, i, ToolsUtil.escapeString(resultDoc.getDocAbstract(), "'")); break;
					case PMCID: vals[i] = getValue(data, i, resultDoc.getDocPMCID()); break;
					case FULLTEXTURL: vals[i] = getValue(data, i, getFullTextUrls(resultDoc.getFulltextReferences()));; break;
					case FULLTEXTJOURNAL: vals[i] = getValue(data, i, getFullTextJournals(resultDoc.getFulltextReferences()));; break;
					case FULLTEXTFREESTATUS: vals[i] = getValue(data, i, getFullTextFreeStatus(resultDoc.getFulltextReferences()));; break;
					case MESHTERM: vals[i] = getValue(data, i, getMeshDescriptor(resultDoc.getMeshTerms())); break;
					case MESHMAJORTOPIC: vals[i] = getValue(data, i, getMeshDescMT(resultDoc.getMeshTerms())); break;
					case MESHUI: vals[i] = getValue(data, i, getMeshDescUi(resultDoc.getMeshTerms())); break;
					case QUALIFIER: vals[i] = getValue(data, i, getMeshQualifier(resultDoc.getMeshTerms())); break;
					case QUALIFIERMAJORTOPIC: vals[i] = getValue(data, i, getMeshQualifierMT(resultDoc.getMeshTerms())); break;
					case QUALIFIERUI: vals[i] = getValue(data, i, getMeshQualifierUi(resultDoc.getMeshTerms())); break;
					}
					i++;
				}
				
				switch(classAttribute){
				case ANIMALTEST: vals[i] = getAssessmentValue(getClassValues(classAttribute), getCombinedAnimalTest(resultDoc.getAnimalTest())); break;
				case SIMILARITY: vals[i] = getAssessmentValue(getClassValues(classAttribute), getCombinedSimilarity(resultDoc.getSimilarity())); break;
				case RELEVANCE: vals[i] = getAssessmentValue(getClassValues(classAttribute), resultDoc.getRelevance()); break;
				}				
				
				data.add(new Instance(1.0, vals));
			}
		}
		
		return data;
	}
	
	private String getCombinedSimilarity(String similarity){
		if(similarity.equals("2"))
			return "1";
		else
			return similarity;
	}
	
	private String getCombinedAnimalTest(String animalTest){
		if(animalTest.equals("2"))
			return "0";
		else
			return animalTest;
	}
	
	
	public Instances getDataSetWithCondition(DocumentAttributes[] attributeList, ClassAttributes classAttribute, FastVector atts, int numOfInstances){
		Instances data = new Instances("document-features", atts, 0);
		double[] vals;
		
//		3. fill with data
		Set<String> pmids = resultDocs.keySet();
		PubMedDocument resultDoc;
		ArrayList<Instance> negativeInstances = new ArrayList<Instance>();
		Boolean classValueMeetsCondition;
		
		for(String pmid: pmids){
			resultDoc = resultDocs.get(pmid);
			if(isAssessed(resultDoc, classAttribute) || !leaveOutNotAssessed){
				classValueMeetsCondition = classValueMeetsCondition(classAttribute, resultDoc);
				
//				meetsCondition => e.g. is relevant, is similar, is not an animal test
//				null if this is not defined or not decidable
				if(classValueMeetsCondition != null){				
					vals = new double[data.numAttributes()];
					
					int i = 0;
					for(DocumentAttributes documentAttribute: attributeList){
						switch(documentAttribute){
						case TITLE: vals[i] = getValue(data, i, ToolsUtil.escapeString(resultDoc.getDocTitle(), "'")); break;
						case ABSTRACT: vals[i] = getValue(data, i, ToolsUtil.escapeString(resultDoc.getDocAbstract(), "'")); break;
						case PMCID: vals[i] = getValue(data, i, resultDoc.getDocPMCID()); break;
						case FULLTEXTURL: vals[i] = getValue(data, i, getFullTextUrls(resultDoc.getFulltextReferences()));; break;
						case FULLTEXTJOURNAL: vals[i] = getValue(data, i, getFullTextJournals(resultDoc.getFulltextReferences()));; break;
						case FULLTEXTFREESTATUS: vals[i] = getValue(data, i, getFullTextFreeStatus(resultDoc.getFulltextReferences()));; break;
						case MESHTERM: vals[i] = getValue(data, i, getMeshDescriptor(resultDoc.getMeshTerms())); break;
						case MESHMAJORTOPIC: vals[i] = getValue(data, i, getMeshDescMT(resultDoc.getMeshTerms())); break;
						case MESHUI: vals[i] = getValue(data, i, getMeshDescUi(resultDoc.getMeshTerms())); break;
						case QUALIFIER: vals[i] = getValue(data, i, getMeshQualifier(resultDoc.getMeshTerms())); break;
						case QUALIFIERMAJORTOPIC: vals[i] = getValue(data, i, getMeshQualifierMT(resultDoc.getMeshTerms())); break;
						case QUALIFIERUI: vals[i] = getValue(data, i, getMeshQualifierUi(resultDoc.getMeshTerms())); break;
						}
						i++;
					}
					
					switch(classAttribute){
					case ANIMALTEST: vals[i] = getAssessmentValue(getClassValues(classAttribute), resultDoc.getAnimalTest()); break;
					case SIMILARITY: vals[i] = getAssessmentValue(getClassValues(classAttribute), resultDoc.getSimilarity()); break;
					case RELEVANCE: vals[i] = getAssessmentValue(getClassValues(classAttribute), resultDoc.getRelevance()); break;
					}	
					
					if(classValueMeetsCondition)
						data.add(new Instance(1.0, vals));
					else
						negativeInstances.add(new Instance(1.0, vals));
				}
			}
		}
		
		while(data.numInstances() < numOfInstances && !negativeInstances.isEmpty()){
			data.add(negativeInstances.remove(0));
		}
		
		return data;
	}
	
	
	public Instances getUnbiasedDataSet(DocumentAttributes[] attributeList, ClassAttributes classAttribute, FastVector atts, boolean dublicatePositiv, int numOfInstances){
		Instances data = new Instances("document-features", atts, 0);
		double[] vals;
		
//		3. fill with data
		Set<String> pmids = resultDocs.keySet();
		PubMedDocument resultDoc;
		ArrayList<Instance> positiveInstances = new ArrayList<Instance>();
		ArrayList<Instance> negativeInstances = new ArrayList<Instance>();
		Boolean classValueMeetsCondition;
		
		int numAddedInstances = 0;
		for(String pmid: pmids){
//			Anzahl der Instanzen ignorieren, wenn numOfInstances nicht festgelegt (z.B. durch -1)
			if(numOfInstances < 0 || numAddedInstances < numOfInstances){
				resultDoc = resultDocs.get(pmid);
				if(isAssessed(resultDoc, classAttribute) || !leaveOutNotAssessed){
					classValueMeetsCondition = classValueMeetsCondition(classAttribute, resultDoc);
					
	//				meetsCondition => e.g. is relevant, is similar, is not an animal test
	//				null if this is not defined or not decidable
					if(classValueMeetsCondition != null){				
						vals = new double[data.numAttributes()];
						
						int i = 0;
						for(DocumentAttributes documentAttribute: attributeList){
							switch(documentAttribute){
							case TITLE: vals[i] = getValue(data, i, ToolsUtil.escapeString(resultDoc.getDocTitle(), "'")); break;
							case ABSTRACT: vals[i] = getValue(data, i, ToolsUtil.escapeString(resultDoc.getDocAbstract(), "'")); break;
							case PMCID: vals[i] = getValue(data, i, resultDoc.getDocPMCID()); break;
							case FULLTEXTURL: vals[i] = getValue(data, i, getFullTextUrls(resultDoc.getFulltextReferences()));; break;
							case FULLTEXTJOURNAL: vals[i] = getValue(data, i, getFullTextJournals(resultDoc.getFulltextReferences()));; break;
							case FULLTEXTFREESTATUS: vals[i] = getValue(data, i, getFullTextFreeStatus(resultDoc.getFulltextReferences()));; break;
							case MESHTERM: vals[i] = getValue(data, i, getMeshDescriptor(resultDoc.getMeshTerms())); break;
							case MESHMAJORTOPIC: vals[i] = getValue(data, i, getMeshDescMT(resultDoc.getMeshTerms())); break;
							case MESHUI: vals[i] = getValue(data, i, getMeshDescUi(resultDoc.getMeshTerms())); break;
							case QUALIFIER: vals[i] = getValue(data, i, getMeshQualifier(resultDoc.getMeshTerms())); break;
							case QUALIFIERMAJORTOPIC: vals[i] = getValue(data, i, getMeshQualifierMT(resultDoc.getMeshTerms())); break;
							case QUALIFIERUI: vals[i] = getValue(data, i, getMeshQualifierUi(resultDoc.getMeshTerms())); break;
							}
							i++;
						}
						
						switch(classAttribute){
						case ANIMALTEST: vals[i] = getAssessmentValue(getClassValues(classAttribute), resultDoc.getAnimalTest()); break;
						case SIMILARITY: vals[i] = getAssessmentValue(getClassValues(classAttribute), resultDoc.getSimilarity()); break;
						case RELEVANCE: vals[i] = getAssessmentValue(getClassValues(classAttribute), resultDoc.getRelevance()); break;
						}	
						
						if(classValueMeetsCondition)
							positiveInstances.add(new Instance(1.0, vals));
						else
							negativeInstances.add(new Instance(1.0, vals));
						
						numAddedInstances++;
					}
				}
			}
		}
		
		if(dublicatePositiv){
			int counter = 0;
			while(!negativeInstances.isEmpty()){
				data.add(negativeInstances.remove(0));
				data.add(positiveInstances.get(counter++));
				
				if(counter >= positiveInstances.size())
					counter = 0;
			}
		}
		else{
			while(!positiveInstances.isEmpty()){
				data.add(positiveInstances.remove(0));
				data.add(negativeInstances.remove(0));
			}
		}
		
		return data;
	}
	
//	http://weka.wikispaces.com/Creating+an+ARFF+file
	public Instances createCompleteDataSet(String refDocPmid, boolean leaveOutNotAssessed){
		this.leaveOutNotAssessed = leaveOutNotAssessed;
		
		FastVector atts;
//		FastVector nominalVals;
		FastVector animalTestVals;
		FastVector similarityVals;
		FastVector relevanceVals;
	    Instances data;
	    double[] vals;
	    
//		1. set up attributes
		atts = new FastVector();	
//		nominalVals = new FastVector();
//		String Attribute
		atts.addElement(new Attribute("title", (FastVector) null));
		atts.addElement(new Attribute("abstract", (FastVector) null));
		atts.addElement(new Attribute("pmcid", (FastVector) null));
		atts.addElement(new Attribute("fulltexturl", (FastVector) null));
		atts.addElement(new Attribute("fulltextjournal", (FastVector) null));
		atts.addElement(new Attribute("fulltextfreestatus", (FastVector) null));
		atts.addElement(new Attribute("meshterm", (FastVector) null));
		atts.addElement(new Attribute("meshmajortopic", (FastVector) null));		
		atts.addElement(new Attribute("meshui", (FastVector) null));
		atts.addElement(new Attribute("qualifier", (FastVector) null));
		atts.addElement(new Attribute("qualifiermajortopic", (FastVector) null));
		atts.addElement(new Attribute("qualifierui", (FastVector) null));
		
//		animalTestVals = getAnimalTestAttributeValues();
//		atts.addElement(new Attribute("animalTest", animalTestVals));
//		similarityVals = getSimilarityAttributeValues();
//		atts.addElement(new Attribute("similarity", similarityVals));
		relevanceVals = getRelevanceAttributeValues();
		atts.addElement(new Attribute("relevance", relevanceVals));
		
		
//		2. create Instances object
		data = new Instances("document-features", atts, 0);
		
		
//		3. fill with data
		LinkedHashMap<String, PubMedDocument> resultDocs = retrieveResultDocumentsWithAssessments(refDocPmid);
		Set<String> pmids = resultDocs.keySet();
		PubMedDocument resultDoc;
		
		int numberOfNotRelevant = 38;
		for(String pmid: pmids){
			resultDoc = resultDocs.get(pmid);
			if(isAssessed(resultDoc, ClassAttributes.RELEVANCE) || !leaveOutNotAssessed){
				if(resultDoc.getRelevance().equals("4") || (numberOfNotRelevant > 0 && resultDoc.getRelevance().equals("3"))){
					if(resultDoc.getRelevance().equals("3"))
						numberOfNotRelevant--;
					vals = new double[data.numAttributes()];
					vals[0] = getValue(data, 0, ToolsUtil.escapeString(resultDoc.getDocTitle(), "'"));		
					vals[1] = getValue(data, 1, ToolsUtil.escapeString(resultDoc.getDocAbstract(), "'"));
					vals[2] = getValue(data, 2, resultDoc.getDocPMCID());
					addFulltextReferences(resultDoc.getFulltextReferences(), data, vals);
					addMeshEntries(resultDoc.getMeshTerms(), data, vals);
					
	//				vals[1] = getAssessmentValue(animalTestVals, resultDoc.getAnimalTest());
	//				vals[1] = getAssessmentValue(similarityVals, resultDoc.getSimilarity());
					vals[12] = getAssessmentValue(relevanceVals, resultDoc.getRelevance());
	//				vals[12] = Instance.missingValue();
	//				vals[13] = Instance.missingValue();
	//				vals[14] = Instance.missingValue();
					data.add(new Instance(1.0, vals));
				}
			}
		}
		
		
//		missing value => vals[2] = Instance.missingValue();
		
//		4. output data
//		System.out.println(data);
		ToolsUtil.saveText(arffPath + refDocPmid + ".arff", data.toString());
//		ToolsUtil.saveText(arffPath + refDocPmid + "_summary.arff", data.toSummaryString());
		
		return data;
	}
	
	private double getAssessmentValue(FastVector attributeValues, String assessment){		
		if(assessment.equals("99"))
			return Instance.missingValue();
		else
			return attributeValues.indexOf(assessment);
	}
	
	private boolean isAssessed(PubMedDocument pmDoc, ClassAttributes classAttr){
		return isValid("99", pmDoc, classAttr);					
	}
	
	private boolean isDecidable(PubMedDocument pmDoc, ClassAttributes classAttr){		
		return isValid("98", pmDoc, classAttr);			
	}
	
	private boolean isValid(String nonValidValue, PubMedDocument pmDoc, ClassAttributes classAttr){		
		switch(classAttr){
		case RELEVANCE: return !pmDoc.getRelevance().equals(nonValidValue);
		case SIMILARITY: return !pmDoc.getSimilarity().equals(nonValidValue);
		case ANIMALTEST: return !pmDoc.getAnimalTest().equals(nonValidValue);
		default: return false;
		}					
	}
	
	private double getValue(Instances data, int attrIndex, String textValue){
		double value;
		
		if(textValue != null && !textValue.isEmpty())
			value = data.attribute(attrIndex).addStringValue(textValue);		
		else
			value = Instance.missingValue();
		
		return value;
	}
	
	private void addFulltextReferences(ArrayList<PubmedFulltextReference> references, Instances data, double[] vals){
		String urls = "";
		String journals = "";
		String freeStatus = "";
		
		for(PubmedFulltextReference reference: references){
			urls += reference.getFullTextLink() + " ";
			journals += "'" + reference.getJournal() + "' ";
			freeStatus += (reference.getFreeStatus() != null ? reference.getFreeStatus().toString() + " " : " ? ");
		}
		
		vals[3] = getValue(data, 3, urls.trim());
		vals[4] = getValue(data, 4, journals.trim());
		vals[5] = getValue(data, 5, freeStatus.trim());		
	}
	
	private String getFullTextUrls(ArrayList<PubmedFulltextReference> references){
		String urls = "";
		
		for(PubmedFulltextReference reference: references){
			urls += reference.getFullTextLink() + " ";
		}
		
		return urls.trim();
	}
	
	private String getFullTextJournals(ArrayList<PubmedFulltextReference> references){
		String journals = "";
		
		for(PubmedFulltextReference reference: references){
			journals += "'" + reference.getJournal() + "' ";
		}
		
		return journals.trim();
	}
	
	private String getFullTextFreeStatus(ArrayList<PubmedFulltextReference> references){
		String freeStatus = "";
		
		for(PubmedFulltextReference reference: references){
			freeStatus += (reference.getFreeStatus() != null ? reference.getFreeStatus().toString() + " " : " ? ");
		}
		
		return freeStatus.trim();
	}
	
	
	private void addMeshEntries(ArrayList<MeshEntry> meshEntries, Instances data, double[] vals){
		String descriptor = "";
		String descMT = "";
		String descUi = "";
		String qualifier = "";
		String qualiMT = "";
		String qualiUi = "";
		
		for(MeshEntry entry: meshEntries){
			descriptor += "'"  +entry.getDescriptorName() + "' ";
			descMT += entry.getDescriptorMajorTopic() + " ";
			descUi += entry.getDescriptorUI() + " ";
			qualifier += (entry.getQualifierName() != null && !entry.getQualifierName().isEmpty() ? "'" + entry.getQualifierName() + "' " : " ? ");
			qualiMT += (entry.getQualifierMajorTopic() != null && !entry.getQualifierMajorTopic().isEmpty() ? "'" + entry.getQualifierMajorTopic() + "' " : " ? ");
			qualiUi += (entry.getQualifierUI() != null && !entry.getQualifierUI().isEmpty() ? "'" + entry.getQualifierUI() + "' " : " ? ");
		}
		
		vals[6] = getValue(data, 6, descriptor.trim());
		vals[7] = getValue(data, 7, descMT.trim());
		vals[8] = getValue(data, 8, descUi.trim());
		vals[9] = getValue(data, 9, qualifier.trim());
		vals[10] = getValue(data, 10, qualiMT.trim());
		vals[11] = getValue(data, 11, qualiUi.trim());		
	}
	
	private String getMeshDescriptor(ArrayList<MeshEntry> meshEntries){
		String descriptor = "";
		
		for(MeshEntry entry: meshEntries){
			descriptor += "'"  +entry.getDescriptorName() + "' ";
		}
		
		return descriptor.trim();
	}
	
	private String getMeshDescMT(ArrayList<MeshEntry> meshEntries){
		String descMT = "";
		
		for(MeshEntry entry: meshEntries){
			descMT += entry.getDescriptorMajorTopic() + " ";
		}
		
		return descMT.trim();
	}
	
	private String getMeshDescUi(ArrayList<MeshEntry> meshEntries){
		String descUi = "";
		
		for(MeshEntry entry: meshEntries){
			descUi += entry.getDescriptorUI() + " ";
		}
		
		return descUi.trim();
	}
	
	private String getMeshQualifier(ArrayList<MeshEntry> meshEntries){
		String qualifier = "";
		
		for(MeshEntry entry: meshEntries){
			qualifier += (entry.getQualifierName() != null && !entry.getQualifierName().isEmpty() ? "'" + entry.getQualifierName() + "' " : " ? ");
		}
		
		return qualifier.trim();
	}

	private String getMeshQualifierMT(ArrayList<MeshEntry> meshEntries){
		String qualifierMT = "";
		
		for(MeshEntry entry: meshEntries){
			qualifierMT += (entry.getQualifierMajorTopic() != null && !entry.getQualifierMajorTopic().isEmpty() ? "'" + entry.getQualifierMajorTopic() + "' " : " ? ");
		}
		
		return qualifierMT.trim();
	}
	
	private String getMeshQualifierUi(ArrayList<MeshEntry> meshEntries){
		String qualifierUi = "";
		
		for(MeshEntry entry: meshEntries){
			qualifierUi += (entry.getQualifierUI() != null && !entry.getQualifierUI().isEmpty() ? "'" + entry.getQualifierUI() + "' " : " ? ");
		}
		
		return qualifierUi.trim();
	}

	
	
	
	private FastVector getAnimalTestAttributeValues(){
		FastVector atts = new FastVector();

//		if(!leaveOutNotDecidable)
//			atts.addElement("98");
		atts.addElement("0");
		atts.addElement("1");
//		atts.addElement("2");
//		setNotAssessed(atts);
		
		return atts;
	}
	
	
	private FastVector getSimilarityAttributeValues(){
		FastVector atts = new FastVector();
//		if(!leaveOutNotDecidable)
//			atts.addElement("98");
		atts.addElement("0");
		atts.addElement("1");
//		atts.addElement("2");
//		setNotAssessed(atts);
		
		return atts;
	}
	
	
	private FastVector getRelevanceAttributeValues(){
		FastVector atts = new FastVector();		
//		if(!leaveOutNotDecidable)
//			atts.addElement("98");
		atts.addElement("3");
		atts.addElement("4");
//		setNotAssessed(atts);
		
		return atts;
	}
	
	
	
//	returns null for not decidable and not defined values (98,99)
	private Boolean classValueMeetsCondition(ClassAttributes classAttribute, PubMedDocument pmDoc){
		Boolean classValueMeetsCondition = null;
		
		String classValue = "";
		String trueValues = "";
		String falseValues = "";
		
		switch(classAttribute){
		case RELEVANCE: classValue = pmDoc.getRelevance();
			trueValues = "4";
			falseValues = "3";
			break;
		case ANIMALTEST: classValue = pmDoc.getAnimalTest();
//		0 => kein Tierversuch, 2 => sowohl als auch, daher beide erwuenschte Werte
			trueValues = "0,2";
			falseValues = "1";
			if(classValue.equals("2"))
				pmDoc.setAnimalTest("0");
			break;
		case SIMILARITY: classValue = pmDoc.getSimilarity();
			trueValues = "1,2";
			falseValues = "0";
			if(classValue.equals("2"))
				pmDoc.setSimilarity("1");
			break;
		}
			
		
			
		if(trueValues.contains(classValue))
			classValueMeetsCondition = true;
		else if(falseValues.contains(classValue))
			classValueMeetsCondition = false;
		else
			classValueMeetsCondition = null;
		
		
		return classValueMeetsCondition;
	}
	
	public void filterResultDocsBySimilarityCondition(){
		
	}
	
	public void filterResultDocsByAnimalTestCondition(){
		
	}
	
	
	private void setNotAssessed(FastVector atts){
		if(!leaveOutNotAssessed)
			atts.addElement("99");
	}
	
//	private double getDBValue(Instances data, int attrIndex, String value){
////		"Nicht definiert" als missing value interpretieren
//		if(value.equals("99"))
//			return Instance.missingValue();
//		else
//			return getValue(data, attrIndex, value);
//	}
	
//	http://weka.wikispaces.com/Save+Instances+to+an+ARFF+File
	public void saveInstancesToArffFile(Instances dataSet, String destFilePath){
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		try {
			saver.setFile(new File(destFilePath));
			saver.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Instances readArffFile(String path){
		Instances data = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			ArffReader arff = new ArffReader(reader);
			data = arff.getData();
			data.setClassIndex(data.numAttributes() -1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	
	public void classify(Instances data){
		StringToWordVector filter = new StringToWordVector();
		final Classifier classifier = new Id3();		
	}
	
	
//	###############################################################################
//	create arff
	/*
	 * creates unbiased DataSet if duplicatePositive is not null
	 */
	public void getArffFiles(String filename, DocumentAttributes[] attributeList, ClassAttributes classAttribute, int numOfInstances, Boolean dupblicatePositive){		
		FastVector atts = getAttributeVector(attributeList);
		addClassAttribute(atts, classAttribute);
		
		
		Instances data;
		
		if(dupblicatePositive == null){
			if(numOfInstances > -1)
				data = getDataSetWithCondition(attributeList, classAttribute, atts, numOfInstances);
			else
				data = getDataSet(attributeList, classAttribute, atts);
		}
		else{
			data = getUnbiasedDataSet(attributeList, classAttribute, atts, dupblicatePositive.booleanValue(), numOfInstances);			
		}
		
		ToolsUtil.saveText(arffPath + filename + ".arff", data.toString());
		ToolsUtil.saveText(arffPath + filename + "_summary.arff", data.toSummaryString());		
	}
	
	public void getArffFiles(String filename, DocumentAttributes[] attributeList, boolean getTestValues){
		FastVector relevanceAtts = getAttributeVector(attributeList);
		addClassAttribute(relevanceAtts, ClassAttributes.RELEVANCE);
		
		FastVector similarityAtts = getAttributeVector(attributeList);
		addClassAttribute(similarityAtts, ClassAttributes.SIMILARITY);
		
		FastVector animalTestAtts = getAttributeVector(attributeList);
		addClassAttribute(animalTestAtts, ClassAttributes.ANIMALTEST);
		
		Instances relevanceData;
		Instances similarityData;
		Instances animalTestData;

		
		if(getTestValues){
			relevanceData = getDataSet(attributeList, relevanceAtts);
			ToolsUtil.saveText(arffPath + filename + "_relevance_mapping.txt", mappingToString());
			similarityData = getDataSet(attributeList, similarityAtts);
			ToolsUtil.saveText(arffPath + filename + "_similarity_mapping.txt", mappingToString());
			animalTestData = getDataSet(attributeList, animalTestAtts);
			ToolsUtil.saveText(arffPath + filename + "_animalTest_mapping.txt", mappingToString());
		}
		else{
			relevanceData = getDataSet(attributeList, ClassAttributes.RELEVANCE, relevanceAtts);
			similarityData = getDataSet(attributeList, ClassAttributes.SIMILARITY, similarityAtts);
			animalTestData = getDataSet(attributeList, ClassAttributes.ANIMALTEST, animalTestAtts);
		}
		
		
		ToolsUtil.saveText(arffPath + filename + "_relevance.arff", relevanceData.toString());
		ToolsUtil.saveText(arffPath + filename + "_relevance_summary.arff", relevanceData.toSummaryString());
		
		ToolsUtil.saveText(arffPath + filename + "_similarity.arff", similarityData.toString());
		ToolsUtil.saveText(arffPath + filename + "_similarity_summary.arff", similarityData.toSummaryString());
		
		ToolsUtil.saveText(arffPath + filename + "_animalTest.arff", animalTestData.toString());
		ToolsUtil.saveText(arffPath + filename + "_animalTest_summary.arff", animalTestData.toSummaryString());
	}
	
	
	private String mappingToString(){
		String mapping = "";
		
		Set<String> instanceIDs = this.instanceID_pmid_mapping.keySet();
		
		for(String instanceID: instanceIDs){
			mapping += instanceID + ":" + this.instanceID_pmid_mapping.get(instanceID) + "\n";
		}
		
		return mapping.trim();
	}
	
	
	public void createWekaTrainingArff(String refDocPmid){
		this.resultDocs = retrieveResultDocumentsWithAssessments(refDocPmid);
		leaveOutNotAssessed = true;
		leaveOutNotDecidable = true;
		
		runArffCreation(refDocPmid, "training", false);
	}
	
	public void createWekaClassificationArff(String refDocPmid){
		this.resultDocs = retrieveResultDocumentsWithAssessments(refDocPmid);
		leaveOutNotAssessed = false;
		leaveOutNotDecidable = false;
		
		runArffCreation(refDocPmid, "classify", true);
	}
	
	
	private void runArffCreation(String refDocPmid, String filename, boolean getTestValues){
		
		DocumentAttributes[] attributeList = {
				DocumentAttributes.TITLE,
				DocumentAttributes.ABSTRACT,
				DocumentAttributes.FULLTEXTURL,
				DocumentAttributes.FULLTEXTJOURNAL,
				DocumentAttributes.FULLTEXTFREESTATUS,
				DocumentAttributes.MESHTERM,
				DocumentAttributes.MESHMAJORTOPIC,
				DocumentAttributes.MESHUI,
				DocumentAttributes.QUALIFIER,
				DocumentAttributes.QUALIFIERMAJORTOPIC,
				DocumentAttributes.QUALIFIERUI,
				DocumentAttributes.PMCID
		};
		
		getArffFiles(refDocPmid + "_" + filename, attributeList, getTestValues);
	}
	
	
	
	public void runUnbiasedArffCreation(){
		String refDocPmid = "16192371";
		loadResultDocs(refDocPmid);
		setLeaveOutNotAssessed(false);
		leaveOutNotDecidable = true;
		
		DocumentAttributes[] attributeList = {
				DocumentAttributes.TITLE,
				DocumentAttributes.ABSTRACT,
				DocumentAttributes.FULLTEXTURL,
				DocumentAttributes.FULLTEXTJOURNAL,
				DocumentAttributes.FULLTEXTFREESTATUS,
				DocumentAttributes.MESHTERM,
				DocumentAttributes.MESHMAJORTOPIC,
				DocumentAttributes.MESHUI,
				DocumentAttributes.QUALIFIER,
				DocumentAttributes.QUALIFIERMAJORTOPIC,
				DocumentAttributes.QUALIFIERUI,
				DocumentAttributes.PMCID
		};
		
//		add as many negatives as there are positives
//		getArffFiles(refDocPmid + "_unbiased_remNeg_trainAndTest_animalTest_allAttributes", attributeList, ClassAttributes.ANIMALTEST, -1, Boolean.FALSE);
//		getArffFiles(refDocPmid + "_unbiased_remNeg_trainAndTest_relevance_allAttributes", attributeList, ClassAttributes.RELEVANCE, -1, Boolean.FALSE);
//		getArffFiles(refDocPmid + "_unbiased_remNeg_trainAndTest_similarity_allAttributes", attributeList, ClassAttributes.SIMILARITY, -1, Boolean.FALSE);
//		add as many positives as there are negatives (by duplicating positives, since there are less)
//		getArffFiles(refDocPmid + "_unbiased_dupPos_trainAndTest_animalTest_allAttributes", attributeList, ClassAttributes.ANIMALTEST, -1, Boolean.TRUE);
//		getArffFiles(refDocPmid + "_unbiased_dupPos_trainAndTest_relevance_allAttributes", attributeList, ClassAttributes.RELEVANCE, -1, Boolean.TRUE);
//		getArffFiles(refDocPmid + "_unbiased_dupPos_trainAndTest_similarity_allAttributes", attributeList, ClassAttributes.SIMILARITY, -1, Boolean.TRUE);
		
//		add as many positives as there are negatives (by duplicating positives, since there are less), reduce number of instances
//		getArffFiles(refDocPmid + "_30_unbiased_dupPos_trainAndTest_animalTest_allAttributes", attributeList, ClassAttributes.ANIMALTEST, 30, Boolean.TRUE);
//		getArffFiles(refDocPmid + "_30_unbiased_dupPos_trainAndTest_relevance_allAttributes", attributeList, ClassAttributes.RELEVANCE, 30, Boolean.TRUE);
//		getArffFiles(refDocPmid + "_30_unbiased_dupPos_trainAndTest_similarity_allAttributes", attributeList, ClassAttributes.SIMILARITY, 30, Boolean.TRUE);
//		getArffFiles(refDocPmid + "_10_unbiased_dupPos_trainAndTest_animalTest_allAttributes", attributeList, ClassAttributes.ANIMALTEST, 10, Boolean.TRUE);
//		getArffFiles(refDocPmid + "_10_unbiased_dupPos_trainAndTest_relevance_allAttributes", attributeList, ClassAttributes.RELEVANCE, 10, Boolean.TRUE);
//		getArffFiles(refDocPmid + "_10_unbiased_dupPos_trainAndTest_similarity_allAttributes", attributeList, ClassAttributes.SIMILARITY, 10, Boolean.TRUE);
		getArffFiles(refDocPmid + "_50_unbiased_dupPos_trainAndTest_animalTest_allAttributes", attributeList, ClassAttributes.ANIMALTEST, 50, Boolean.TRUE);
		getArffFiles(refDocPmid + "_50_unbiased_dupPos_trainAndTest_relevance_allAttributes", attributeList, ClassAttributes.RELEVANCE, 50, Boolean.TRUE);
		getArffFiles(refDocPmid + "_50_unbiased_dupPos_trainAndTest_similarity_allAttributes", attributeList, ClassAttributes.SIMILARITY, 50, Boolean.TRUE);
		getArffFiles(refDocPmid + "_75_unbiased_dupPos_trainAndTest_animalTest_allAttributes", attributeList, ClassAttributes.ANIMALTEST, 75, Boolean.TRUE);
		getArffFiles(refDocPmid + "_75_unbiased_dupPos_trainAndTest_relevance_allAttributes", attributeList, ClassAttributes.RELEVANCE, 75, Boolean.TRUE);
		getArffFiles(refDocPmid + "_75_unbiased_dupPos_trainAndTest_similarity_allAttributes", attributeList, ClassAttributes.SIMILARITY, 75, Boolean.TRUE);
	}
	
	
	public void runArffCreation(){
		String refDocPmid = "16192371";
		loadResultDocs(refDocPmid);
		setLeaveOutNotAssessed(false);
		leaveOutNotDecidable = true;
		int numOfInstances = 50;		
		
		
		
//		Attributes
		DocumentAttributes[] attributeList = {
				DocumentAttributes.TITLE,
				DocumentAttributes.ABSTRACT,
				DocumentAttributes.FULLTEXTURL,
				DocumentAttributes.FULLTEXTJOURNAL,
				DocumentAttributes.FULLTEXTFREESTATUS,
				DocumentAttributes.MESHTERM,
				DocumentAttributes.MESHMAJORTOPIC,
				DocumentAttributes.MESHUI,
				DocumentAttributes.QUALIFIER,
				DocumentAttributes.QUALIFIERMAJORTOPIC,
				DocumentAttributes.QUALIFIERUI,
				DocumentAttributes.PMCID
		};
		DocumentAttributes[] titleAttr = {
				DocumentAttributes.TITLE
		};
		DocumentAttributes[] abstractAttr = {
				DocumentAttributes.ABSTRACT
		};
		DocumentAttributes[] titleAndAbstractAttr = {
				DocumentAttributes.TITLE,
				DocumentAttributes.ABSTRACT
		};
		DocumentAttributes[] meshTermAttr = {
				DocumentAttributes.MESHTERM
		};
		DocumentAttributes[] meshMTAttr = {
				DocumentAttributes.MESHMAJORTOPIC
		};
		DocumentAttributes[] meshUIAttr = {
				DocumentAttributes.MESHUI
		};
		DocumentAttributes[] meshAllDescAttr = {
				DocumentAttributes.MESHTERM,
				DocumentAttributes.MESHMAJORTOPIC,
				DocumentAttributes.MESHUI
		};		
		DocumentAttributes[] meshQualifierAttr = {
				DocumentAttributes.QUALIFIER
		};
		DocumentAttributes[] meshQualifierMTAttr = {
				DocumentAttributes.QUALIFIERMAJORTOPIC
		};
		DocumentAttributes[] meshQualifierUIAttr = {
				DocumentAttributes.QUALIFIERUI
		};
		DocumentAttributes[] meshAllQualifierAttr = {
				DocumentAttributes.QUALIFIER,
				DocumentAttributes.QUALIFIERMAJORTOPIC,
				DocumentAttributes.QUALIFIERUI
		};
		
		DocumentAttributes[] fullTextUrlAttr = {
				DocumentAttributes.FULLTEXTURL
		};
		DocumentAttributes[] fullTextJournalAttr = {
				DocumentAttributes.FULLTEXTJOURNAL
		};
		DocumentAttributes[] fullTextFreeStatusAttr = {
				DocumentAttributes.FULLTEXTFREESTATUS
		};
		DocumentAttributes[] fullTextAllAttr = {
				DocumentAttributes.FULLTEXTURL,
				DocumentAttributes.FULLTEXTJOURNAL,
				DocumentAttributes.FULLTEXTFREESTATUS
		};
		

		DocumentAttributes[] pmcidAttr = {
				DocumentAttributes.PMCID
		};
		
//		######################################
		
//		numOfInstances = -1;
////		getArffFiles - animal test
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_meshterm", meshTermAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_meshMT", meshMTAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_meshUi", meshUIAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_meshAllDesc", meshAllDescAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_meshQualifier", meshQualifierAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_meshQualifierMT", meshQualifierMTAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_meshQualifierUi", meshQualifierUIAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_meshAllQualifier", meshAllQualifierAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_fullTextUrl", fullTextUrlAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_fullTextJournal", fullTextJournalAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_fullTextFreeStatus", fullTextFreeStatusAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_fullTextAll", fullTextAllAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_trainAndTest_animalTest_pmcid", pmcidAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		
//		
////		getArffFiles - similarity
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_meshterm", meshTermAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_meshMT", meshMTAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_meshUi", meshUIAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_meshAllDesc", meshAllDescAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_meshQualifier", meshQualifierAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_meshQualifierMT", meshQualifierMTAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_meshQualifierUi", meshQualifierUIAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_meshAllQualifier", meshAllQualifierAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_fullTextUrl", fullTextUrlAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_fullTextJournal", fullTextJournalAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_fullTextFreeStatus", fullTextFreeStatusAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_fullTextAll", fullTextAllAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_trainAndTest_similarity_pmcid", pmcidAttr, ClassAttributes.SIMILARITY, numOfInstances);
		
//		#############################################
		
//		numOfInstances = 20;
////		getArffFiles - animal test
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_allAttributes", attributeList, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_title", titleAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_abstract", abstractAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_titleAndAbstract", titleAndAbstractAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_meshterm", meshTermAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_meshMT", meshMTAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_meshUi", meshUIAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_meshAllDesc", meshAllDescAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_meshQualifier", meshQualifierAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_meshQualifierMT", meshQualifierMTAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_meshQualifierUi", meshQualifierUIAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_meshAllQualifier", meshAllQualifierAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_fullTextUrl", fullTextUrlAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_fullTextJournal", fullTextJournalAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_fullTextFreeStatus", fullTextFreeStatusAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_fullTextAll", fullTextAllAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_animalTest_pmcid", pmcidAttr, ClassAttributes.ANIMALTEST, numOfInstances);
//		
//		
////		getArffFiles - similarity
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_allAttributes", attributeList, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_title", titleAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_abstract", abstractAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_titleAndAbstract", titleAndAbstractAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_meshterm", meshTermAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_meshMT", meshMTAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_meshUi", meshUIAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_meshAllDesc", meshAllDescAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_meshQualifier", meshQualifierAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_meshQualifierMT", meshQualifierMTAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_meshQualifierUi", meshQualifierUIAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_meshAllQualifier", meshAllQualifierAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_fullTextUrl", fullTextUrlAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_fullTextJournal", fullTextJournalAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_fullTextFreeStatus", fullTextFreeStatusAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_fullTextAll", fullTextAllAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_similarity_pmcid", pmcidAttr, ClassAttributes.SIMILARITY, numOfInstances);
//		
////		getArffFiles - relevance
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_allAttributes", attributeList, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_title", titleAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_abstract", abstractAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_titleAndAbstract", titleAndAbstractAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_meshterm", meshTermAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_meshMT", meshMTAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_meshUi", meshUIAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_meshAllDesc", meshAllDescAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_meshQualifier", meshQualifierAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_meshQualifierMT", meshQualifierMTAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_meshQualifierUi", meshQualifierUIAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_meshAllQualifier", meshAllQualifierAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_fullTextUrl", fullTextUrlAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_fullTextJournal", fullTextJournalAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_fullTextFreeStatus", fullTextFreeStatusAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_fullTextAll", fullTextAllAttr, ClassAttributes.RELEVANCE, numOfInstances);
//		
//		getArffFiles(refDocPmid + "_" + numOfInstances + "_trainAndTest_relevance_pmcid", pmcidAttr, ClassAttributes.RELEVANCE, numOfInstances);
		
//		########### get complete set of assessed documents ###########
		numOfInstances = 100;
		getArffFiles(refDocPmid + "_test_completeAssessedSet_similarity_allAttributes", attributeList, ClassAttributes.SIMILARITY, numOfInstances, null);
		getArffFiles(refDocPmid + "_test_completeAssessedSet_relevance_allAttributes", attributeList, ClassAttributes.RELEVANCE, numOfInstances, null);
		getArffFiles(refDocPmid + "_test_completeAssessedSet_animalTest_allAttributes", attributeList, ClassAttributes.ANIMALTEST, numOfInstances, null);
	}
	
	
	public void filterPositiveInstancesFromArff(String sourceArffFilePath, String targetArffFilePath, String positiveValue){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(sourceArffFilePath)));
			ArrayList<String> positiveInstances = new ArrayList<String>();
			String line;
			
			while((line = reader.readLine()) != null){
				if(line.endsWith(positiveValue))
					positiveInstances.add(line);
			}
			reader.close();
			
			ToolsUtil.writeListContentToFile(targetArffFilePath, positiveInstances);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void testClassifier(){
		Instances data = readArffFile("C:/Users/du/Projekte/SMAFIRA/git/data/arff/nurBewertete16192371.arff");
		classify(data);
	}
	
	public void testFilterInstances(){
		String sourceRelevance = "C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_random/testSet/16192371_test_completeAssessedSet_relevance_allAttributes.arff";
		String targetRelevance = "C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_random/testSet/positiveRelevance.arff";
		String positiveRelevance = "4";
		filterPositiveInstancesFromArff(sourceRelevance, targetRelevance, positiveRelevance);
		
		String sourceSimilarity = "C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_random/testSet/16192371_test_completeAssessedSet_similarity_allAttributes.arff";
		String targetSimilarity = "C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_random/testSet/positiveSimilarity.arff";
		String positiveSimilarity = "1";
		filterPositiveInstancesFromArff(sourceSimilarity, targetSimilarity, positiveSimilarity);
		
		String sourceAnimal = "C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_random/testSet/16192371_test_completeAssessedSet_animalTest_allAttributes.arff";
		String targetAnimal = "C:/Users/du/Projekte/SMAFIRA/git/data/arff/arff_random/testSet/positiveAnimalTest.arff";
		String positiveAnimal = "0";
		filterPositiveInstancesFromArff(sourceAnimal, targetAnimal, positiveAnimal);
	}
	
	
	
	public static void main(String[] args) {
		WekaArffCreator wt = new WekaArffCreator();
		wt.createWekaTrainingArff("16192371");
		wt.createWekaClassificationArff("16192371");
		
//		wt.runArffCreation("random", "fullTestset");
//		wt.runUnbiasedArffCreation();
//		wt.runArffCreation();
//		wt.createCompleteDataSet("11932745", true);
//		wt.testNominal();
//		wt.testClassifier();
//		wt.testFilterInstances();
	}

}
