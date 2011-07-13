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
        100.times { datamatrix.add ( [100,200,300,400,500,600,700, 800, 900, 1000]) }

		def dataTables = [:]
		if (datamatrix) {
			datamatrix[0].size().times { headerColumns += [sTitle: "Column" + it]}

			dataTables = [param1: "param1", iTotalRecords: datamatrix.size(), iColumns: datamatrix.size(), iTotalDisplayRecords: datamatrix.size(), aoColumns: headerColumns, aaData: datamatrix]
		}

		render dataTables as JSON
    }
}
