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
	
	def delete = {
		if (!params.id) redirect(controller: 'home') // id of an MeasurementPlatformVersion must be present
		
		def mpv = MeasurementPlatformVersion.get(params.id)
		
		try {
			mpv.features.each { it.delete() } //delete all entries from the intermediate table that links features to a mpv
			mpv.delete()
		} catch (e) {
			log.error (e)
		}
		
		redirect(controller: 'measurementPlatform')
	}
}
