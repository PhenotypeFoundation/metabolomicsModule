package org.dbxp.metabolomicsModule.measurements

import org.dbxp.metabolomicsModule.identity.Feature

class MeasurementPlatformVersionFeature {
	
	MeasurementPlatformVersion measurementPlatformVersion
	Feature feature
	HashMap props

    static constraints = {
		props(nullable: true)
    }
}
