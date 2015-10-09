package assessmenttool



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PubmedResultDocumentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        if (!params.sort) {
            params.sort = "rank"
            params.order = "asc"
        }

        respond PubmedResultDocument.list(params), model:[pubmedResultDocumentInstanceCount: PubmedResultDocument.count()]
    }


    def show(PubmedResultDocument pubmedResultDocumentInstance) {
        respond pubmedResultDocumentInstance
    }

    def create() {
        respond new PubmedResultDocument(params)
    }

    @Transactional
    def save(PubmedResultDocument pubmedResultDocumentInstance) {
        if (pubmedResultDocumentInstance == null) {
            notFound()
            return
        }

        if (pubmedResultDocumentInstance.hasErrors()) {
            respond pubmedResultDocumentInstance.errors, view:'create'
            return
        }

        pubmedResultDocumentInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pubmedResultDocument.label', default: 'PubmedResultDocument'), pubmedResultDocumentInstance.id])
                redirect pubmedResultDocumentInstance
            }
            '*' { respond pubmedResultDocumentInstance, [status: CREATED] }
        }
    }

    def edit(PubmedResultDocument pubmedResultDocumentInstance) {
        respond pubmedResultDocumentInstance
    }

    @Transactional
    def update(PubmedResultDocument pubmedResultDocumentInstance) {
        if (pubmedResultDocumentInstance == null) {
            notFound()
            return
        }

        if (pubmedResultDocumentInstance.hasErrors()) {
            respond pubmedResultDocumentInstance.errors, view:'edit'
            return
        }


        pubmedResultDocumentInstance.lastChange=new Date()
        pubmedResultDocumentInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'pubmedResultDocument.label', default: 'PubmedResultDocument'), pubmedResultDocumentInstance.id])
                redirect (controller: "pubmedReferenceDocument", action: "show", id: pubmedResultDocumentInstance.refDocId, params:['setSimilarityMode':params.setSimilarityMode, 'setRelevanceMode':params.setRelevanceMode, 'setAnimalTestMode': params.setAnimalTestMode])
//                redirect pubmedResultDocumentInstance
            }
            '*'{ respond pubmedResultDocumentInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(PubmedResultDocument pubmedResultDocumentInstance) {

        if (pubmedResultDocumentInstance == null) {
            notFound()
            return
        }

        pubmedResultDocumentInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'pubmedResultDocument.label', default: 'PubmedResultDocument'), pubmedResultDocumentInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'pubmedResultDocument.label', default: 'PubmedResultDocument'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
