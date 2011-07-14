package org.dbxp.metabolomicsModule.measurements

class PocController {
	
	def measurementFactoryService

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
}
