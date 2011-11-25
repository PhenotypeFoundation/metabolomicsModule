package org.dbxp.metabolomicsModule.measurements

import org.codehaus.groovy.grails.commons.ApplicationHolder

class MeasurementPlatformVersionFeatureController {
	
	def FeaturePropertyService
	
    def view = {
		
		def mpvfeature = MeasurementPlatformVersionFeature.get(params.id)
		def metaboliteMappingFile = ApplicationHolder.getApplication().getParentContext().getResource("/files/db/metabolites.txt").getFile().toString()
		def mpvfeatureMappings = mpvfeature.propertyMappings("idmapper-text:file://${metaboliteMappingFile}")
		
		// prepare properties view
		def mpvfeatureProps = [:]
		mpvfeature.props.each { propKey, propValue ->
			if (propValue){
				mpvfeatureProps[propKey] = FeaturePropertyService.view(propKey as String, propValue as String)
			}
		}
		
		// prepare unique mappings view
		def uniqueMappings = [:]
		mpvfeatureMappings.each { mappingKey, mappingValue ->
			mappingValue.each { mappings ->
				if (mappings.value?.identifier){
					def mappingEntry = [:]
						mappingEntry['hash']			= "${mappings.value.source}${mappings.value.identifier}".encodeAsMD5()
						mappingEntry['source']			= mappings.value.source
						mappingEntry['identifier'] 		= mappings.value?.identifier
						mappingEntry['view'] 			= FeaturePropertyService.view(mappings.value.source as String, mappings.value.identifier as String)
					
					//add to list
					uniqueMappings[mappingEntry['hash']] = mappingEntry
				}
			}
		}
		
		// prepare mappings view by property
		def mpvfeatureMappingsByProperty = [:]
		mpvfeatureMappings.each { propertyMappingKey, propertyMappingValue ->
			
			def propertyName = ''
			propertyMappingKey.each { propertyName += FeaturePropertyService.view(it.key as String, it.value as String) }
			propertyMappingKey.each { propertyName += " (${it.key as String})" }
			
			def propertyMappings = [:]
			propertyMappingValue.each { mappingKey, mappingValue ->
				
				if (mappingValue.identifier){
					propertyMappings[mappingValue.source] = FeaturePropertyService.view(mappingValue.source as String, mappingValue.identifier as String)
				}
			}
			mpvfeatureMappingsByProperty[propertyName] = propertyMappings
		}

		[ mpvfeature: mpvfeature, mpvfeatureProps: mpvfeatureProps, mpvfeatureMappingsByProperty: mpvfeatureMappingsByProperty, uniqueMappings: uniqueMappings ]
	}
}
