package org.dbxp.metabolomicsModule

class MetabolomicsModuleTagLib {

    // abbriviation for Metabolomics Module
    static namespace = "mm"

    def uploadedFileList = { attrs ->

        out << 'uploaded file list goes here ....'

    }

    def studyList = { attrs ->

        out << 'study list goes here ....'

    }

}
