<html>
<head>
  <meta name="layout" content="main" />
</head>
	<body>		 
		<h1>Measurement Platform</h1>	
		
		<h2>${measurementPlatform?.name}</h2>
		
  		<g:if test="${measurementPlatform.description}">
  			<p>${measurementPlatform.description}</p>  			
  		</g:if>		
		
		<ul>
			<g:each in="${measurementPlatform.versions}" var="measurementPlatformVersion">
				<li>
					<g:link controller="measurementPlatformVersion" action="view" id="${measurementPlatformVersion.id}">
						${measurementPlatform?.name} ${measurementPlatformVersion.versionNumber}
					</g:link>
				</li>
			</g:each>				
		</ul>			
	</body>
</html>  	