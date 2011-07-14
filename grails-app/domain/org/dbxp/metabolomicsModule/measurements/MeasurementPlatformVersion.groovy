package org.dbxp.metabolomicsModule.measurements

class MeasurementPlatformVersion {
	
	MeasurementPlatform measurementPlatform	
	Float versionnumber
	String changelist

    static constraints = {
		changelist(nullable: true)
    }
}