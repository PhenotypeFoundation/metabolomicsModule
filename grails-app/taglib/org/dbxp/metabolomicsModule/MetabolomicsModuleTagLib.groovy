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

    def assayService

    def uploadedFileList = { attrs ->

        out << '<h1>Uploaded files</h1>'

        def uploadedFiles = UploadedFile.all

        out << uploadr.add(name: "uploadrArea", path: "/tmp", placeholder: "Drop file(s) here to upload", direction: 'up', maxVisible: 5) {

            uploadedFiles.each { uploadedFile ->

                uploadr.file(name: uploadedFile.fileName) {
                    uploadr.fileSize { uploadedFile.fileSize }
                    uploadr.fileModified { uploadedFile.lastUpdated.time }
                    uploadr.fileId { uploadedFile.id }
                }
            }

            uploadr.onSuccess {
                '$.ajax({url: \'' + g.createLink(plugin: 'dbxpModuleStorage', controller: 'uploadedFile', action: 'uploadFinished') + '?fileName=\'+file.fileName, ' +
                        'success: function(data){file.fileId=data.fileId}});'
            }

            uploadr.onFailure {
                "console.log('failed uploading ' + file.fileName);"
            }

            uploadr.onAbort {
                "console.log('aborted uploading ' + file.fileName);"
            }

            uploadr.onView {
                "openParseConfigurationDialog(file.fileName, file.fileId)"
            }

            uploadr.onDownload {
                "console.log('you clicked download on ' + file.fileName);" +
                 'window.open(\'' + g.createLink(plugin: 'dbxpModuleStorage', controller: 'uploadedFile', action: 'downloadUploadedFile') + '?fileId=\'+file.fileId);'
            }

            uploadr.onDelete {
                "console.log('you clicked delete on ' + file.fileName);" +
                "var deletionWasSuccessful = false;" +
                '$.ajax({url: \'' + g.createLink(plugin: 'dbxpModuleStorage', controller: 'uploadedFile', action: 'deleteUploadedFile') + '?fileId=\'+file.fileId, ' +
                        'success: function(data){deletionWasSuccessful=data.status},' +
                        'async: false});' +
                'return deletionWasSuccessful'
            }
        }
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
