package org.dbxp.metabolomicsModule.measurements

import org.codehaus.groovy.grails.commons.ApplicationHolder

class MeasurementPlatformVersion {
	
	MeasurementPlatform measurementPlatform	
	Float versionNumber
	String changeList

    static constraints = {
		changeList(nullable: true)
    }
	
	/**
	 * Returns the object as a HashMap to be used in the API
	 **/
	HashMap toApi() {
		return ['measurement_platform': this.measurementPlatform.toApi(), 'version_number': this.versionNumber, 'changelist': this.changeList ?: '']
	}
	
	/**
	 * Transients
	 **/
	static transients = ['features', 'featuresfolder', 'featuresfile']
	
	// fetch features linked to this version
	//List getFeatures(){ return MeasurementPlatformVersionFeature.findAllByMeasurementPlatformVersion(this).collect { it.features } }
    List getFeatures(){ return MeasurementPlatformVersionFeature.findAllByMeasurementPlatformVersion(this) }
	
	// returns the folder to store the feature file in
	String getFeaturesfolder(){
		
		//TODO: Make User/Group specific
		
		def featuresFolder =	"${ApplicationHolder.getApplication().getParentContext().getResource("/").getFile().toString()}/features/" +
								("${this.id}-${this.measurementPlatform.id}-${this.versionNumber}").encodeAsMD5()
		// make sure it exists
		try {
			new File("${featuresFolder}").mkdirs()
		} catch (e) {
			log.error ("Unable to create directory for storing the features! ${e}")
		}
		return featuresFolder
	}
	
	// returns the path+file of features file
	String getFeaturesfile() { return "${this.featuresfolder}/.features" } 
}