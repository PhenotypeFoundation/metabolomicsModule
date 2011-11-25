package org.dbxp.metabolomicsModule.identity

class FeatureController {

    def view = {
		try {
			def feature = Feature.get(params.id)
			render feature
		} catch (e) {
			render "Unable to load the feature!"
		}
	}
}
