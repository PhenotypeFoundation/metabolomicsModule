package org.dbxp.metabolomicsModule

/**
 * This is a temporary controller, solely for development purposes. This is a place where you
 * can bootstrap some data without having to bootstrap data every time the application loads
 * via BootStrap.groovy. Especially handy since mongo data persists after application restart.
 */
class BootStrapController {

    def uploadedFileService
    def parsedFileService

    def index = { render 'index' }

    def loadSmallDataSet = {

        def file            = new File('testData/DiogenesMockData_mini.txt')
        def uploadedFile    = uploadedFileService.createUploadedFileFromFile(file)

        def parsedFile      = parsedFileService.parseUploadedFile(uploadedFile, [delimiter: '\t'])

        uploadedFile.save()
        parsedFile.save()

        render 'done loading mock data'

    }
}
