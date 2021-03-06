/**
 *  ParseConfigurationController, a controller for handling the Configuration Dialog
 *
 *  Copyright (C) 2011 Tjeerd Abma, Siemen Sikkema
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * @author Tjeerd Abma
 * @since 20110710
 *
 *  Revision information:
 *
 *  $Author$
 *  $Rev$
 *  $Date$
 */

package org.dbxp.metabolomicsModule

import grails.converters.JSON
import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.moduleBase.Assay
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatform

class ParseConfigurationController {

	def uploadedFileService
	def measurementService
	static final int tableCellMaxContentLength = 40

	def index = {

		[dialogProperties:
			[	uploadedFileId: params.uploadedFileId,
				fileName: params.fileName,
				mmBaseUrl: grailsApplication.config.grails.serverURL,
				controllerName: params.controller,
				buttons: ['save', 'cancel'],
				refreshPageAfterClose: true,
				redirectUrl: resource('/', absolute: true)
			]]
	}

	def features = {
		def uploadedFile = UploadedFile.get(params.uploadedFileId)
		if (!uploadedFile) {
			response.sendError(400)
			return
		}

		def errorMessage = ''

		session.uploadedFile = uploadedFile
		if (!uploadedFile.matrix) {
			try {
				uploadedFile.parse()
			} catch (e) {
				errorMessage = e.message
			}
		}

		def columns = uploadedFileService.getHeaderRow(uploadedFile).collect {it}
		def headerSuggestions = measurementService.createHeaderSuggestions(columns)

		[	uploadedFile: uploadedFile,
			columns: columns.collect {[sTitle: it]},
			data: uploadedFile.matrix[1..-1],
			headerSuggestions: headerSuggestions,
			errorMessage: errorMessage
		]
	}

	def cleanData = {
		if (!params.uploadedFileId) {
			throw new RuntimeException('The uploadedFileId was not set, please report this error message to the system administrator.')
		}

		session.uploadedFile = UploadedFile.get(params.uploadedFileId)

		def errorMessage = ''

		if (!session.uploadedFile?.matrix) {
			try {
				session.uploadedFile.parse()
			} catch (e) {
				errorMessage = e.message
			}
		}

		[	uploadedFile: session.uploadedFile,
			errorMessage: errorMessage,
			parseInfo: session.uploadedFile.parseInfo,
			controlsDisabled: errorMessage ? true : false]
	}

	def rawData = {
		if (!params.uploadedFileId) {
			throw new RuntimeException('The uploadedFileId was not set, please report this error message to the system administrator.')
		}

		session.uploadedFile = UploadedFile.get(params.uploadedFileId)
		[	uploadedFile: session.uploadedFile,
			errorMessage: '']
	}

	/**
	 * Method to read all form parameters and to update the dataMatrix preview
	 *
	 * @param form control parameters (filename, filetype, orientation et cetera)
	 * @return JSON (datatables) formatted string representing the dataMatrix
	 */
	def handleCleanDataForm = {
		if (!session.uploadedFile) return

		switch (params.formAction) {
			case 'init':
				render(getDataTablesObject() as JSON)
				break
			case 'update':
				render(handleUpdateFormAction(params) as JSON)
				break
			case 'updateAssay':
				updateAssayIfNeeded(params)
				render([message: buildSampleMappingString()] as JSON)
				break
			case 'save':
				render(handleSaveFormAction(params) as JSON)
				break
			default:
				render ''
		}
	}

	def handleRawDataForm = {
		println 'handleRawDataForm ' + params

		if (!session.uploadedFile) return

		def uploadedFile = UploadedFile.get(session.uploadedFile.id)
		session.uploadedFile = uploadedFile

		updateAssayIfNeeded(params)

		if (session.uploadedFileWasMovedToAssay) session.uploadedFileWasMovedToAssay.isSaved = true

		session.uploadedFile.save(failOnError: true)

		render ''
	}

	/**
	 * Same as handleCleanDataForm but specifically for the features pop-up dialog
	 */
	def handleFeatureForm = {

		if (!session.uploadedFile) return

		def uploadedFile = session.uploadedFile

		switch (params.formAction) {
			case 'save':

				// generate new measurement platform version
				MeasurementPlatform mp = MeasurementPlatform.get(params.platformVersionId)
				if (!mp) {
					response.sendError(400)
					return
				}

				def existingVersionNumbers = mp.versions*.versionNumber

				def newVersionNumber = existingVersionNumbers ? existingVersionNumbers.sort()[-1] + 0.1 : 0.1
				
				def headerReplacements = [:]
				params.keySet().findAll {
					it in uploadedFileService.getDataColumnHeaders(session.uploadedFile)
				}.each {
					// if multiple values for a key exist, they are returned as a list, we need only the first one
					// this happens when a feature file contains some columns more than once
					headerReplacements[it]  = (params[it] instanceof String) ? params[it] : params[it][0]
				}

				measurementService.createMeasurementPlatformVersion(
					uploadedFile,
					[versionNumber: newVersionNumber, measurementPlatform: mp],
					headerReplacements).save(failOnError: true)

				// if successful, delete uploaded file
				uploadedFileService.deleteUploadedFile(uploadedFile)

				render ''

				break
			default:
				render ''
		}
	}

