import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

@GrailsPlugin(name='ckeditor', version='4.4.1.0')
class gsp_ckeditorofm_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/plugins/ckeditor-4.4.1.0/grails-app/views/ofm.gsp" }
public Object run() {
Writer out = getOut()
Writer expressionOut = getExpressionOut()
registerSitemeshPreprocessMode()
printHtmlPart(0)
createTagBody(1, {->
printHtmlPart(1)
invokeTag('captureMeta','sitemesh',6,['gsp_sm_xmlClosingForEmptyTag':("/"),'http-equiv':("content-type"),'content':("text/html; charset=utf-8")],-1)
printHtmlPart(1)
createTagBody(2, {->
createClosureForHtmlPart(2, 3)
invokeTag('captureTitle','sitemesh',7,[:],3)
})
invokeTag('wrapTitleTag','sitemesh',7,[:],2)
printHtmlPart(3)
expressionOut.print(resource(dir: 'js/ofm/styles', file: 'reset.css', plugin: 'ckeditor'))
printHtmlPart(4)
expressionOut.print(resource(dir: 'js/ofm/scripts/jquery.filetree', file: 'jqueryFileTree.css', plugin: 'ckeditor'))
printHtmlPart(4)
expressionOut.print(resource(dir: 'js/ofm/scripts/jquery.contextmenu', file: 'jquery.contextMenu-1.01.css', plugin: 'ckeditor'))
printHtmlPart(4)
expressionOut.print(resource(dir: 'js/ofm/styles', file: 'filemanager.css', plugin: 'ckeditor'))
printHtmlPart(5)
expressionOut.print(resource(dir: 'js/ofm/styles', file: 'ie9.css', plugin: 'ckeditor'))
printHtmlPart(6)
expressionOut.print(resource(dir: 'js/ofm/styles', file: 'ie8.css', plugin: 'ckeditor'))
printHtmlPart(7)
out.print(configUrl)
printHtmlPart(8)
})
invokeTag('captureHead','sitemesh',23,[:],1)
printHtmlPart(9)
createTagBody(1, {->
printHtmlPart(10)
expressionOut.print(resource(dir: 'js/ofm/scripts', file: 'jquery-1.8.3.min.js', plugin: 'ckeditor'))
printHtmlPart(11)
expressionOut.print(resource(dir: 'js/ofm/scripts', file: 'jquery.form-3.24.js', plugin: 'ckeditor'))
printHtmlPart(11)
expressionOut.print(resource(dir: 'js/ofm/scripts/jquery.splitter', file: 'jquery.splitter-1.5.1.js', plugin: 'ckeditor'))
printHtmlPart(11)
expressionOut.print(resource(dir: 'js/ofm/scripts/jquery.filetree', file: 'jqueryFileTree.js', plugin: 'ckeditor'))
printHtmlPart(11)
expressionOut.print(resource(dir: 'js/ofm/scripts/jquery.contextmenu', file: 'jquery.contextMenu-1.01.js', plugin: 'ckeditor'))
printHtmlPart(11)
expressionOut.print(resource(dir: 'js/ofm/scripts', file: 'jquery.impromptu-3.2.min.js', plugin: 'ckeditor'))
printHtmlPart(11)
expressionOut.print(resource(dir: 'js/ofm/scripts', file: 'jquery.tablesorter-2.7.2.min.js', plugin: 'ckeditor'))
printHtmlPart(11)
expressionOut.print(resource(dir: 'js/ofm/scripts', file: 'filemanager.min.js', plugin: 'ckeditor'))
printHtmlPart(12)
})
invokeTag('captureBody','sitemesh',82,[:],1)
printHtmlPart(13)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1400592248000L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'none'
public static final String TAGLIB_CODEC = 'none'
}
