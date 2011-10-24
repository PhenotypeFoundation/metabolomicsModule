<html>
<head>
  <meta name="layout" content="main" />
</head>
	<body>	
		<h1>Measurement Platform Version</h1>	
		
		<h2>${measurementPlatformVersion?.measurementPlatform?.name} (${measurementPlatformVersion?.versionNumber})</h2>
		
		<g:if test="${measurementPlatformVersion?.features}">
			<h2>Features</h2>
			<ul>		
				<g:each in="${measurementPlatformVersion?.features}" var="feature">
					<li>${feature.props}</li>
				</g:each>
			</ul>
		</g:if>		
	</body>
</html>  	