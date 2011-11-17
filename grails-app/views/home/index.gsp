<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>

<g:if test="${movedFileMessage}">
<mm:notification>${movedFileMessage}</mm:notification>
</g:if>

<div id="studyOverview">
	<mm:studyList highlightedAssay="${highlightedAssay}" />
</div>
<div id="uploadedFiles">
	<mm:uploadedFileList files="${files}" dialogProperties="[title: 'Please choose the uploaded file data type', buttons: ['close'], controllerName: 'parseConfiguration', actionName: 'index', mmBaseUrl: grailsApplication.config.grails.serverURL]"/>
</div>
<g:if test="${((ConfigurationHolder.config.development.bar).contains(grails.util.GrailsUtil.environment))}"><g:render template="development"/></g:if>
</body>
</html>