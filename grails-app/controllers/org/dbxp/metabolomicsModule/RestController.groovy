package org.dbxp.metabolomicsModule

import grails.converters.JSON

class RestController extends org.dbxp.dbxpModuleStorage.RestController {

    def getMeasurementMetaData = {
	
		def resp = []
		
		if (params.assayToken){
			def assay = MetabolomicsAssay.findByAssayToken(params.assayToken as String)

			def requestedMeasurementTokens  = params.measurementToken instanceof String ? [params.measurementToken] : params.measurementToken
			def measurementTokens 			= assay?.measurementPlatformVersion?.features

			if (requestedMeasurementTokens) {
        		measurementTokens           = measurementTokens.findAll { it in requestedMeasurementTokens }
			}

			measurementTokens.each {

				def featureMap = [name: it.feature.label]
				resp += featureMap + (it?.props ?: [])
			}
		}

		render resp as JSON	
	}
}
