<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
<div id="uploadedFiles">
	<mm:uploadedFileList files="${files}" dialogProperties="[title: 'Please choose the uploaded file data type', buttons: ['close']]"/>
</div>
<div id="studyOverview">
	<g:render template="studyList"/>
</div>
<g:if test="${((ConfigurationHolder.config.development.bar).contains(grails.util.GrailsUtil.environment))}"><g:render template="development"/></g:if>
</body>
</html>