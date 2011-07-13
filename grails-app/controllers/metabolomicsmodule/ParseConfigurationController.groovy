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

package metabolomicsmodule

import grails.converters.JSON

class ParseConfigurationController {

    def index = { }

    /**
     * Method to update all controls in the view
     * @return JSON formatted string
     */
    def updateDatamatrix = {

        render getDatamatrixAsJSON()
    }

     /**
     * @datamatrix two dimensional datamatrix containing data
     * @return JSON formatted string containing [iTotalRecords, iColumns, iTotalDisplayRecords, aoColumns, aaData]
     */

    def getDatamatrixAsJSON = {
		def headerColumns = []
		// Initialize the datamatrix

		def olddatamatrix = [ [123,1234,123], [123,123,123]]

        def datamatrix = []
        1000.times { datamatrix.add ( [100,200,300,400,500,600,700, 800, 900, 1000]) }

		def dataTables = [:]
		if (datamatrix) {
			datamatrix[0].size().times { headerColumns += [sTitle: "Column" + it]}

			dataTables = [param1: "param1", iTotalRecords: datamatrix.size(), iColumns: datamatrix.size(), iTotalDisplayRecords: datamatrix.size(), aoColumns: headerColumns, aaData: datamatrix]
		}

		render dataTables as JSON
    }
}
