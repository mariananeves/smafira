package pubmedSearch;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import properties.PropertiesHandler;
import properties.PropertiesHandler.NonValuePropertyNames;
import properties.PropertiesHandler.PropertiesHanderMode;
import properties.PropertiesHandler.PropertiesIDs;
import properties.PropertyType;


public class NcbiEntrezURIBuilder {
	
	private ResourceBundle settings;
	private PropertiesHandler propertiesHandler;
	
	public NcbiEntrezURIBuilder(){
		settings = ResourceBundle.getBundle(PropertyType.SETTINGS.getPropertyFilePath());
		propertiesHandler = new PropertiesHandler(PropertiesHanderMode.ENTREZ);
	}
	
	
//	## esearch ##
	
	public URI buildSearchNCBIEntrezURI(String query, ArrayList<NameValuePair> parameters) throws URISyntaxException{
				
		if(parameters == null || parameters.isEmpty()){
			parameters = new ArrayList<NameValuePair>();
			
			parameters.add(getNameValuePair(PropertiesIDs.ESEARCH_DB));
			parameters.add(getNameValuePair(PropertiesIDs.ESEARCH_RETMAX));
			parameters.add(getNameValuePair(PropertiesIDs.ESEARCH_USEHISTORY));
		}
		
		if(query == null || query.isEmpty()){
			parameters.add(getNameValuePair(PropertiesIDs.ESEARCH_TERM));
		}
		
		return buildNCBIEntrezURI(query, parameters, PropertiesIDs.ENTREZ_MODE_ESEARCH);
	}
	
//	## epost ##
	
	public URI buildPubMedIDPostURI(ArrayList<String> pubMedIDList) throws URISyntaxException{
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		String postIDList = getIDListForEPost(pubMedIDList);
		
//		parameters.add(getNameValuePair(PropertiesIDs.EPOST_ID));
		parameters.add(getNameValuePair(PropertiesIDs.EPOST_DB));
		parameters.add(new BasicNameValuePair("id", postIDList));
		
		return buildNCBIEntrezURI(null, parameters, PropertiesIDs.ENTREZ_MODE_EPOST);
	}
	
	private String getIDListForEPost(ArrayList<String> pubMedIDList){
		String postList = "";
		
		for(String id: pubMedIDList){
			postList += id + ",";
		}
		
		postList = postList.substring(0, postList.length()-1).trim();
		
		return postList;
	}
	
	
//	## efetch ##
	
	public URI buildEFetchURI(String webEnv, String queryKey) throws URISyntaxException{
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		parameters.add(getNameValuePair(PropertiesIDs.EFETCH_RETTYPE));
		parameters.add(getNameValuePair(PropertiesIDs.EFETCH_RETMODE));
		parameters.add(getNameValuePair(PropertiesIDs.EFETCH_DB));
		parameters.add(getNameValuePair(PropertiesIDs.EFETCH_RETMAX));
		parameters.add(getNameValuePair(NonValuePropertyNames.WEBENV, webEnv));
		parameters.add(getNameValuePair(NonValuePropertyNames.QUERY_KEY, queryKey));
		
		return buildNCBIEntrezURI(null, parameters, PropertiesIDs.ENTREZ_MODE_EFETCH);		
	}
	
//	## esummary ##
	
	public URI buildESummaryURI(String webEnv, String queryKey) throws URISyntaxException{
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		parameters.add(getNameValuePair(PropertiesIDs.ESUMMARY_DB));
		parameters.add(getNameValuePair(PropertiesIDs.ESUMMARY_RETMAX));
		parameters.add(getNameValuePair(NonValuePropertyNames.WEBENV, webEnv));
		parameters.add(getNameValuePair(NonValuePropertyNames.QUERY_KEY, queryKey));
		
		return buildNCBIEntrezURI(null, parameters, PropertiesIDs.ENTREZ_MODE_ESUMMARY);		
	}
	
	
//	## relatedCitation ##
//	public URI buildRelatedCitationURI(String pubmedUid) throws URISyntaxException{
//		ArrayList<NameValuePair> parameters = getELinkParameters(pubmedUid, "pubmed", "pubmed");
//		return buildNCBIEntrezURI(null, parameters, EntrezSearchType.RELATEDCITATION);		
//	}
//	
	public URI buildRelatedCitationPostURI(String pubmedUid) throws URISyntaxException{
		ArrayList<String> idList = new ArrayList<String>();
		idList.add(pubmedUid);
		
		return buildPubMedIDPostURI(idList);
	}
	
