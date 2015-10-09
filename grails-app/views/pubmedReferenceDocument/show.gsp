
<%@ page import="assessmenttool.PubmedReferenceDocument" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'pubmedReferenceDocument.label', default: 'PubmedReferenceDocument')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
		<ckeditor:resources/>
		<script type="text/javascript">
			<ckeditor:config var="toolbar_Mytoolbar">
			[
				[ 'Bold', 'Italic', 'Underline'], ['TextColor', 'BGColor'], ['Undo', 'Redo']
			]
			</ckeditor:config>
		</script>

	</head>
	<body>
		<a href="#show-pubmedReferenceDocument" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				<li><g:link class="corpus" action="getPubmedData" controller="pubmedData" id="${pubmedReferenceDocumentInstance.id}"><g:message code="default.pubmedData.label" args="[entityName]" /></g:link></li>
				<li><g:link class="datamining" action="reranking" controller="reRanking" id="${pubmedReferenceDocumentInstance.id}"><g:message code="default.reranking.label" args="[entityName]" /></g:link></li>
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

		<div class="choice content">
			<h1>Ergebnisse filtern</h1>
			<form name="filter" id="filter" method="get">
				<ol class="property-list">
					<li class="fieldcontain">
						<span class="property-selector-label">
							Ähnlichkeit
						</span>
						<span class="property-selector">
							<g:select id="setSimilarityMode" name="setSimilarityMode" from="${[97: 'alles', 99: 'nicht definiert', 98: 'nicht entscheidbar', 0: 'nicht ähnlich', 1: 'ähnlich', 2: 'sehr ähnlich'].entrySet()}" optionKey="key" optionValue="value" value="${params.setSimilarityMode ?: 97}"/>
						</span>
					</li>

					<li class="fieldcontain">
						<span class="property-selector-label">
							Relevanz
						</span>
						<span class="property-selector">
							<g:select id="setRelevanceMode" name="setRelevanceMode" from="${[97: 'alles', 99: 'nicht definiert', 98: 'nicht entscheidbar', 3: 'nicht relevant', 4: 'relevant'].entrySet()}" optionKey="key" optionValue="value" value="${params.setRelevanceMode ?: 97}"/>
						</span>
					</li>

					<li class="fieldcontain">
						<span class="property-selector-label">
							Tierversuch
						</span>
						<span class="property-selector">
							<g:select id="setAnimalTestMode" name="setAnimalTestMode" from="${[97: 'alles', 99: 'nicht definiert', 98: 'nicht entscheidbar', 0: 'nein', 1: 'ja', 2: 'sowohl als auch'].entrySet()}" optionKey="key" optionValue="value" value="${params.setAnimalTestMode ?: 97}"/>
						</span>
						<span class="property-selector-button">
							<input type="submit" class="save" value="Filtern"/>
						</span>
					</li>

				</ol>
			</form>

			<br/>
		</div>





		<div class="content reftable">
			<h1>Ergebnisse</h1>

			<form name="filter" id="filter" method="get">
				<ol class="property-list">
					<li class="fieldcontain">
						<span class="ranking-selector-label">
							Ranking
						</span>
						<span class="ranking-selector">
							<g:select id="setRankingMode" name="setRankingMode" from="${[0: 'Pubmed Similar Articles', 1: 'Data Mining: SMO'].entrySet()}" optionKey="key" optionValue="value" value="${params.setRankingMode ?: 0}"/>
						</span>
					</li>
				</ol>
			</form>

			<table border="1">
				<thead>
					<tr>
						<th>Rang</th>
						<th>PMID</th>
						<th>Titel</th>
						<th>Ähnlichkeit</th>
						<th>Relevanz</th>
						<th>Tierversuch</th>
						<th>Zusatzinformationen</th>
						<th>zuletzt geändert</th>
						<th>Aktionen</th>
					</tr>
				</thead>

				<g:set var="similarityModeCheck" value="${Integer.parseInt(params.setSimilarityMode)}"/>
				<g:set var="relevanceModeCheck" value="${Integer.parseInt(params.setRelevanceMode)}"/>
				<g:set var="animalTestModeCheck" value="${Integer.parseInt(params.setAnimalTestMode)}"/>

				<tbody>
					<g:each in="${results}" var="resultDocument">
						<g:if test="${((similarityModeCheck ==97) || (resultDocument.similarity==similarityModeCheck)) && ((relevanceModeCheck ==97) || (resultDocument.relevance==relevanceModeCheck)) && ((animalTestModeCheck==97) || (resultDocument.isAnimalTest == animalTestModeCheck)) }">
							<g:form url="[resource: resultDocument, action: 'update']" method="PUT">
								<tr>
									<g:hiddenField id="setSimilarityMode" name="setSimilarityMode" value="${similarityModeCheck}"/>
									<g:hiddenField id="setRelevanceMode" name="setRelevanceMode" value="${relevanceModeCheck}"/>
									<g:hiddenField id="setAnimalTestMode" name="setAnimalTestMode" value="${animalTestModeCheck}"/>
									<td>${resultDocument.rank}</td>
									<td><a href="http://www.ncbi.nlm.nih.gov/pubmed/${resultDocument.pmid}" target="_blank">${resultDocument.pmid}</a></td>
									<td>
										${resultDocument.title}
										<div style="display: block;"><a href="javascript:toggleDetails('abstract${resultDocument.id}')" name="1" id="b-abstract${resultDocument.id}">Abstract anzeigen</a></div>
										<div id="abstract${resultDocument.id}" style="display:none;">
											<p>
												<ckeditor:editor id="docAbstract${resultDocument.id}" name="docAbstract" toolbar="Mytoolbar" height="120px" width="90%">
													${resultDocument.docAbstract}
												</ckeditor:editor>
												%{--<span style="font-size:75%">--}%
													%{--${resultDocument.docAbstract}--}%
												%{--</span>--}%
											</p>
										</div>
									</td>
									<td>
										<g:select name="similarity" from="${[99: 'nicht definiert', 98: 'nicht entscheidbar', 0: 'nicht ähnlich', 1: 'ähnlich', 2: 'sehr ähnlich'].entrySet()}" optionKey="key" optionValue="value" value="${resultDocument.similarity}"/>

									</td>

									<td>
										<g:select name="relevance" from="${[99: 'nicht definiert', 98: 'nicht entscheidbar', 3: 'nicht relevant', 4: 'relevant'].entrySet()}" optionKey="key" optionValue="value" value="${resultDocument.relevance}"/>

									</td>
									<td>
										<g:select name="isAnimalTest" from="${[99: 'nicht definiert', 98: 'nicht entscheidbar', 0: 'nein', 1: 'ja', 2: 'sowohl als auch'].entrySet()}" optionKey="key" optionValue="value" value="${resultDocument.isAnimalTest}"/>
									</td>
									<td>
										<div style="display: block;"><a href="javascript:toggleInfoDetails('info${resultDocument.id}')" name="2" id="b-info${resultDocument.id}">Zusatzinformationen anzeigen</a></div>
										<div id="info${resultDocument.id}" style="display:none;">
											<p>
												<g:textArea id="addInfo${resultDocument.id}" name="addInfo" value="${resultDocument.addInfo}" />
											</p>
										</div>
									</td>
									<td>${resultDocument.lastChange}</td>

									<td>
										<g:actionSubmit class="save" action="update" controller="pubmedResultDocument"
														value="${message(code: 'default.button.update.label', default: 'Update')}" />
										%{--<g:actionSubmit class="edit" action="update" value="Änderungen speichern"/>--}%
									</td>

									%{--<fieldset class="buttons">--}%
									%{--<g:link class="edit" action="edit" resource="${resultDocument}"><g:message code="default.button.edit.label" default="Edit" /></g:link>--}%
									%{--<g:actionSubmit class="update" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"></g:actionSubmit>--}%
									%{--</fieldset>--}%
								</tr>
							</g:form>
						</g:if>
					</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${pubmedResultDocumentInstanceCount ?: 0}" id="${params.id}"/>
			</div>

			<g:form url="[resource:pubmedReferenceDocumentInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${pubmedReferenceDocumentInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
