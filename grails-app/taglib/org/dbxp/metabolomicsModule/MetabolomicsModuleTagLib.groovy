package org.dbxp.metabolomicsModule

import org.dbxp.moduleBase.Auth

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

    def uploadedFileList = { attrs ->

        out << '<h1>Uploaded files</h1>'

        out << 'uploaded file list goes here ....'

    }

    def studyList = { attrs ->

        out << '<h1>Study overview</h1>'

        // TODO: is 'read' flag always enabled when 'write' or 'isOwner' is?
        // find all Auth objects connected to this user with read access to a study
        def authorizations = Auth.findAllByUserAndCanRead(session.user, true)

        // find all studies the user can read and have at least one assay
        def readableStudiesWithAssays = authorizations*.study.findAll { it.assays }
        out << '<ul class=studyList>'

        readableStudiesWithAssays.each { study ->
            out << studyTag(study: study)
        }

        out << '</ul>'

    }
    
    def studyTag = { attrs ->

        out << '<li class="studyName">' + attrs.study.name + '<span class="sampleCount">' + attrs.study.assays.collect{it.samples.size()}.sum() + ' samples</span><ul class="assayList">'

        attrs.study.assays.each { assay ->
            out << assayTag(assay: assay)
        }

        out << '</ul></li>'

    }

    def assayTag = { attrs ->

        // TODO: make assay clickable with a link to relevant pop-up when assay is associated with an uploaded file.
        out << '<li class=assayName>' + attrs.assay.name + '<span class=sampleCount>' + attrs.assay.samples.size() + ' samples</span></li>'

    }
}
