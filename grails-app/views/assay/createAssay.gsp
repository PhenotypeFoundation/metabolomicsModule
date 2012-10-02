<html>
<head>
<meta name="layout" content="main"/>
</head>

<body>

<div id="pageTitle" style="width:200px;"><h1>Create an Assay</h1></div>

	<p>When you have data that belongs to an assay/study which is not in the list you can create it here.</p>

	<!--${params}-->
	<p>${flash.message}</p>

	<g:form name="createAssay" action="createAssay">

		<g:if test="${studies.count}">

			<h3>select a study</h3>
			<select name="studyToken">
				<g:each in="${studies.studies}" var="study">
					<option <g:if test="${study.token == params.studyToken}">selected=selected</g:if> value="${study.token}">${study.title}</option>
				</g:each>
			</select> <small>(Please select to which study the assay belongs)</small><br />

			<g:if test="${assayTemplates}">
				<h3>select a template</h3>
				<select name="assayTemplate">
					<g:each in="${assayTemplates.templates}" var="template">
						<option <g:if test="${template.token == params.assayTemplate}">selected=selected</g:if> value="${template.token}">${template.name}</option>
					</g:each>
				</select> <small>(Please select what type of assay you wish to create)</small><br />

				<h3>name of the assay</h3>
				<g:textField name="assayName" value="${params.assayName}" /><br />
			</g:if>

		</g:if>


		<br />
		<g:submitButton name="submit" value="create" />
	</g:form>


</body>
</html>
