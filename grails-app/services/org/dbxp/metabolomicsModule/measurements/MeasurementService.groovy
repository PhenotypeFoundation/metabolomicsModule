package org.dbxp.metabolomicsModule.measurements

import org.dbxp.metabolomicsModule.identity.Feature
import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.moduleBase.Assay
import org.dbxp.metabolomicsModule.MetabolomicsAssay

class MeasurementService {

	static transactional = true

	def uploadedFileService

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

		List propertyNames

		// get labels for properties if they exist
		if (uploadedFile.matrix[0].size() > 1) {
			propertyNames = uploadedFile.matrix[0][1..-1]
		}

		// replace property names with user chosen replacements
		headerReplacements.each { replacement ->
			propertyNames[propertyNames.findIndexOf { it == replacement.key }] = replacement.value
		}

		// iterate all rows (=features) and assign properties
		uploadedFile.matrix[1..-1].each { row ->
			def props = [:]

			propertyNames.eachWithIndex { name, index ->

				// disregard empty strings and already set properties
				if (name && !props[name]) {
					props[name] = row[index+1]
				}
			}

			def feature = Feature.findByLabel(row[0]) ?: new Feature(label: row[0])
			feature.save()
			new MeasurementPlatformVersionFeature(feature: feature, props: props, measurementPlatformVersion: mpv).save()
		}
		mpv
	}

	def createHeaderSuggestions(columns) {
		
		def featureHeaderSuggestions = [:]
		FeatureProperty.list().collect { featureHeaderSuggestions[it.label] = it.synonyms.split(",") }

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
			if (assay?.samples?.size() == uploadedFile.determineAmountOfSamplesWithData()) {
				points++

				// 4 : all features are recognized
				if ((assay.measurementPlatformVersion?.features*.feature*.label as Set)?.containsAll(uploadedFileService.getFeatureNames(uploadedFile) as Set)) {
					points++

					// 5 : study is public
					if (assay.study.isPublic) points++
				}
			}
		}

		// calculate and return rating
		return (points / maxPoints)
	}

	// Constructs a paragraph based on an uploadedFile to describe why the rating (the stars) is the way it is
	def constructRatingText(UploadedFile uploadedFile) {

		def rating = (determineUploadedFileRating(uploadedFile) * 5) as int
		MetabolomicsAssay assay = Assay.get(uploadedFile.assay?.id)

		def ratingTextLines = [
			"1: file is uploaded",
			"0: file is not connected to an assay",
			"0: ${(assay?.samples?.size() ?: 0) - uploadedFile.determineAmountOfSamplesWithData()} samples have no data",
			"0: ${(((uploadedFileService.getFeatureNames(uploadedFile) ?: []) as Set) - ((assay?.measurementPlatformVersion?.features*.feature*.label ?: []) as Set)).size()} features not recognized in platform",
		    "0: Study is not public",
			"Total: ${rating} stars"
		]

		// deliberate fall through for switch statement!
		switch (rating) {
			case 5: ratingTextLines[4] = "1: Study is public"
			case 4: ratingTextLines[3] = "1: All features recognized"
			case 3: ratingTextLines[2] = "1: All samples have data"
			case 2: ratingTextLines[1] = "1: File is connected to an assay"
		}

		"<div style=\"text-align:left;\"> ${ratingTextLines.join('<br />')}</div>"
	}
}

