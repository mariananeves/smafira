package assessmenttool

import dataMining.WekaArffCreator
import dataMining.WekaClassifier

class ReRankingController {

    def index() {}

    def reranking(PubmedReferenceDocument pubmedReferenceDocumentInstance) {
        respond pubmedReferenceDocumentInstance
    }

    def send(PubmedReferenceDocument pubmedReferenceDocumentInstance){
        WekaArffCreator arffCreator = new WekaArffCreator()
        arffCreator.createWekaTrainingArff(pubmedReferenceDocumentInstance.pmid)

        WekaClassifier wekaClassifier = new WekaClassifier()
        wekaClassifier.getReranking(pubmedReferenceDocumentInstance.pmid, WekaClassifier.ClassifierType.SMO.getType(String.valueOf(params.setDataMiningMethod)), String.valueOf(params.setDataMiningMethod).toLowerCase())

        respond pubmedReferenceDocumentInstance
    }
}
