<html>
<head>
  <meta name="layout" content="main" />
</head>
	<body> 
		<g:each in="${measurementPlatforms}" var="measurementPlatform">
			<g:link controller="measurementPlatform" action="view" id="${measurementPlatform.id}">${measurementPlatform.name}</g:link><br />
		</g:each>
	</body>
</html>