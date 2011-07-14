<%--
 /**
 *  Index page for the Parse Configurator containing all controls
 *  and preview of the data
 *
 *  Copyright (C) 2011 Tjeerd Abma
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  @author Tjeerd Abma
 *  @since 20110710
 *
 *  Revision information:
 *
 *  $Author$
 *  $Rev$
 *  $Date$
 */
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">

  <pc:initParseConfigurationDialog/>

  <title>Parse Configuration panel</title>
  </head>
  <body>
    <div class="parseConfigDialog" id="parseConfigDialog">
        <g:formRemote name="pcform" onFailure="alert('No response from server')" before="updateStatistics('loading...')" onSuccess="updateDatamatrix(data, textStatus)" action="updateDatamatrix" url="${[action:'updateDatamatrix']}">
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
        </g:formRemote>
    </div>
  </body>
</html>