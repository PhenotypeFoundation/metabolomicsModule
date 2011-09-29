<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<body>

<g:link uri="/#"
		onclick="parseConfigurationDialog.dialog('close'); parseConfigurationDialog = openParseConfigurationDialog(${(dialogProperties + [dataType: 'clean'] + [buttons: ['save', 'close']] + [actionName: 'data'] + [refreshStudyList: true] + [title: 'Clean Data Interpretation']) as JSON});">
	Clean Data
</g:link>

<br>

<g:link uri="/#"
		onclick="parseConfigurationDialog.dialog('close'); parseConfigurationDialog = openParseConfigurationDialog(${(dialogProperties + [dataType: 'raw'] + [buttons: ['save', 'close']] + [actionName: 'data'] + [refreshStudyList: true] + [title: 'Raw Data Interpretation']) as JSON});">
	Raw Data
</g:link>

<br>

<g:link uri="/#"
		onclick="parseConfigurationDialog.dialog('close'); parseConfigurationDialog = openParseConfigurationDialog(${(dialogProperties + [buttons: ['save', 'close']] + [actionName: 'features'] + [title: 'Feature List']) as JSON});">
	Features
</g:link>

</body>
</html>