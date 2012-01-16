<html>
<head>
	<meta name="layout" content="main"/>
</head>

<body>
<h1>Measurement Platform Version</h1>

<h2>${measurementPlatformVersion?.measurementPlatform?.name} (${measurementPlatformVersion?.versionNumber})</h2>

<!-- Linked Assays -->
<g:if test="${measurementPlatformVersion?.assays}">
	<h2>Assays</h2>
	<mm:assayList assays="${measurementPlatformVersion?.assays}"/>
</g:if>

<!-- Linked Features -->
<g:if test="${measurementPlatformVersion?.features}">
	<h2>Features</h2>
	<mm:platformVersionTable measurementPlatformVersion="${measurementPlatformVersion}"/>
</g:if>

<!-- Delete buttong -->
<div style="margin: 15px;">
	<g:if test="${!measurementPlatformVersion?.assays}">
		<g:link action="delete" id="${measurementPlatformVersion.id}" title="delete this Measurement Platform Version">
			<img title="delete this Measurement Platform Version" style="border: 0px;" src="<g:createLinkTo dir="images/skin" file="database_delete.png" />" /> delete
		</g:link>
	</g:if>
	<g:else>
		<img title="delete is only possible when there are no Assays linked!" style="border: 0px;" src="<g:createLinkTo dir="images/skin" file="database_delete_grey.png" />" />
		<font style="color: grey">delete</font>
	</g:else>
</div>
</body>
</html>