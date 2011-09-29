package org.dbxp.metabolomicsModule

import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion
import org.dbxp.moduleBase.Assay

class MetabolomicsAssay extends Assay {
	
	// all Metabolomics Assay's will be linked to one measurementPlatformVersion
	MeasurementPlatformVersion measurementPlatformVersion

    static constraints = {
		measurementPlatformVersion(nullable: true)
    }
}
