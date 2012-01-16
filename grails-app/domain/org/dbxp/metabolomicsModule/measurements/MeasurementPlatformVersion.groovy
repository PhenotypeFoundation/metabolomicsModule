package org.dbxp.metabolomicsModule.measurements

import org.dbxp.metabolomicsModule.MetabolomicsAssay

class MeasurementPlatformVersion {
	
	MeasurementPlatform measurementPlatform	
	Float versionNumber
	String changeList

    static constraints = {
		changeList(nullable: true)
    }
	
	/**
	 * Returns the object as a HashMap to be used in the API
	 **/
	HashMap toApi() {
		return ['measurement_platform': this.measurementPlatform.toApi(), 'version_number': this.versionNumber, 'changelist': this.changeList ?: '']
	}
	
	/**
	 * Transients
	 **/
	static transients = ['features', 'assays']
	
	// fetch features linked to this version
    List getFeatures(){ return MeasurementPlatformVersionFeature.findAllByMeasurementPlatformVersion(this) }
	
	// fetch assays linked to this version
	List getAssays(){ return MetabolomicsAssay.findAllByMeasurementPlatformVersion(this) }
}