	/**
	 *
	 * @param uploadedFile uploaded file
	 * @return sample names from the Assay object (IF there is an assay linked to this uploaded file)
	 */
	def getAssaySampleNames(uploadedFile) {

		// somehow uploadedFile.assay sometimes equals to 'false', getting the assay this way prevents that
		def assay = Assay.get(uploadedFile?.assay?.id)

		assay ? assay?.samples*.name : []
	}

	/**
	 *
	 * @param params form parameters (sampleColumnIndex, featureRow, assay et cetera)
	 * @return status message and next action
	 */
	def handleSaveFormAction(params) {

		updateSampleColumnAndFeatureRow(params)

		updateAssayIfNeeded(params)

		if (session.uploadedFileWasMovedToAssay) session.uploadedFileWasMovedToAssay.isSaved = true

		// Workaround for a bug introduced in Mongo GORM 1.0.0 M7, omitting this step would result in NPE
		UploadedFile.get(session.uploadedFile.id)

		session.uploadedFile.save(failOnError: true)

		[message: buildSampleMappingString(), formAction: "update"]
	}

	/**
	 *
	 * @param params sampleColumnIndex and featureRowIndex
	 * @return void
	 */
	def updateSampleColumnAndFeatureRow(params) {
		def uploadedFile = session.uploadedFile
		uploadedFile.sampleColumnIndex = params.sampleColumnIndex as int ?: 0
		uploadedFile.featureRowIndex = params.featureRowIndex as int ?: 0
	}

	/**
	 * This method reads the uploaded file from the session and generates a message with
	 * information about the amount of samples found and (un)mapped.
	 *
	 * @return status message
	 */
	def buildSampleMappingString() {

		def uploadedFile = session.uploadedFile

		def mappingString

		// somehow uploadedFile.assay sometimes equals to 'false', getting the assay this way prevents that
		def assay = Assay.get(uploadedFile?.assay?.id)
		if (uploadedFile.matrix && assay) {

			def fileSampleCount = uploadedFileService.sampleCount(uploadedFile)

			Set uploadedFiles = UploadedFile.findAllByAssay(assay)
			if (!(uploadedFile.gridFSFile_id in uploadedFiles*.gridFSFile_id)) {
				uploadedFiles += uploadedFile
			}

			def mappedSampleCount = uploadedFiles.sum { it.determineAmountOfSamplesWithData() ?: 0 } ?: 0
			def unmappedSampleCount = assay.samples.size() - mappedSampleCount

			mappingString = "Out of the $fileSampleCount samples from the file, ${uploadedFile.determineAmountOfSamplesWithData()} could be matched to the assay. There are still $unmappedSampleCount unmapped samples in the assay."

		} else if (assay) mappingString = 'File is linked to the assay.'
		else mappingString = 'File is not linked with an assay.'

		// check for duplicate sample names
		def sampleNames = uploadedFileService.getSampleNames(uploadedFile) ?: []
		if ((sampleNames+[]).unique().size() < sampleNames.size()) {
			mappingString += " Warning: duplicate sample names exist in the data file."
		}

		mappingString
	}

	/**
	 *
	 * @param params assayId
	 * @return void
	 */
	def updateAssayIfNeeded(params) {
		def assay = Assay.get(params.assayId)
		if (params.assayId != session.uploadedFile.assay?.id) {

			// store info about uploadedfile and assay connection temporarily in the session to be able to inform the user
			session.uploadedFileWasMovedToAssay = [msg: "File has been moved to highlighted assay page.", assay: assay]
			session.uploadedFile.assay = assay
		}
	}

	/**
	 *
	 * @param params all form parameters (sampleColumnIndex, featureRow, orientation et cetera)
	 * @return on succes return Datatables object with datamatrix, otherwise return failure message
	 */
	Map handleUpdateFormAction(params) {
		transposeMatrixIfNeeded(params)
		parseFileAgainIfNeeded(params)
		updateSampleColumnAndFeatureRow(params)

		// Get the current datatable object and add the params - these are control settings which are changed
		// by the user, but we do not want to store yet
		Map currentDataTablesObject = getDataTablesObject()

		// the sample column index was changed so for preview purposes remove the assay sample names
		if (session.uploadedFile.sampleColumnIndex != params.sampleColumnIndex?.toInteger())
			currentDataTablesObject = removeAssaySampleNamesFromDataTables(params, currentDataTablesObject)

		currentDataTablesObject ?: [message: 'Could not parse datamatrix, no data found.']
	}

