package org.dbxp.metabolomicsModule.identity

class FeaturePropertyService {

	def ChemspiderService
	def ChebiService
	def LipidmapsService
	def PubchemService

    static transactional = true

    def view(String label, String value) {

		//reformat the label to a method compatible format
		label = label.toLowerCase()
		label = label.replaceAll(" ", "")
		label = label.replaceAll("_", "")
		label = label.replaceFirst("${label[0]}", "${label[0].toUpperCase()}")

		try {
			return this."view${label}"(value)
		} catch (e) {
			return value
		}
    }

	def viewInchi(inchi){
		return '<a target="_blank" href="http://www.metitree.nl/inchi2image/png/' + inchi + '">' + ((inchi.size() >= 20) ? "${inchi[0..20]}..." : inchi) + '</a>'
	}

	def viewInchikey(inchikey){
		return "THIS IS AN INCHIKEY ${inchikey}"
	}

	def viewChebi(chebiId){
		return "THIS IS A CHEBI ID ${chebiId}"
	}

	def viewPubchem(pubchemId){
		return '<a target="_blank" href="' + PubchemService.pubchemUrlByPubchemId(pubchemId) + '">' + pubchemId + '</a>'
	}

	def viewLipidmap(LMID){
		return '<a target="_blank" href="' + LipidmapsService.lipidmapsUrlByLMID(LMID) + '">' + LMID + '</a>'
	}

	def viewChemspider(chemspiderId){
		return '<a target="_blank" href="' + ChemspiderService.chemspiderUrlByChemspiderId(chemspiderId) + '">' + chemspiderId + '</a>'
	}
}
