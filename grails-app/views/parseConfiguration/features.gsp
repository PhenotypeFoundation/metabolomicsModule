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

  <div class="dataMatrixContainer"></div>

  </body>
</html>