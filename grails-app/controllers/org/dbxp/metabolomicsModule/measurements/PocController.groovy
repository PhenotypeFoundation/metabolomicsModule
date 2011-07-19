package org.dbxp.metabolomicsModule.measurements

import grails.converters.*

class PocController {
	
	def measurementFactoryService
	def identityFactoryService

    def index = {
			
		measurementFactoryService.findAllMeasurementPlatforms().each { mp ->
			measurementFactoryService.findAllMeasurementPlatformVersions(['measurementPlatform': mp]).each { mpv ->
				render "<h1>${mp.name} (Version: ${mpv.versionnumber})</h1>"
				render "<h2>Features</h2>"
				measurementFactoryService.findAllMeasurementPlatformVersionFeatures(['measurementPlatformVersion': mpv]).each { mpvf ->
					render " - - - - - - - - - - - - -<br />"					
					render "<h3>Feature <i><font color=\"blue\">${mpvf.feature.label}</font></i></h3>"					
					render "<b>Properties (feature specific)</b><br />"
					mpvf.feature.props.each { fp ->
						render " - <i>${fp.key}</i> :: <font color=\"blue\">${fp.value}</font><br />"
					}					
					
					render "<b>Properties (platform specific) :</b><br />"
					mpvf.props.each { mpvfp ->
						render " - <i>${mpvfp.key}</i> :: <font color=\"blue\">${mpvfp.value}</font><br />"
					}
					render "<br />"

				}
				render "<br /> #################################################################################### <br /><br />"
			}
		}
	}
	
	def platforms = {
		
		render "<b>FETCH MEASUREMENT PLATFORM VERSIONS PER PLATFORM...</b><br /><br />"
		
		/*
		 * Fetch all Measurement Platform versions (per platform) 
		 */
		def mps = measurementFactoryService.findAllMeasurementPlatforms()
		
		mps.sort { a,b -> a.name <=> b.name }.each { mp ->
			render "<b>${mp.name}</b><br />has version(s): "
			
			def mp_mpvs = measurementFactoryService.findAllMeasurementPlatformVersions(['measurementPlatform':mp])
			
			println mp_mpvs.features
			
			mp_mpvs.each {
				render " :: V${it.versionnumber} "
			}
			
			render "<br />"
		}
		
		render "<br /><b>OR FETCH ALL IN ONE GO...</b><br /><br />"
		
		/*
		* Fetch all Measurement Platform versions (in one go)
		*/
		def mpvs = measurementFactoryService.findAllMeasurementPlatformVersions()
		
		mpvs.sort { a,b -> a.measurementPlatform.name <=> b.measurementPlatform.name }.each { mpv ->
			render "Platform: ${mpv.measurementPlatform.name} - V${mpv.versionnumber}<br />"			
		}
		
		
		
		render "Done"
		
	}
	
	def identityExample = {
		
		
		def labels = ['PA(12:0/13:0)', 'PA(6:0/6:0)', 'PA(12:0/15:0)']
		
		labels.each { label ->
			render identityFactoryService.featureFromLabel(label,['eager': true]) as JSON
		}
	}
	
}
