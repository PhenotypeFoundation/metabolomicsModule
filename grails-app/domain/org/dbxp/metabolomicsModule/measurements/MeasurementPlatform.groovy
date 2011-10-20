package org.dbxp.metabolomicsModule.measurements

class MeasurementPlatform {
	
	String name
	String description

    static constraints = {
		description(nullable: true)
    }
	
	/*
	 * returns the object as a HashMap to be used in the API
	 */
	HashMap toApi() {
		return ['name': this.name, 'description': this.description ?: '']
	}

	/*******************************************************************************************************************************************************
	* Transients
	*/
	static transients = ['versions']
	
	// fetch versions of this platform
	List getVersions(){ return MeasurementPlatformVersion.findAllByMeasurementPlatform(this) as List }
	//******************************************************************************************************************************************************
}