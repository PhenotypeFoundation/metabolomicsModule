package org.dbxp.metabolomicsModule

import org.dbxp.dbxpModuleStorage.UploadedFile

/**
 * The metabolomics module tag library delivers a rich set of tags to make it easier to re-use components
 * and elements in the interface.
 *
 * @version: $Rev$
 * @author: $Author$
 * @lastrevision: $Date$
*/

class MetabolomicsModuleTagLib {

    // abbreviation for Metabolomics Module
    static namespace = "mm"

    def uploadedFileService
    def assayService
    def metabolomicsModuleDB

    def uploadedFileList = { attrs ->

        out << '<h1>Uploaded files</h1>'

//        def uploadedFiles = uploadedFileService.getUploadedFilesForUser((User) session.user)
        def uploadedFiles = UploadedFile.all

        out << '<ul class=uploadedFileList>'

        uploadedFiles.each { uploadedFile ->
            out << uploadedFileTag(uploadedFile: uploadedFile)
        }

        out << '</ul>'

    }

    def uploadedFileTag = { attrs ->

        out << '<li class="uploadedFileTag">'

        out << attrs.uploadedFile.fileName

        out << '</li>'

    }

    def studyList = { attrs ->

        out << '<h1>Study overview</h1>'

        // find all studies the user can read and have at least one assay
        def readableStudiesWithAssays = assayService.getAssaysReadableByUserAndGroupedByStudy(session.user)
        out << '<ul class=studyList>'

        readableStudiesWithAssays.each { study, assays ->
            out << studyTag(study: study, assays: assays)
        }

        out << '</ul>'

    }
    
    def studyTag = { attrs ->

        out << '<li class="studyTag">' + attrs.study.name + '<span class="sampleCount">' + attrs.assays.collect{it.samples.size()}.sum() + ' samples</span><ul class="assayList">'

        attrs.assays.each { assay ->
            out << assayTag(assay: assay)
        }

        out << '</ul></li>'

    }

    def assayTag = { attrs ->

        // TODO: make assay clickable with a link to relevant pop-up when assay is associated with an uploaded file.
        out << '<li class=assayTag>' + attrs.assay.name + '<span class=sampleCount>' + attrs.assay.samples.size() + ' samples</span></li>'

    }
}
