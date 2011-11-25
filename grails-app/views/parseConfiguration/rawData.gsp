<%--
  Created by IntelliJ IDEA.
  User: siemensikkema
  Date: 11/24/11
  Time: 2:42 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head></head>

<body>

<div class="parseConfigDialog" id="parseConfigDialog">
	<g:formRemote name="pcform" onFailure="updateStatus('server does not respond')"
				  before="if (\$('#formAction').val() == 'save' && \$('#assayId').val()==null) {updateStatus('Please select an assay.'); return false}"
				  onSuccess="\$('#parseConfigurationDialog').dialog('close')"
				  action="handleRawDataForm" url="${[action:'handleRawDataForm']}">
		<input type="hidden" name="filename" value="${uploadedFile.fileName}"/>
		<input id="formAction" type="hidden" name="formAction" value=""/>

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