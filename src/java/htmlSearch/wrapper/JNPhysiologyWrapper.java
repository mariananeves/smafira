package htmlSearch.wrapper;

import java.io.File;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import tools.HtmlDocumentUtil;
import tools.ToolsUtil;

import htmlSearch.HtmlWrapper;
import htmlSearch.Reference;
import htmlSearch.Reference.Author;

public class JNPhysiologyWrapper extends HtmlWrapper{

	public enum ContentXPath{
		REFERENCE("ol[class*=cit-list] > li > div[class*=cit ref-cit]"),
		REFERENCE_DOI_ATTR("data-doi"),
		REFERENCE_UNSTRUCTURED("div[class=cit-metadata unstructured]"),
//		authors version 1
		FIRST_AUTHOR_SURNAME("cite > span[class*=arthw-firstauthor] > span[class*=arthw-snm]"),
		FIRST_AUTHOR_FIRSTNAME("cite > span[class*=arthw-firstauthor] > span[class*=arthw-fnm]"),
		AUTHORS("cite > strong"),
//		authors version 2
		AUTHORS2_ROOT("ol[class=cit-auth-list]"),
		AUTHORS2_ENTRY("li > span[class=cit-auth]"),
		AUTHORS2_SURNAME("surname"),
		AUTHORS2_FIRSTNAME("given-names"),
		CITE_SOURCE("cite > span[class=cit-source]"),
		CITE_SOURCE_JNL("cite > abbr[class=cit-jnl-abbrev]"),
		CITE_VOLUME("cite > span[class=cit-vol]"),
		CITE_FPAGE("cite > span[class=cit-fpage]"),
		CITE_LPAGE("cite > span[class=cit-lpage]"),
		CITE_DATE("cite > span[class=cit-pub-date]"),
		CITE_COMPLETE("cite"),
		CITE_TITLE("cite > span[class=cit-article-title]"),
		MEDLINE_LINK("div[class=cit-extra] > a[class*=medline]"),
		MEDLINE_LINK_ATTR("href"),
		PUBL_NAME("cite > span[class=cit-publ-name]"),
		PUBL_LOC("cite > span[class=cit-publ-loc]");
		
		String path;
		
		private ContentXPath(String path) {
			this.path = path;
		}
	}
	
