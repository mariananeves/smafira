package pubmedSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import properties.PropertiesHandler;

import tools.DOMHandlerUtil;


//TODO: Text -> XML-Doc (evtl. wenn sinnvoll, vorher mal ausgeben lassen)
public class HttpSearcher {
	CloseableHttpClient httpClient;
	HttpGet httpGet;
	
	
	
	public HttpSearcher(){
		httpClient = HttpClients.createDefault();		
	}
	
	
	

	
	
//	##### http communication #####
	
	public HttpResponse httpGetRequest(URI searchUri){
		HttpResponse response = null;
		try {
			HttpGet request = new HttpGet(searchUri);
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	
	public HttpResponse httpPostRequest(URI searchUri){
		HttpResponse response = null;
		try {
			HttpPost request = new HttpPost(searchUri);
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
		
	
	public Document httpResponseToXMLDocument(HttpResponse response){
		return DOMHandlerUtil.httpResponseToXMLDocument(response);
	}
	
	
	public String httpResponseAsText(HttpResponse response){
		String responseAsText = "";
		try {
			InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
			BufferedReader br = new BufferedReader(isr);
			String line;
			while((line = br.readLine()) != null){
				responseAsText += line + "\n";
//				System.out.println(line);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return responseAsText;
	}
	
	
//	##########
	
	
	
	public static void main(String[] args){
		HttpSearcher testSearcher = new HttpSearcher();
		NcbiEntrezURIBuilder uriBuilder = new NcbiEntrezURIBuilder();
		try {
			URI uri = uriBuilder.buildSearchNCBIEntrezURI(null, null);
			testSearcher.httpResponseAsText(testSearcher.httpGetRequest(uri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	
}
