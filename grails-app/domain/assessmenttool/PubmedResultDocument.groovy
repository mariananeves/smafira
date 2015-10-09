package assessmenttool

class PubmedResultDocument {

    String pmid
    String title
    String pmcid
    String docAbstract
    String addInfo
    PubmedReferenceDocument refDoc
    Integer similarity
    Integer relevance
    Integer isAnimalTest
    Integer rank
    Date lastChange = new Date()



    static constraints = {
        pmid blank: false, nullable: false
        title blank: false, nullable: false
        refDoc blank: false, nullable: false
        similarity nullable: true
        relevance nullable: true
        isAnimalTest nullable: true
        rank nullable: false
        lastChange nullable: false
        pmcid blank: true, nullable: true
        docAbstract blank: true, nullable: true, maxSize: 10000
        addInfo blank: true, nullable: true, maxSize: 10000
    }
}
