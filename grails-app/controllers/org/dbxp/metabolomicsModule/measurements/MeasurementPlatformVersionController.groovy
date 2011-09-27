package org.dbxp.metabolomicsModule.measurements

import org.dbxp.matriximporter.MatrixImporter

class MeasurementPlatformVersionController {

    def index = { redirect(url: request.getHeader('Referer')) }
	
	/*
	 * Metabolomics MeasurementPlatformVersion page
	 * 
	 * params.id is required to load the MeasurementPlatformVersion
	 */
   def view = {
		if (!params.id) redirect(url: request.getHeader('Referer')) // id of an MeasurementPlatformVersion must be present
		
		[ measurementPlatformVersion: MeasurementPlatformVersion.get(params.id) ]
   }
   
   /*
    * Upload feature(s) file
    */
   def featureFile = {
		
	   def uploadedfile = request.getFile('featuresfile')
	   
	   if (!uploadedfile.isEmpty() && params.mpv){

			//determine extension of uploaded file
			def filename = uploadedfile.getOriginalFilename().toLowerCase()
			def ext = filename.tokenize(".")[-1]

			// select correct way of processing the file based on extension
			switch (ext) {

				case 'xls' 		:	// uploaded xls
				case 'xlsx'		:	// uploaded xlsx
					
									def mpv = MeasurementPlatformVersion.get(params.mpv as Long)
								
									//move file to new location
									def featuresFile = new File("${mpv?.featuresfile}")
									uploadedfile.transferTo(featuresFile)
									
									//read excel
									def inputStream = new FileInputStream(featuresFile)
									def (matrix, parseInfo) = MatrixImporter.instance.importInputStream(inputStream, [:], true)
									
									if (!matrix) {
										throw new RuntimeException("Error parsing file ${featuresFile.fileName}; No features found.")
									}
									
									println matrix.dump()
									println matrix.size()
									println matrix[1]
									
									//clear old features
									
									//insert MeasurementPlatformVersionFeatures entries
								
									
									break
				default			:	//unsupported file!
					log.error("Unsupported filetype! ${filename}")
			}
		}

		//return to where you came from...
		redirect(url: request.getHeader('Referer'))
   }
}
