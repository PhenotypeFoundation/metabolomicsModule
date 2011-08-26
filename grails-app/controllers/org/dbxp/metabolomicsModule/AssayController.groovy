package org.dbxp.metabolomicsModule

import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatform
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion

class AssayController {
	
	/*
	 * embed required service(s)
	 */
	def assayService

	
    def index = { redirect(url: request.getHeader('Referer')) }
	
	/*
	 * Metabolomics Assay page
	 * - list basic assay info (name, members etc)
	 * - list files related to this assay
	 * 
	 * params.id is required to load the assay
	 */
	def view = {
		
		if (!params.id) redirect(url: request.getHeader('Referer')) // id of an assay must be present
				
		// load assay from id (for session.user)
		def assay = assayService.getAssayReadableByUserById(session.user, params.id)
		
		def assayFiles = UploadedFile.findAllByAssay(assay) ?: []
		
		def measurementPlatformVersions = []
		def measurementPlatformVersionUploadedFiles = [:]
		assayFiles.each{ assayFile ->
			
			// get MeasurementPlatformVersion from AssayFile
			def mpv = MeasurementPlatformVersion.get((Long) assayFile['platformVersionId'])
			
			// add MeasurementPlatformVersion to List
			measurementPlatformVersions.add(mpv)
			
			// prepare a Map with MeasuremtentPlatformVersions and their linked files
			if (!measurementPlatformVersionUploadedFiles[mpv.id]) { measurementPlatformVersionUploadedFiles[mpv.id] = [] }
			measurementPlatformVersionUploadedFiles[mpv.id] << assayFile
		}
				
		[assay: assay, assayFiles: assayFiles, measurementPlatformVersions: measurementPlatformVersions, measurementPlatformVersionUploadedFiles: measurementPlatformVersionUploadedFiles]
	}
}
