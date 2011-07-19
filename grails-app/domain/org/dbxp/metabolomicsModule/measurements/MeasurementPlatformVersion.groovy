package org.dbxp.metabolomicsModule.measurements

class MeasurementPlatformVersion {
	
	MeasurementPlatform measurementPlatform	
	Float versionnumber
	String changelist

    static constraints = {
		changelist(nullable: true)
    }
	
	/*
	* returns the object as a HashMap to be used in the API
	*/
	HashMap toApi() {
		return ['measurement_platform': this.measurementPlatform.toApi(), 'version_number': this.versionnumber, 'changelist': this.changelist ?: '']
	}
	
	/*******************************************************************************************************************************************************
	* Transients
	*/
	static transients = ['features']
	
	// fetch features linked to this version
	List getFeatures(){ return MeasurementPlatformVersionFeature.findAllByMeasurementPlatformVersion(this).collect { it.feature } }
	//******************************************************************************************************************************************************
}