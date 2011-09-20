<div id="development">
	<div class="label">development</div>
	<div class="button">
		<input type="button" value="delete all files" onclick="if (confirm('are you really super massive sure?')) { deleteAllFiles(this); } else { return false; }"/>
	</div>
</div>

<r:script>
function deleteAllFiles(element) {
	<g:each var="file" in="${files}" status="s">
	deleteFile({fileName: '${file.name}', fileId: ${file.id}});</g:each>
console.log(element);
//	document.location = document.location;
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