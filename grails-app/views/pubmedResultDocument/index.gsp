
<%@ page import="assessmenttool.PubmedResultDocument" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'pubmedResultDocument.label', default: 'PubmedResultDocument')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-pubmedResultDocument" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-pubmedResultDocument" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="pmid" title="${message(code: 'pubmedResultDocument.pmid.label', default: 'Pmid')}" />
					
						<g:sortableColumn property="title" title="${message(code: 'pubmedResultDocument.title.label', default: 'Title')}" />
					
						<th><g:message code="pubmedResultDocument.refDoc.label" default="Ref Doc" /></th>
					
						<g:sortableColumn property="relevance" title="${message(code: 'pubmedResultDocument.relevance.label', default: 'Relevance')}" />
					
						<g:sortableColumn property="isAnimalTest" title="${message(code: 'pubmedResultDocument.isAnimalTest.label', default: 'Is Animal Test')}" />
					
						<g:sortableColumn property="rank" title="${message(code: 'pubmedResultDocument.rank.label', default: 'Rank')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${pubmedResultDocumentInstanceList}" status="i" var="pubmedResultDocumentInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${pubmedResultDocumentInstance.id}">${fieldValue(bean: pubmedResultDocumentInstance, field: "pmid")}</g:link></td>
					
						<td>${fieldValue(bean: pubmedResultDocumentInstance, field: "title")}</td>
					
						<td>${fieldValue(bean: pubmedResultDocumentInstance, field: "refDoc")}</td>
					
						<td>${fieldValue(bean: pubmedResultDocumentInstance, field: "relevance")}</td>
					
						<td>${fieldValue(bean: pubmedResultDocumentInstance, field: "isAnimalTest")}</td>
					
						<td>${fieldValue(bean: pubmedResultDocumentInstance, field: "rank")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${pubmedResultDocumentInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
