package org.dbxp.metabolomicsModule.identity

class FeaturePropertyService {

	def ChemspiderService
	def ChebiService
	def LipidmapsService
	def PubchemService
	def HmdbService
	def KeggService
	def CasService
	def KnapsackService

	static transactional = true

	/**
	 * Format the label to match the 'known property labels' convention
	 * 
	 * @param label
	 * @return Formatted label
	 */
	String formatLabel(String label){

		//reformat the label to a method compatible format
		label = label.toLowerCase()
		label = label.replaceAll(" ", "")
		label = label.replaceAll("_", "")
		label = label.replaceFirst("${label[0]}", "${label[0].toUpperCase()}")

		return label
	}

	/**
	 * Property mapping
	 * @param label	Property Label (eg. Kegg, Pubchem, etc)
	 * @param value
	 * @param mappingService What service to use to find similar properties
	 * @return HashMap
	 */
	HashMap propertyMapping(String label, String value,	String mappingService) {

		def resp = [:]
	
		try {
			resp = this."${formatLabel(label)}Service"?.identicalResourceKeys(value, null, mappingService)
		} catch (e) {
			//log.error(e)
			//we do nothing
		}

		return resp
	}

	def view(String label, String value) {

		//reformat the label to a method compatible format
		label = formatLabel(label)

		try {
			return this."view${label}"(value)
		} catch (e) {
			return value
		}
	}

	def viewInchi(String inchi){
		return '<a target="_blank" href="http://www.metitree.nl/inchi2image/png/' + inchi + '">' + ((inchi.size() >= 20) ? "${inchi[0..20]}..." : inchi) + '</a>'
	}

	def viewInchikey(String inchikey){
		return "${inchikey}[INCHIKEY]"
	}

	def viewChebi(String chebiId){
		return '<a target="_blank" href="' + ChebiService.chebiUrlByChebiId(chebiId) + '">' + chebiId + '</a>'
	}

	def viewCas(String casId){
		return '<a target="_blank" href="' + CasService.chemindexUrlByCasId(casId) + '">' + casId + '</a>'
	}
	
	def viewKnapsack(String knapsackId){
		return '<a target="_blank" href="' + KnapsackService.knapsackUrlByKnapsackId(knapsackId) + '">' + knapsackId + '</a>'
	}
	
	def viewPubchem(String pubchemId){
		return '<a target="_blank" href="' + PubchemService.pubchemUrlByPubchemId(pubchemId) + '">' + pubchemId + '</a>'
	}

	def viewLipidmaps(String LMID){
		return '<a target="_blank" href="' + LipidmapsService.lipidmapsUrlByLMID(LMID) + '">' + LMID + '</a>'
	}

	def viewChemspider(String chemspiderId){
		return '<a target="_blank" href="' + ChemspiderService.chemspiderUrlByChemspiderId(chemspiderId) + '">' + chemspiderId + '</a>'
	}

	def viewHmdb(String hmdbId){
		return '<a target="_blank" href="' + HmdbService.hmdbUrlByHmdbId(hmdbId) + '">' + hmdbId + '</a>'
	}

	def viewKegg(String keggId){
		return '<a target="_blank" href="' + KeggService.keggUrlByKeggId(keggId) + '">' + keggId + '</a>'
	}
}
