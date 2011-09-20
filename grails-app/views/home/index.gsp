<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
<div id="uploadedFiles">
	<mm:uploadedFileList files="${files}"/>
</div>

<div id="studyOverview">
	<g:render template="studyList"/>
</div>
<g:if env="development">
	<g:render template="development" model="[files:files]"/>
</g:if>
</body>
</html>