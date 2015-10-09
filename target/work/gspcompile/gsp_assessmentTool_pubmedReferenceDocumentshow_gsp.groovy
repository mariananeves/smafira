import assessmenttool.PubmedReferenceDocument
import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_assessmentTool_pubmedReferenceDocumentshow_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/pubmedReferenceDocument/show.gsp" }
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
invokeTag('set','g',7,['var':("entityName"),'value':(message(code: 'pubmedReferenceDocument.label', default: 'PubmedReferenceDocument'))],-1)
printHtmlPart(2)
createTagBody(2, {->
createTagBody(3, {->
invokeTag('message','g',8,['code':("default.show.label"),'args':([entityName])],-1)
})
invokeTag('captureTitle','sitemesh',8,[:],3)
})
invokeTag('wrapTitleTag','sitemesh',8,[:],2)
printHtmlPart(2)
invokeTag('resources','ckeditor',9,[:],-1)
printHtmlPart(3)
createClosureForHtmlPart(4, 2)
invokeTag('config','ckeditor',15,['var':("toolbar_Mytoolbar")],2)
printHtmlPart(5)
})
invokeTag('captureHead','sitemesh',18,[:],1)
printHtmlPart(6)
createTagBody(1, {->
printHtmlPart(7)
invokeTag('message','g',20,['code':("default.link.skip.label"),'default':("Skip to content&hellip;")],-1)
printHtmlPart(8)
expressionOut.print(createLink(uri: '/'))
printHtmlPart(9)
invokeTag('message','g',23,['code':("default.home.label")],-1)
printHtmlPart(10)
createTagBody(2, {->
invokeTag('message','g',24,['code':("default.list.label"),'args':([entityName])],-1)
})
invokeTag('link','g',24,['class':("list"),'action':("index")],2)
printHtmlPart(11)
createTagBody(2, {->
invokeTag('message','g',25,['code':("default.new.label"),'args':([entityName])],-1)
})
invokeTag('link','g',25,['class':("create"),'action':("create")],2)
printHtmlPart(12)
if(true && (flash.message)) {
printHtmlPart(13)
expressionOut.print(flash.message)
printHtmlPart(14)
}
printHtmlPart(15)
if(true && (pubmedReferenceDocumentInstance?.pmid)) {
printHtmlPart(16)
invokeTag('message','g',38,['code':("pubmedReferenceDocument.pmid.label"),'default':("Pmid")],-1)
printHtmlPart(17)
expressionOut.print(pubmedReferenceDocumentInstance?.pmid)
printHtmlPart(18)
invokeTag('fieldValue','g',41,['bean':(pubmedReferenceDocumentInstance),'field':("pmid")],-1)
printHtmlPart(19)
}
printHtmlPart(20)
if(true && (pubmedReferenceDocumentInstance?.title)) {
printHtmlPart(21)
invokeTag('message','g',49,['code':("pubmedReferenceDocument.title.label"),'default':("Title")],-1)
printHtmlPart(22)
invokeTag('fieldValue','g',51,['bean':(pubmedReferenceDocumentInstance),'field':("title")],-1)
printHtmlPart(23)
}
printHtmlPart(24)
invokeTag('select','g',68,['id':("setSimilarityMode"),'name':("setSimilarityMode"),'from':([97: 'alles', 99: 'nicht definiert', 98: 'nicht entscheidbar', 0: 'nicht ähnlich', 1: 'ähnlich', 2: 'sehr ähnlich'].entrySet()),'optionKey':("key"),'optionValue':("value"),'value':(params.setSimilarityMode ?: 97)],-1)
printHtmlPart(25)
invokeTag('select','g',77,['id':("setRelevanceMode"),'name':("setRelevanceMode"),'from':([97: 'alles', 99: 'nicht definiert', 98: 'nicht entscheidbar', 3: 'nicht relevant', 4: 'relevant'].entrySet()),'optionKey':("key"),'optionValue':("value"),'value':(params.setRelevanceMode ?: 97)],-1)
printHtmlPart(26)
invokeTag('select','g',86,['id':("setAnimalTestMode"),'name':("setAnimalTestMode"),'from':([97: 'alles', 99: 'nicht definiert', 98: 'nicht entscheidbar', 0: 'nein', 1: 'ja', 2: 'sowohl als auch'].entrySet()),'optionKey':("key"),'optionValue':("value"),'value':(params.setAnimalTestMode ?: 97)],-1)
printHtmlPart(27)
invokeTag('set','g',118,['var':("similarityModeCheck"),'value':(Integer.parseInt(params.setSimilarityMode))],-1)
printHtmlPart(28)
invokeTag('set','g',119,['var':("relevanceModeCheck"),'value':(Integer.parseInt(params.setRelevanceMode))],-1)
printHtmlPart(28)
invokeTag('set','g',120,['var':("animalTestModeCheck"),'value':(Integer.parseInt(params.setAnimalTestMode))],-1)
printHtmlPart(29)
for( resultDocument in (results) ) {
printHtmlPart(30)
if(true && (((similarityModeCheck ==97) || (resultDocument.similarity==similarityModeCheck)) && ((relevanceModeCheck ==97) || (resultDocument.relevance==relevanceModeCheck)) && ((animalTestModeCheck==97) || (resultDocument.isAnimalTest == animalTestModeCheck)))) {
printHtmlPart(31)
createTagBody(4, {->
printHtmlPart(32)
invokeTag('hiddenField','g',126,['id':("setSimilarityMode"),'name':("setSimilarityMode"),'value':(similarityModeCheck)],-1)
printHtmlPart(33)
invokeTag('hiddenField','g',127,['id':("setRelevanceMode"),'name':("setRelevanceMode"),'value':(relevanceModeCheck)],-1)
printHtmlPart(33)
invokeTag('hiddenField','g',128,['id':("setAnimalTestMode"),'name':("setAnimalTestMode"),'value':(animalTestModeCheck)],-1)
printHtmlPart(34)
expressionOut.print(resultDocument.rank)
printHtmlPart(35)
expressionOut.print(resultDocument.pmid)
printHtmlPart(36)
expressionOut.print(resultDocument.pmid)
printHtmlPart(37)
expressionOut.print(resultDocument.title)
printHtmlPart(38)
expressionOut.print(resultDocument.id)
printHtmlPart(39)
expressionOut.print(resultDocument.id)
printHtmlPart(40)
expressionOut.print(resultDocument.id)
printHtmlPart(41)
createTagBody(5, {->
printHtmlPart(42)
expressionOut.print(resultDocument.docAbstract)
printHtmlPart(43)
})
invokeTag('editor','ckeditor',136,['id':("docAbstract${resultDocument.id}"),'name':("docAbstract"),'toolbar':("Mytoolbar"),'height':("120px"),'width':("90%")],5)
printHtmlPart(44)
invokeTag('select','g',146,['name':("similarity"),'from':([99: 'nicht definiert', 98: 'nicht entscheidbar', 0: 'nicht ähnlich', 1: 'ähnlich', 2: 'sehr ähnlich'].entrySet()),'optionKey':("key"),'optionValue':("value"),'value':(resultDocument.similarity)],-1)
printHtmlPart(45)
invokeTag('select','g',151,['name':("relevance"),'from':([99: 'nicht definiert', 98: 'nicht entscheidbar', 3: 'nicht relevant', 4: 'relevant'].entrySet()),'optionKey':("key"),'optionValue':("value"),'value':(resultDocument.relevance)],-1)
printHtmlPart(46)
invokeTag('select','g',155,['name':("isAnimalTest"),'from':([99: 'nicht definiert', 98: 'nicht entscheidbar', 0: 'nein', 1: 'ja', 2: 'sowohl als auch'].entrySet()),'optionKey':("key"),'optionValue':("value"),'value':(resultDocument.isAnimalTest)],-1)
printHtmlPart(47)
expressionOut.print(resultDocument.lastChange)
printHtmlPart(48)
invokeTag('actionSubmit','g',160,['class':("save"),'action':("update"),'controller':("pubmedResultDocument"),'value':(message(code: 'default.button.update.label', default: 'Update'))],-1)
printHtmlPart(49)
})
invokeTag('form','g',161,['url':([resource: resultDocument, action: 'update']),'method':("PUT")],4)
printHtmlPart(30)
}
printHtmlPart(29)
}
printHtmlPart(50)
createTagBody(2, {->
printHtmlPart(51)
createTagBody(3, {->
invokeTag('message','g',166,['code':("default.button.edit.label"),'default':("Edit")],-1)
})
invokeTag('link','g',166,['class':("edit"),'action':("edit"),'resource':(pubmedReferenceDocumentInstance)],3)
printHtmlPart(30)
invokeTag('actionSubmit','g',167,['class':("delete"),'action':("delete"),'value':(message(code: 'default.button.delete.label', default: 'Delete')),'onclick':("return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');")],-1)
printHtmlPart(52)
})
invokeTag('form','g',167,['url':([resource:pubmedReferenceDocumentInstance, action:'delete']),'method':("DELETE")],2)
printHtmlPart(53)
})
invokeTag('captureBody','sitemesh',167,[:],1)
printHtmlPart(54)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1432057846348L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'html'
public static final String TAGLIB_CODEC = 'none'
}
