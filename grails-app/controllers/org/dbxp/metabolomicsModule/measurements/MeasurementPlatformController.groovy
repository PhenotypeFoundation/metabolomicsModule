package org.dbxp.metabolomicsModule.measurements

class MeasurementPlatformController {

    def index = { redirect(action: 'list') }
	
	/*
	* Metabolomics MeasurementPlatform page
	*
	* params.id is required to load the MeasurementPlatform
	*/
   def view = {
	   
		if (!params.id) redirect(action: 'list')
		
		[ measurementPlatform: MeasurementPlatform.get(params.id) ]
   }
   
   def list = {

	   /**
	    * List all accessible MeasurementPlatforms
	    */
	   
	   [ measurementPlatforms: MeasurementPlatform.list() ]
   }
}
