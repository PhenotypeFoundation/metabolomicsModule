package org.dbxp.metabolomicsModule

class HomeController {
	def uploadedFileService

	def index = {
		render(view: 'index', model:[ files: uploadedFileService.getUploadedFilesForUser(session.user) ])
	}
}
