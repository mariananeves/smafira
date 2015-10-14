package pubmedSearch;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.http.NameValuePair;

import properties.PropertiesHandler;
import properties.PropertiesHandler.PropertiesIDs;
import properties.PropertyType;
import properties.PropertiesHandler.PropertiesHanderMode;


public enum EntrezSearchType {
	ESEARCH, EPOST, ESUMMARY, EFETCH, RELATEDCITATION;
	
	
	public String getTypeURIAsString(){
		PropertiesHandler propertiesHandler = new PropertiesHandler(PropertiesHanderMode.ENTREZ);
		
		ResourceBundle settings = ResourceBundle.getBundle(PropertyType.LUCENE.getPropertyFilePath());		
		
		
		String keyword = propertiesHandler.getPropertyKey(PropertiesIDs.ENTREZ_MODE);
		ArrayList<NameValuePair> pairs = propertiesHandler.getPropertyNameValuePairs(keyword, settings);
		
		for(NameValuePair pair: pairs){
			if(pair.getName().toLowerCase().contains(this.toString().toLowerCase())){
				return pair.getValue();
			}
		}
		
		return "";
	}
	
	public boolean isESearch(){
		return this.equals(ESEARCH);
	}
	
	public boolean isEPost(){
		return this.equals(EPOST);
	}
	
	public boolean isESummary(){
		return this.equals(ESUMMARY);
	}
	
	public boolean isEFetch(){
		return this.equals(EFETCH);
	}
	
	public boolean isRelatedCitation(){
		return this.equals(RELATEDCITATION);
	}
}
