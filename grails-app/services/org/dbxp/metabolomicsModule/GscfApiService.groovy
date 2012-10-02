/** Service to provide access to the GSCF API using the GSCF-GroovyClient lib **/

package org.dbxp.metabolomicsModule

import org.dbxp.GSCF
import org.codehaus.groovy.grails.commons.*

class GscfApiService {

    // init GSCF object
    final GSCF gscf = new GSCF()

    def config = ConfigurationHolder.config

    // point to running instance of GSCF
    final String URL = config.api.url
    final String EndPoint = config.api.endpoint
    final String Username = config.api.username
    final String Password = config.api.password
    final String ApiKey = config.api.apikey

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
        return callGscf('createEntityWithTemplate', [entityType: 'Assay', templateToken: args.assayTemplate, studyToken: args.studyToken, module: config.api.module, name: args.assayName])
    }
}
