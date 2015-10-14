package pubmedSearch;

import dataCollection.PubMedDataCollector;
import db.AssessmentDBWriter;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.w3c.dom.Document;
import properties.PropertiesHandler;
import properties.PropertyType;
import tools.DOMHandlerUtil;
import tools.ToolsUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by du on 13.10.2015.
 */
public class MainSearcher {
    ResourceBundle settings;
    String eSummaryPath;
    String relatedSearchPath;

    public MainSearcher(){
        settings = ResourceBundle.getBundle(PropertyType.SETTINGS.getPropertyFilePath());
        eSummaryPath = settings.getString("project.path") + settings.getString("results.esummaries.path");
        relatedSearchPath = settings.getString("project.path") + settings.getString("data.pmDocuments.relatedSearch.path");
    }

    public void findRelatedCitations(String pubmedUid){
        NcbiEntrezURIBuilder uriBuilder = new NcbiEntrezURIBuilder();
        URI uri;
        PubMedDataHandler dataHandler = new PubMedDataHandler();
        HttpSearcher searcher = new HttpSearcher();
        HttpResponse searchResponse;
        Document searchResults;
        NameValuePair[] pubMedResultKeys;
        String webEnv = "", queryKey = "";
        String abstracts;

        try {
//				build pubmed search uri
            uri = uriBuilder.buildElinkURI(pubmedUid);
            System.out.println("uri: " + uri.getPath());
//				search
            searchResponse = searcher.httpGetRequest(uri);
//				get search results as xml doc
//			searchResults = searcher.httpResponseToXMLDocument(searchResponse);
//			searcher.httpResponseAsText(searchResponse);

            searchResults = searcher.httpResponseToXMLDocument(searchResponse);

            webEnv = dataHandler.getWebEnvFromElinkResponse(searchResults).getValue();
            queryKey = dataHandler.getQueryKeyFromElinkResponse(searchResults).getValue();

//			ESummary
            uri = uriBuilder.buildESummaryURI(webEnv, queryKey);
            searchResponse = searcher.httpGetRequest(uri);
            searchResults = searcher.httpResponseToXMLDocument(searchResponse);
//			searcher.httpResponseAsText(searchResponse);

            DOMHandlerUtil.writeXMLFile(searchResults, eSummaryPath + pubmedUid + "_summary.xml");

//			getAbstracts (EFetch)
            uri = uriBuilder.buildEFetchURI(webEnv, queryKey);
            searchResponse = searcher.httpGetRequest(uri);
            abstracts = searcher.httpResponseAsText(searchResponse);
            ToolsUtil.saveText(eSummaryPath + pubmedUid + "_fetch.xml", abstracts);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public void writeRelatedCitationsContentToXML(String pubmedUid){
        PubMedDataCollector collector = new PubMedDataCollector();

        collector.writeRelatedCitationsContentToXML(relatedSearchPath, pubmedUid, (eSummaryPath + pubmedUid + "_summary.xml"), (eSummaryPath + pubmedUid + "_fetch.xml"));
    }

    public void writeResultDocsToDB(String pubmedUid){
        AssessmentDBWriter writer = new AssessmentDBWriter();
        writer.writeNewResultDocumentsToDB(relatedSearchPath + pubmedUid + ".xml", pubmedUid);
    }

}
