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

    def index = {

        if (!params.uploadedFileId) {
            throw new RuntimeException('The uploadedFileId was not set, please report this error message to the system administrator.')
        }

        session.uploadedFile = UploadedFile.get(params.uploadedFileId)

        def errorMessage = ''

        if (!session.uploadedFile?.parsedFile) {
            try {
                // Read the uploaded file and parse it
                def parsedFile = parsedFileService.parseUploadedFile(session.uploadedFile)

                // Store the parsed file in the 'uploadedFile' object
                session.uploadedFile.parsedFile = parsedFile

            } catch (Exception e) {

                errorMessage = e.message

            }
        }

        // TODO: make sure errorMessage is displayed
        [uploadedFile: session.uploadedFile, errorMessage: errorMessage]
    }

    /**
     * Method to read all form parameters and to update the dataMatrix preview
     *
     * @param form control parameters (filename, filetype, orientation et cetera)
     * @return JSON (datatables) formatted string representing the dataMatrix
     */
    def handleForm = {

        transposeMatrixIfNeeded(params)

        def dataTables = getDataMatrix(session.uploadedFile?.parsedFile)

        if (!dataTables) dataTables = [errorMessage: 'Could not parse uploaded file.']

        if (params.formAction == 'save') {
            try {
                // make sure we have an id for uploadedFile by saving it
                session.uploadedFile.save(failOnError: true)

                def assay = AssayWithUploadedFile.get(params.assayID)

                if (params.assayID) {
                    if (params.assayID != session.uploadedFile.assay?.id) {

                        assay.uploadedFile = session.uploadedFile
                        session.uploadedFile.assay = assay

                    }
                } else {
                    // remove link with assay
                    assay?.uploadedFile = null
                    session.uploadedFile.assay = null
                }

                session.uploadedFile.save(failOnError: true)

            } catch (Exception e) {
                dataTables.errorMessage = e.message
            }
        }

        render dataTables as JSON
    }

    def transposeMatrixIfNeeded(params) {

        if (    session.uploadedFile?.parsedFile?.matrix &&
                params.formAction == 'update' &&
                params.isColumnOriented != session.uploadedFile.parsedFile.isColumnOriented) {

            session.uploadedFile.parsedFile.isColumnOriented = params.isColumnOriented
            session.uploadedFile.parsedFile = parsedFileService.transposeMatrix(session.uploadedFile?.parsedFile)
        }
    }

    /**
     * @dataMatrix two dimensional dataMatrix containing data
     * @return Map containing: [iTotalRecords, iColumns, iTotalDisplayRecords, aoColumns, aaData]
     */
    def getDataMatrix(ParsedFile parsedFile) {
		def headerColumns = []

        def rows = parsedFile.rows
        def columns = parsedFile.columns
        def totalEntries = rows * columns

		def dataTables = [:]
		if (parsedFile.matrix) {
			columns.times { headerColumns += [sTitle: "Column " + it]}

			dataTables = [iTotalRecords: totalEntries, iColumns: columns, iTotalDisplayRecords: totalEntries, aoColumns: headerColumns, aaData: parsedFile.matrix]
		}

		return dataTables
    }
}