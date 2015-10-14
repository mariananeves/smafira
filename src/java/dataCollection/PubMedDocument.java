package dataCollection;

import htmlSearch.PubmedFulltextReference;

import java.util.ArrayList;

public class PubMedDocument {
	private String rank;
	private String docPubMedID;
	private String docPMCID;
	private String docTitle;
	private ArrayList<String> references;
	private boolean hasAbstract;
	private String docAbstract;
	private ArrayList<MeshEntry> meshTerms;
	private String doi;
	private String pii;
	private ArrayList<PubmedFulltextReference> fulltextReferences;
	
	
//	assessments
	private String similarity;
	private String relevance;
	private String animalTest;



	private String docFullText;
	
	
	public enum ContentXPath{
		ESEARCHROOT("eSearchResult"), ID_LIST_SEARCH("IdList"), ID_SEARCH("Id"),
		SUMROOT("eSummaryResult"), DOCROOT_SUM("DocSum"), ID_SUM("Id"), TITLE_SUM("Item[@Name='Title']"), REFERENCES_SUM("Item[@Name='References']"),
		PMCID_SUM("Item[@Name='ArticleIds']/Item[@Name='pmcid']"), HASABSTRACT_SUM("Item[@Name='HasAbstract']"), DOI_SUM("Item[@Name='DOI']"),
		PII_SUM("Item[@Name='ArticleIds']/Item[@Name='pii']"),
		AUTHOR_SUM("Item[@Name='AuthorList']/Item[@Name='Author']"),
		FETROOT("PubmedArticleSet"), DOCROOT_FET("PubmedArticle"), ID_FET("MedlineCitation/PMID"), TITLE_FET("MedlineCitation/Article/ArticleTitle"),
		ABSTRACT_FET("MedlineCitation/Article/Abstract/AbstractText"), PMCID_FET("PubmedData/ArticleIdList/ArticleId[@IdType='pmc']"),
		MESHTERMS_FET("MedlineCitation/MeshHeadingList/MeshHeading"), MESHDESCRIPTOR("DescriptorName"), MESHQUALIFIER("QualifierName"),
		MESH_ATTR_MAJORTOPIC("MajorTopicYN"), MESH_ATTR_UI("UI");
		
		public String path;
		
		private ContentXPath(String path) {
			this.path = path;
		}
		
		public String getPath(){
			return this.path;
		}	
	};
	
	public enum RelatedDocumentXPath{
		ROOT("RelatedDocumentSet"), RELDOC_ROOT("RelatedDocument"),
		RANK("Rank"),
		ID("PubMedID"),
		TITLE("Title"),
		ABSTRACT("Abstract"),
		REFERENCES("References"),
		PMCID("PMCID"),
		SEARCHDOC_ID_ATTR("SearchDocPMID"),
		FULLTEXTLINKS_ROOT("FulltextLinks"), FULLTEXTLINK("FulltextLink"), LINK_URL_NODE("Url"), LINK_FREE_STATUS_NODE("Free_Status"),
			LINK_JOURNAL_NODE("Journal"),
		MESHTERMS_ROOT("MeshTerms"), MESHTERM("MeshTerm"), DESCRIPTORNAME("DescriptorName"), DESCRIPTORMAJORTOPIC("DescriptorMajorTopic"),
			DESCRIPTORUI("DescriptorUi"), QUALIFIERNAME("QualifierName"), QUALIFIERMAJORTOPIC("QualifierMajorTopic"), QUALIFIERUI("QualifierUi");
		
		
		String path;
		
		private RelatedDocumentXPath(String path) {
			this.path = path;
		}
		
		public String getPath(){
			return this.path;
		}
	};
	
	
	
	
	
	
	
	public String getRank() {
		return rank;
	}


	public void setRank(String rank) {
		this.rank = rank;
	}


	public String getDoi() {
		return doi;
	}


	public void setDoi(String doi) {
		this.doi = doi;
	}


	public String getPii() {
		return pii;
	}


	public void setPii(String pii) {
		this.pii = pii;
	}


	public ArrayList<MeshEntry> getMeshTerms() {
		return meshTerms;
	}


	public void setMeshTerms(ArrayList<MeshEntry> meshTerms) {
		this.meshTerms = meshTerms;
	}


	public PubMedDocument(){
		this.hasAbstract = false;
	}
	
	
	public static boolean isSummaryResultDoc(String xmlRootName){
		return xmlRootName.equals(ContentXPath.SUMROOT.getPath());
	}
	
	public static boolean isFetchResultDoc(String xmlRootName){
		return xmlRootName.equals(ContentXPath.FETROOT.getPath());
	}
	
		
//	##### getter & setter #####
	public String getDocPubMedID() {
		return docPubMedID;
	}



	public String getDocTitle() {
		return docTitle;
	}



	public ArrayList<String> getReferences() {
		return references;
	}



	public boolean hasAbstract() {
		return hasAbstract;
	}



	public String getDocAbstract() {
		return docAbstract;
	}



	public String getDocFullText() {
		return docFullText;
	}


	public void setDocPubMedID(String docPubMedID) {
		this.docPubMedID = docPubMedID;
	}


	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}


	public void setReferences(ArrayList<String> references) {
		this.references = references;
	}


	public void setHasAbstract(boolean hasAbstract) {
		this.hasAbstract = hasAbstract;
	}


	public void setDocAbstract(String docAbstract) {
		this.docAbstract = docAbstract;
	}


	public void setDocFullText(String docFullText) {
		this.docFullText = docFullText;
	}


	public String getDocPMCID() {
		return docPMCID;
	}


	public void setDocPMCID(String docPMCID) {
		this.docPMCID = docPMCID;
	}
	
	public ArrayList<PubmedFulltextReference> getFulltextReferences() {
		return fulltextReferences;
	}


	public void setFulltextReferences(
			ArrayList<PubmedFulltextReference> fulltextReferences) {
		this.fulltextReferences = fulltextReferences;
	}
	
	
	
	public String getSimilarity() {
		return similarity;
	}


	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}


	public String getRelevance() {
		return relevance;
	}


	public void setRelevance(String relevance) {
		this.relevance = relevance;
	}


	public String getAnimalTest() {
		return animalTest;
	}


	public void setAnimalTest(String animalTest) {
		this.animalTest = animalTest;
	}



//	###################################

	public class MeshEntry{
		private String descriptorName;
		private String descriptorMajorTopic;
		private String descriptorUI;
		
		private String qualifierName;
		private String qualifierMajorTopic;
		private String qualifierUI;
		
		
		public String getDescriptorName() {
			return descriptorName;
		}
		public void setDescriptorName(String descriptorName) {
			this.descriptorName = descriptorName;
		}
		public String getDescriptorMajorTopic() {
			return descriptorMajorTopic;
		}
		public void setDescriptorMajorTopic(String descriptorMajorTopic) {
			this.descriptorMajorTopic = descriptorMajorTopic;
		}
		public String getDescriptorUI() {
			return descriptorUI;
		}
		public void setDescriptorUI(String descriptorUI) {
			this.descriptorUI = descriptorUI;
		}
		public String getQualifierName() {
			return qualifierName;
		}
		public void setQualifierName(String qualifierName) {
			this.qualifierName = qualifierName;
		}
		public String getQualifierMajorTopic() {
			return qualifierMajorTopic;
		}
		public void setQualifierMajorTopic(String qualifierMajorTopic) {
			this.qualifierMajorTopic = qualifierMajorTopic;
		}
		public String getQualifierUI() {
			return qualifierUI;
		}
		public void setQualifierUI(String qualifierUI) {
			this.qualifierUI = qualifierUI;
		}		
	}
	
}
