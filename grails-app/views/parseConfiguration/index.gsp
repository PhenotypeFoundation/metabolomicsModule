<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<body>

<mm:dataTypeOption dialogProperties="${(dialogProperties + [dataType: 'clean', actionName: 'data', title: 'Clean Data Interpretation']) as JSON}">
	<b>Clean Data</b><br />
	Metabolomics data without quality control samples.
</mm:dataTypeOption>

<mm:dataTypeOption dialogProperties="${(dialogProperties + [dataType: 'raw', actionName: 'data', title: 'Raw Data Interpretation']) as JSON}">
	<b>Raw Data</b><br />
	Metabolomics data including quality control samples.
</mm:dataTypeOption>

<mm:dataTypeOption dialogProperties="${(dialogProperties + [actionName: 'features', title: 'Feature List']) as JSON}">
	<b>Feature List</b><br />
	A list of platform features describing the measured compounds. Connect this file to an assay to link the assay's associated data files with the features from this file.
</mm:dataTypeOption>

</body>
</html>