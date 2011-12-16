package org.dbxp.metabolomicsModule.measurements

import org.dbxp.metabolomicsModule.identity.Feature

class MeasurementPlatformVersionFeature {

	def FeaturePropertyService

	MeasurementPlatformVersion measurementPlatformVersion
	Feature feature
	HashMap props

	static constraints = { props(nullable: true) }

	/**
	 * returns the object as a HashMap to be used in the API
	 */
	HashMap toApi() {
		return ['measurement_platform_version': this.measurementPlatformVersion.toApi(), 'feature': this.feature.toApi(), 'properties': this.props ?: [:]]
	}

	/**
	 * returns similar identifiers
	 * 
	 * @param mappingService
	 * @return HashMap
	 */
	HashMap propertyMappings(String mappingService) {

		def mappings = [:]

		this.props.each { mpvfproperty ->
			if (mpvfproperty.key){
				mappings[["${mpvfproperty.key}": "${mpvfproperty.value}"]] = FeaturePropertyService.propertyMapping(mpvfproperty.key, mpvfproperty.value as String, mappingService)
			}
		}

		return mappings
	}
}
