package org.dbxp.metabolomicsModule.identity

class FeaturePropertyService {

	def ChemspiderService
	def ChebiService
	def LipidmapsService
	def PubchemService
	def HmdbService
	def KeggService

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

	def viewInchi(String inchi){
		return '<a target="_blank" href="http://www.metitree.nl/inchi2image/png/' + inchi + '">' + ((inchi.size() >= 20) ? "${inchi[0..20]}..." : inchi) + '</a>'
	}

	def viewInchikey(String inchikey){
		return "${inchikey}[INCHIKEY]"
	}

	def viewChebi(String chebiId){
		return '<a target="_blank" href="' + ChebiService.chebiUrlByChebiId(chebiId) + '">' + chebiId + '</a>'
	}

	def viewPubchem(String pubchemId){
		return '<a target="_blank" href="' + PubchemService.pubchemUrlByPubchemId(pubchemId) + '">' + pubchemId + '</a>'
	}

	def viewLipidmap(String LMID){
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
