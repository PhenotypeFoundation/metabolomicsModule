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
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">

  <link rel="stylesheet" href="${createLinkTo(dir:'css',file: 'parseConfiguration.css')}" />

  <pc:initParseConfigurationDialog/>

  <title>Parse Configuration panel</title>
  </head>
  <body>
    <div class="parseConfigDialog" id="parseConfigDialog">
        <g:formRemote name="pcform" on404="alert('not found!')" update="something" action="show" url="${[action:'show']}">
        <div class="fileType">
            <pc:fileTypeControl/>
         </div>
        <div class="platform">
            <pc:platformControl/>
        </div>
        <div class="assays">
            <pc:assaysControl/>
        </div>
        <div class="orientation">
            <pc:orientationControl/>
        </div>
        <div class="normalized">
            <pc:normalizedControl/>
        </div>
        <div class="datamatrix">
            <pc:dataMatrixControl/>
        </div>
        <div class="statistics">
            <pc:statisticsControl/>
        </div>
        <input type="submit" value="Send"/>
        </g:formRemote>
    </div>
  </body>
</html>