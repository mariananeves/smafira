<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'pubmedReferenceDocument.label', default: 'PubmedReferenceDocument')}" />
    <title><g:message code="default.reranking.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-pubmedReferenceDocument" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
        <li><g:link action="show" controller="pubmedReferenceDocument" id="${pubmedReferenceDocumentInstance.id}">Referenzdokument: ${fieldValue(bean: pubmedReferenceDocumentInstance, field: "pmid")}</g:link></li>
    </ul>
</div>

<h2>Daten wurden geladen!</h2>

</body>
</html>