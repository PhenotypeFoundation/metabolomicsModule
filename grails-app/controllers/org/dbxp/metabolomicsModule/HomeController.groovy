package org.dbxp.metabolomicsModule

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
		render(template: "developmentActions")
	}
}
