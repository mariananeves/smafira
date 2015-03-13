<%@ page import="assessmenttool.PubmedResultDocument" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'pubmedResultDocument.label', default: 'PubmedResultDocument')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-pubmedResultDocument" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                           default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-pubmedResultDocument" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list pubmedResultDocument">

        <g:if test="${pubmedResultDocumentInstance?.pmid}">
            <li class="fieldcontain">
                <span id="pmid-label" class="property-label"><g:message code="pubmedResultDocument.pmid.label"
                                                                        default="Pmid"/></span>

                <span class="property-value" aria-labelledby="pmid-label"><g:fieldValue
                        bean="${pubmedResultDocumentInstance}" field="pmid"/></span>

            </li>
        </g:if>

        <g:if test="${pubmedResultDocumentInstance?.title}">
            <li class="fieldcontain">
                <span id="title-label" class="property-label"><g:message code="pubmedResultDocument.title.label"
                                                                         default="Title"/></span>

                <span class="property-value" aria-labelledby="title-label"><g:fieldValue
                        bean="${pubmedResultDocumentInstance}" field="title"/></span>

            </li>
        </g:if>

        <g:if test="${pubmedResultDocumentInstance?.refDoc}">
            <li class="fieldcontain">
                <span id="refDoc-label" class="property-label"><g:message code="pubmedResultDocument.refDoc.label"
                                                                          default="Ref Doc"/></span>

                <span class="property-value" aria-labelledby="refDoc-label"><g:link controller="pubmedReferenceDocument"
                                                                                    action="show"
                                                                                    id="${pubmedResultDocumentInstance?.refDoc?.id}">${pubmedResultDocumentInstance?.refDoc?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>


            <li class="fieldcontain">
                <span id="relevance-label" class="property-label"><g:message code="pubmedResultDocument.relevance.label"
                                                                             default="Relevance"/></span>

                <span class="property-value" aria-labelledby="relevance-label"><g:fieldValue
                        bean="${pubmedResultDocumentInstance}" field="relevance"/></span>

            </li>

        <li class="fieldcontain">
            <span id="relevance2-label" class="property-label"><g:message code="pubmedResultDocument.relevance2.label"
                                                                         default="Relevance2"/></span>

            <span class="property-value" aria-labelledby="relevance2-label"><g:fieldValue
                    bean="${pubmedResultDocumentInstance}" field="relevance2"/></span>

        </li>



            <li class="fieldcontain">
                <span id="isAnimalTest-label" class="property-label"><g:message
                        code="pubmedResultDocument.isAnimalTest.label" default="Is Animal Test"/></span>

                <span class="property-value" aria-labelledby="isAnimalTest-label"><g:fieldValue
                        bean="${pubmedResultDocumentInstance}" field="isAnimalTest"/></span>

            </li>


        <g:if test="${pubmedResultDocumentInstance?.rank}">
            <li class="fieldcontain">
                <span id="rank-label" class="property-label"><g:message code="pubmedResultDocument.rank.label"
                                                                        default="Rank"/></span>

                <span class="property-value" aria-labelledby="rank-label"><g:fieldValue
                        bean="${pubmedResultDocumentInstance}" field="rank"/></span>

            </li>
        </g:if>

        <g:if test="${pubmedResultDocumentInstance?.pmcid}">
            <li class="fieldcontain">
                <span id="pmcid-label" class="property-label"><g:message code="pubmedResultDocument.pmcid.label"
                                                                         default="PMCID"/></span>

                <span class="property-value" aria-labelledby="title-label"><g:fieldValue
                        bean="${pubmedResultDocumentInstance}" field="pmcid"/></span>

            </li>
        </g:if>

        <g:if test="${pubmedResultDocumentInstance?.docAbstract}">
            <li class="fieldcontain">
                <span id="docAbstract-label" class="property-label"><g:message code="pubmedResultDocument.docAbstract.label"
                                                                               default="Abstract"/></span>

                <span class="property-value" aria-labelledby="docAbstract-label"><g:fieldValue
                        bean="${pubmedResultDocumentInstance}" field="docAbstract"/></span>

            </li>
        </g:if>

        <g:if test="${pubmedResultDocumentInstance?.lastChange}">
            <li class="fieldcontain">
                <span id="lastChange-label" class="property-label"><g:message
                        code="pubmedResultDocument.lastChange.label" default="Last Change"/></span>

                <span class="property-value" aria-labelledby="lastChange-label"><g:formatDate
                        date="${pubmedResultDocumentInstance?.lastChange}"/></span>

            </li>
        </g:if>

    </ol>
    <g:form url="[resource: pubmedResultDocumentInstance, action: 'delete']" method="DELETE">
        <fieldset class="buttons">
            <g:link class="edit" action="edit" resource="${pubmedResultDocumentInstance}"><g:message
                    code="default.button.edit.label" default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>


</div>
</body>
</html>
