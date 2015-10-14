package properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


/*
 * Handles all keys of properties files
 */
public class PropertiesHandler {
	
	ResourceBundle resource;
	public enum PropertiesHanderMode{ENTREZ, LUCENE, RELATEDCITATION};
	public enum PropertiesIDs{ENTREZ_SCHEME, ENTREZ_HOST, ENTREZ_PATH, ENTREZ_MODE,
		ENTREZ_MODE_ESEARCH, ENTREZ_MODE_EPOST, ENTREZ_MODE_ESUMMARY, ENTREZ_MODE_EFETCH, ENTREZ_MODE_ELINK,
		PARAMETER_ESEARCH, PARAMETER_EFETCH, PARAMETER_RELATEDSEARCH_EPOST, PARAMETER_RELATEDSEARCH_ELINK,
		ESEARCH_DB, ESEARCH_RETMAX, ESEARCH_USEHISTORY, ESEARCH_TERM,
		EPOST_DB, EPOST_ID,
		EFETCH_RETTYPE, EFETCH_RETMODE, EFETCH_DB, EFETCH_RETMAX,
		ESUMMARY_DB, ESUMMARY_RETMAX,
		RELATED_ID,
		RELATED_EPOST_DB,
		RELATED_ELINK_DBFROM, RELATED_ELINK_DB, RELATED_ELINK_LINKNAME, RELATED_ELINK_CMD,
		PROJECT_PATH, RESULTS_ESUMMARIES_PATH,
		DATA_PMRELATEDSEARCH_TARGET_PATH,
		DATA_ARFF_PATH, DATA_RESULT_PATH,
		RELATED_CITATION_SOURCEDIR_PATH,
		RELATED_CITATION_PROPERTY_SOURCE_PATH, RELATED_CITATION_NAME_SUM, RELATED_CITATION_NAME_FET, RELATED_CITATION_REFID,
		REFERENCES_DBFROM, REFERENCES_DB, REFERENCES_LINKNAME, REFERENCES_CMD
		};
		
	public enum NonValuePropertyNames{
		WEBENV, QUERY_KEY, ID;	
		
		public String getEntrezName(){
			switch(this){
			case WEBENV: return "WebEnv";
			case QUERY_KEY: return "query_key";
			case ID: return "from_uid";
			default: return "";
			}
		}
	};
	
	private String praefix;
	private final String entrez_praefix = "ncbi.entrez";
	private final String lucene_praefix = "lucene";
	private final String relatedCitations_praefix = "relatedCitation";
	
	
	
	public PropertiesHandler(PropertiesHanderMode phMode){
		setPraefix(phMode);
		resource = getResource(phMode);
	}
	
	private void setPraefix(PropertiesHanderMode phMode){
		switch(phMode){
		case ENTREZ: this.praefix = entrez_praefix; break;
		case LUCENE: this.praefix = lucene_praefix; break;
		case RELATEDCITATION: this.praefix = relatedCitations_praefix; break;
		default: setPraefix(PropertiesHanderMode.ENTREZ);
		}
	}
	
