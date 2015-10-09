<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'pubmedReferenceDocument.label', default: 'PubmedReferenceDocument')}" />
		<title><g:message code="default.pubmedData.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-pubmedReferenceDocument" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
			</ul>
		</div>



		<div id="show-pubmedReferenceDocument" class="content scaffold-show" role="main">
			%{--<h1><g:message code="default.show.label" args="[entityName]" /></h1>--}%
			<h1>Referenzdokument</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list pubmedReferenceDocument">

				<g:if test="${pubmedReferenceDocumentInstance?.pmid}">
					<li class="fieldcontain">
						<span id="pmid-label" class="property-label"><g:message code="pubmedReferenceDocument.pmid.label" default="Pmid" /></span>

						<a href="http://www.ncbi.nlm.nih.gov/pubmed/${pubmedReferenceDocumentInstance?.pmid}" target="_blank">
							<span class="property-value" aria-labelledby="pmid-label"><g:fieldValue bean="${pubmedReferenceDocumentInstance}" field="pmid"/></span>
						</a>
					</li>

				</g:if>

				<g:if test="${pubmedReferenceDocumentInstance?.title}">
					<li class="fieldcontain">
						<span id="title-label" class="property-label"><g:message code="pubmedReferenceDocument.title.label" default="Title" /></span>

						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${pubmedReferenceDocumentInstance}" field="title"/></span>

					</li>
				</g:if>

			</ol>
		</div>


		<div class="content scaffold-form">
			<h1>Pubmed-Dokumente laden</h1>
			<form name="data" id="data" method="get">
				<ol class="property-list">
					<li class="fieldcontain">
						<span class="property-selector-label">
							Max. Anzahl Dokumente
						</span>
						<span class="property-selector">
							<g:field name="numDocs" type="number" min="0"/>
						</span>
					</li>
				</ol>
				<fieldset class="buttons">
					<g:submitButton name="loadDocuments" class="save" value="${message(code: 'default.button.loadDocuments.label', default: 'Dokumente laden')}" />
				</fieldset>
			</form>

			<br/>
		</div>
	</body>
</html>
