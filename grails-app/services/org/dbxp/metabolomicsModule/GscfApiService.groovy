/** Service to provide access to the GSCF API using the GSCF-GroovyClient lib **/

package org.dbxp.metabolomicsModule

import org.dbxp.GSCF

class GscfApiService {

    // init GSCF object
    final GSCF gscf = new GSCF()

    // point to running instance of GSCF
    final String URL = 'http://localhost:8080/'
    final String EndPoint = 'gscf/api'
    final String Username = 'api'
    final String Password = 'apI123!'
    final String ApiKey = '11111111-2222-3333-4444-555555555555'

    static transactional = true

    private connectToGscf() {
        gscf.setURL(URL)
        gscf.setEndPoint(EndPoint)
        gscf.setUsername(Username)
        gscf.setPassword(Password)
        gscf.setApiKey(ApiKey)
    }

    private callGscf(String method, args = [:], retry = false){
        connectToGscf()
        return gscf.apiCall(method, args, retry)
    }

    public findAllStudies() {
        return callGscf('getStudies')
    }

    public findAllAssayTemplates(){
        return callGscf('getTemplatesForEntity', [entityType: 'Assay'])
    }

    public createAssay(args = [:]){
        return callGscf('createEntityWithTemplate', [entityType: 'Assay', templateToken: args.assayTemplate, studyToken: args.studyToken, module: 'Metabolomics module', name: args.assayName])
    }
}
