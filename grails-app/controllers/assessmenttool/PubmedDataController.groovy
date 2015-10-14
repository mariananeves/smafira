package assessmenttool

import pubmedSearch.MainSearcher

class PubmedDataController {

    def index() {}

    def getPubmedData(PubmedReferenceDocument pubmedReferenceDocumentInstance){
        respond pubmedReferenceDocumentInstance
    }

    def loadData(PubmedReferenceDocument pubmedReferenceDocumentInstance){
        MainSearcher searcher = new MainSearcher()
        System.out.println("null: " + (pubmedReferenceDocumentInstance == null))
        System.out.println("pmid: " + pubmedReferenceDocumentInstance.pmid)
        searcher.findRelatedCitations(pubmedReferenceDocumentInstance.pmid)
        searcher.writeRelatedCitationsContentToXML(pubmedReferenceDocumentInstance.pmid)
        searcher.writeResultDocsToDB(pubmedReferenceDocumentInstance.pmid)

        respond pubmedReferenceDocumentInstance
    }
}
