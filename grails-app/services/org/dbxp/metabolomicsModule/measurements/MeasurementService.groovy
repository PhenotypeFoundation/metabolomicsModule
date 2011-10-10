package org.dbxp.metabolomicsModule.measurements

import org.dbxp.metabolomicsModule.identity.Feature

class MeasurementService {

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
		if (args['measurementPlatform']) {
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

	MeasurementPlatformVersion createMeasurementPlatformVersion(uploadedFile, Map mpvProperties) {

		MeasurementPlatformVersion mpv = new MeasurementPlatformVersion(mpvProperties)
		mpv.save()

		def propertyNames = uploadedFile.matrix[0][1..-1]

		uploadedFile.matrix[1..-1].each { row ->
			def props = [:]

			propertyNames.eachWithIndex { name, index ->
				props[name] = row[index+1]
			}

			def feature = Feature.findByLabel(row[0]) ?: new Feature(label: row[0])
			feature.save()
			new MeasurementPlatformVersionFeature(feature: feature, props: props, measurementPlatformVersion: mpv).save()
		}
		mpv
	}
}

