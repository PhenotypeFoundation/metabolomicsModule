package org.dbxp.metabolomicsModule.measurements

class MeasurementFactoryService {
	
    static transactional = true

	/*
	 * Wrapper function to fetch all MeasurementPlatforms based on the provided arguments
	 */
    def findAllMeasurementPlatforms(args = [:]) {
		return MeasurementPlatform.list()
    }
	
	/*
	 * Wrapper function to fetch all MeasurementPlatformVersions based on the provided arguments
	 */
	def findAllMeasurementPlatformVersions(args = [:]) {
		if (args['measurementPlatform']){
			return MeasurementPlatformVersion.findAllByMeasurementPlatform(args['measurementPlatform'])
		}
		
		//default return all
		return MeasurementPlatformVersion.list()
	}
	
	/*
	* Wrapper function to fetch all MeasurementPlatformVersionFeatures based on the provided arguments
	*/
   def findAllMeasurementPlatformVersionFeatures(args = [:]) {
	   return MeasurementPlatformVersionFeature.findAllByMeasurementPlatformVersion(args['measurementPlatformVersion'])
   }
	
}
