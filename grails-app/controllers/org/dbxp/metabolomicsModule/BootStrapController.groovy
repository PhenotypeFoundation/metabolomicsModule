package org.dbxp.metabolomicsModule

import org.dbxp.dbxpModuleStorage.AssayWithUploadedFile
import org.dbxp.metabolomicsModule.identity.Feature
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatform
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersionFeature
import org.dbxp.moduleBase.Auth
import org.dbxp.moduleBase.Study
import org.dbxp.moduleBase.User

/**
 * This is a temporary controller, solely for development purposes. This is a place where you
 * can bootstrap some data without having to bootstrap data every time the application loads
 * via BootStrap.groovy. Especially handy since mongo data persists after application restart.
 */
class BootStrapController {

    def uploadedFileService
    def parsedFileService

    def index = { render 'index' }

    def loadSmallDataSet = {

        def file            = new File('testData/DiogenesMockData_mini.txt')
        def uploadedFile    = uploadedFileService.createUploadedFileFromFile(file)

        uploadedFile.parse([delimiter: '\t'])

        uploadedFile.save()

        render 'done loading mock data'

    }
	
	def loadMP = {
		
		/*
		 * this will load example data into:
		 * - org.dbxp.metabolomicsModule.identity.Feature
		 * - org.dbxp.metabolomicsModule.measurements.MeasurementPlatform
		 * - org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion
		 * - org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersionFeature
		 */

		def rand  = new Random() //very handy to generate fake data
		
		// setup features (including feature specific properties)
		def f0 = new Feature(label: "PA(12:0/13:0)", props: ['mass':'567.39','formula':'C28H58NO8P', 'class':'GP10']).save()
		def f1 = new Feature(label: "PA(16:0/18:1(9Z))", props: ['mass':'691.52','formula':'C37H71O8P', 'class':'GP10']).save()
		def f2 = new Feature(label: "PA(17:0/14:1(9Z))", props: ['mass':'649.47','formula':'C34H68NO8P', 'class':'GP10']).save()
		def f3 = new Feature(label: "PA(6:0/6:0)", props: ['mass':'368.16','formula':'C15H29O8P', 'class':'GP10']).save()
		def f4 = new Feature(label: "PA(8:0/8:0)", props: ['mass':'424.22','formula':'C19H37O8P', 'class':'GP10']).save()
		def f5 = new Feature(label: "PA(10:0/10:0)", props: ['mass':'424.22','formula':'C19H37O8P', 'class':'GP10']).save()
		def f6 = new Feature(label: "PA(18:0/18:0)", props: ['mass':'704.54','formula':'C39H77O8P', 'class':'GP10']).save()
		def f7 = new Feature(label: "PA(12:0/12:0)", props: ['mass':'536.35','formula':'C27H53O8P', 'class':'GP10']).save()
		def f8 = new Feature(label: "PA(12:0/15:0)", props: ['mass':'578.39','formula':'C30H59O8P', 'class':'GP10']).save()
		def f9 = new Feature(label: "PA(12:0/18:2(9Z,12Z))", props: ['mass':'616.41','formula':'C33H61O8P', 'class':'GP10']).save()

		// setup measurement platform
		3.times { // create MeasurementPlatforms
			def mp = new MeasurementPlatform(name: "MP-" + (1 + rand.nextInt(100000))).save()
			
			rand.nextInt(3).times { // create MeasurementPlatformVersions
				def mpv = new MeasurementPlatformVersion(measurementPlatform: mp, versionnumber: it + 1 as Float).save()
				
				// setup features (only platform version specific feature properties)
				Feature.list().each { feature ->
					new MeasurementPlatformVersionFeature(
							measurementPlatformVersion: mpv,
							feature: feature,
							props: [
								'rt'	: (((1 + rand.nextInt(60*60)) * 0.2672) as Float),	// time it comes from the column
								'is_id'	: 'IS-' + (1 + rand.nextInt(40)) as String 			// internal standard used to correct
							]
						).save()
				}
			}
		}

		render 'done loading mock data'
	}

    def createStudy = {

        def rand = new Random()
        def intLimit = 1000000

        def user = new User(
                identifier:         rand.nextInt(intLimit),
                username:           'userName' + rand.nextInt(intLimit),
                isAdministrator:    false
        ).save()

        def study = new Study(
                studyToken: 'token' + rand.nextInt(intLimit),
                name:       'name'  + rand.nextInt(intLimit),
        )

        def auth = new Auth(
                canRead:    true,
                canWrite:   true,
                isOwner:    true
        )

        study.addToAuth(auth)
        user.addToAuth(auth)

        def assay = new AssayWithUploadedFile(
                assayToken: 'token' + rand.nextInt(intLimit),
                name:       'name'  + rand.nextInt(intLimit)
        )

        study.addToAssays(assay)

        rand.nextInt(10).times{
            assay.addToSamples(
                    sampleToken:    'token'     + it,
                    name:           'name'      + it,
                    subject:        'subject'   + it,
                    event:          'event'     + it
            )
        }

        study.save()

        render 'done'

    }
}
