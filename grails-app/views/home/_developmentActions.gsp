<div class="button"><input type="button" id="deleteAllFiles" value="delete all files" onclick="if (confirm('are you really super massive sure?')) { deleteAllFiles(this); } else { return false; }"/></div>

<r:script>
function before(element, message) {
	var parent = $(element).parent().parent();
	parent.html('<div class="wait"><img src="'+baseUrl+'/images/development-spinner.gif"/></div>');
	$('img',parent).tipTip({content: (message) ? message : 'please wait...'});
}
function after(element) {
	// refresh the development bar
//	document.location = document.location;
	// update studylist
	$.ajax({
		url: baseUrl + '/home/developmentBar',
		cache: false,
		success: function(html) {
			$('div#development').html(html);
		}
	});
}

// delete all files
function deleteAllFiles(element) {
	before(element,'please wait while all files are deleted...');

	var a = $.ajax(
		'<g:createLink plugin="dbxpModuleStorage" controller="uploadedFile" action="deleteAllUploadedFilesForCurrentUser"/>',
		{
			async: false,
			headers: {
				'X-File-Name': 'a',
				'X-File-Id': 'b'
			},
			success: function(data) {
				after(element);
			}
		}
	);
}
</r:script>