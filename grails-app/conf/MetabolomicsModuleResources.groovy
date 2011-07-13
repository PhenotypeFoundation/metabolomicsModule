modules = {

    jqueryUi {

        dependsOn 'jquery'

        resource url: '/js/jquery-ui-1.8.14.custom.min.js'

    }

    parseConfiguration {

        dependsOn 'jqueryUi'

        resource url: '/js/parseConfiguration.js'
        resource url: '/css/parseConfiguration.css'

    }

}