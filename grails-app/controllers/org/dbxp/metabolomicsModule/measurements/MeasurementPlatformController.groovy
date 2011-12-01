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
		
		def measurementPlatform = MeasurementPlatform.get(params.id)
		
		if (params.measurementPlatformName) { 
			measurementPlatform.name 		= params.measurementPlatformName
			measurementPlatform.description = params.measurementPlatformDescription
			measurementPlatform.save()
			params.edit = false
		} 

		[ measurementPlatform: measurementPlatform ]
	}

	def list = {

		/**
		 * List all accessible MeasurementPlatforms
		 */

		[ measurementPlatforms: MeasurementPlatform.list() ]
	}

	def add = {

		if (!MeasurementPlatform.findByName(params.measurementplatform as String)){
			new MeasurementPlatform(name: params.measurementplatform).save()
		}
		
		render mm.measurementPlatformOverview(measurementPlatforms: MeasurementPlatform.list())
	}
}
