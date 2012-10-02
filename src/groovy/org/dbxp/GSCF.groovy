/**
 * SDK for interfacing with GSCF / DBNP using Groovy / Java
 *
 * API specification: http://studies.dbnp.org/api
 *
 *
 * Copyright 2012 Jeroen Wesbeek <work@osx.eu>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dbxp

// uncomment the following line if you intend to use this class
// in your Grails project, or add the http-builder to
// your BuildConfig.groovy
//@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2' )

import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

import java.security.MessageDigest

class GSCF implements GSCFClient{
	private String version	= "0.1"
	private BigInteger sequence = -1
	private String token

	protected String username
	private String password
	private String apiKey

	private String deviceID
	private String url
	private String endPoint	= 'api'

	/**
	 * class constructor
	 * @void
	 */
	public GSCF() {
		// add a shutdown hook to cache token and sequence
		Runtime.getRuntime().addShutdownHook { cacheToDisk() }
	}

	public static void main(String [] args){
		//TODO: entry for executable JAR
	}

	/**
	 * cache token and sequence to reduce 'authenticate' calls
	 * @return Boolean
	 */
	private Boolean cacheToDisk() {
		File cacheFile = new File(getCacheFile())

		// got a token and a sequence?
		if (token && sequence) {
			// cache to disk
			try {
				cacheFile.withObjectOutputStream { out ->
					out << [
						token	: token,
						sequence: sequence
					]
				}
			} catch (Exception e) {
				// seems like we cannot cache files
			}
		}
	}

	/**
	 * restore token and sequence from disk
	 * @return Boolean
	 */
	private Boolean restoreFromDisk() {
		File cacheFile = new File(getCacheFile())
		Boolean success = false

		// read cache file if it exists
		if (cacheFile.exists() && cacheFile.canRead()) {
			cacheFile.withObjectInputStream { instream ->
				instream.eachObject {
					if (it.token && it.sequence) {
						// restore token and sequence from cache
						token		= it.token
						sequence	= it.sequence - 1
						success		= true
					}
				}
			}
		}

		return success
	}

	/**
	 * get the path of the cache file
	 * @return String
	 */
	private String getCacheFile() {
		String tempName = (System.properties['os.name'].toLowerCase().contains('windows')) ? 'TEMP' : 'tmp'
		String cacheFile = "/${tempName}/gscf-${getDeviceID()}.data"

		return cacheFile
	}

	/**
	 * username setter
	 * @param String username
	 * @void
	 */
	public void setUsername(String user) {
		username = user
	}

	/**
	 * password setter
	 * @param String password
	 * @void
	 */
	public void setPassword(String pass) {
		password = pass
	}

	/**
	 * api key setter
	 * @param String api key
	 * @void
	 */
	public void setApiKey(String key) {
		apiKey = key
	}

	/**
	 * url setter
	 * @param String url
	 * @void
	 */
	public void setURL(String apiURL) {
		url = apiURL
	}

	/**
	 * endpoint setter
	 * @param String
	 * @void
	 */
	public void setEndPoint(String apiEndPoint) {
		endPoint = apiEndPoint
	}

	/**
	 * device id setter
	 * @param String device id
	 * @void
	 */
	public void setDeviceID(String id) {
		deviceID = id
	}

	/**
	 * return the device authentication token
	 * @return String
	 */
	private String getToken() {
		// got a token?
		if (!token) {
			// try to read token (and sequence) from cache file
			if (!restoreFromDisk()) {
				// authenticate to fetch token and sequence
				authenticate()
			}
		}

		return token
	}

	/**
	 * return the sequence
	 * @return Long
	 */
	private BigInteger getSequence() {
		// authenticate if we do yet have done so
		if (sequence < 0) {
			// try to read sequence (and token) from cache file
			if (!restoreFromDisk()) {
				// authenticate to fetch token and sequence
				authenticate()
			}
		}

		// increment sequence
		sequence++

		return sequence
	}

	/**
	 * generate a unique device id based on MAC address, script location and username
	 * @return void
	 */
	private void generateDeviceID() {
		InetAddress ip
		String macAddress

		// get Mac Address
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
			        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
			}
			macAddress = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// define path
		String scriptPath = getClass().protectionDomain.codeSource.location.path

		// calculate the deviceID
                MessageDigest digest = MessageDigest.getInstance("MD5")
                deviceID = new BigInteger(1,digest.digest("${macAddress}::${scriptPath}::${username}".getBytes())).toString(16).padLeft(32,"0")
	}

	/**
	 * getter for the deviceID
	 * @return String
	 */
	private String getDeviceID() {
		if (!deviceID) generateDeviceID()

		return deviceID
	}

	/**
	 * authenticate against the GSCF api
	 * @void
	 */
	private void authenticate() {
		// instantiate HTTP Builder
		def http = new HTTPBuilder( url )

		// set HTTP Basic credentials
		http.auth.basic username, password

		// perform authenticate call
		http.request( POST, JSON ) {
			uri.path	= "/${endPoint}/authenticate"
			uri.query	= [ deviceID: getDeviceID() ]

			// define user agent
			headers."User-Agent" = "GSCF-GroovyClient version ${version}"

			// success handler
			response.success = { resp, json ->
				if (resp.statusLine.statusCode == 200) {
					// set token and sequence
					sequence	= json.sequence as BigInteger
					token		= json.token as String
				} else {
					throw new Exception("server replied with an unexpected status code ${resp.statusLine.statusCode}");
				}
			}

			// failure handler
			response.failure = { resp ->
				if (resp.statusLine.statusCode == 401) {
					throw new Exception("password for user '${username}' is invalid (${"*".multiply(password.size())}) or user it not authorized to use the api at '${url}/${endPoint}' (has ROLE_CLIENT been assigned to the user?)")
				} else if (resp.statusLine.statusCode == 404) {
					throw new Exception("server appears to be down at '${url}'")
				} else if (resp.statusLine.statusCode == 409) {
					throw new Exception(json.error)
				} else {
					throw new Exception("server replied with an unexpected status code ${resp.statusLine.statusCode}")
				}
			}
		}
	}

	/**
	 * perform an api call
	 * @param String service to call
	 * @param LinkedHashMap arguments
	 * @param Boolean retried call?
	 * @return JSONObject
	 */
	public apiCall(String service) {
		apiCall(service, [:], false)
	}

	public apiCall(String service, LinkedHashMap args) {
		apiCall(service, args, false)
	}

	public apiCall(String service, LinkedHashMap args, Boolean retry) {
		def token	= getToken()
		def sequence	= getSequence()

		// calculate validation hash
		MessageDigest digest = MessageDigest.getInstance("MD5")
		String validationSum = new BigInteger(1,digest.digest("${token}${sequence}${apiKey}".getBytes())).toString(16).padLeft(32,"0")

		// define post arguments
		def arguments				= args
			arguments['deviceID'] 	= deviceID
			arguments['validation']	= validationSum

		// perform api call
		def http = new HTTPBuilder(url)

		http.request( POST, JSON ) {
			uri.path	= "/${endPoint}/${service}"
			uri.query	= arguments

			// define user agent
			headers."User-Agent" = "GSCF-GroovyClient version ${version}"

			// success handler
			response.success = { resp, json ->

				if (resp.statusLine.statusCode == 200) {
					return json
				} else {
					throw new Exception("server replied with an unexpected status code ${resp.statusLine.statusCode}");
				}
			}

			// failure handler
			response.failure = { resp ->

				def code = resp.statusLine.statusCode

				if (code == 401) {
					// unauthorized, check if this was a retry call
					if (retry) {
						// yes, seems like we are really denied access
						throw new Exception('Unautorized api call');
					} else {
						// unauthorized call, this may happen if the client and server
						// become out of sync (e.g. the sequence differs). Try to re-
						// authenticate to re-synchronize the sequence
						authenticate()

						// and retry the api call
						apiCall(service, args, true)
					}
				} else if (code == 404) {
					throw new Exception("the server appears to be down at ${url}/${endPoint}/${service}")
				} else if (code == 500) {
					throw new Exception("the server was not able to handle your request: status code ${resp.statusLine.statusCode}");
				} else {
					throw new Exception("server replied with an unexpected status code ${resp.statusLine.statusCode}");
				}
			}
		}
	}

	/**
	 * public call to fetch studies
	 */
	public getStudies() {
		return apiCall('getStudies')
	}

	/**
	 * public call to fetch all subjects of a study
	 */
	public getSubjectsForStudy(String studyToken) {
		return apiCall('getSubjectsForStudy', ['studyToken': studyToken])
	}

	/**
	 * public call to fetch all assays of a study
	 */
	public getAssaysForStudy(String studyToken) {
		return apiCall('getAssaysForStudy', ['studyToken': studyToken])
	}

	/**
	 * public call to fetch all event groups of a study
	 */
	public getEventGroupsForStudy(String studyToken) {
		return apiCall('getEventGroupsForStudy', ['studyToken': studyToken])
	}

	/**
	 * public call to fetch all events of a study
	 */
	public getEventsForStudy(String studyToken) {
		return apiCall('getEventsForStudy', ['studyToken': studyToken])
	}

	/**
	 * public call to fetch all events of a study
	 */
	public getSamplingEventsForStudy(String studyToken) {
		return apiCall('getSamplingEventsForStudy', ['studyToken': studyToken])
	}

	/**
	 * public call to fetch all sample of an assay
	 */
	public getSamplesForAssay(String assayToken) {
		return apiCall('getSamplesForAssay', ['assayToken': assayToken])
	}

	/**
	 * public call to fetch all measurements of an assay
	 */
	public getMeasurementDataForAssay(String assayToken) {
		return apiCall('getMeasurementDataForAssay', ['assayToken': assayToken])
	}

	/**
	 * public call to fetch all modules
	 */
	public getModules() {
		return apiCall('getModules')
	}

	/**
	 * public call to fetch all entity types
	 */
	public getEntityTypes() {
		return apiCall('getEntityTypes')
	}

	/**
	 * public call to fetch all entity types
	 */
	public getTemplatesForEntity(String entityType) {
		return apiCall('getTemplatesForEntity', ['entityType': entityType])
	}

	/**
	 * public call to fetch all fields for an entity
	 *
	 * entityType only will return the generic (model) fields only.
	 * entityToken will add the additional fields based on the template related to the entity
	 */
	public getFieldsForEntity(String entityType, String entityToken = '') {
		return apiCall('getFieldsForEntity', ['entityType': entityType, 'entityToken': entityToken])
	}

	/**
	 * public call to fetch all fields for an entity by template
	 */
	public getFieldsForEntityWithTemplate(String entityType, String templateToken) {
		return apiCall('getFieldsForEntityWithTemplate', ['entityType': entityType, 'templateToken': templateToken])
	}

	/**
	 * public call to create an entity with the fields provided
	 */
	public createEntity(String entityType, Map fields) {
		return apiCall('createEntity', ['entityType': entityType] + fields)
	}

	/**
	 * public call to create an entity with the fields provided with a template
	 */
	public createEntityWithTemplate(String entityType, String templateToken, Map fields) {
		return apiCall('createEntityWithTemplate', ['entityType': entityType, 'templateToken': templateToken] + fields)
	}
}
