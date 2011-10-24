<html>
<head>
	<meta name="layout" content="main"/>
</head>

<body>
<h1>${assay.name} <small>(${assay.study.token()})</small></h1>

<mm:assayPlatformChooser assay="${assay}" />
<mm:commentFieldEditor assay="${assay}" />

<div id="uploadedAssayFiles">
	<mm:uploadedFileList
		files="${assayFiles}"
		dialogProperties="${[title: 'Please choose the uploaded file data type', buttons: ['save', 'close'], assayId: id, controllerName: 'parseConfiguration', actionName: 'data', refreshPageAfterClose: true, mmBaseUrl: resource('/', absolute: true), redirectUrl:resource('/assay/view/'+assay.id, absolute: true)]}"
		assay="${assay}" />
</div>

<div id=dataVersusFeatures>
<h2>Data vs. Features</h2>
<mm:assayFeatureTables assay="${assay}" />
</div>
</body>
</html>