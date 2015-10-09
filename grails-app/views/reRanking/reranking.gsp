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
		<h1>Re-Ranking</h1>
		<form name="data" id="data" method="get">
			<ol class="property-list">

				<li class="fieldcontain">
					<span class="property-selector-label">
						Data Mining Methode
					</span>
					<span class="property-selector">
						<g:select id="setDataMiningMethod" name="setDataMiningMethod" from="${[1: 'SMO', 2: 'NaiveBayes', 3: 'BayesNet', 4: 'JRip', 5: 'J48', 6: 'LWL'].entrySet()}" optionKey="key" optionValue="value" value="${params.setDataMiningMethod ?: 0}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						Verwendete Dokument-Features:
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						Alles auswählen
					</span>
					<span class="property-selector">
						<g:checkBox name="check_all" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						Titel
					</span>
					<span class="property-selector">
						<g:checkBox name="titel_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						Abstract
					</span>
					<span class="property-selector">
						<g:checkBox name="abstract_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						PMCID
					</span>
					<span class="property-selector">
						<g:checkBox name="pmcid_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						Volltext-URL
					</span>
					<span class="property-selector">
						<g:checkBox name="fulltextUrl_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						Volltext-Freestatus
					</span>
					<span class="property-selector">
						<g:checkBox name="fulltextFreestatus_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						Journal
					</span>
					<span class="property-selector">
						<g:checkBox name="fulltextJournal_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						MeSH-Terme
					</span>
					<span class="property-selector">
						<g:checkBox name="meshterms_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						MeSH-UI
					</span>
					<span class="property-selector">
						<g:checkBox name="meshui_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						MeSH-Majortopic
					</span>
					<span class="property-selector">
						<g:checkBox name="meshmajortopic_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						MeSH-Qualifier
					</span>
					<span class="property-selector">
						<g:checkBox name="qualifier_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						MeSH-Qualifier-UI
					</span>
					<span class="property-selector">
						<g:checkBox name="qualifierUi_check" value="${true}"/>
					</span>
				</li>
				<li class="fieldcontain">
					<span class="property-selector-label">
						MeSH-Qualifier-Majortopic
					</span>
					<span class="property-selector">
						<g:checkBox name="qualifierMajortopic_check" value="${true}"/>
					</span>
				</li>
			</ol>
			<fieldset class="buttons">
				<g:submitButton name="rerank" class="save" value="${message(code: 'default.button.rerank.label', default: 'Berechnen')}" />
			</fieldset>
		</form>

		<br/>
	</div>
</body>
</html>
