// delete file
var result = null;
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

// handle status
if (a.status != 200 || (a.status == 200 && result.status != 200)) {
	// something went wrong, open dialog
	$( "<div title=\"Error\">" + ((result.message) ? result.message : "an unknown error occured") + "</div>" ).dialog({
		modal: true,
		buttons: {
			Ok: function() {
				$( this ).dialog( "close" );
			}
		}
	});
}

return (a.status == 200 && result.status == 200);