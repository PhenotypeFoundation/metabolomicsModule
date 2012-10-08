grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.server.port.http = 8083

//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        grailsRepo "http://grails.org/plugins"
        mavenCentral()

        // grails 1.3.9 does not seem to properly inherit maven repo's from plugins
        // so explicitely put ontocat in here. When upgraded to Grails 2.x this can
        // probably be removed
        // mavenRepo "http://ontocat.sourceforge.net/maven/repo"

        // other maven repo's
        mavenRepo "http://nexus.nmcdsp.org/content/repositories/releases"
        mavenRepo "http://repository.springsource.com/maven/bundles/release"
        mavenRepo "http://repository.springsource.com/maven/bundles/external"
        mavenRepo "http://repository.springsource.com/maven/libraries/release"
        mavenRepo "http://repository.springsource.com/maven/libraries/external"
    }
	plugins {
        compile(":hibernate:$grailsVersion")
        compile ":tomcat:$grailsVersion"

		compile ":dbxp-module-base:0.5.0"
        compile ":dbxp-module-storage:0.4.1"

		compile ":mongodb:1.0.0.RC3"
		compile ':famfamfam:1.0.1'
		compile ':jquery:1.7.1'
		compile ":jquery-ui:1.8.15"
		compile (":jquery-datatables:1.7.5"){
			transitive = false
		}
		compile ":dbxp-chemistry:0.1.2"

        compile ':grom:0.2.3'
        compile ":uploadr:0.6.0"
        compile ':matrix-importer:0.2.3.5'
        compile ':trackr:0.7.3'
        runtime ':modernizr:2.0.6'
        runtime ':resources:1.1.6'
	}
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		// runtime 'com.github.groovy-wslite:groovy-wslite:0.1'
        // runtime 'mysql:mysql-connector-java:5.1.13'
		// runtime 'postgresql:postgresql:9.0-801.jdbc4'
		runtime 'postgresql:postgresql:9.1-901.jdbc3'
		runtime 'javassist:javassist:3.12.1.GA'
        runtime('org.codehaus.groovy.modules.http-builder:http-builder:0.5.2') {
            excludes 'xalan'
            excludes 'xml-apis'
            excludes 'groovy'
        }

    }
}

//grails.plugin.location.'dbxp-module-base'     = '../dbxpModuleBase'
//grails.plugin.location.'dbxp-module-storage'  = '../dbxpModuleStorage'
//grails.plugin.location.'matrixImporter'     = '../matrixImporter'
//grails.plugin.location.'uploadr'            = '../uploadr'

