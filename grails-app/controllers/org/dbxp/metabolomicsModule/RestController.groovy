package org.dbxp.metabolomicsModule

import grails.converters.JSON

class RestController {

    def getMeasurementMetadata = { 
	
		def resp = [:]
		
		if (params.assayToken){
			def assay = MetabolomicsAssay.findByAssayToken(params.assayToken as String)
			resp[params.assayToken] = assay?.measurementPlatformVersion?.features
		}
		
		render resp as JSON	
	}
}
