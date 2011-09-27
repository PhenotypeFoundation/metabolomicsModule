package org.dbxp.metabolomicsModule.measurements

class MeasurementPlatformController {

    def index = { redirect(url: request.getHeader('Referer')) }
	
	/*
	* Metabolomics MeasurementPlatform page
	*
	* params.id is required to load the MeasurementPlatform
	*/
   def view = {
	   
		if (!params.id) redirect(url: request.getHeader('Referer')) // id of an MeasurementPlatform must be present
		
		[ measurementPlatform: MeasurementPlatform.get(params.id) ]
   }
   
   def list = {

	   /**
	    * List all accessible MeasurementPlatforms
	    */
	   
	   [ measurementPlatforms: MeasurementPlatform.list() ]
   }
}
