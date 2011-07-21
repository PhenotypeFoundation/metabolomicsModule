package org.dbxp.metabolomicsModule.api

import grails.converters.JSON

class JsonController {
	
	def identityFactoryService
	
	def call = {
		try {
						
			//call service with optional parameters
			render (	
						[
							'message'	: 'success', 
							'response'	: this."${params.service}"(params)
						] as JSON
					)
		} catch(e) {
		
			//call failed, return response in JSON
			render (
						[
							'message'	: 'invalid API service call'
						] as JSON
					)
			
			log.error(e)			
		}
	}

    def feature = { 
		
		if (params.label){ // fetch a feature from the provided label
			return identityFactoryService.featureFromLabel(['label': params.label, 'eager': true]) //set argument eager to true to fetch all details
		}
		
		// fetch all features
		return identityFactoryService.featuresAsList()
	} 
}