	public JNPhysiologyWrapper(){
		
	}
	
	
	public void extractReferences(Document htmlDoc){
		Elements references = htmlDoc.select(ContentXPath.REFERENCE.path);
		Element firstAuthorSNM, firstAuthorFNM, authors, citationAuthorsList, source, volume, fpage, lpage, date, citeComplete, medline, titleElement,
		publisherName, publisherLocation, referenceUnstructured;
		Elements citationAuthorsEntries, authorElements;
		Document referenceHtml, authorsHtml;
		Reference refInfo;
		String authorsList, firstPage, lastPage, completePages, title, completeCitation, citeNodeText, medlineLink, authorClass, firstName, lastName;

		if(references != null){
			for(Element reference: references){		
				refInfo = new Reference();

//				doi
				refInfo.setDoi(reference.attr(ContentXPath.REFERENCE_DOI_ATTR.path));
				
//				parse content of reference
				referenceHtml = HtmlDocumentUtil.getHtmlDocumentFromString(reference.html());
				
//				check if reference is unstructured
				referenceUnstructured = referenceHtml.select(ContentXPath.REFERENCE_UNSTRUCTURED.path).first();
				if(referenceUnstructured != null){
//					TODO: handle unstructured Data
				}
				else{

					
//					authors
					authorsList = "";
					citationAuthorsList = referenceHtml.select(ContentXPath.AUTHORS2_ROOT.path).first();
					if(citationAuthorsList != null){
//						citation version 2
						authorsHtml = HtmlDocumentUtil.getHtmlDocumentFromString(citationAuthorsList.html());
						citationAuthorsEntries = authorsHtml.select(ContentXPath.AUTHORS2_ENTRY.path);
						for(Element authorEntry: citationAuthorsEntries){
							authorElements = authorEntry.children();
							lastName = "";
							firstName = "";
							for(Element authorElement: authorElements){
								authorClass = authorElement.attr("class");
								if(authorClass.contains(ContentXPath.AUTHORS2_SURNAME.path))
									lastName = authorElement.text();
								else if(authorClass.contains(ContentXPath.AUTHORS2_FIRSTNAME.path))
									firstName = authorElement.text();
							}
							
							refInfo.addAuthor(refInfo.new Author(lastName, firstName));
						}
					}
					else{				
//						citation version 1
						firstAuthorSNM = referenceHtml.select(ContentXPath.FIRST_AUTHOR_SURNAME.path).first();
						firstAuthorFNM = referenceHtml.select(ContentXPath.FIRST_AUTHOR_FIRSTNAME.path).first();
						
						refInfo.addAuthor(refInfo.new Author(firstAuthorSNM.text(), firstAuthorFNM.text()));				
						
						authors = referenceHtml.select(ContentXPath.AUTHORS.path).first();
						authorsList = "";
						if(authors != null){
							authorsList = authors.text();
							addAuthorsToReference(authorsList,refInfo);
						}
					
					}
				
//					reference source/journal
					source = referenceHtml.select(ContentXPath.CITE_SOURCE.path).first();
					if(source != null){
						refInfo.setSource(source.text());
					}
					else{
						source = referenceHtml.select(ContentXPath.CITE_SOURCE_JNL.path).first();
						if(source != null){
							refInfo.setSource(source.text());
						}
					}
					
//					reference volume
					volume = referenceHtml.select(ContentXPath.CITE_VOLUME.path).first();
					if(volume != null)
						refInfo.setVolume(volume.text());
					
//					reference date
					date = referenceHtml.select(ContentXPath.CITE_DATE.path).first();
					if(date != null)
						refInfo.setDate(date.text());
					
					
					
//					complete citation
					citeComplete = referenceHtml.select(ContentXPath.CITE_COMPLETE.path).first();
					completeCitation = citeComplete.text();
					refInfo.setCompleteCitation(completeCitation);
					
					
//					reference title
					titleElement = referenceHtml.select(ContentXPath.CITE_TITLE.path).first();
					if(titleElement != null){
						title = titleElement.text();
						refInfo.setTitle(title);
					}
					else{
						citeNodeText = citeComplete.ownText();
						title = extractTitleFromCitation(citeNodeText);
						refInfo.setTitle(title);					
					}
					
					
//					reference first page
					fpage = referenceHtml.select(ContentXPath.CITE_FPAGE.path).first();
					firstPage = "";
					if(fpage != null)
						firstPage = fpage.text();
					
					
//					reference last page
					lpage = referenceHtml.select(ContentXPath.CITE_LPAGE.path).first();
					if(lpage != null){
						lastPage = lpage.text();
						completePages = firstPage + "-" + lastPage;
						refInfo.setPages(completePages);
					}
					else if(fpage != null){
						completePages = extractCompletePagesFromCitation(completeCitation, firstPage);
						refInfo.setPages(completePages);
					}
							
//					pmid from medline link (if available)
					medline = referenceHtml.select(ContentXPath.MEDLINE_LINK.path).first();
					if(medline != null){
						medlineLink = medline.attr(ContentXPath.MEDLINE_LINK_ATTR.path);
						refInfo.setPmid(extractPMIDFromMedlineLink(medlineLink));
					}
					
//					publisherName
					publisherName = referenceHtml.select(ContentXPath.PUBL_NAME.path).first();
					if(publisherName != null)
						refInfo.setPublisherName(publisherName.text());
					
//					publisherLocation
					publisherLocation = referenceHtml.select(ContentXPath.PUBL_LOC.path).first();
					if(publisherLocation != null)
						refInfo.setPublisherLocation(publisherLocation.text());
				}
				
				
//				System.out.print("doi: " + refInfo.getDoi() + "; authors: ");
//				for(Author author: refInfo.getAuthors()){
//					System.out.print(author.getLastName() + ", " + author.getFirstName() + "; ");
//				}
//				System.out.print("source: " + refInfo.getSource() + "; volume: " + refInfo.getVolume() + "; date: " + refInfo.getDate() + "; pages: " + refInfo.getPages() + "; title: " + title);
//				System.out.println();
//				System.out.println("pages: " + refInfo.getPages());
//				System.out.println("pmid: " + refInfo.getPmid());
//				System.out.println("citeNode: " + citeNodeText);
//				System.out.println("title: " + refInfo.getTitle());
//				System.out.println("publisher: " + refInfo.getPublisherName() + "; loc: " + refInfo.getPublisherLocation());
			}
		}
		
	}
	
	
	private void addAuthorsToReference(String authors, Reference reference){
		if(authors.trim().endsWith("."))
			authors = authors.substring(0, authors.lastIndexOf(".")).trim();
		String[] authorList = authors.split(",");
		String firstName, lastName;
		
		for(String author: authorList){
			author = author.trim();
			if(!author.isEmpty() && author.length() > 2){
				if(author.contains(" ")){
					lastName = author.substring(0, author.indexOf(" ")).trim();
					firstName = author.substring(author.indexOf(" ")).trim();
				}
				else{
					lastName = author;
					firstName = "";
				}
				
				
				reference.addAuthor(reference.new Author(lastName, firstName));
			}
		}
	}
	
