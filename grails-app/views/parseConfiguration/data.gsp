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

	<script>
		<g:if test="${!controlsDisabled}">
		$(document).ready(function() {
			initParseConfigurationDialogListeners();
		});
		</g:if>
	</script>

	<title>Parse Configuration panel</title>
</head>

<body>
<div class="parseConfigDialog" id="parseConfigDialog">
	<g:formRemote name="pcform" onFailure="updateStatus('server does not respond')"
				  before="if (\$('#assayId').val()==null) {updateStatus('Please select an assay.'); return false}"
				  onSuccess="if (\$('#formAction').val() == 'save') {\$('#parseConfigurationDialog').dialog('close')}; updateDialog(data);" action="handleForm" url="${[action:'handleForm']}">
		<input type="hidden" name="filename" value="${uploadedFile.fileName}"/>
		<input id="formAction" type="hidden" name="formAction" value=""/>

		<div class="matrixOptions">

			<table id="matrixOptionsTable">
			<tr>
				<td>
					<pc:sampleColumnControl sampleColumnIndex="${uploadedFile?.sampleColumnIndex}"
									maxIndex="${uploadedFile?.columns}" disabled="${controlsDisabled}"/>
				</td><td>
					<pc:featureRowControl featureRowIndex="${uploadedFile?.featureRowIndex}" disabled="${controlsDisabled}"/>
				</td>
			</tr>
			<tr>
				<td>
					<pc:orientationControl isColumnOriented="${uploadedFile?.isColumnOriented}" disabled="${controlsDisabled}"/>
				</td><td>
					<g:if test="${parseInfo?.parserClassName=='ExcelParser'}">
						<pc:sheetSelectControl numberOfSheets="${parseInfo.numberOfSheets}"
											   sheetIndex="${parseInfo.sheetIndex}"
											   disabled="${controlsDisabled}"/>
					</g:if>
					<g:elseif test="${parseInfo?.parserClassName=='CsvParser'}">
						<pc:delimiterControl value="${parseInfo.delimiter}" delimiterNameMap="${parseInfo.delimiterNameMap}"
											 disabled="${controlsDisabled}"/>
					</g:elseif>
			</td>
			</tr>
			</table>

		</div>
		
		<div class="dataMatrixContainer">
		</div>

		<div class="assays">
			<pc:assaysControl assayId="${uploadedFile?.assay?.id}"/>
		</div>

		<div class="status">
			<pc:statusControl initialStatus="${errorMessage}"/>
		</div>
	</g:formRemote>
</div>
</body>
</html>