package org.dbxp.metabolomicsModule.measurements

import org.dbxp.metabolomicsModule.identity.Feature
import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.moduleBase.Assay
import org.dbxp.metabolomicsModule.MetabolomicsAssay

class MeasurementService {

	static transactional = true

	def uploadedFileService

	static featureHeaderSuggestions = [
		'm/z': 		['mz', 'm z', 'm over z'],
		'InChI':	['inchi','Inchi','inchie'],
		'PubChem':	['pubchem'],
		'ChEBI ID':	['chebi', 'Chebi', 'chebi_id', 'ChEBI_ID']
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

		columns.collect { column ->

			def otherSuggestions = featureHeaderSuggestions.keySet()

			for (key in featureHeaderSuggestions.keySet()) {
				if (column in featureHeaderSuggestions[key]) {

					otherSuggestions -= key

					return [key]  + otherSuggestions
				}
			}
			[null] + otherSuggestions
		}
	}

	/**
	 * Rating calculation:
	 * 1 = file is uploaded (exists)
	 * 2 = file is attached to an assay
	 * 3 = all samples are recognized
	 * 4 = all features are recognized
	 * 5 = study is public
	 */
	def determineUploadedFileRating(UploadedFile uploadedFile) {
		def maxPoints = 5

		// 1 point, file uploaded
		def points = 1

		MetabolomicsAssay assay = Assay.get(uploadedFile.assay?.id)

		if (assay) {

			// connected to an assay
			points++

			// 3 : all samples are recognized
			if ( uploadedFile.matrix && (assay?.samples?.size() == uploadedFile.determineAmountOfSamplesWithData())) {
				points++

				// 4 : all features are recognized
				if (assay.measurementPlatformVersion?.features?.size() == uploadedFileService.getFeatureNames(uploadedFile).size()) {
					points++

					// 5 : study is public
					if (assay.study.isPublic) points++
				}
			}
		}

		// calculate and return rating
		return (points / maxPoints)
	}
}

