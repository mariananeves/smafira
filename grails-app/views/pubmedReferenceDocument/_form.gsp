<%@ page import="assessmenttool.PubmedReferenceDocument" %>



<div class="fieldcontain ${hasErrors(bean: pubmedReferenceDocumentInstance, field: 'pmid', 'error')} required">
	<label for="pmid">
		<g:message code="pubmedReferenceDocument.pmid.label" default="Pmid" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="pmid" required="" value="${pubmedReferenceDocumentInstance?.pmid}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: pubmedReferenceDocumentInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="pubmedReferenceDocument.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" required="" value="${pubmedReferenceDocumentInstance?.title}"/>

</div>

