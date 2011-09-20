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
import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.moduleBase.Assay

class ParseConfigurationController {

    def uploadedFileService
    static final int tableCellMaxContentLength = 40

    def index = {

        if (!params.uploadedFileId) {
            throw new RuntimeException('The uploadedFileId was not set, please report this error message to the system administrator.')
        }

        session.uploadedFile = UploadedFile.get(params.uploadedFileId)
        def platformVersionId = session.uploadedFile['platformVersionId']

        def errorMessage = ''

        if (!session.uploadedFile?.matrix) {
            try {
                session.uploadedFile.parse()
            } catch (e) {
                e.printStackTrace()
                errorMessage = e.message
            }
        }

        [       uploadedFile: session.uploadedFile,
                errorMessage: errorMessage,
                parseInfo: session.uploadedFile.parseInfo,
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
                render(getDataTablesObject() as JSON)
                break
            case 'update':
                render(handleUpdateFormAction(params) as JSON)
                break
            case 'updateAssay':
                updateAssayIfNeeded(params);
                render([message: buildSampleMappingString()] as JSON)
                break
            case 'save':
                render(handleSaveFormAction(params) as JSON)
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

        updatePlatformVersionId(params)
        updateSampleColumnAndFeatureRow(params)

        updateAssayIfNeeded(params)

        // Workaround for a bug introduced in Mongo GORM 1.0.0 M7, omitting this step would result in NPE
        UploadedFile.get(session.uploadedFile.id)

        session.uploadedFile.save(failOnError:true)

        [message: buildSampleMappingString(), formAction:"update"]
    }

    /**
     *
     * @param params (platformVersionId)
     * @return void
     */
    def updatePlatformVersionId(def params) {
        if (params.platformVersionId) {

            // Workaround for a bug introduced in Mongo GORM 1.0.0 M7, omitting this step would result in NPE
            UploadedFile.get(session.uploadedFile.id)

            session.uploadedFile['platformVersionId'] = params.platformVersionId as Long
        }
    }

    /**
     *
     * @param params sampleColumnIndex and featureRowIndex
     * @return void
     */
    def updateSampleColumnAndFeatureRow(params) {
		def uploadedFile = session.uploadedFile
		uploadedFile.sampleColumnIndex = params.sampleColumnIndex as int
		uploadedFile.featureRowIndex = params.featureRowIndex as int
    }

    /**
     * This method reads the uploaded file from the session and generates a message with
     * information about the amount of samples found and (un)mapped.
     *
     * @return  status message
     */
    def buildSampleMappingString() {

        def uploadedFile = session.uploadedFile

        // somehow uploadedFile.assay sometimes equals to 'false', getting the assay this way prevents that
        def assay = Assay.get(uploadedFile?.assay?.id)
        if (uploadedFile && assay) {

            def fileSampleCount = uploadedFileService.sampleCount(uploadedFile)
            def assaySampleCount = assay.samples.size()

            def unmappedSampleCount = fileSampleCount - (uploadedFile.determineAmountOfSamplesWithData() ?: 0)

            "${uploadedFile.determineAmountOfSamplesWithData()} of the $assaySampleCount samples in the assay found; $unmappedSampleCount samples from file remain unmapped."

        } else if (assay) 'File is linked to the assay.'
        else 'File is not linked with an assay.'
    }

    /**
     *
     * @param params assayId
     * @return void
     */
    def updateAssayIfNeeded(params) {

        def assay = Assay.get(params.assayId)
        if (params.assayId != session.uploadedFile.assay?.id) {
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

        // Get the current datatable object and add the params - these are control settings which are changed
        // by the user, but we do not want to store yet
        Map currentDataTablesObject = getDataTablesObject()

        // the sample column index was changed so for preview purposes remove the assay sample names
        if (session.uploadedFile.sampleColumnIndex != params.sampleColumnIndex.toInteger() )
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

        def uploadedFile = session.uploadedFile
        def parseInfo = uploadedFile?.parseInfo

        if (parseInfo?.parserClassName == 'ExcelParser') {

            if (params.sheetIndex != null && parseInfo.sheetIndex != params.sheetIndex as int) {
                try {
                    session.uploadedFile = uploadedFileService.parseUploadedFile(uploadedFile, [sheetIndex: params.sheetIndex as int])
                } catch (e) {
                    //TODO: figure out whether all this is really necessary and if so, whether it should be put into a service
                    session.uploadedFile.matrix = []
                    session.uploadedFile.rows = 0
                    session.uploadedFile.columns = 0
                    session.uploadedFile.parseInfo.sheetIndex = params.sheetIndex as int
                    session.uploadedFile.save(failOnError: true)
                    flash.errorMessage = e.message
                }
            }

        } else if (parseInfo?.parserClassName == 'CsvParser') {

            if (params.delimiter != null && parseInfo.delimiter != params.delimiter) {
                try {
                    parseInfo.delimiter = params.delimiter
                    session.uploadedFile = uploadedFileService.parseUploadedFile(uploadedFile, [delimiter: params.delimiter as byte])
                } catch (e) {
                    session.uploadedFile.matrix = []
                    flash.errorMessage = e.message
                }
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

        def requestedColumnOrientation = params.isColumnOriented.toBoolean()

        if (    session.uploadedFile?.matrix &&
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

        if (!uploadedFile.matrix) return [:]

        def headerColumns = []

		uploadedFileService.getHeaderRow(uploadedFile).each { headerColumns += [sTitle: it] }

		[		aoColumns: headerColumns,
				message: buildSampleMappingString(),
				parseInfo: uploadedFile.parseInfo,
				isColumnOriented: uploadedFile.isColumnOriented,
				sampleColumnIndex : uploadedFile.sampleColumnIndex,
				assaySampleNames: getAssaySampleNames(uploadedFile),
				ajaxSource: g.createLink(action: 'ajaxDataTablesSource') ]
    }

    /**
     * Method which retrieves the uploaded and parsed file from the session and performs several data "shaping" steps:
     *  - limit the string length of the values in the datamatrix
     *  - round double values
     * In the end it builds up a Datatables JSON object so the Datatables (JavaScript) library can use this object to render the data
     * visually
     *
     *  @return Datatables JSON-formatted object
     */
    def ajaxDataTablesSource = {
        def uploadedFile = session.uploadedFile

        int rows = uploadedFile.rows

        int dataStart = uploadedFile.featureRowIndex + 1

        def start = (params.iDisplayStart as int) + dataStart
        def end = Math.min(start + (params.iDisplayLength as int) - 1, rows - 1)

        def data = uploadedFile.matrix[start..end]

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
                iTotalRecords: rows-uploadedFile.featureRowIndex-1,
                iTotalDisplayRecords: rows-uploadedFile.featureRowIndex-1,
                aaData: choppedData,
                assaySampleNames: getAssaySampleNames(uploadedFile)
        ]

        render response as JSON
    }
}