	private ResourceBundle getResource(PropertiesHanderMode phMode){
		switch(phMode){
		case ENTREZ: return ResourceBundle.getBundle(PropertyType.SETTINGS.getPropertyFilePath());
		case LUCENE: return ResourceBundle.getBundle(PropertyType.LUCENE.getPropertyFilePath());
		case RELATEDCITATION: return ResourceBundle.getBundle(PropertyType.RELATEDCITATION.getPropertyFilePath());
		default: return ResourceBundle.getBundle(PropertyType.SETTINGS.getPropertyFilePath());
		}
	}
	
	
	public String getPropertyKey(PropertiesIDs propertyID){
		switch(propertyID){
		case ENTREZ_SCHEME: return entrez_praefix + ".scheme";
		case ENTREZ_HOST: return entrez_praefix + ".host";
		case ENTREZ_PATH: return entrez_praefix + ".path";
		case ENTREZ_MODE: return entrez_praefix + ".mode";
		
		case ENTREZ_MODE_ESEARCH: return entrez_praefix + ".mode.1.esearch";
		case ENTREZ_MODE_EPOST: return entrez_praefix + ".mode.2.epost";
		case ENTREZ_MODE_ESUMMARY: return entrez_praefix + ".mode.3.esummary";
		case ENTREZ_MODE_EFETCH: return entrez_praefix + ".mode.4.efetch";
		case ENTREZ_MODE_ELINK: return entrez_praefix + ".mode.5.elink";
		
		case PARAMETER_ESEARCH: return entrez_praefix + ".esearch.parameter";
		case PARAMETER_EFETCH: return entrez_praefix + ".efetch.parameter";
		case PARAMETER_RELATEDSEARCH_EPOST: return entrez_praefix + ".relatedSearch.epost.parameter";
		case PARAMETER_RELATEDSEARCH_ELINK: return entrez_praefix + ".relatedSearch.elink.parameter";
		
		case ESEARCH_DB: return entrez_praefix + ".esearch.parameter.db";
		case ESEARCH_RETMAX: return entrez_praefix + ".esearch.parameter.retMax";
		case ESEARCH_USEHISTORY: return entrez_praefix + ".esearch.parameter.usehistory";
		case ESEARCH_TERM: return entrez_praefix + ".esearch.query.term";
		
		case EPOST_DB: return entrez_praefix + ".epost.parameter.db";
		case EPOST_ID: return entrez_praefix + ".epost.parameter.id";
		
		case EFETCH_RETMODE: return entrez_praefix + ".efetch.parameter.retmode";
		case EFETCH_RETTYPE: return entrez_praefix + ".efetch.parameter.rettype";
		case EFETCH_DB: return entrez_praefix + ".efetch.parameter.db";
		case EFETCH_RETMAX: return entrez_praefix + ".efetch.parameter.retMax";
		
		case ESUMMARY_DB: return entrez_praefix + ".esummary.parameter.db";
		case ESUMMARY_RETMAX: return entrez_praefix + ".esummary.parameter.retMax";
		

		case RELATED_ID: return entrez_praefix + ".relatedSearch.parameter.id";
		
		case RELATED_EPOST_DB: return entrez_praefix + ".relatedSearch.epost.parameter.db";
		
		case RELATED_ELINK_DBFROM: return entrez_praefix + ".relatedSearch.elink.parameter.dbfrom";
		case RELATED_ELINK_DB: return entrez_praefix + ".relatedSearch.elink.parameter.db";
		case RELATED_ELINK_LINKNAME: return entrez_praefix + ".relatedSearch.elink.parameter.linkname";
		case RELATED_ELINK_CMD: return entrez_praefix + ".relatedSearch.elink.parameter.cmd";
		
		case PROJECT_PATH: return "project.path";
		case RESULTS_ESUMMARIES_PATH: return "results.esummaries.path";
		
		case DATA_PMRELATEDSEARCH_TARGET_PATH: return "data.pmDocuments.relatedSearch.path";
		case DATA_ARFF_PATH: return "data.pmDocuments.arff.path";
		case DATA_RESULT_PATH: return "data.pmDocuments.results.path";
		
		case RELATED_CITATION_SOURCEDIR_PATH: return relatedCitations_praefix + ".sourceDocs.sourceDir.path";
		case RELATED_CITATION_PROPERTY_SOURCE_PATH: return relatedCitations_praefix + ".sourceDocs";
		case RELATED_CITATION_NAME_SUM: return ".name.summary";
		case RELATED_CITATION_NAME_FET: return ".name.fetch";
		case RELATED_CITATION_REFID: return ".refDocID";
		
		case REFERENCES_DBFROM: return entrez_praefix + ".references.elink.parameter.dbfrom";
		case REFERENCES_DB: return entrez_praefix + ".references.elink.parameter.db";
		case REFERENCES_LINKNAME: return entrez_praefix + ".references.elink.parameter.linkname";
		case REFERENCES_CMD: return entrez_praefix + ".references.elink.parameter.cmd";
		
		default: return null;
		}
	}
	
	
	public String getPropertyEntrezName(PropertiesIDs propertyID){
		String propertyPath = getPropertyKey(propertyID);
		
		return getPropertyEntrezNameByKey(propertyPath);
	}
		
	public String getPropertyEntrezNameByKey(String propertyKey){
		return propertyKey.substring(propertyKey.lastIndexOf(".")+1).trim();
	}
	
	
	public NameValuePair getSinglePropertyNameValuePair(String keyword, ResourceBundle propertyFile){
		ArrayList<NameValuePair> pairs = getPropertyNameValuePairs(keyword, propertyFile);
		
		if(!pairs.isEmpty())
			return pairs.get(0);
		else
			return null;
	}
	
	public ArrayList<NameValuePair> getPropertyNameValuePairs(String keyword, ResourceBundle propertyFile){
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String name, value;
		
		
		Set<String> keys = propertyFile.keySet();
		
		for(String key: keys){
			if(key.contains(keyword)){
				name = getPropertyEntrezNameByKey(key);
				value = propertyFile.getString(key);
				
				pairs.add(new BasicNameValuePair(name, value));
			}
		}
		
		return pairs;
	}
	
	public String getPropertyValue(PropertiesIDs propertyID){
		return resource.getString(getPropertyKey(propertyID));
	}
	
	public String getPropertyValue(String propertyKey){
		return resource.getString(propertyKey);
	}
	
	
	public boolean isESearch(PropertiesIDs id){
		String propertyKey = getPropertyKey(id);
		
		return propertyKey.contains("esearch");
	}
	
	public Set<String> getKeyList(String keyPraefix){		
		Set<String> keys = resource.keySet();
		
		Iterator<String> iter = keys.iterator();
		
		String key;
		while(iter.hasNext()){
			key = iter.next();
			
			if(!key.startsWith(keyPraefix))
				iter.remove();
		}
		
		return keys;
	}
	
}