	public URI buildElinkURI(String pubmedUid) throws URISyntaxException{
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		

		parameters.add(getNameValuePair(PropertiesIDs.RELATED_ELINK_DBFROM));
		parameters.add(getNameValuePair(PropertiesIDs.RELATED_EPOST_DB));
		parameters.add(getNameValuePair(PropertiesIDs.RELATED_ELINK_CMD));
		parameters.add(getNameValuePair(PropertiesIDs.RELATED_ELINK_LINKNAME));
		parameters.add(new BasicNameValuePair("id", pubmedUid));

		return buildNCBIEntrezURI(null, parameters, PropertiesIDs.ENTREZ_MODE_ELINK);
	}

	public URI buildReferencesURI(String pubmedUid) throws URISyntaxException{
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		parameters.add(getNameValuePair(PropertiesIDs.REFERENCES_DBFROM));
		parameters.add(getNameValuePair(PropertiesIDs.REFERENCES_DB));
		parameters.add(getNameValuePair(PropertiesIDs.REFERENCES_LINKNAME));
		parameters.add(getNameValuePair(PropertiesIDs.REFERENCES_CMD));
		parameters.add(getNameValuePair(NonValuePropertyNames.ID, pubmedUid));
		
		return buildNCBIEntrezURI(null, parameters, PropertiesIDs.ENTREZ_MODE_ELINK);
	}
	
	private URI buildNCBIEntrezURI(String query, ArrayList<NameValuePair> parameters, PropertiesIDs entrezMode) throws URISyntaxException{
	
		
//		if no query is provided, get it from the settings.properties file
		if(propertiesHandler.isESearch(entrezMode) && (query == null || query.isEmpty())){
			query = propertiesHandler.getPropertyValue(PropertiesIDs.ESEARCH_TERM);
		}
		
		
		URI newUri;
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(propertiesHandler.getPropertyValue(PropertiesIDs.ENTREZ_SCHEME));
		uriBuilder.setHost(propertiesHandler.getPropertyValue(PropertiesIDs.ENTREZ_HOST));
		uriBuilder.setPath(propertiesHandler.getPropertyValue(PropertiesIDs.ENTREZ_PATH) + propertiesHandler.getPropertyValue(entrezMode));
		uriBuilder.setParameters(parameters);
		if(propertiesHandler.isESearch(entrezMode));
			uriBuilder.setParameter("term", query);
		newUri = uriBuilder.build();
		
		System.out.println("Uri: " + newUri.toString());
		
		return newUri;
	}
	
	private ArrayList<NameValuePair> getParametersFromResource(ResourceBundle settings, String settingsPraefix){
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		String keyword = settingsPraefix + ".parameter";
		parameters = propertiesHandler.getPropertyNameValuePairs(keyword, settings);
		
		return parameters;
	}
	
	
	/*
	 * returns elements of an ArrayList as a String (with elements separated by ",")
	 */
	private String getListAsString(ArrayList<String> list){
		String listAsString = "";
		for(String value: list){
			listAsString += value + ",";
		}
		
//		remove trailing ","
		listAsString = listAsString.substring(0, listAsString.length()-1);
		
		return listAsString;
	}
	
	private NameValuePair getNameValuePair(PropertiesIDs propertyID){
		String name, value;
		
		name = propertiesHandler.getPropertyEntrezName(propertyID);
		value = settings.getString(propertiesHandler.getPropertyKey(propertyID));
		return (new BasicNameValuePair(name, value));
	}
	
	private NameValuePair getNameValuePair(NonValuePropertyNames name, String value){
		return (new BasicNameValuePair(name.getEntrezName(), value));
	}
}
