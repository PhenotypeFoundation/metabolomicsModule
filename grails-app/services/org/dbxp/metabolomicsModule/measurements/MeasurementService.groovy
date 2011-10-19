package org.dbxp.metabolomicsModule.measurements

import org.dbxp.metabolomicsModule.identity.Feature

class MeasurementService {

	static transactional = true

	static featureHeaderSuggestions = [
		'm/z': 		['mz', 'm z', 'm over z'],
		'InChI':	['inchi','Inchi','inchie'],
		'PubChem':	['pubchem'],
		'ChEBI':	['chebi', 'Chebi']
	]

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

	MeasurementPlatformVersion createMeasurementPlatformVersion(uploadedFile, Map mpvProperties, headerReplacements = [:]) {

		MeasurementPlatformVersion mpv = new MeasurementPlatformVersion(mpvProperties)
		mpv.save()

		List propertyNames = uploadedFile.matrix[0][1..-1]

		headerReplacements.each { replacement ->
			propertyNames[propertyNames.findIndexOf { it == replacement.key }] = replacement.value
		}

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

	def createHeaderSuggestions(columns) {

		def otherSuggestions = featureHeaderSuggestions.keySet()

		columns.collect { column ->

			for (key in featureHeaderSuggestions.keySet()) {
				if (column in featureHeaderSuggestions[key]) {

					otherSuggestions -= key

					return [key]  + otherSuggestions
				}
			}
			[null] + otherSuggestions
		}
	}
}

