package org.dbxp.metabolomicsModule

import grails.converters.JSON

class RestController extends org.dbxp.dbxpModuleStorage.RestController {

    def getMeasurementMetaData = {
	
		def resp = []
		
		if (params.assayToken){
			def assay = MetabolomicsAssay.findByAssayToken(params.assayToken as String)
						
			def requestedMeasurementTokens  = params.measurementToken instanceof String ? [params.measurementToken] : params.measurementToken
			def measurementTokens 			= assay?.measurementPlatformVersion?.features

			assay?.measurementPlatformVersion?.features?.each { mpvf ->

				if (!params.measurementToken || mpvf.feature.label in requestedMeasurementTokens){
					def featureMap = [name: mpvf.feature.label]
					resp += featureMap + (mpvf.props ?: [])
				}
			}
		}

		render resp as JSON	
	}
}
