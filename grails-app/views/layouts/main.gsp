<g:applyLayout name="module">
  <html>
  <head>
    <script src="${createLinkTo(dir: 'js', file: 'jquery-ui-1.8.14.custom.min.js')}" type="text/javascript"></script>
    <title><g:layoutTitle default="Metabolomics Module" /></title>
    <g:layoutHead />
  </head>

  <body>
  <content tag="topnav">
    <!-- Insert only li tags for the top navigation, without surrounding ul -->
    <li><a href="${resource(dir: '')}">Home</a></li>
    <li>
      <a href="#" onClick="return false;">GSCF</a>
      <ul class="subnav">
        <li>
          <g:link url="${org.codehaus.groovy.grails.commons.ConfigurationHolder.config.gscf.baseURL}">
            Go to GSCF
          </g:link>
        </li>
      </ul>
    </li>
  </content>
  <g:layoutBody />
  </body>
  </html>
</g:applyLayout>