package org.dbxp.metabolomicsModule

import org.dbxp.metabolomicsModule.measurements.MeasurementPlatform
import grails.converters.JSON

/**
 * This is a temporary controller, solely for development purposes. This is a place where you
 * can bootstrap some data without having to bootstrap data every time the application loads
 * via BootStrap.groovy. Especially handy since mongo data persists after application restart.
 */
class BootStrapController {

    def uploadedFileService
	def measurementService
	
	def index = {
		
		def assayYYY = MetabolomicsAssay.findByName("Assay Y")

		if (assayYYY) {

			/*
			 * Import example data for assay aaa
			 */
			def file            = new File('testData/assay_yyy_test_data.txt')
			def uploadedFile    = uploadedFileService.createUploadedFileFromFile(file, session.user)
				uploadedFile.parse([delimiter: '\t'])
				uploadedFile.assay = assayYYY
				uploadedFile.save()

			/*
			 * this will load example data into:
			 * - org.dbxp.metabolomicsModule.identity.Feature
			 * - org.dbxp.metabolomicsModule.measurements.MeasurementPlatform
			 * - org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion
			 * - org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersionFeature
			 */

			def mp = new MeasurementPlatform(name: "MP-AAA").save()

			file = new File('testData/feature_mock_data.xls')
			def uploadedFeatureFile = uploadedFileService.createUploadedFileFromFile(file, session.user)
				uploadedFeatureFile.parse()

			assayYYY.measurementPlatformVersion = measurementService.createMeasurementPlatformVersion(uploadedFeatureFile, [versionNumber: 0.1f, measurementPlatform: mp])
			assayYYY.measurementPlatformVersion.save(failOnError: true)
			assayYYY.save(failOnError: true)
		}

//		if (request.getHeader('Referer')) redirect(url: request.getHeader('Referer'))
//		else redirect(controller: 'home')
		render ([status: 200, message: "loaded mock data"] as JSON)
	}
}
