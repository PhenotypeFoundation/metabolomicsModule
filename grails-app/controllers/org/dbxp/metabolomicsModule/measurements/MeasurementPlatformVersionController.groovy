package org.dbxp.metabolomicsModule.measurements

class MeasurementPlatformVersionController {

    def index = { redirect(url: request.getHeader('Referer')) }
	
	/*
	* Metabolomics MeasurementPlatformVersion page
	*
	* params.id is required to load the MeasurementPlatformVersion
	*/
   def view = {
		if (!params.id) redirect(url: request.getHeader('Referer')) // id of an MeasurementPlatformVersion must be present
		
		[ measurementPlatformVersion: MeasurementPlatformVersion.get(params.id) ]
   }
}
