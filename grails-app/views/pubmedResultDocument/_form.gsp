<%@ page import="assessmenttool.PubmedResultDocument" %>



<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'pmid', 'error')} required">
    <label for="pmid">
        <g:message code="pubmedResultDocument.pmid.label" default="Pmid"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="pmid" required="" value="${pubmedResultDocumentInstance?.pmid}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'title', 'error')} required">
    <label for="title">
        <g:message code="pubmedResultDocument.title.label" default="Title"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="title" required="" value="${pubmedResultDocumentInstance?.title}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'refDoc', 'error')} required">
    <label for="refDoc">
        <g:message code="pubmedResultDocument.refDoc.label" default="Ref Doc"/>
        <span class="required-indicator">*</span>
    </label>
    <g:select id="refDoc" name="refDoc.id" from="${assessmenttool.PubmedReferenceDocument.list()}" optionKey="id"
              required="" value="${pubmedResultDocumentInstance?.refDoc?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'relevance', 'error')} ">
    <label for="relevance">
        <g:message code="pubmedResultDocument.relevance.label" default="Relevance"/>

    </label>
    <g:field name="relevance" type="number" value="${pubmedResultDocumentInstance.relevance}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'relevance2', 'error')} ">
    <label for="relevance2">
        <g:message code="pubmedResultDocument.relevance2.label" default="Relevance2"/>

    </label>
    <g:field name="relevance2" type="number" value="${pubmedResultDocumentInstance.relevance2}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'isAnimalTest', 'error')} ">
    <label for="isAnimalTest">
        <g:message code="pubmedResultDocument.isAnimalTest.label" default="Is Animal Test"/>

    </label>
    <g:field name="isAnimalTest" type="number" value="${pubmedResultDocumentInstance?.isAnimalTest}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'rank', 'error')} required">
    <label for="rank">
        <g:message code="pubmedResultDocument.rank.label" default="Rank"/>
        <span class="required-indicator">*</span>
    </label>
    <g:field name="rank" type="number" value="${pubmedResultDocumentInstance.rank}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'lastChange', 'error')} required">
    <label for="lastChange">
        <g:message code="pubmedResultDocument.lastChange.label" default="Last Change"/>
        <span class="required-indicator">*</span>
    </label>
    <g:formatDate name="lastChange" date="${pubmedResultDocumentInstance?.lastChange}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'pmcid', 'error')}">
    <label for="pmcid">
        <g:message code="pubmedResultDocument.pmcid.label" default="PMCID"/>
    </label>
    <g:textField name="pmcid" value="${pubmedResultDocumentInstance?.pmcid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pubmedResultDocumentInstance, field: 'docAbstract', 'error')}">
    <label for="docAbstract">
        <g:message code="pubmedResultDocument.docAbstract.label" default="Abstract"/>
    </label>
    <g:textField name="docAbstract" value="${pubmedResultDocumentInstance?.docAbstract}"/>
</div>