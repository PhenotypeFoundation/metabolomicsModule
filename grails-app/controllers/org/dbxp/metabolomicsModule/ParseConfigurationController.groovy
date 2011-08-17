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

import org.dbxp.dbxpModuleStorage.AssayWithUploadedFile
import org.dbxp.dbxpModuleStorage.ParsedFile
import org.dbxp.dbxpModuleStorage.UploadedFile
import grails.converters.JSON

class ParseConfigurationController {

    def parsedFileService
    def uploadedFileService
    static final int tableCellMaxContentLength = 40

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
                log.error e.printStackTrace()
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
                def returnStatus = [formAction:params.formAction, message: buildSavedMessage()]
                render returnStatus as JSON
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

            parsedFile.amountOfSamplesWithData = foundSampleCount
        } else {
            parsedFile?.amountOfSamplesWithData = 0
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

        // Get the current datatable object and add the params - these are control settings which are changed
        // by the user, but we do not want to store yet
        def currentDataTablesObject = getCurrentDataTablesObject()
        if (currentDataTablesObject) currentDataTablesObject.sampleColumnIndex = params.sampleColumnIndex

        //currentDataTablesObject ?: [errorMessage: flash.errorMessage ?: "No parsed data available."]
        currentDataTablesObject ?: [message: 'Could not parse datamatrix, no data found.']
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
                    session.uploadedFile.parsedFile.matrix = []
                    session.uploadedFile.parsedFile.parseInfo.sheetIndex = params.sheetIndex
                    session.uploadedFile.parsedFile.save()
                    flash.errorMessage = e.message
                }
            }

        } else if (parseInfo?.parserClassName == 'CsvParser') {

            if (parseInfo.delimiter != params.delimiter) {
                try {
                    parseInfo.delimiter = params.delimiter
                    session.uploadedFile.parsedFile = parsedFileService.parseUploadedFile(uploadedFile, [delimiter: params.delimiter as byte])
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

            //parsedFile.matrix

			dataTablesObject = [
                    aoColumns: headerColumns,
                    message: 'Done',
                    parseInfo: parsedFile.parseInfo,
                    isColumnOriented: parsedFile.isColumnOriented,
                    sampleColumnIndex : parsedFile.sampleColumnIndex,
                    featurRowIndex : '',
                    ajaxSource: g.createLink(action: 'ajaxDataTablesSource')
            ]
        }

		dataTablesObject
    }

    def ajaxDataTablesSource = {
        def parsedFile = session.uploadedFile.parsedFile

        int rows = parsedFile.rows

        int dataStart = parsedFile.featureRowIndex + 1

        def start = (params.iDisplayStart as int) + dataStart
        def end = Math.min(start + (params.iDisplayLength as int) - 1, rows - 1)

        def data = parsedFile.matrix[start..end]

        // Round or limit the string length of the values in the datamatrix
        def choppedData = data.collect{ row ->
            row.collect {
                String cellValue ->

                // If the cell value is a double round it and leave it as is, otherwise it is probably
                // a string value which should be chopped of if it exceeds the predefined maximum
                // cell content length
                cellValue.isDouble() ?
                    cellValue.toDouble().round(3) :
                    (cellValue.trim().length()<tableCellMaxContentLength ) ?
                        cellValue.trim() :
                        cellValue[0..Math.min(tableCellMaxContentLength , cellValue.trim().length()-1)] + ' (...)'
            }
        }

        def response = [
                sEcho: params.sEcho,
                iTotalRecords: rows-parsedFile.featureRowIndex-1,
                iTotalDisplayRecords: rows-parsedFile.featureRowIndex-1,
                aaData: choppedData
        ]

        render response as JSON
    }
}