import assessmenttool.PubmedReferenceDocument
import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_assessmentTool_pubmedReferenceDocument_form_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/pubmedReferenceDocument/_form.gsp" }
public Object run() {
Writer out = getOut()
Writer expressionOut = getExpressionOut()
registerSitemeshPreprocessMode()
printHtmlPart(0)
expressionOut.print(hasErrors(bean: pubmedReferenceDocumentInstance, field: 'pmid', 'error'))
printHtmlPart(1)
invokeTag('message','g',7,['code':("pubmedReferenceDocument.pmid.label"),'default':("Pmid")],-1)
printHtmlPart(2)
invokeTag('textField','g',10,['name':("pmid"),'required':(""),'value':(pubmedReferenceDocumentInstance?.pmid)],-1)
printHtmlPart(3)
expressionOut.print(hasErrors(bean: pubmedReferenceDocumentInstance, field: 'title', 'error'))
printHtmlPart(4)
invokeTag('message','g',16,['code':("pubmedReferenceDocument.title.label"),'default':("Title")],-1)
printHtmlPart(2)
invokeTag('textField','g',19,['name':("title"),'required':(""),'value':(pubmedReferenceDocumentInstance?.title)],-1)
printHtmlPart(5)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1423559886708L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'html'
public static final String TAGLIB_CODEC = 'none'
}
