/**
 *  ParseConfigurationController, a controller for handling the Configuration Dialog
 *
 *  Copyright (C) 2011 Tjeerd Abma
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
 *  @author Tjeerd Abma
 *  @since 20110710
 *
 *  Revision information:
 *
 *  $Author$
 *  $Rev$
 *  $Date$
 */

package org.dbxp.metabolomicsModule

import grails.converters.JSON
import org.dbxp.dbxpModuleStorage.AssayWithUploadedFile
import org.dbxp.dbxpModuleStorage.ParsedFile
import org.dbxp.dbxpModuleStorage.UploadedFile

class ParseConfigurationController {

    def parsedFileService
    def uploadedFileService

    def index = {

        if (!params.uploadedFileId) {
            throw new RuntimeException('The uploadedFileId was not set, please report this error message to the system administrator.')
        }

        session.uploadedFile = UploadedFile.get(params.uploadedFileId)

        def errorMessage = ''

        def parsedFile = session.uploadedFile?.parsedFile

        if (!parsedFile) {
            try {
                // Read the uploaded file and parse it
                parsedFile = parsedFileService.parseUploadedFile(session.uploadedFile).save(failOnError: true)

                // Store the parsed file in the 'uploadedFile' object
                session.uploadedFile.parsedFile = parsedFile

            } catch (e) {
                e.printStackTrace()
                errorMessage = e.message
            }
        }

        [       uploadedFile: session.uploadedFile,
                errorMessage: errorMessage,
                parseInfo: parsedFile?.parseInfo,
                disabled: errorMessage?true:false]
    }

    /**
     * Method to read all form parameters and to update the dataMatrix preview
     *
     * @param form control parameters (filename, filetype, orientation et cetera)
     * @return JSON (datatables) formatted string representing the dataMatrix
     */
    def handleForm = {
        if (!session.uploadedFile) return
        
        switch (params.formAction) {
            case 'init':
                render(getCurrentDataTablesObject() as JSON)
                break
            case 'update':
                render(handleUpdateFormAction(params) as JSON)
                break
            case 'save':
                handleSaveFormAction(params)
                render([message: buildSavedMessage()] as JSON)
                break
            default:
                render ''
        }
    }

    def getCurrentDataTablesObject() {
        getDataTablesObject(session.uploadedFile.parsedFile)
    }

    def handleSaveFormAction(params) {

        def uploadedFile = session.uploadedFile

        updatePlatformVersionId(params)
        updateSampleColumnAndFeatureRow(params)

        // make sure we have an id for uploadedFile by saving it
        if (!uploadedFile.id)
            uploadedFile.save(failOnError: true)

        updateAssayIfNeeded(params)
        uploadedFile.save(failOnError: true)
    }

    def updatePlatformVersionId(def params) {
        if (params.platformVersionId) {
            session.uploadedFile.platformVersionId = params.platformVersionId as int
        }
    }

    def updateSampleColumnAndFeatureRow(params) {
        def parsedFile = session.uploadedFile.parsedFile
        parsedFile.sampleColumnIndex = params.sampleColumnIndex as int
        parsedFile.featureRowIndex = params.featureRowIndex as int
    }

    def buildSavedMessage() {

        def msg = 'Saved'

        def uploadedFile = session.uploadedFile
        def parsedFile = uploadedFile.parsedFile
        def assay = uploadedFile.assay

        if (parsedFile && assay) {
            def sampleNamesInFile = parsedFileService.getSampleNames(parsedFile)
            def sampleNamesInAssay = assay.samples*.name

            def foundSampleCount = sampleNamesInAssay.intersect(sampleNamesInFile).size()
            def unmappedSampleCount = sampleNamesInFile.size() - foundSampleCount
            def assaySampleCount = assay.samples.size()

            msg += ", $foundSampleCount of the $assaySampleCount samples in the assay found; $unmappedSampleCount samples from file remain unmapped."
        }
        msg
    }

