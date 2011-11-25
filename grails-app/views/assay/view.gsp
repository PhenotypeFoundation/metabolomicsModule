<html>
<head>
	<meta name="layout" content="main"/>
</head>

<body>
<h1>${assay.name} <small>(${assay.study.token()})</small></h1>
<mm:assayPropertiesEditor assay="${assay}"/>

<div id="uploadedAssayFiles">
	<mm:uploadedFileList
		files="${assayFiles}"
		dialogProperties="${[title: 'Please choose the uploaded file data type', buttons: ['save', 'cancel'], assayId: id, controllerName: 'parseConfiguration', actionName: 'data', refreshPageAfterClose: true, mmBaseUrl: grailsApplication.config.grails.serverURL, redirectUrl:resource('/assay/view/'+assay.id, absolute: true)]}"
		assay="${assay}"/>
</div>

<mm:assayFeatureTables assay="${assay}"/>
<mm:sampleTable assay="${assay}"/>
</body>
</html>