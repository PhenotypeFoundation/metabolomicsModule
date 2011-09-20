modules = {
	metabolomicsModule {
		dependsOn 'moduleBase, uploadr'

		resource id:'pc-js', url:[dir:'js', file:'parseConfiguration.js']
		resource id:'pc-css', url:[dir:'css', file:'parseConfiguration.css']
        resource id:'main-css', url:[dir:'css', file:'main.css']

		// add development resources?
		if (grails.util.GrailsUtil.environment == "development") {
			resource id:'dev-css', url:[dir:'css', file: 'development.css']
		}
	}
}