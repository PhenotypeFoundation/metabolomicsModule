modules = {
	metabolomicsModule {
		dependsOn 'moduleBase, uploadr'

		resource id:'js', url:[dir:'js', file:'parseConfiguration.js']
		resource id:'css', url:[dir:'css', file:'parseConfiguration.css']
	}
}