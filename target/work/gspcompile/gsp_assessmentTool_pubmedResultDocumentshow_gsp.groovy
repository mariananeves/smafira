import assessmenttool.PubmedResultDocument
import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_assessmentTool_pubmedResultDocumentshow_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/pubmedResultDocument/show.gsp" }
public Object run() {
Writer out = getOut()
Writer expressionOut = getExpressionOut()
registerSitemeshPreprocessMode()
printHtmlPart(0)
createTagBody(1, {->
printHtmlPart(1)
invokeTag('captureMeta','sitemesh',5,['gsp_sm_xmlClosingForEmptyTag':(""),'name':("layout"),'content':("main")],-1)
printHtmlPart(1)
invokeTag('set','g',6,['var':("entityName"),'value':(message(code: 'pubmedResultDocument.label', default: 'PubmedResultDocument'))],-1)
printHtmlPart(1)
createTagBody(2, {->
createTagBody(3, {->
invokeTag('message','g',7,['code':("default.show.label"),'args':([entityName])],-1)
})
invokeTag('captureTitle','sitemesh',7,[:],3)
})
invokeTag('wrapTitleTag','sitemesh',7,[:],2)
printHtmlPart(2)
})
invokeTag('captureHead','sitemesh',8,[:],1)
printHtmlPart(3)
createTagBody(1, {->
printHtmlPart(4)
invokeTag('message','g',12,['code':("default.link.skip.label"),'default':("Skip to content&hellip;")],-1)
printHtmlPart(5)
expressionOut.print(createLink(uri: '/'))
printHtmlPart(6)
invokeTag('message','g',16,['code':("default.home.label")],-1)
printHtmlPart(7)
createTagBody(2, {->
invokeTag('message','g',17,['code':("default.list.label"),'args':([entityName])],-1)
})
invokeTag('link','g',17,['class':("list"),'action':("index")],2)
printHtmlPart(8)
createTagBody(2, {->
invokeTag('message','g',19,['code':("default.new.label"),'args':([entityName])],-1)
})
invokeTag('link','g',19,['class':("create"),'action':("create")],2)
printHtmlPart(9)
invokeTag('message','g',24,['code':("default.show.label"),'args':([entityName])],-1)
printHtmlPart(10)
if(true && (flash.message)) {
printHtmlPart(11)
expressionOut.print(flash.message)
printHtmlPart(12)
}
printHtmlPart(13)
if(true && (pubmedResultDocumentInstance?.pmid)) {
printHtmlPart(14)
invokeTag('message','g',33,['code':("pubmedResultDocument.pmid.label"),'default':("Pmid")],-1)
printHtmlPart(15)
invokeTag('fieldValue','g',36,['bean':(pubmedResultDocumentInstance),'field':("pmid")],-1)
printHtmlPart(16)
}
printHtmlPart(17)
if(true && (pubmedResultDocumentInstance?.title)) {
printHtmlPart(18)
invokeTag('message','g',44,['code':("pubmedResultDocument.title.label"),'default':("Title")],-1)
printHtmlPart(19)
invokeTag('fieldValue','g',47,['bean':(pubmedResultDocumentInstance),'field':("title")],-1)
printHtmlPart(16)
}
printHtmlPart(17)
if(true && (pubmedResultDocumentInstance?.refDoc)) {
printHtmlPart(20)
invokeTag('message','g',55,['code':("pubmedResultDocument.refDoc.label"),'default':("Ref Doc")],-1)
printHtmlPart(21)
createTagBody(3, {->
expressionOut.print(pubmedResultDocumentInstance?.refDoc?.encodeAsHTML())
})
invokeTag('link','g',59,['controller':("pubmedReferenceDocument"),'action':("show"),'id':(pubmedResultDocumentInstance?.refDoc?.id)],3)
printHtmlPart(16)
}
printHtmlPart(22)
invokeTag('message','g',67,['code':("pubmedResultDocument.relevance.label"),'default':("Relevance")],-1)
printHtmlPart(23)
invokeTag('fieldValue','g',70,['bean':(pubmedResultDocumentInstance),'field':("relevance")],-1)
printHtmlPart(24)
invokeTag('message','g',76,['code':("pubmedResultDocument.relevance2.label"),'default':("Relevance2")],-1)
printHtmlPart(25)
invokeTag('fieldValue','g',79,['bean':(pubmedResultDocumentInstance),'field':("relevance2")],-1)
printHtmlPart(26)
invokeTag('message','g',87,['code':("pubmedResultDocument.isAnimalTest.label"),'default':("Is Animal Test")],-1)
printHtmlPart(27)
invokeTag('fieldValue','g',90,['bean':(pubmedResultDocumentInstance),'field':("isAnimalTest")],-1)
printHtmlPart(28)
if(true && (pubmedResultDocumentInstance?.rank)) {
printHtmlPart(29)
invokeTag('message','g',98,['code':("pubmedResultDocument.rank.label"),'default':("Rank")],-1)
printHtmlPart(30)
invokeTag('fieldValue','g',101,['bean':(pubmedResultDocumentInstance),'field':("rank")],-1)
printHtmlPart(16)
}
printHtmlPart(17)
if(true && (pubmedResultDocumentInstance?.pmcid)) {
printHtmlPart(31)
invokeTag('message','g',109,['code':("pubmedResultDocument.pmcid.label"),'default':("PMCID")],-1)
printHtmlPart(19)
invokeTag('fieldValue','g',112,['bean':(pubmedResultDocumentInstance),'field':("pmcid")],-1)
printHtmlPart(16)
}
printHtmlPart(17)
if(true && (pubmedResultDocumentInstance?.docAbstract)) {
printHtmlPart(32)
invokeTag('message','g',120,['code':("pubmedResultDocument.docAbstract.label"),'default':("Abstract")],-1)
printHtmlPart(33)
invokeTag('fieldValue','g',123,['bean':(pubmedResultDocumentInstance),'field':("docAbstract")],-1)
printHtmlPart(16)
}
printHtmlPart(17)
if(true && (pubmedResultDocumentInstance?.lastChange)) {
printHtmlPart(34)
invokeTag('message','g',131,['code':("pubmedResultDocument.lastChange.label"),'default':("Last Change")],-1)
printHtmlPart(35)
invokeTag('formatDate','g',134,['date':(pubmedResultDocumentInstance?.lastChange)],-1)
printHtmlPart(16)
}
printHtmlPart(36)
createTagBody(2, {->
printHtmlPart(37)
createTagBody(3, {->
invokeTag('message','g',143,['code':("default.button.edit.label"),'default':("Edit")],-1)
})
invokeTag('link','g',143,['class':("edit"),'action':("edit"),'resource':(pubmedResultDocumentInstance)],3)
printHtmlPart(38)
invokeTag('actionSubmit','g',146,['class':("delete"),'action':("delete"),'value':(message(code: 'default.button.delete.label', default: 'Delete')),'onclick':("return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');")],-1)
printHtmlPart(39)
})
invokeTag('form','g',148,['url':([resource: pubmedResultDocumentInstance, action: 'delete']),'method':("DELETE")],2)
printHtmlPart(40)
})
invokeTag('captureBody','sitemesh',152,[:],1)
printHtmlPart(41)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1425480139616L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'html'
public static final String TAGLIB_CODEC = 'none'
}
