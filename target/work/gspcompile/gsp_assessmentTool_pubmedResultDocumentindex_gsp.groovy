import assessmenttool.PubmedResultDocument
import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_assessmentTool_pubmedResultDocumentindex_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/pubmedResultDocument/index.gsp" }
public Object run() {
Writer out = getOut()
Writer expressionOut = getExpressionOut()
registerSitemeshPreprocessMode()
printHtmlPart(0)
printHtmlPart(1)
createTagBody(1, {->
printHtmlPart(2)
invokeTag('captureMeta','sitemesh',6,['gsp_sm_xmlClosingForEmptyTag':(""),'name':("layout"),'content':("main")],-1)
printHtmlPart(2)
invokeTag('set','g',7,['var':("entityName"),'value':(message(code: 'pubmedResultDocument.label', default: 'PubmedResultDocument'))],-1)
printHtmlPart(2)
createTagBody(2, {->
createTagBody(3, {->
invokeTag('message','g',8,['code':("default.list.label"),'args':([entityName])],-1)
})
invokeTag('captureTitle','sitemesh',8,[:],3)
})
invokeTag('wrapTitleTag','sitemesh',8,[:],2)
printHtmlPart(3)
})
invokeTag('captureHead','sitemesh',9,[:],1)
printHtmlPart(3)
createTagBody(1, {->
printHtmlPart(4)
invokeTag('message','g',11,['code':("default.link.skip.label"),'default':("Skip to content&hellip;")],-1)
printHtmlPart(5)
expressionOut.print(createLink(uri: '/'))
printHtmlPart(6)
invokeTag('message','g',14,['code':("default.home.label")],-1)
printHtmlPart(7)
createTagBody(2, {->
invokeTag('message','g',15,['code':("default.new.label"),'args':([entityName])],-1)
})
invokeTag('link','g',15,['class':("create"),'action':("create")],2)
printHtmlPart(8)
invokeTag('message','g',19,['code':("default.list.label"),'args':([entityName])],-1)
printHtmlPart(9)
if(true && (flash.message)) {
printHtmlPart(10)
expressionOut.print(flash.message)
printHtmlPart(11)
}
printHtmlPart(12)
invokeTag('sortableColumn','g',27,['property':("pmid"),'title':(message(code: 'pubmedResultDocument.pmid.label', default: 'Pmid'))],-1)
printHtmlPart(13)
invokeTag('sortableColumn','g',29,['property':("title"),'title':(message(code: 'pubmedResultDocument.title.label', default: 'Title'))],-1)
printHtmlPart(14)
invokeTag('message','g',31,['code':("pubmedResultDocument.refDoc.label"),'default':("Ref Doc")],-1)
printHtmlPart(15)
invokeTag('sortableColumn','g',33,['property':("similarity"),'title':(message(code: 'pubmedResultDocument.similarity.label', default: 'Similarity'))],-1)
printHtmlPart(16)
invokeTag('sortableColumn','g',35,['property':("relevance"),'title':(message(code: 'pubmedResultDocument.relevance.label', default: 'Relevance'))],-1)
printHtmlPart(13)
invokeTag('sortableColumn','g',37,['property':("isAnimalTest"),'title':(message(code: 'pubmedResultDocument.isAnimalTest.label', default: 'Is Animal Test'))],-1)
printHtmlPart(13)
invokeTag('sortableColumn','g',39,['property':("rank"),'title':(message(code: 'pubmedResultDocument.rank.label', default: 'Rank'))],-1)
printHtmlPart(17)
loop:{
int i = 0
for( pubmedResultDocumentInstance in (pubmedResultDocumentInstanceList) ) {
printHtmlPart(18)
expressionOut.print((i % 2) == 0 ? 'even' : 'odd')
printHtmlPart(19)
createTagBody(3, {->
expressionOut.print(fieldValue(bean: pubmedResultDocumentInstance, field: "pmid"))
})
invokeTag('link','g',47,['action':("show"),'id':(pubmedResultDocumentInstance.id)],3)
printHtmlPart(20)
expressionOut.print(fieldValue(bean: pubmedResultDocumentInstance, field: "title"))
printHtmlPart(20)
expressionOut.print(fieldValue(bean: pubmedResultDocumentInstance, field: "refDoc"))
printHtmlPart(21)
expressionOut.print(fieldValue(bean: pubmedResultDocumentInstance, field: "similarity"))
printHtmlPart(20)
expressionOut.print(fieldValue(bean: pubmedResultDocumentInstance, field: "relevance"))
printHtmlPart(20)
expressionOut.print(fieldValue(bean: pubmedResultDocumentInstance, field: "isAnimalTest"))
printHtmlPart(20)
expressionOut.print(fieldValue(bean: pubmedResultDocumentInstance, field: "rank"))
printHtmlPart(22)
i++
}
}
printHtmlPart(23)
invokeTag('paginate','g',66,['total':(pubmedResultDocumentInstanceCount ?: 0)],-1)
printHtmlPart(24)
})
invokeTag('captureBody','sitemesh',69,[:],1)
printHtmlPart(25)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1432058144649L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'html'
public static final String TAGLIB_CODEC = 'none'
}
