<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
  <head>
	  
	  <script>
		  $(document).ready(function(){
			  initFeaturesPage( ${[aaData: data, aoColumns: columns, headerSuggestions: headerSuggestions] as JSON} );
		  });
	  </script>

  </head>
  <body>
  <div class="parseConfigDialog" id="parseConfigDialog">

  <g:formRemote name="pcform"
	  			before="if (\$('#platformVersionId').val()==null) {updateStatus('Please select a measurement platform version.'); return false}"
	  			onFailure="updateStatus('Unable to save feature file.')"
	  			onSuccess="\$('#parseConfigurationDialog').dialog('close');"
				action="handleFeatureForm"
				url="${[action:'handleFeatureForm']}">
    <input type="hidden" name="filename" value="${uploadedFile.fileName}" />
    <input id="formAction" type="hidden" name="formAction" value="" />

	<div class="dataMatrixContainer"></div>

	<pc:platformControl/>
  	<pc:statusControl initialStatus="${errorMessage}"/>

  </g:formRemote>
  </div>
  </body>
</html>