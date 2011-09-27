<html>
<head>
  <meta name="layout" content="main" />
</head>
	<body>	
		<h1>Measurement Platform Version</h1>	
		
		<h2>${measurementPlatformVersion?.measurementPlatform?.name} (${measurementPlatformVersion?.versionNumber})</h2>	
	
		<h2>
			<g:if test="${measurementPlatformVersion?.features}">
				Update existing
			</g:if>
			<g:else>
				Upload new
			</g:else>
			Features
		</h2>
		<g:uploadForm controller="measurementPlatformVersion" action="featureFile" name="featureUpload">
			<input type="hidden" name="mpv" value="${measurementPlatformVersion.id}" />
   			<g:message code="mpv.feature.uploadfile.label" /> : <input type="file" name="featuresfile" />
   			<g:submitButton name="submit" value="submit" />
		</g:uploadForm>						
		
		<g:if test="${measurementPlatformVersion?.features}">
			<h2>Features</h2>
			<ul>		
				<g:each in="${measurementPlatformVersion?.features}" var="feature">
					<li>${feature.label} ${feature.props}</li>
				</g:each>
			</ul>
		</g:if>		
	</body>
</html>  	