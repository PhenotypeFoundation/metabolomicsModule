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
<h1>Measurement Platform</h1>

<g:if test="${flash.message}">
	<div class="errorMessage">${flash.message}</div>
</g:if>

<g:if test="${params.edit}">
	<g:form action="view" id="${measurementPlatform?.id}">
		<input name="edit" type="hidden" value="false"/>
		<table>
			<tr>
				<td valign="top">name</td>
				<td valign="top"><input name="measurementPlatformName" value="${measurementPlatform?.name}"
										type="text"/></td>
			</tr>
			<tr>
				<td valign="top">description</td>
				<td valign="top">
					<textarea name="measurementPlatformDescription">${measurementPlatform.description}</textarea>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td valign="top">
					<g:submitButton name="update" value="update"/>
				</td>
			</tr>
		</table>
	</g:form>
</g:if>
<g:else>
	<h2><g:link url="?edit=true">${measurementPlatform?.name}</g:link></h2>

	<g:if test="${measurementPlatform.description}">
		<p><g:link url="?edit=true">${measurementPlatform.description}</g:link></p>
	</g:if>
</g:else>

<ul>
	<g:each in="${measurementPlatform.versions}" var="measurementPlatformVersion">
		<li>
			<g:link controller="measurementPlatformVersion" action="view" id="${measurementPlatformVersion.id}">
				${measurementPlatform?.name} ${measurementPlatformVersion.versionNumber}
			</g:link>
		</li>
	</g:each>
</ul>

<g:link action="delete" id="${measurementPlatform.id}"
		onclick="return confirm('Are you sure you want to delete this Measurement Platform?')"><input type="button"
																									  value="delete"/></g:link>
</body>
</html>  	