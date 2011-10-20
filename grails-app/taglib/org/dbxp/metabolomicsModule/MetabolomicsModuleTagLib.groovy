package org.dbxp.metabolomicsModule

import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion
import org.apache.commons.logging.LogFactory

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
    def uploadedFileService
    def measurementService
	private static final log = LogFactory.getLog(this);

    def uploadedFileList = { attrs ->

		attrs.dialogProperties.baseUrl = resource('/', absolute: true);
		if (!attrs.dialogProperties.redirectUrl) attrs.dialogProperties.redirectUrl = attrs.dialogProperties.baseUrl

		// get file list from attributes
  		def uploadedFiles
		if (attrs.containsKey('files')) {
			uploadedFiles = attrs.get('files')
		} else {
			throw new Exception("required attribute 'files' is missing from the <uploadedFilesList files=\"...\"/> tag")
		}

		// output file uploadr
        out << '<h1>Uploaded files</h1>'
        out << uploadr.add(name: "uploadrArea", path: "/tmp", placeholder: "Drop file(s) here to upload", direction: 'up', maxVisible: 8, rating: true, colorPicker: true, voting: true) {
            uploadedFiles.each { uploadedFile ->
				// add file to the uploadr
                uploadr.file(name: uploadedFile.fileName) {
					uploadr.deletable { ((uploadedFile.uploader.id == session.user.id || session.user.isAdministrator)) }
					if (uploadedFile.hasProperty('color') && uploadedFile.color) uploadr.color { "${uploadedFile.color}" }
					uploadr.rating { "${measurementService.determineUploadedFileRating(uploadedFile)}" }
					uploadr.fileSize { uploadedFile.fileSize }
                    uploadr.fileModified { uploadedFile.lastUpdated.time }
                    uploadr.fileId { uploadedFile.id }
                }
            }

            uploadr.onSuccess {
				out << g.render(template:'/js/uploadr/onSuccess', model:[assay: attrs.assay])
            }

            uploadr.onFailure {
                "console.log('failed uploading ' + file.fileName);"
            }

            uploadr.onAbort {
                "console.log('aborted uploading ' + file.fileName);"
            }

			uploadr.onView {
				out << g.render(template:'/js/uploadr/onView',
					model:[dialogProperties: attrs.dialogProperties]);
            }

            uploadr.onDownload {
				out << g.render(template:'/js/uploadr/onDownload', model:[])
            }

            uploadr.onDelete {
				out << g.render(template:'/js/uploadr/onDelete', model:[redirectUrl: attrs.dialogProperties.redirectUrl])
            }

			uploadr.onChangeColor {
				out << g.render(template:'/js/uploadr/onChangeColor', model:[])
			}
        }
    }

    def studyList = { attrs ->

		out << '''
<script>
$(document).ready(function() {
	$('#studyOverviewTable').dataTable(
	{
		bFilter: false,
		bInfo: false,
		bLengthChange: false,
		bPaginate: false
    });
});
</script>'''

		out << '<h1>Study overview</h1>'

		out << '<table id="studyOverviewTable"><thead><tr><th>Studies</th><th>Assays</th><th>Samples</th><th>Assigned</th><th>Platform</th></tr></thead><tbody>'

		def readableStudiesWithAssays = assayService.getAssaysReadableByUserAndGroupedByStudy(session.user)

		for (studyWithAssays in readableStudiesWithAssays) {

			out << "<tr><td>$studyWithAssays.key.name</td><td>"

			def assayNames = []
			def sampleCounts = []
			def assignedSampleCounts = []
			def measurementPlatformStrings = []

			studyWithAssays.value.each { assay ->

				def assayNameString

				if (attrs.highlightedAssay?.id == assay?.id)
					assayNameString = "<b>$assay.name</b>"
				else
					assayNameString = assay.name

				assayNames += g.link(action:"view", controller:"assay", id: assay.id) { assayNameString }

				sampleCounts += assay.samples.size()

				// avoiding mongo bugs by obtaining the uploaded file instance like this
				Long uploadedFileId = UploadedFile.findByAssay(assay)?.id
				UploadedFile uploadedFile = UploadedFile.get(uploadedFileId)

				if (uploadedFile) {
					assignedSampleCounts += uploadedFile.determineAmountOfSamplesWithData()
					if (uploadedFile['platformVersionId']) {
						def mpv = MeasurementPlatformVersion.get((Long) uploadedFile['platformVersionId'])
						measurementPlatformStrings += mpv ? "${mpv.measurementPlatform?.name} (${mpv.versionNumber})" : ""
					}
				}
			}

			out << assayNames.join('<br />')

			out << '</td><td>'

			out << sampleCounts.join('<br />')

			out << '</td><td>'

			out << assignedSampleCounts.join('<br />')

			out << '</td><td>'

			out << measurementPlatformStrings.join('<br />')

			out << '</td></tr>'
		}

		out << '</tbody></table>'
    }

    /**
     * Dropdown list control to choose the platform used (DCL lipodomics, et cetera).
     *
     * @param assay
     */
    def assayPlatformChooser = { attrs, body ->
        out << "Platform:  <br />"
        out << '<form>'
        out << '<select name="platformVersionId" size="8" style="width:100%;" ' + (attrs.disabled ? 'disabled>' : '>')

        measurementService.findAllMeasurementPlatforms().each { platform ->
			// if new studygroup create new label
			out << '<optgroup label="' + platform.name + '">'
            measurementService.findAllMeasurementPlatformVersions(measurementPlatform:platform).each { platformVersion ->

                out << '<option value="' + platformVersion.id + '" ' + ((platformVersion.id == attrs.assay.measurementPlatformVersion?.id) ? 'selected' : '') + '>' + platformVersion.versionNumber + '</option>'
            }

            out << '</optgroup>'

        }

        out << '</select>'
        out << '<input type="submit" value="Submit" />'
        out << '</form>'
    }

	def commentFieldEditor = { attrs, body ->

		out << "Comments: <br />"
		out << '<form>'
		out << '<textarea name="comments" rows="8" style="width:100%;padding:0;">'
		out << attrs.assay.comments
		out << '</textarea>'
		out << '<br /><input type="submit" value="Submit" />'
		out << '</form>'

	}

	def assayFeatureTables = { attrs, body ->

		def assayFiles = UploadedFile.findAllByAssay(attrs.assay)
		assayFiles.each{ assayFile ->
			if (assayFile.matrix)
				out << mm.assayFeatureTable(assay: attrs.assay, assayFile: assayFile)
		}
	}

	def assayFeatureTable = {attrs, body ->

		out << '<table><thead><tr><th>Platform Feature</th><th>Feature in Data File</th>'

		def measurementPlatformVersionFeatures = attrs.assay.measurementPlatformVersion?.features
		def propertyLabels = measurementPlatformVersionFeatures ? measurementPlatformVersionFeatures[0].props.keySet() : []


		def propertyValueMap = [:]
		measurementPlatformVersionFeatures.each { mpvf ->
			propertyValueMap[mpvf.feature.label] = mpvf.props.values()
		}

		for (propertyLabel in propertyLabels) {
			out << "<th>$propertyLabel</th>"
		}

		out << '</tr></thead><tbody>'

		def dataColumnHeaders = uploadedFileService.getDataColumnHeaders(attrs.assayFile)
		def measurementPlatformVersionFeatureLabels = measurementPlatformVersionFeatures*.feature?.label

		Set combinedLabels = dataColumnHeaders + measurementPlatformVersionFeatureLabels

		for (label in combinedLabels) {
			out << '<tr><td>'

			if (label in measurementPlatformVersionFeatureLabels) out << label

			out << '</td><td>'

			if (label in dataColumnHeaders) out << label + '</td>'

			propertyValueMap[label].each { propertyValue ->
				out << "<td>${propertyValue}</td>"
			}
			out << '</tr>'
		}

		out << '</tbody></table>'
	}

	def notification = {attrs, body ->

		out << '<div class="notification">'

		out << body()

		out << '</div>'
	}

}
