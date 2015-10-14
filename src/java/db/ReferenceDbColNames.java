package db;

public enum ReferenceDbColNames {
	ID("id"),
	VERSION("version"),
	PMID("pmid"),
	TITLE("title"),;
	
	String path;
	
	private ReferenceDbColNames(String path){
		this.path = path;
	}
}
