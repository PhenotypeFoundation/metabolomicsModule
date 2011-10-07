<html>
<head>
	<meta name="layout" content="main"/>
</head>

<body>
<h1>${assay.name} <small>(${assay.study.token()})</small></h1>

<mm:assayPlatformChooser assay="${assay}" />

<div id="uploadedAssayFiles">
	<mm:uploadedFileList files="${assayFiles}" dialogProperties="${[title: 'Please choose the uploaded file data type', buttons: ['save', 'close'], assayId: id, controllerName: 'parseConfiguration', actionName: 'data']}"/>
</div>

%{--${assayFeatures}--}%

<div id=dataVersusFeatures>
<h2>Data vs. Features</h2>
<g:each in="${assayFiles}" var="assayFile">

    <table>
        <thead>
        <tr>
            <th>Feature</th>
            <th>Properties</th>
        </tr>

        </thead>
        <tbody>

        <g:each in="${assayFile.dataColumnHeaders}" var="dataColumnHeader">
            <tr>
                <td>
                    ${dataColumnHeader}
                </td>
                <td>
                    ${(dataColumnHeader in assayFeatures.keySet()) ? assayFeatures[dataColumnHeader] : ''}
                </td>
            </tr>
        </g:each>

        </tbody>
    
    </table>

</g:each>
</div>
</body>
</html>  