<html>
<head>
	<meta name="layout" content="main"/>
</head>

<body>
<h1>Measurement Platform Version Feature</h1>

<h2>${mpvfeature?.feature.label}</h2>

<g:if test="${mpvfeatureProps}">
	<h2>Feature properties</h2>
	<g:each in="${mpvfeatureProps}" var="prop">
		${prop.value} (${prop.key}),
	</g:each>
	...
</g:if>

<g:if test="${uniqueMappings}">
	<h2>Feature property mappings (unique)</h2>
	<g:each in="${uniqueMappings.sort { a,b -> b.value.source <=> a.value.source}}" var="propertyMapping">
		${propertyMapping.value.view} (${propertyMapping.value.source})<br />
	</g:each>
</g:if>

<g:if test="${mpvfeatureMappingsByProperty}">
	<h2>Feature property mappings (by property)</h2>
	
	<g:each in="${mpvfeatureMappingsByProperty}" var="propertyMapping">
		<g:if test="${propertyMapping.value}">
			<p>
				<b>${propertyMapping.key}</b><br />
				<g:each in="${propertyMapping.value}" var="propertyMappingDetails">
					${propertyMappingDetails.value} (${propertyMappingDetails.key}),
				</g:each>
				...<br />
			</p>
		</g:if>
	</g:each>	
</g:if>
</body>
</html>