<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>

<g:if test="${movedFileMessage}">
<mm:notification>${movedFileMessage}</mm:notification>
</g:if>

<div id="uploadedFiles">
	<g:render template="uploadedFileList" />
</div>
<div id="studyOverview">
	<mm:studyList highlightedAssay="${highlightedAssay}" />
</div>
<g:if test="${((ConfigurationHolder.config.development.bar).contains(grails.util.GrailsUtil.environment))}"><g:render template="development"/></g:if>
</body>
</html>