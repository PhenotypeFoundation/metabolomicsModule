package org.dbxp.metabolomicsModule

import org.codehaus.groovy.grails.commons.GrailsApplication

class HomeController {
	def uploadedFileService

	def index = {
		def files = uploadedFileService.getUploadedFilesForUser(session.user)

		render(view: 'index', model:[ files: files ])
	}

	def studyList = {
		render(template: "studyList")
	}

	def developmentBar = {
		// make super sure this only works in development
		if (grails.util.GrailsUtil.environment == GrailsApplication.ENV_DEVELOPMENT) {
			render(template: "developmentActions")
		} else {
			render 'This functionality is not available...'
		}
	}
}
