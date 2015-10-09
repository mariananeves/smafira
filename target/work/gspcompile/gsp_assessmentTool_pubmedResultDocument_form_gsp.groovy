import assessmenttool.PubmedResultDocument
import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_assessmentTool_pubmedResultDocument_form_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/pubmedResultDocument/_form.gsp" }
public Object run() {
Writer out = getOut()
Writer expressionOut = getExpressionOut()
registerSitemeshPreprocessMode()
printHtmlPart(0)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'pmid', 'error'))
printHtmlPart(1)
invokeTag('message','g',7,['code':("pubmedResultDocument.pmid.label"),'default':("Pmid")],-1)
printHtmlPart(2)
invokeTag('textField','g',10,['name':("pmid"),'required':(""),'value':(pubmedResultDocumentInstance?.pmid)],-1)
printHtmlPart(3)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'title', 'error'))
printHtmlPart(4)
invokeTag('message','g',16,['code':("pubmedResultDocument.title.label"),'default':("Title")],-1)
printHtmlPart(2)
invokeTag('textField','g',19,['name':("title"),'required':(""),'value':(pubmedResultDocumentInstance?.title)],-1)
printHtmlPart(3)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'refDoc', 'error'))
printHtmlPart(5)
invokeTag('message','g',25,['code':("pubmedResultDocument.refDoc.label"),'default':("Ref Doc")],-1)
printHtmlPart(2)
invokeTag('select','g',29,['id':("refDoc"),'name':("refDoc.id"),'from':(assessmenttool.PubmedReferenceDocument.list()),'optionKey':("id"),'required':(""),'value':(pubmedResultDocumentInstance?.refDoc?.id),'class':("many-to-one")],-1)
printHtmlPart(3)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'similarity', 'error'))
printHtmlPart(6)
invokeTag('message','g',35,['code':("pubmedResultDocument.similarity.label"),'default':("Similarity")],-1)
printHtmlPart(7)
invokeTag('field','g',38,['name':("similarity"),'type':("number"),'value':(pubmedResultDocumentInstance.similarity)],-1)
printHtmlPart(3)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'relevance', 'error'))
printHtmlPart(8)
invokeTag('message','g',44,['code':("pubmedResultDocument.relevance.label"),'default':("Relevance")],-1)
printHtmlPart(7)
invokeTag('field','g',47,['name':("relevance"),'type':("number"),'value':(pubmedResultDocumentInstance.relevance)],-1)
printHtmlPart(3)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'isAnimalTest', 'error'))
printHtmlPart(9)
invokeTag('message','g',53,['code':("pubmedResultDocument.isAnimalTest.label"),'default':("Is Animal Test")],-1)
printHtmlPart(7)
invokeTag('field','g',56,['name':("isAnimalTest"),'type':("number"),'value':(pubmedResultDocumentInstance?.isAnimalTest)],-1)
printHtmlPart(3)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'rank', 'error'))
printHtmlPart(10)
invokeTag('message','g',62,['code':("pubmedResultDocument.rank.label"),'default':("Rank")],-1)
printHtmlPart(2)
invokeTag('field','g',65,['name':("rank"),'type':("number"),'value':(pubmedResultDocumentInstance.rank),'required':("")],-1)
printHtmlPart(3)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'lastChange', 'error'))
printHtmlPart(11)
invokeTag('message','g',71,['code':("pubmedResultDocument.lastChange.label"),'default':("Last Change")],-1)
printHtmlPart(2)
invokeTag('formatDate','g',74,['name':("lastChange"),'date':(pubmedResultDocumentInstance?.lastChange)],-1)
printHtmlPart(12)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'pmcid', 'error'))
printHtmlPart(13)
invokeTag('message','g',79,['code':("pubmedResultDocument.pmcid.label"),'default':("PMCID")],-1)
printHtmlPart(14)
invokeTag('textField','g',81,['name':("pmcid"),'value':(pubmedResultDocumentInstance?.pmcid)],-1)
printHtmlPart(12)
expressionOut.print(hasErrors(bean: pubmedResultDocumentInstance, field: 'docAbstract', 'error'))
printHtmlPart(15)
invokeTag('message','g',86,['code':("pubmedResultDocument.docAbstract.label"),'default':("Abstract")],-1)
printHtmlPart(14)
invokeTag('textField','g',88,['name':("docAbstract"),'value':(pubmedResultDocumentInstance?.docAbstract)],-1)
printHtmlPart(16)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1432058048179L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'html'
public static final String TAGLIB_CODEC = 'none'
}
