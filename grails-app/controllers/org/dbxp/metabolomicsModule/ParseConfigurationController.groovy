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
import org.dbxp.dbxpModuleStorage.UploadedFile

class ParseConfigurationController {

    def parsedFileService

    def index = {
        // get uploaded file: def uploadedFile = UploadedFile.get(my_id)
        // parse file: def parsedFile = uploadedFile.parse([fileName: uploadedFile.fileName, delimiter: .... etc.)
        // get data: parsedFileService.getMeasurements(parsedFile)
        [filename:params.filename]
    }

    /**
     * Method to read all form parameters and to update the datamatrix preview
     *
     * @param form control parameters (filename, filetype, orientation et cetera)
     * @return JSON (datatables) formatted string representing the datamatrix
     */
    def updateDatamatrix = {
        // Get the uploaded file from the database
        def uploadedFile = UploadedFile.findByFileName(params.filename)

        //try {
            // Read the uploaded file and parse it
            def parsedFile = parsedFileService.parseUploadedFile(uploadedFile, [delimiter: '\t', fileName: uploadedFile.fileName])

            // Store the parsed file in the 'uploadedFile' object
            uploadedFile.parsedFile = parsedFile

            // Render the parsed datamatrix as JSON
            render getDatamatrixAsJSON(uploadedFile.parsedFile.matrix)
        /*} catch (Exception e) {
                println e.printStackTrace()
                def dataTables = [iTotalRecords: 0, iColumns: 0, iTotalDisplayRecords: 0, aoColumns: [], aaData: []]
                render dataTables as JSON
                }*/

    }

     /**
     * @datamatrix two dimensional datamatrix containing data
     * @return JSON formatted string containing [iTotalRecords, iColumns, iTotalDisplayRecords, aoColumns, aaData]
     */

    def getDatamatrixAsJSON(datamatrix) {
		def headerColumns = []

		def dataTables = [:]
		if (datamatrix) {
			datamatrix[0].size().times { headerColumns += [sTitle: "Column" + it]}

			dataTables = [param1: "param1", iTotalRecords: datamatrix.size(), iColumns: datamatrix.size(), iTotalDisplayRecords: datamatrix.size(), aoColumns: headerColumns, aaData: datamatrix]
		}

		render dataTables as JSON
    }
}