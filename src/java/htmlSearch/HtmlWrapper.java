package htmlSearch;

public abstract class HtmlWrapper {
	
	public enum HtmlContentString{
		FULL_TEXT_LINKS("div[class=icons portlet] > a[href]"), LINK_ATTR("href"), FREE_STATUS_ATTR("free_status"), JOURNAL_ATTR("journal"),
		HTML_PAGE_BASEURL("http://www.ncbi.nlm.nih.gov/pubmed/");
		
		String path;
		
		private HtmlContentString(String path) {
			this.path = path;
		}
		
		public String getPath(){
			return this.path;
		}	
	};


}
