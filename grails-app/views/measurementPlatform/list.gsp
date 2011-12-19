<html>
<head>
	<meta name="layout" content="main"/>
	<script type="text/javascript">
		$(document).ready(function() {
			// ignore submitting though enter key
			$('input').bind('keydown', function(event) {
				return (event.keyCode != 13);
			});
		});
	</script>
</head>

<body>
<g:if test="${flash.errorMessage}">
	<div class="errorMessage">${flash.errorMessage}</div>
</g:if>
<h1>Measurement Platform(s)</h1>
<g:form action="add">
	<input name="measurementplatform" id="measurementplatform" type="text"/>
	<g:submitToRemote url="[action:'add']" value="add" update="measurementplatformlist"
					  onSuccess="\$('#measurementplatform').val('')"/>
</g:form><br/>

<div id="measurementplatformlist">
	<mm:measurementPlatformOverview measurementPlatforms="${measurementPlatforms}"/>
</div>
</body>
</html>