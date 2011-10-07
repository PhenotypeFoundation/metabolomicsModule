package org.dbxp.metabolomicsModule

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class HomeController {
	def uploadedFileService

	def index = {
		def files = uploadedFileService.getUnassignedUploadedFilesForUser(session.user)

		render(view: 'index', model:[ files: files ])
	}

	def studyList = {
		render(template: "studyList")
	}

	def uploadedFileList = {

		def files = uploadedFileService.getUnassignedUploadedFilesForUser(session.user)
		
		println files
		
		render(template: "uploadedFileList", model: [files: files])
	}

	def developmentBar = {
		// make super sure this only works in development
		if (((ConfigurationHolder.config.development.bar).contains(grails.util.GrailsUtil.environment))) {
			render(template: "developmentActions")
		} else {
			render 'This functionality is not available...'
		}
	}
}
