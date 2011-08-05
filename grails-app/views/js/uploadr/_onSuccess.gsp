// set tooltip on spinner icon
$('.spinner', domObj).tipTip({
	content: 'almost done, please wait while the uploaded file is being imported'
});

// perform ajax request
$.ajax(
	'<g:createLink plugin="dbxpModuleStorage" controller="uploadedFile" action="uploadFinished"/>',
	{
		async: true,
		headers: {
			'X-File-Name': file.fileName
		},
		success: function(data) {
			file.fileId = data.fileId;
			callback();
		}
	}
);