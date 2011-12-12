<html>
<head>
<meta name="layout" content="main"/>
<r:script>
$(document).ready(function() {
	$("#tabs").tabs({selected: "${selectedTab}"});
	$("#pageTitle").tipTip({
		content: 'assay token: ${assay.study.token()}',
		maxWidth: 'auto',
		edgeOffset: 10,
		defaultPosition: 'left'
	});
});
</r:script>
</head>

<body>

<div id="pageTitle" style="width:200px;"><h1>${assay.name}</h1></div>

<div id="tabs">
	<ul>
		<li><a href="#tabs-1">Features</a></li>
		<li><a href="#tabs-2">Samples</a></li>
		<li><a href="#tabs-3">Platforms</a></li>
		<li><a href="#tabs-4">Uploaded files</a></li>
	</ul>

	<div id="tabs-1">
		<mm:assayFeatureTables assay="${assay}"/>
	</div>

	<div id="tabs-2">
		<mm:sampleTable assay="${assay}"/>
	</div>

	<div id="tabs-3">
		<mm:assayPropertiesEditor assay="${assay}"/>
	</div>

	<div id="tabs-4">
		<div id="uploadedAssayFiles">
			<mm:uploadedFileList
				files="${assayFiles}"
				dialogProperties="${[title: 'Please choose the uploaded file data type', buttons: ['save', 'cancel'], assayId: id, controllerName: 'parseConfiguration', actionName: 'cleanData', refreshPageAfterClose: true, mmBaseUrl: grailsApplication.config.grails.serverURL, redirectUrl:resource('/assay/view/'+assay.id+'?selectedTab=3', absolute: true)]}"
				assay="${assay}"/>
		</div>
	</div>

</div>

</body>
</html>
