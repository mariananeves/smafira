package htmlSearch;

public class PubmedFulltextReference {

	private String fullTextLink = "";
	private Boolean freeStatus = null;
	private String journal = "";
	
	
	public void setFreeStatus(String freeStatusAttrContent){
		if(freeStatusAttrContent == null || freeStatusAttrContent.isEmpty())
			freeStatus = null;
		else if(freeStatusAttrContent.equals("free"))
			freeStatus = true;
		else
			freeStatus = false;
	}
	
	
	public String getFullTextLink() {
		return fullTextLink;
	}
	public void setFullTextLink(String fullTextLink) {
		this.fullTextLink = fullTextLink;
	}
	public Boolean getFreeStatus() {
		return freeStatus;
	}
	public void setFreeStatus(Boolean freeStatus) {
		this.freeStatus = freeStatus;
	}
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	
	

}
