<%@ page import="grails.converters.JSON" %>

var dp = ${dialogProperties as JSON};

dp['uploadedFileId'] = file.fileId;
dp['fileName'] = file.fileName;

openParseConfigurationDialog(dp);