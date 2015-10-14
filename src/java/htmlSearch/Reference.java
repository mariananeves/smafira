package htmlSearch;

import java.util.ArrayList;

public class Reference {

	private String title = "";
	private ArrayList<Author> authors;
	private String doi = "";
	private String pmid = "";
	private String source = "";
	private String volume = "";
	private String pages = "";
	private String date = "";
	private String completeCitation = "";
	private String publisherName = "";
	private String publisherLocation = "";
	
	
	public Reference(){
		authors = new ArrayList<Author>();
	}
	
	
	
	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public ArrayList<Author> getAuthors() {
		return authors;
	}


	public void addAuthor(Author author){
		this.authors.add(author);
	}
	
	public void setAuthors(ArrayList<Author> authors) {
		this.authors = authors;
	}



	public String getDoi() {
		return doi;
	}



	public void setDoi(String doi) {
		this.doi = doi;
	}

	


	public String getPmid() {
		return pmid;
	}



	public void setPmid(String pmid) {
		this.pmid = pmid;
	}


	
	

	public String getSource() {
		return source;
	}



	public void setSource(String source) {
		this.source = source;
	}



	public String getVolume() {
		return volume;
	}



	public void setVolume(String volume) {
		this.volume = volume;
	}



	public String getPages() {
		return pages;
	}



	public void setPages(String pages) {
		this.pages = pages;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}


	


	public String getCompleteCitation() {
		return completeCitation;
	}



	public void setCompleteCitation(String completeCitation) {
		this.completeCitation = completeCitation;
	}


	


	public String getPublisherName() {
		return publisherName;
	}



	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}



	public String getPublisherLocation() {
		return publisherLocation;
	}



	public void setPublisherLocation(String publisherLocation) {
		this.publisherLocation = publisherLocation;
	}





	public class Author{
		private String firstName;
		private String lastName;
		
		public Author(String lastName, String firstName){
			this.lastName = lastName;
			this.firstName = firstName;
		}
		
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		
	}

}
