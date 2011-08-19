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
import org.dbxp.dbxpModuleStorage.ParsedFile
import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.moduleBase.Assay

class ParseConfigurationController {

    def parsedFileService
    def uploadedFileService
    static final int tableCellMaxContentLength = 40

    def index = {

        if (!params.uploadedFileId) {
            throw new RuntimeException('The uploadedFileId was not set, please report this error message to the system administrator.')
        }

        session.uploadedFile = UploadedFile.get(params.uploadedFileId)
        def platformVersionId = session.uploadedFile['platformVersionId']

        def errorMessage = ''

        if (!session.uploadedFile?.parsedFile) {
            try {
                session.uploadedFile.parse()
            } catch (e) {
                e.printStackTrace()
                errorMessage = e.message
            }
        }

        [       uploadedFile: session.uploadedFile,
                errorMessage: errorMessage,
                parseInfo: session.uploadedFile.parsedFile?.parseInfo,
                controlsDisabled: errorMessage?true:false,
                platformVersionId: platformVersionId]
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
                render(handleSaveFormAction(params) as JSON)
                break
            default:
                render ''
        }
    }

    def getCurrentDataTablesObject() {
        getDataTablesObject(session.uploadedFile.parsedFile)
    }

    def handleSaveFormAction(params) {

        updatePlatformVersionId(params)
        updateSampleColumnAndFeatureRow(params)

        updateAssayIfNeeded(params)

        // Workaround for a bug introduced in Mongo GORM 1.0.0 M7, omitting this step would result in NPE
        UploadedFile.get(session.uploadedFile.id)

        session.uploadedFile.save(failOnError:true)

        [message: buildSampleMappingString()]
    }

    def updatePlatformVersionId(def params) {
        if (params.platformVersionId) {

            // Workaround for a bug introduced in Mongo GORM 1.0.0 M7, omitting this step would result in NPE
            UploadedFile.get(session.uploadedFile.id)

            session.uploadedFile['platformVersionId'] = params.platformVersionId as Long
        }
    }

    def updateSampleColumnAndFeatureRow(params) {
        def parsedFile = session.uploadedFile.parsedFile
        if (parsedFile) {
            parsedFile.sampleColumnIndex = params.sampleColumnIndex as int
            parsedFile.featureRowIndex = params.featureRowIndex as int
        }
    }

    def buildSampleMappingString() {

        def uploadedFile = session.uploadedFile
        def parsedFile = uploadedFile.parsedFile

        // somehow uploadedFile.assay sometimes equals to 'false', getting the assay this way prevents that
        def assay = Assay.get(uploadedFile?.assay?.id)

        if (parsedFile && assay) {

            // Workaround for a bug introduced in Mongo GORM 1.0.0 M7, omitting this step would result in NPE
            ParsedFile.get(parsedFile.id)

            def fileSampleCount = parsedFileService.sampleCount(parsedFile)
            def assaySampleCount = assay.samples.size()
            def unmappedSampleCount = fileSampleCount - (parsedFile['amountOfSamplesWithData'] ?: 0)

            "${parsedFile['amountOfSamplesWithData'] ?: 0} of the $assaySampleCount samples in the assay found; $unmappedSampleCount samples from file remain unmapped."

        } else if (assay) 'File is linked to the assay.'
        else 'File is not linked with an assay.'
    }

    def updateAssayIfNeeded(params) {

        def assay = Assay.get(params.assayId)

        if (params.assayId != session.uploadedFile.assay?.id) {

            session.uploadedFile.assay = assay

            if (session.uploadedFile.parsedFile) {
                // Workaround for a bug introduced in Mongo GORM 1.0.0 M7, omitting this step would result in NPE
                ParsedFile.get(session.uploadedFile.parsedFile.id)

                if (session.uploadedFile.parsedFile)
                    session.uploadedFile.parsedFile['amountOfSamplesWithData'] = determineAmountOfSamplesWithData(session.uploadedFile)
            }
        }
    }

    def determineAmountOfSamplesWithData(UploadedFile uploadedFile) {

        def sampleNamesInFile = parsedFileService.getSampleNames(uploadedFile?.parsedFile)

        def samplesInAssay = uploadedFile?.assay ? uploadedFile.assay.samples : []
        def sampleNamesInAssay = samplesInAssay*.name

        sampleNamesInAssay.intersect(sampleNamesInFile).size()
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
                    //TODO: figure out whether all this is really necessary and if so, whether it should be put into a service
                    session.uploadedFile.parsedFile.matrix = []
                    session.uploadedFile.parsedFile.rows = 0
                    session.uploadedFile.parsedFile.columns = 0
                    session.uploadedFile.parsedFile.parseInfo.sheetIndex = params.sheetIndex as int
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

			dataTablesObject = [
                    aoColumns: headerColumns,
                    message: buildSampleMappingString(),
                    parseInfo: parsedFile.parseInfo,
                    isColumnOriented: parsedFile.isColumnOriented,
                    sampleColumnIndex : parsedFile.sampleColumnIndex,
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