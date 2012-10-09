<g:applyLayout name="module">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="chrome=1"/>
		<meta app-name="${meta(name: 'app.name')} "/>
		<meta app-version="${meta(name: 'app.version')} "/>
		<meta app-revision="${meta(name: 'app.build.svn.revision')}"/>
		<title><g:layoutTitle default="Metabolomics Module"/></title>
		<g:layoutHead/>
		<r:require modules="metabolomicsModule"/>
	</head>

	<body>
	<content tag="topnav">
		<!-- Insert only li tags for the top navigation, without surrounding ul -->
		<li><a href="${grailsApplication.config.grails.serverURL}">Home</a></li>
		<li>
			<a href="#" onClick="return false;">GSCF</a>
			<ul class="subnav">
				<li>
					<g:link url="${org.codehaus.groovy.grails.commons.ConfigurationHolder.config.gscf.baseURL}">
						Go to GSCF
					</g:link>
				</li>
				<li><g:link controller="home" action="sync">Sync</g:link></li>
			</ul>
		</li>
		<li>
			<g:link controller="measurementPlatform" action="list">Measurement Platforms</g:link>
		</li>
	</content>
	<g:layoutBody/>

		<trackr:track reference="${session.user ?: 'unknown'}"/>
	</body>
</html>
</g:applyLayout>
