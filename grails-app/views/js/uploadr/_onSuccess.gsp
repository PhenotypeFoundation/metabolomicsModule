var result = null;
var a = $.ajax(
	'<g:createLink plugin="dbxpModuleStorage" controller="uploadedFile" action="uploadFinished"/>',
	{
		async: false,
		headers: {
			'X-File-Name': file.fileName
		},
		success: function(data) {
			file.fileId = data.fileId;
			result = data;
		}
	}
);

return (a.status == 200 && result.status == 200);