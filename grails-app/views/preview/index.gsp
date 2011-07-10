<%--
  Created by IntelliJ IDEA.
  User: tjeerd
  Date: 7/9/11
  Time: 9:34 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
      <script type="text/javascript">
        $(document).ready(function() {
            $("#datacolumns").dropdownchecklist( { width: 150, height:100 } );
        });
        </script>

  <title>Preview data</title>
  </head>
  <body>

    <mm:previewAssaysControl/>
    <mm:previewDataColumnsControl/>
    <mm:previewDataControl/>
    <mm:previewFeatureRowControl/>
    <mm:previewFileTypeControl/>
    <mm:previewOneParameterControl/>
    <mm:previewOrientationControl/>
    <mm:previewPlatformControl/>
    <mm:previewSampleColumnControl/>
  </body>
</html>