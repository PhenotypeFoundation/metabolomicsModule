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
		
		def measurementPlatformName = params.measurementplatform.trim() as String

		if (measurementPlatformName.size() > 0){
			if (!MeasurementPlatform.findByName(measurementPlatformName)){
				new MeasurementPlatform(name: measurementPlatformName).save()
			}
		}

		render mm.measurementPlatformOverview(measurementPlatforms: MeasurementPlatform.list())
	}

	def delete = {

		def measurementPlatform = MeasurementPlatform.get(params.id)

		if (measurementPlatform.versions) {
			flash.errorMessage = "Could not delete measurement platform because it is not empty."
		} else {
			measurementPlatform.delete(flush: true)
		}
		
		redirect(action: 'list')
	}
}
