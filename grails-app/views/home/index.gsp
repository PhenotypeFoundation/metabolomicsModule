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
<input type="button" value="delete all files" onclick="if (confirm('are you really super massive sure?')) { deleteAll(); } else { return false; }"/>
<r:script>
function deleteAll() {
	<g:each var="file" in="${files}" status="s">
	deleteFile({fileName: '${file.name}', fileId: ${file.id}});</g:each>
	document.location = document.location;
}
function deleteFile(file) {
	var a = $.ajax(
		'<g:createLink plugin="dbxpModuleStorage" controller="uploadedFile" action="deleteUploadedFile"/>',
		{
			async: false,
			headers: {
				'X-File-Name': file.fileName,
				'X-File-Id': file.fileId
			},
			success: function(data) { result = data; }
		}
	);
}
</r:script>
</g:if>
</body>
</html>