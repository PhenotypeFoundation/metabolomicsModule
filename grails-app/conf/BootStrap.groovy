import org.dbxp.metabolomicsModule.identity.FeatureProperty

class BootStrap {
    def init = { servletContext ->
        // add default feature properties if we don't have any
        if (FeatureProperty.count() == 0) {
            if (!FeatureProperty.findByLabel('m/z')) { new FeatureProperty(label: 'm/z', synonyms: 'mz,m z,m over z,Mass Quan#').save() }
            if (!FeatureProperty.findByLabel('InChI')) { new FeatureProperty(label: 'InChI', synonyms: 'inchi,Inchi,inchie').save() }
            if (!FeatureProperty.findByLabel('PubChem')) { new FeatureProperty(label: 'PubChem', synonyms: 'pubchem').save() }
            if (!FeatureProperty.findByLabel('ChEBI ID')) { new FeatureProperty(label: 'ChEBI ID', synonyms: 'chebi,Chebi,chebi_id,ChEBI_ID').save() }
        }
    }

    def destroy = {
    }
}
