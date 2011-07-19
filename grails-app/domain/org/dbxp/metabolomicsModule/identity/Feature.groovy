package org.dbxp.metabolomicsModule.identity

import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersionFeature

class Feature {
	
	String label
	HashMap props

    static constraints = {
		props(nullable: true)
    }
	
	/*
	 * returns the object as a HashMap to be used in the API
	 */
	HashMap toApi() {
		return ['label': this.label, 'properties': this.props]
	} 
	
	/*******************************************************************************************************************************************************
	 * Transients
	 */
	static transients = ['platforms', 'platformVersions', 'platformVersionFeatureProperties']
	
	//platforms this feature is linked to
	List getPlatforms(){ return MeasurementPlatformVersionFeature.findAllByFeature(this)?.collect { it?.measurementPlatformVersion?.measurementPlatform } }
	
	//platforms versions this feature is linked to
	List getPlatformVersions(){ return MeasurementPlatformVersionFeature.findAllByFeature(this)?.collect { it?.measurementPlatformVersion } }
   
   	//platforms version properties of this feature
	List getPlatformVersionFeatureProperties(){ return MeasurementPlatformVersionFeature.findAllByFeature(this)?.collect { it } }
	
	//******************************************************************************************************************************************************
}
