package org.dbxp.metabolomicsModule

import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatform
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion

class AssayController {
	
	/*
	 * embed required service(s)
	 */
	def assayService

	
	/*
	 * Metabolomics Assay page
	 * - list basic assay info (name, members etc)
	 * - list files related to this assay
	 * 
	 * params.id is required to load the assay
	 */
	def view = {
		if (!params.id) response.sendError(400, "No assay id specified.") // id of an assay must be present
		
		// load assay from id (for session.user)
		def assay = assayService.getAssayReadableByUserById(session.user, params.id as Long)

        if (!assay) response.sendError(404, "No assay found with id $params.id")
		
		def assayFiles = UploadedFile.findAllByAssay(assay)
		
		def measurementPlatformVersions = []
		def measurementPlatformVersionUploadedFiles = [:]
		assayFiles.each{ assayFile ->
			
			// get MeasurementPlatformVersion from AssayFile
			def mpv = MeasurementPlatformVersion.get((Long) assayFile['platformVersionId'])

            if (mpv) {
                // add MeasurementPlatformVersion to List
                measurementPlatformVersions.add(mpv)

                // prepare a Map with MeasuremtentPlatformVersions and their linked files
                if (!measurementPlatformVersionUploadedFiles[mpv.id]) { measurementPlatformVersionUploadedFiles[mpv.id] = [] }
                measurementPlatformVersionUploadedFiles[mpv.id] << assayFile
            }
		}
				
		[	assay: assay,
			assayFiles: assayFiles,
			measurementPlatformVersions: measurementPlatformVersions,
			measurementPlatformVersionUploadedFiles: measurementPlatformVersionUploadedFiles]
	}
}
