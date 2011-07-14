package org.dbxp.metabolomicsModule.identity

class Feature {
	
	String label
	HashMap props

    static constraints = {
		props(nullable: true)
    }
}
