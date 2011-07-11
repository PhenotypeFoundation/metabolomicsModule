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
  <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'preview.css')}" />

  <title>Preview data</title>
  </head>
  <body>
    <div class="previewpanel" id="previewpanel">
        <div class="fileType">
            <mm:previewFileTypeControl/>
         </div>

        <div class="platform">
            <mm:previewPlatformControl/>
        </div>

        <div class="assays">
            <mm:previewAssaysControl/>
        </div>

        <div class="orientation">
            <mm:previewOrientationControl/>
        </div>

        <div class="normalized">
            <mm:previewNormalizedControl/>
        </div>


        <div class="datamatrix">
            <mm:previewDataMatrixControl/>
        </div>

        <div class="statistics">
            <mm:previewStatisticsControl/>
        </div>


        <mm:previewFeatureRowControl/>

        <mm:previewOneParameterControl/>



    </div>
  </body>
</html>