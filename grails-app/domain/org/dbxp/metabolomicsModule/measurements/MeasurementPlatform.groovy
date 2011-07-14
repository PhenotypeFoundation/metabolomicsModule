package org.dbxp.metabolomicsModule.measurements

class MeasurementPlatform {
	
	String name
	String description

    static constraints = {
		description(nullable: true)
    }
}