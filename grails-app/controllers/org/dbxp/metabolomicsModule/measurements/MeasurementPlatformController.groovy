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
		
		if (params.edit && params.measurementPlatformName){
			measurementPlatform.name 		= params.measurementPlatformName?.trim()
			measurementPlatform.description = params.measurementPlatformDescription

			if (!measurementPlatform.save()){
				flash.message = 'unable to save, make sure the name is set and unique!'
			} else {
				params.edit = false
			}
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

		if ((params.measurementplatform).trim().size() > 0){
			if (!MeasurementPlatform.findByName(params.measurementplatform as String)){
				new MeasurementPlatform(name: params.measurementplatform).save()
			}
		}

		render mm.measurementPlatformOverview(measurementPlatforms: MeasurementPlatform.list())
	}
}
