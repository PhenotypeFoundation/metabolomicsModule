<div id="development">
	<div class="label">development</div>
	<div class="actions">
	<g:render template="developmentActions"/>
	</div>
</div>

<r:script>
function before(element, message) {
	var parent = $(element).parent().parent();
	parent.html('<div class="wait"><img src="'+baseUrl+'/images/development-spinner.gif"/></div>');
	$('img',parent).tipTip({content: (message) ? message : 'please wait...'});
}
function after(element) {
	// refresh the development bar
	$.ajax({
		url: baseUrl + '/home/developmentBar',
		cache: false,
		success: function(html) {
			$('div.actions', $('div#development')).html(html);
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
				// remove all files from DOM
				var visible = $('div.file:visible',$('div.files',$('div.uploadr[name="uploadrArea"]'))).size();
				var shown = false;
				$('div.file',$('div.files',$('div.uploadr[name="uploadrArea"]'))).each(function() {
					var file = $(this);
					if (file.is(':visible')) {
						file.animate({height: '0px'}, 500, 'swing', function() {
							file.remove();
							visible--;

							if (!shown && visible == 0) {
								// show placeholder
								shown = true;
								$('.placeholder',$('div.uploadr[name="uploadrArea"]')).show();
							}
						});
					} else {
						file.remove();
					}
				});

				// call after method
				after(element);
			}
		}
	);
}
</r:script>