    def updateAssayIfNeeded(params) {

        def assay = AssayWithUploadedFile.get(params.assayId)

        if (params.assayId != session.uploadedFile.assay?.id) {
            assay?.uploadedFile = session.uploadedFile
            session.uploadedFile.assay = assay
        }
    }

    Map handleUpdateFormAction(params) {
        transposeMatrixIfNeeded(params)
        parseFileAgainIfNeeded(params)
        getCurrentDataTablesObject() ?: [errorMessage: flash.errorMessage ?: "No parsed data available."]
    }

    def parseFileAgainIfNeeded(params) {

        def uploadedFile = session.uploadedFile
        def parsedFile = uploadedFile.parsedFile
        def parseInfo = parsedFile?.parseInfo

        if (parseInfo?.parserClassName == 'ExcelParser') {

            if (parseInfo.sheetIndex != params.sheetIndex as int) {
                try {
                    session.uploadedFile.parsedFile = parsedFileService.parseUploadedFile(uploadedFile, [sheetIndex: params.sheetIndex as int])
                } catch (e) {
                    session.uploadedFile.parsedFile = null
                    flash.errorMessage = e.message
                }
            }

        } else if (parseInfo?.parserClassName == 'CsvParser') {

            if (parseInfo.delimiter != params.delimiter) {
                try {
                    parseInfo.delimiter = params.delimiter
                    session.uploadedFile.parsedFile = parsedFileService.parseUploadedFile(uploadedFile, [delimiter: params.delimiter])
                } catch (e) {
                    session.uploadedFile.parsedFile.matrix = []
                    flash.errorMessage = e.message
                }
            }
        }
    }

    def transposeMatrixIfNeeded(params) {

        def requestedColumnOrientation = params.isColumnOriented.toBoolean()

        if (    session.uploadedFile?.parsedFile?.matrix &&
                params.formAction == 'update' &&
                requestedColumnOrientation != session.uploadedFile.parsedFile.isColumnOriented) {

            session.uploadedFile.parsedFile = parsedFileService.transposeMatrix(session.uploadedFile?.parsedFile)
        }
    }

    /**
     * @dataMatrix two dimensional dataMatrix containing data
     * @return Map containing: [iTotalRecords, iColumns, iTotalDisplayRecords, aoColumns, aaData]
     */
    Map getDataTablesObject(ParsedFile parsedFile) {

        if (!parsedFile) return [:]

		def headerColumns = []

		def dataTablesObject = [:]
		if (parsedFile.matrix) {

			parsedFileService.getHeaderRow(parsedFile).each { headerColumns += [sTitle: it] }

			dataTablesObject = [
                    aoColumns: headerColumns,
                    message: 'Done',
                    parseInfo: parsedFile.parseInfo,
                    isColumnOriented: parsedFile.isColumnOriented,
                    ajaxSource: g.createLink(action: 'ajaxDataTablesSource')
            ]
        }

		dataTablesObject
    }

    def ajaxDataTablesSource = {
        def parsedFile = session.uploadedFile.parsedFile

        def rows = parsedFile.rows
        def columns = parsedFile.columns

        def dataStart = parsedFile.featureRowIndex + 1

        def start = (params.iDisplayStart as int) + dataStart
        def end = Math.min(start + (params.iDisplayLength as int) - 1, rows - 1)

        def data = parsedFile.matrix[start..end]

        def roundedData = data.collect{ row ->
            row.collect {
                String cellValue ->
                cellValue.isDouble() ?
                    cellValue.toDouble().round(3) :
                    cellValue
            }
        }

        def response = [
                sEcho: params.sEcho,
                iTotalRecords: (rows-parsedFile.featureRowIndex)*columns,
                iTotalDisplayRecords: (params.iDisplayLength as int)*columns,
                aaData: roundedData
        ]

        render response as JSON
    }
}