	/**
	 *
	 * @param params (sampleColumnIndex)
	 * @param dataTablesObject Datatables object
	 * @return datatables object with the assay sample names removed
	 */
	Map removeAssaySampleNamesFromDataTables(params, dataTablesObject) {
		dataTablesObject.assaySampleNames = []
		dataTablesObject.sampleColumnIndex = params.sampleColumnIndex

		dataTablesObject
	}

	/**
	 * Method which checks the parameters passed and if these have changed it will try to re-parse the uploaded file
	 * and store the parsed file.
	 *
	 * @param params form parameters (sheetIndex (in case of Excel),, sampleColumnIndex, featureRow, orientation et cetera)
	 * @return void
	 */
	def parseFileAgainIfNeeded(params) {

		def uploadedFile = UploadedFile.get(session.uploadedFile.id)
		def parseInfo = uploadedFile?.parseInfo

		if ((parseInfo?.parserClassName == 'ExcelParser' && (params.sheetIndex != null && (parseInfo.sheetIndex as int != params.sheetIndex as int))) ||
			(parseInfo?.parserClassName == 'CsvParser' && (params.delimiter != null && (parseInfo.delimiter as Byte != params.delimiter as Byte)))) {
			try {
				session.uploadedFile.parse([sheetIndex: params.sheetIndex as int, delimiter: params.delimiter as byte])
			} catch (e) {
				session.uploadedFile.clearParsedData()
				flash.errorMessage = e.message
			}
		}
	}

	/**
	 * This method checks if the form parameter for orientation is changed and if so will try to
	 * transpose the parsed file
	 *
	 * @param params form parameter columnOrientation
	 * @return void
	 */
	def transposeMatrixIfNeeded(params) {

		def requestedColumnOrientation = params.isColumnOriented?.toBoolean()

		if (session.uploadedFile?.matrix &&
			params.formAction == 'update' &&
			requestedColumnOrientation != session.uploadedFile.isColumnOriented) {

			session.uploadedFile = uploadedFileService.transposeMatrix(session.uploadedFile)
		}
	}

	/**
	 *  Method to return the parsed file datamatrix as a map, also included in this function is the ajaxSource which
	 *  is a reference to the ajaxDataTables source
	 *
	 * @return Map containing parsed file information
	 */
	Map getDataTablesObject() {
		def uploadedFile = session.uploadedFile

		if (!uploadedFile?.matrix) return [:]

		def headerColumns = []

		uploadedFileService.getHeaderRow(uploadedFile).each { headerColumns += [sTitle: it] }

		[aoColumns: headerColumns,
			message: buildSampleMappingString(),
			parseInfo: uploadedFile.parseInfo,
			isColumnOriented: uploadedFile.isColumnOriented,
			sampleColumnIndex: uploadedFile.sampleColumnIndex,
			assaySampleNames: getAssaySampleNames(uploadedFile),
			ajaxSource: g.createLink(action: 'ajaxDataTablesSource')]
	}

	/**
	 * Method which retrieves the uploaded and parsed file from the session and performs several data "shaping" steps:
	 *  - limit the string length of the values in the datamatrix
	 *  - round double values
	 * In the end it builds up a Datatables JSON object so the Datatables (JavaScript) library can use this object to render the data
	 * visually
	 *
	 * @return Datatables JSON-formatted object
	 */
	def ajaxDataTablesSource = {
		def uploadedFile = session.uploadedFile

		int rows = uploadedFile.rows

		int dataStart = uploadedFile.featureRowIndex + 1

		def start = (params.iDisplayStart as int) + dataStart
		def end = Math.min(start + (params.iDisplayLength as int) - 1, rows - 1)

		def data = uploadedFile.matrix[start..end]

		// Round or limit the string length of the values in the datamatrix
		def choppedData = data.collect { row ->
			row.collect {
				def cellValue ->

				if (cellValue instanceof Double) {

					cellValue.round(3)

				} else {

					def stringRepresentation = cellValue.toString().trim()

					// chop off strings that are too long
					if (stringRepresentation.length() <= tableCellMaxContentLength) {
						stringRepresentation
					} else {
						stringRepresentation[0..Math.min(tableCellMaxContentLength, stringRepresentation.length() - 1)] + ' (...)'
					}

					stringRepresentation
				}
			}
		}

		def response = [
			sEcho: params.sEcho,
			iTotalRecords: rows - uploadedFile.featureRowIndex - 1,
			iTotalDisplayRecords: rows - uploadedFile.featureRowIndex - 1,
			aaData: choppedData,
			assaySampleNames: getAssaySampleNames(uploadedFile)
		]

		render response as JSON
	}
}