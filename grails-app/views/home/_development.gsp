<%
/**
 * The development toolbar. If you would like to extend this with
 * your own actions, put the (logicless) button in the
 * _developmentActions.gsp view, and the JavaScript logic
 * in this view.
 *
 * The JavaScript logic should always call the developmentBarBefore
 * function on start, and the developmentBarAfter function when the
 * operation was finished.
 *
 * $Author: duh $
 * $Rev: 74532 $
 * $Date: 2011-09-19 15:56:33 +0200 (Mon, 19 Sep 2011) $
 */
%>
<div id="development">
	<div class="label">${grails.util.GrailsUtil.environment}<g:if env="development"></g:if><g:else>#${meta(name: 'app.build.svn.revision')}</g:else></div>
	<div class="actions">
	<g:render template="developmentActions"/>
	</div>
</div>

<r:script>
// fancy sounds
var soundId, sound;
updateSound();

// generic function to show a spinner plus tooltip
function developmentBarBefore(element, message) {
	var parent = $(element).parent().parent();
	sound.play();
	parent.html('<div class="wait"><img src="'+baseUrl+'/images/development-spinner.gif"/></div>');
	$('img',parent).tipTip({content: (message) ? message : 'please wait...'});
}

// generic function to refresh the button bar
function developmentBarAfter(element) {
	// refresh the development bar
	$.ajax({
		url: baseUrl + '/home/developmentBar',
		cache: false,
		success: function(html) {
			$('div.actions', $('div#development')).html(html);
			updateSound();
		}
	});
}

// update sound
function updateSound() {
	soundId = Math.floor(Math.random()*25);
	sound = new Audio('http://www.talkingwav.com/cartoon/cartoon_' + ((soundId<10) ? 0 : '') + soundId + '.wav')
}

// load mock data
function loadMockData(element) {
	developmentBarBefore(element,'loading mock data...');

	var a = $.ajax(
		'<g:createLink controller="bootStrap" action="index"/>',
		{
			async: false,
			headers: {
				'X-File-Name': 'a',
				'X-File-Id': 'b'
			},
			success: function(data) {
				developmentBarAfter(element);
			}
		}
	);
}

// delete all files
function deleteAllFiles(element) {
	developmentBarBefore(element,'please wait while all files are deleted...');

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
				var all = $('div.file',$('div.files',$('div.uploadr[name="uploadrArea"]'))).size();
				var shown = false;
				var done = false;

				// got files?
				if (all <= 0) {
					// no, call after function
					developmentBarAfter(element);
				}

				// hide files and show drop placeholder
				$('div.file',$('div.files',$('div.uploadr[name="uploadrArea"]'))).each(function() {
					var file = $(this);
					if (file.is(':visible')) {
						file.animate({height: '0px'}, 500, 'swing', function() {
							file.remove();
							visible--;
							all--;

							// no more visible files left?
							if (!shown && visible == 0) {
								// no, show placeholder
								shown = true;
								$('.placeholder',$('div.uploadr[name="uploadrArea"]')).show();
							}

							// are all files gone?
							if (all <= 0) {
								// yes, call after method
								developmentBarAfter(element);
							}
						});
					} else {
						file.remove();
						all--;

						// are all files gone?
						if (all <= 0) {
							// yes, call after method
							developmentBarAfter(element);
						}
					}
				});
			}
		}
	);
}
</r:script>