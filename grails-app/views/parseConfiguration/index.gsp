<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<body>

<mm:dataTypeOption dialogProperties="${(dialogProperties + [dataType: 'clean', actionName: 'cleanData', title: 'Clean Data Interpretation']) as JSON}">
	<div class="dataTypeOptionText">
		<b>Clean Data</b><br />
		Metabolomics data without quality control samples.
	</div>
	<img class="dataTypeImage" src="images/filetype_clean.png" alt="Clean Data Files">
</mm:dataTypeOption>

<mm:dataTypeOption dialogProperties="${(dialogProperties + [dataType: 'raw', actionName: 'rawData', title: 'Raw Data Interpretation']) as JSON}">
	<div class="dataTypeOptionText">
		<b>Raw Data</b><br />
		Metabolomics data including quality control samples.
	</div>
	<img class="dataTypeImage" src="images/filetype_raw.png" alt="Raw Data Files">
</mm:dataTypeOption>

<mm:dataTypeOption dialogProperties="${(dialogProperties + [actionName: 'features', title: 'Feature List']) as JSON}">
	<div class="dataTypeOptionText">
		<b>Feature List</b><br />
		A list of platform features describing the measured compounds. Connect this file to an assay to link the assay's associated data files with the features from this file.
	</div>
	<img class="dataTypeImage" src="images/filetype_features.png" alt="Feature Files">
</mm:dataTypeOption>

</body>
</html>