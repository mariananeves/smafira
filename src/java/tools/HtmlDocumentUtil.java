package tools;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import pubmedSearch.HttpSearcher;

public class HtmlDocumentUtil {

	
	public static Document getHtmlDocument(String documentUrl) throws IOException{
		Document doc = null;
		

		Response response = Jsoup.connect(documentUrl).followRedirects(true).execute();
		doc = response.parse();
		
		return doc;
	}
	
	public static Document getHtmlDocumentFromString(String htmlDoc){
		Document doc = null;
		
		doc = Jsoup.parse(htmlDoc);
		
		return doc;
	}
	
	public static Document getHtmlDocumentFromFile(String filePath){
		File file = new File(filePath);
		Document doc = null;
		try {
			doc = Jsoup.parse(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return doc;
	}

	
	public static URI buildUri(String host, String path){
		URI uri = null;				
		
		try {
			URIBuilder uriBuilder = new URIBuilder();
			
			uriBuilder.setScheme("http");
			uriBuilder.setHost(host);
			uriBuilder.setPath(path);	
			
			uri = uriBuilder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return uri;
	}
	
	
	public static String getHtmlDocumentAsString(String host, String path){
		URI uri = buildUri(host, path);		
		return getHtmlDocumentAsString(uri);	
	}
	
	public static String getHtmlDocumentAsString(URI uri){
		HttpSearcher searcher = new HttpSearcher();
		String htmlDoc = "";
		
		htmlDoc = searcher.httpResponseAsText(searcher.httpGetRequest(uri));
		
		return htmlDoc;
	}
}
