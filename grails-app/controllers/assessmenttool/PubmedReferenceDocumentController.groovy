package assessmenttool


import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PubmedReferenceDocumentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", show:['POST', 'GET']]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond PubmedReferenceDocument.list(params), model: [pubmedReferenceDocumentInstanceCount: PubmedReferenceDocument.count()]
    }

    def show(PubmedReferenceDocument pubmedReferenceDocumentInstance) {
        def results = PubmedResultDocument.findAllByRefDoc(pubmedReferenceDocumentInstance)

        if(!params.setRelevanceMode)
            params.setRelevanceMode='97'
        if(!params.setRelevance2Mode)
            params.setRelevance2Mode='97'
        if(!params.setAnimalTestMode)
            params.setAnimalTestMode='97'

        results.sort{it.id}

        respond pubmedReferenceDocumentInstance, model: [results: results]
    }

    def create() {
        respond new PubmedReferenceDocument(params)
    }

    @Transactional
    def save(PubmedReferenceDocument pubmedReferenceDocumentInstance) {
        if (pubmedReferenceDocumentInstance == null) {
            notFound()
            return
        }

        if (pubmedReferenceDocumentInstance.hasErrors()) {
            respond pubmedReferenceDocumentInstance.errors, view: 'create'
            return
        }

        pubmedReferenceDocumentInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pubmedReferenceDocument.label', default: 'PubmedReferenceDocument'), pubmedReferenceDocumentInstance.id])
                redirect pubmedReferenceDocumentInstance
            }
            '*' { respond pubmedReferenceDocumentInstance, [status: CREATED] }
        }
    }

    def edit(PubmedReferenceDocument pubmedReferenceDocumentInstance) {
        respond pubmedReferenceDocumentInstance
    }

    @Transactional
    def update(PubmedReferenceDocument pubmedReferenceDocumentInstance) {
        if (pubmedReferenceDocumentInstance == null) {
            notFound()
            return
        }

        if (pubmedReferenceDocumentInstance.hasErrors()) {
            respond pubmedReferenceDocumentInstance.errors, view: 'edit'
            return
        }

        pubmedReferenceDocumentInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'pubmedReferenceDocument.label', default: 'PubmedReferenceDocument'), pubmedReferenceDocumentInstance.id])
                redirect pubmedReferenceDocumentInstance
            }
            '*' { respond pubmedReferenceDocumentInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(PubmedReferenceDocument pubmedReferenceDocumentInstance) {

        if (pubmedReferenceDocumentInstance == null) {
            notFound()
            return
        }

        pubmedReferenceDocumentInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'pubmedReferenceDocument.label', default: 'PubmedReferenceDocument'), pubmedReferenceDocumentInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'pubmedReferenceDocument.label', default: 'PubmedReferenceDocument'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
