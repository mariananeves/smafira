package htmlSearch.wrapper;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tools.HtmlDocumentUtil;



import htmlSearch.PubmedFulltextReference;
import htmlSearch.HtmlWrapper;

public class PubmedWrapper extends HtmlWrapper{
	

	public PubmedWrapper(){
		super();
	}
	
	public ArrayList<PubmedFulltextReference> getFulltextReferences(String documentUrl){
		try {
			return getFulltextReferences(HtmlDocumentUtil.getHtmlDocument(documentUrl));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<PubmedFulltextReference> getFulltextReferences(Document pubmedDocumentHtmlDoc){
		ArrayList<PubmedFulltextReference> fullTextReferenceList = new ArrayList<PubmedFulltextReference>();
		PubmedFulltextReference fulltextReference;
		String link, free_status, journal;
		
		System.out.println("docTitle: " + pubmedDocumentHtmlDoc.title());
		
		Elements fulltextLinks = pubmedDocumentHtmlDoc.select(HtmlContentString.FULL_TEXT_LINKS.getPath());

		if(fulltextLinks != null){
			for(Element linkElement: fulltextLinks){
				link = linkElement.attr(HtmlContentString.LINK_ATTR.getPath());
				free_status = linkElement.attr(HtmlContentString.FREE_STATUS_ATTR.getPath());
				journal = linkElement.attr(HtmlContentString.JOURNAL_ATTR.getPath());
				
				fulltextReference = new PubmedFulltextReference();
				
				fulltextReference.setFullTextLink(link);
				fulltextReference.setFreeStatus(free_status);
				fulltextReference.setJournal(journal);

				fullTextReferenceList.add(fulltextReference);
			}
		}
		
		
		return fullTextReferenceList;
	}
	
	
	
	public static void testWrapper(){
		PubmedWrapper wrapper = new PubmedWrapper();
		wrapper.getFulltextReferences("http://www.ncbi.nlm.nih.gov/pubmed/16310349");
	}
	

	public static void main(String[] args){
		testWrapper();
	}
}
