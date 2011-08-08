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
        def uploadedFile    = uploadedFileService.createUploadedFileFromFile(file, null)

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
		def f0 = new Feature(label: "PA(12:0/13:0)", props: ['pubchem_sid':'4266297','hmdb_id':'','lipidmaps_id':'LMGP10010001']).save()
		def f1 = new Feature(label: "PA(16:0/18:1(9Z))", props: ['pubchem_sid':'4266298','hmdb_id':'HMDB07859','lipidmaps_id':'LMGP10010002']).save()
		def f2 = new Feature(label: "PA(17:0/14:1(9Z))", props: ['pubchem_sid':'4266302','hmdb_id':'','lipidmaps_id':'LMGP10010006']).save()
		def f3 = new Feature(label: "PA(6:0/6:0)", props: ['pubchem_sid':'14714439','hmdb_id':'','lipidmaps_id':'LMGP10010020']).save()
		def f4 = new Feature(label: "PA(8:0/8:0)", props: ['pubchem_sid':'14714440','hmdb_id':'','lipidmaps_id':'LMGP10010021']).save()
		def f5 = new Feature(label: "PA(10:0/10:0)", props: ['pubchem_sid':'14714441','hmdb_id':'','lipidmaps_id':'LMGP10010022']).save()
		def f6 = new Feature(label: "PA(18:0/18:0)", props: ['pubchem_sid':'14714447','hmdb_id':'','lipidmaps_id':'LMGP10010028']).save()
		def f7 = new Feature(label: "PA(12:0/12:0)", props: ['pubchem_sid':'14714449','hmdb_id':'','lipidmaps_id':'LMGP10010030']).save()
		def f8 = new Feature(label: "PA(12:0/15:0)", props: ['pubchem_sid':'123066206','hmdb_id':'','lipidmaps_id':'LMGP10010045']).save()
		def f9 = new Feature(label: "PA(12:0/18:2(9Z,12Z))", props: ['pubchem_sid':'123066212','hmdb_id':'','lipidmaps_id':'LMGP10010051']).save()

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
								'mass'	: (((1 + rand.nextInt(1000)) * 0.2627) as Float),	// mass
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

        session.user = user

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
