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
    def fileTypes = ["CSV", "TAB", "XLS/XLSX"]
    def platformList = ["DCL Lipidomics", "Platform2", "Platform 3"]
    def assayList = ["Assay1", "Assay2", "Assay3"]

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

        out << '<li class="studyName">' + attrs.study.name + '<ul class="assayList">'

        attrs.study.assays.each { assay ->
            out << assayTag(assay: assay)
        }

        out << '</ul></li>'

    }

    def assayTag = { attrs ->

        // TODO: make assay clickable with a link to relevant pop-up when assay is associated with an uploaded file. Something like: UploadedFile.findByAssay attrs.assay
        out << '<li class=assayName>' + attrs.assay.name + '</li>'

    }

    /**
     * Renders a popup dialog which loads the preview controller to
     * configure and display the preview data
     *
     * @param id id of the HTML element being clicked
     */

    def previewDialog = { attrs, body ->
       out << 'var $link = "preview";'
	   out << 'var $previewDialog = $("<div></div>")'
	   out << '.load($link)'
	   out << '.dialog({'
       out << 'autoOpen: false,'
	   out << 'title: "Preview dialog",'
	   out << 'width: 500,'
	   out << 'height: 300'
	   out << '});'

       out << '$("#' + attrs['id'] + '").click(function() {'
	   out << '$previewDialog.dialog("open");'
       out << 'return false;'
	   out << '	});'
    }

    /**
     * Dropdown list control to choose the type of data formatting used: tabular, comma separated, Excel et cetera
     */

    def previewFileTypeControl = { attrs, body ->
        out << "Filetype: " + g.select(name:"fileType", from:fileTypes)
    }

    /**
     * Dropdown list control to choose the platform used (DCL lipodomics)
     */
    def previewPlatformControl = { attrs, body ->
        out << "Platform: " + g.select(name:"platform", from:platformList)
    }

    /**
     * Radio button to switch orientation/transpose data: 1 sample per row or per column
     */
    def previewOrientationControl = { attrs, body ->
        out << "Sample per "
        out << "<b>row</b>" + g.radio(name:"orientation", value:"samplePerRow", checked:true)
        out << "<b>column</b>" + g.radio(name:"orientation", value:"samplePerColumn")
    }

    /**
     * Dropdown list control to choose where the sample column starts
     */
    def previewSampleColumnControl = { attrs, body ->

    }

    /**
     * Dropdown list control to choose one parameter (not clear)
     */
    def previewOneParameterControl = { attrs, body ->

    }

    /**
     * Dropdown list control to choose the feature row
     */
    def previewFeatureRowControl = { attrs, body ->

    }

    /**
     * Multiselect control to choose assays
     */
    def previewAssaysControl = { attrs, body ->
        out << "Assay: " + g.select(name:"assays", from:assayList)

    }

    /**
     * Checkbox list control to select the data columns
     * TODO: jQuery plugin at http://code.google.com/p/dropdown-check-list/
     */
    def previewDataColumnsControl = { attrs, body ->
       out << "Datacolumns: "
    }

    /**
     * Preview control displaying a preview of the data according to the settings made by other controls
     */
    def previewDataControl = { attrs, body ->

    }

}
