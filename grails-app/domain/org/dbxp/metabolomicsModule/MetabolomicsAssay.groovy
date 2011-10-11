package org.dbxp.metabolomicsModule

import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion
import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.moduleBase.Assay

class MetabolomicsAssay extends Assay {
	
	// all Metabolomics Assay's will be linked to one measurementPlatformVersion
	MeasurementPlatformVersion measurementPlatformVersion
	String comments = ''

    static constraints = {
		measurementPlatformVersion(nullable: true)
    }
	
	static transients = ['uploadedfiles']
	
	def getUploadedfiles() {
		return UploadedFile.findAllByAssay(this)
	}
}