	private String extractTitleFromCitation(String citeNodeText){
		String title;
		if(citeNodeText.contains(". "))
			title = citeNodeText.substring(0, citeNodeText.indexOf(". ")+1);
		else if(citeNodeText.contains("?"))
			title = citeNodeText.substring(0, citeNodeText.lastIndexOf("?")+1);
		else
			title = citeNodeText;
		
		return title;
	}
	
	private String extractCompletePagesFromCitation(String completeCitation, String firstPage){
		if(firstPage.isEmpty())
			return "";
		
		
		String firstPagePrefix1 = firstPage + "�";
		String firstPagePrefix2 = firstPage + "-";
		

		String completePages = completeCitation.substring(completeCitation.lastIndexOf(firstPage));
		if(completePages.contains(","))
			completePages = completePages.substring(0, completePages.indexOf(",")).trim();
//		comment: "–" != "-" !!!!
		else if(completeCitation.contains(firstPagePrefix1)){
			completePages = completeCitation.substring(completeCitation.lastIndexOf(firstPagePrefix1)+firstPagePrefix1.length()).trim();
			completePages = ToolsUtil.getFirstStringOfNumbersInText(completePages);
			completePages = firstPage + "-" + completePages;
		}
		else if(completeCitation.contains(firstPagePrefix2)){
			completePages = completeCitation.substring(completeCitation.lastIndexOf(firstPagePrefix2)+firstPagePrefix2.length()).trim();
			completePages = ToolsUtil.getFirstStringOfNumbersInText(completePages);
			completePages = firstPage + "-" + completePages;
		}
		else{
			completePages = firstPage;
		}
		
		
		return completePages;
	}
	
	private String extractPMIDFromMedlineLink(String link){
		String pmid = "";
		String prefix = "access_num=";
		String postfix = "&";
		
		if(!link.isEmpty()){
			pmid = link.substring(link.indexOf(prefix)+prefix.length());
			pmid = pmid.substring(0, pmid.indexOf(postfix)).trim();
		}
				
		return pmid;
	}
	
	
	private static void testWrapperOnAllPhysio(){
		JNPhysiologyWrapper jpw = new JNPhysiologyWrapper();
		String directory = "C:/Users/du/Projekte/SMAFIRA/git/data/html/fulltextsWithRefs/";
		String[] fileNameList = (new File(directory)).list();
		
		for(String fileName: fileNameList){
//			System.out.println("fileName: " + fileName);
			if(fileName.contains("jn.physiology.org")){
				System.out.println("fileName_extracted: " + fileName);
				jpw.extractReferences(HtmlDocumentUtil.getHtmlDocumentFromFile(directory + fileName));

			}
		}
	}
	
	private static void testWrapper(){
		JNPhysiologyWrapper jpw = new JNPhysiologyWrapper();
		jpw.extractReferences(HtmlDocumentUtil.getHtmlDocumentFromFile("C:/Users/du/Projekte/SMAFIRA/git/data/html/fulltexts/18003881_jn.physiology.org.html"));
//		jpw.extractReferences(HtmlDocumentUtil.getHtmlDocumentFromFile("C:/Users/du/Projekte/SMAFIRA/git/data/html/fulltexts/12686573_jn.physiology.org.html"));

	}
	
	private static void copyFiles(){
		String[] fileList = ToolsUtil.getListOfFileNames("C:/Users/du/Projekte/SMAFIRA/git/data/html/fulltextWithReferences.txt");
		ToolsUtil.copyFiles("C:/Users/du/Projekte/SMAFIRA/git/data/html/fulltexts", fileList, "C:/Users/du/Projekte/SMAFIRA/git/data/html/fulltextsWithRefs");
	}
	
	public static void main(String[] args) {	
//		copyFiles();
//		testWrapper();	
		testWrapperOnAllPhysio();
	}

}
