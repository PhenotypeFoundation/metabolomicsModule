package org.dbxp.metabolomicsModule

import org.dbxp.metabolomicsModule.identity.FeatureProperty;
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatform
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion

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
		
		def assayName 		= "Assay Y"
		def altAssayName 	= "Lipidomics profile after"
		def mpName 			= "MP-AAA"
		def mpvVersion 		= 0.1f
		
		def mpv
		def mp
		
		// determine Assay
		def assay = null
		if (!MetabolomicsAssay.findByName(assayName)){
			assay = MetabolomicsAssay.findByName(altAssayName)
		} else {
			assay = MetabolomicsAssay.findByName(assayName)
		}
		
		//add default feature property mappings
//		if (!FeatureProperty.findByLabel('m/z')) { new FeatureProperty(label: 'm/z', synonyms: 'mz,m z,m over z,Mass Quan#').save() }
//		if (!FeatureProperty.findByLabel('InChI')) { new FeatureProperty(label: 'InChI', synonyms: 'inchi,Inchi,inchie').save() }
//		if (!FeatureProperty.findByLabel('PubChem')) { new FeatureProperty(label: 'PubChem', synonyms: 'pubchem').save() }
//		if (!FeatureProperty.findByLabel('ChEBI ID')) { new FeatureProperty(label: 'ChEBI ID', synonyms: 'chebi,Chebi,chebi_id,ChEBI_ID').save() }
		
		if (assay != null) {
			
			log.info "Bootstrapping Assay: ${assay.name} for user: ${session.user}"
		
			if (!MeasurementPlatform.findByName(mpName)){

				// Upload features file 
				def uploadedFeatureFile = uploadedFileService.createUploadedFileFromFile(new File('testData/feature_mock_data.xls'), session.user)
					uploadedFeatureFile.parse()
				
				// setup the MeasurementPlatform and Version
				mp 	= new MeasurementPlatform(name: mpName).save()
				log.info "Created new MeasurementPlatform ${mpName}"
				mpv	= measurementService.createMeasurementPlatformVersion(uploadedFeatureFile, [versionNumber: mpvVersion, measurementPlatform: mp])
				log.info "Created new MeasurementPlatformVersion ${mpvVersion}"
				
				// only add data for asssay YYY
				if (assay.name == assayName){
					def uploadedFile    = uploadedFileService.createUploadedFileFromFile(new File('testData/assay_yyy_test_data.txt'), session.user)
					uploadedFile.parse([delimiter: '\t'])
					uploadedFile.assay = assay
					uploadedFile.save()
					log.info "Uploaded example dataset for assay ${assayName}"
				}
				
				assay.measurementPlatformVersion = mpv
				assay.save(failOnError: true)	
			} 
			
			log.info "Done bootstrapping"
		}

		render ([status: 200, message: "loaded mock data"] as JSON)
	}
}
