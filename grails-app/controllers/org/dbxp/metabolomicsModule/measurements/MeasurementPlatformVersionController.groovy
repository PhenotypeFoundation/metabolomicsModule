package org.dbxp.metabolomicsModule.measurements

class MeasurementPlatformVersionController {

	/*
	 * Metabolomics MeasurementPlatformVersion page
	 * 
	 * params.id is required to load the MeasurementPlatformVersion
	 */
	   def view = {
		if (!params.id) redirect(controller: 'home') // id of an MeasurementPlatformVersion must be present
		
		[ measurementPlatformVersion: MeasurementPlatformVersion.get(params.id) ]
   }
}
