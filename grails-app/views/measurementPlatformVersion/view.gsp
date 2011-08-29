<html>
<head>
  <meta name="layout" content="main" />
</head>
	<body>		 
		${measurementPlatformVersion?.measurementPlatform?.name} (${measurementPlatformVersion?.versionNumber})
		
		<h2>Features</h2>
		<g:each in="${measurementPlatformVersion?.features}" var="feature">
			${feature.label}<br />
		</g:each>		
	</body>
</html>  	