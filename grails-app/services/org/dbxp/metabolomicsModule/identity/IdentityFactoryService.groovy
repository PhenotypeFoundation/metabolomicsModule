package org.dbxp.metabolomicsModule.identity

class IdentityFactoryService {

    static transactional = true

	/*
	 * return a single feature by the provided label
	 * optionally arguments can be added to tweak the ouput
	 */
    def featureFromLabel(String label, args = ['eager': false]) {
		
		def returnValue = [:]
		
		def feature = Feature.findByLabel(label)		
		
		if (feature) {		
			
			//init and add feature to returnValue
			returnValue["${feature.label}"] = []
			returnValue["${feature.label}"].add(['feature': feature.toApi()])
						
			if (args['eager'] == true) { //show all details 
				
				// add a list of platforms
				returnValue["${feature.label}"].add(
					['platforms': feature.platforms.collect { it.toApi() }
				])
				
				// add a list of platform versions
				returnValue["${feature.label}"].add(
					['platformversions': feature.platformVersions.collect { it.toApi() }
				])
				
				// add a list of platforms feature properties
				returnValue["${feature.label}"].add(
					['platformversionfeatureproperties': feature.platformVersionFeatureProperties.collect { it.toApi() }
				])		
			}
    	} else {
			returnValue = ["${label}": []] //only return the label as passed to the function
		}

		return returnValue
    }
	
	/*
	 * returns all features (List of featuresFromLabel)
	 */
	List featuresAsList(){
		return Feature.list().collect { this.featureFromLabel(it.label) } as List
	}
}