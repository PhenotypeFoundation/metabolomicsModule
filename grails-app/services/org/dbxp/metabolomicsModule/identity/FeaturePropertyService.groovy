package org.dbxp.metabolomicsModule.identity

class FeaturePropertyService {

	def ChemspiderService
	def ChebiService
	def LipidmapsService
	def PubchemService

    static transactional = true

    def view(String label, String value) {

		//reformat the label to a method compatible format
		label = label.replaceAll(" ", "").toLowerCase()
		label = label.replaceFirst("${label[0]}", "${label[0].toUpperCase()}")

		try {
			return this."view${label}"(value)
		} catch (e) {
			return value
		}
    }

	def viewInchi(inchi){
		return '<a target="_blank" href="http://www.metitree.nl/inchi2image/png/'+inchi+'">'+inchi+'</a>'
	}

	def viewInchikey(inchikey){
		return "THIS IS AN INCHIKEY ${inchikey}"
	}

	def viewChebi(chebiId){
		return "THIS IS A CHEBI ID ${chebiId}"
	}

	def viewPubchem(pubchemId){
		return "THIS IS A PUBCHEM ID ${pubchemId}"
	}

	def viewLipidmap(LMID){
		return "THIS IS A Lipidmaps IDtoURL ${LipidmapsService.lipidmapsUrlByLMID(LMID)}"
	}

	def viewChemspider(chemspiderId){
		return "THIS IS A Chemspider IDtoURL ${ChemspiderService.chemspiderUrlByChemspiderId(chemspiderId)}"
	}
}
