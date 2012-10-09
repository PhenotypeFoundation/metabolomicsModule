package org.dbxp.metabolomicsModule

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class HomeController {
	def uploadedFileService
	def synchronizationService

	def index = {
		def files = uploadedFileService.getUnassignedUploadedFilesForUser(session.user)

		def uploadedFileWasMovedToAssay
		if (session.uploadedFileWasMovedToAssay?.isSaved) {
			uploadedFileWasMovedToAssay = session.uploadedFileWasMovedToAssay
		}
		session.removeAttribute('uploadedFileWasMovedToAssay')

		[ 	files: files,
			movedFileMessage: uploadedFileWasMovedToAssay?.msg,
			highlightedAssay: uploadedFileWasMovedToAssay?.assay]
	}

	def developmentBar = {
		// make super sure this only works in development
		if (((ConfigurationHolder.config.development.bar).contains(grails.util.GrailsUtil.environment))) {
			render(template: "developmentActions")
		} else {
			render 'This functionality is not available...'
		}
	}

	def sync = {
		synchronizationService.fullSynchronization()
		redirect(url: request.getHeader('Referer'))
	}
}
