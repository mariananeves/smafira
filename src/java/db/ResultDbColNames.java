package db;

public enum ResultDbColNames {
	ID("id"),
	VERSION("version"),
	ABSTRACT("doc_abstract"),
	ANIMALTEST("is_animal_test"),
	LASTCHANGE("last_change"),
	PMCID("pmcid"),
	PMID("pmid"),
	RANK("rank"),
	REFDOCID("ref_doc_id"),
	SIMILARITY("similarity"),
	TITLE("title"),
	RELEVANCE("relevance");
	
	String path;
	
	private ResultDbColNames(String path){
		this.path = path;
	}
}
