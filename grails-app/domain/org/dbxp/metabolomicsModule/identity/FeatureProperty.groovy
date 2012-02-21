package org.dbxp.metabolomicsModule.identity

/**
 * 
 * @author Michael van Vliet
 *
 */
class FeatureProperty {
	
	String label
	String synonyms

    static constraints = {
		synonyms(nullable: true)
    }
}
