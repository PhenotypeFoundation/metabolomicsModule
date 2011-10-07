<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
  <head>
	  
	  <script>
		  $(document).ready(function(){
			  initFeaturesPage( ${[aaData: data, aoColumns: columns] as JSON} );
		  });
	  </script>

  </head>
  <body>
  <div class="parseConfigDialog" id="parseConfigDialog">
  <g:formRemote name="pcform" onFailure="updateStatus('server does not respond')"
                onSuccess="\$('#parseConfigurationDialog').dialog('close');" action="handleFeatureForm" url="${[action:'handleFeatureForm']}">
    <input type="hidden" name="filename" value="${uploadedFile.fileName}" />
    <input id="formAction" type="hidden" name="formAction" value="" />

	<div class="dataMatrixContainer"></div>

	<pc:platformControl/>
  	<pc:statusControl initialStatus="initial status"/>

  </g:formRemote>
  </div>
  </body>
</html>