package assessmenttool

class PubmedDataController {

    def index() {}

    def getPubmedData(PubmedReferenceDocument pubmedReferenceDocumentInstance){
        respond pubmedReferenceDocumentInstance
    }
}
