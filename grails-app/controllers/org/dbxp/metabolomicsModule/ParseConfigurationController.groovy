package org.dbxp.metabolomicsModule

import grails.converters.JSON

class ParseConfigurationController {

    def index = { }

     /**
     * @param1
     * @param2
     * @paramn
     * @return JSON formatted string containing [iTotalRecords, iColumns, iTotalDisplayRecords, aoColumns, aaData]
     */

    def getDatamatrixAsJSON = {
		def headerColumns = []
		// Initialize the datamatrix

		def datamatrix = [ [123,1234,123], [123,123,123]]

		def dataTables = [:]
		if (datamatrix) {
			datamatrix[0].size().times { headerColumns += [sTitle: "Column" + it]}

			dataTables = [param1: 12345, iTotalRecords: datamatrix.size(), iColumns: datamatrix.size(), iTotalDisplayRecords: datamatrix.size(), aoColumns: headerColumns, aaData: datamatrix]
		}

		render dataTables as JSON
    }
}
