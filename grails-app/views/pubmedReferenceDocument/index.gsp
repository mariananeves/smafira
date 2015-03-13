
<%@ page import="assessmenttool.PubmedReferenceDocument" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'pubmedReferenceDocument.label', default: 'PubmedReferenceDocument')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-pubmedReferenceDocument" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-pubmedReferenceDocument" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="pmid" title="${message(code: 'pubmedReferenceDocument.pmid.label', default: 'Pmid')}" />
					
						<g:sortableColumn property="title" title="${message(code: 'pubmedReferenceDocument.title.label', default: 'Title')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${pubmedReferenceDocumentInstanceList}" status="i" var="pubmedReferenceDocumentInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${pubmedReferenceDocumentInstance.id}">${fieldValue(bean: pubmedReferenceDocumentInstance, field: "pmid")}</g:link></td>
					
						<td>${fieldValue(bean: pubmedReferenceDocumentInstance, field: "title")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${pubmedReferenceDocumentInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
