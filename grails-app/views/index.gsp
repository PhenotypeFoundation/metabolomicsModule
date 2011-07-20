<html>
<head>
  <meta name="layout" content="main" />
  <style type="text/css">
    #uploadedFiles {
      float: left;
      width: 50%;
    }

    #studyOverview {
      float: left;
      width: auto;
    }

    .sampleCount {
      float: right;
    }

    .studyList {
      width: 100%;
    }

    .studyTag {

    }

    .assayList {

    }

    .assayTag {

    }

    .uploadedFileList {

    }

    .uploadedFileTag {
      background-color: #dcdcdc;
    }

    /*prevent our floating divs from overlapping footer */
    #footer {
      clear: both;
    }

    .buttons {
      margin-top: 1px;
    }

  </style>
</head>
<body>

%{--<div id="uploadedFiles">--}%

  %{--<mm:uploadedFileList/>--}%

%{--</div>--}%

<div id="studyOverview">
      <mm:studyList/>
</div>

<br/>

<uploadr:add name="myuploadr" path="/tmp">
    <uploadr:onSuccess>
        console.log(file.fileName)
    </uploadr:onSuccess>
</uploadr:add>

<div id="configInfo">
  %{--<pc:popupDialog uploadedFileId="100">Your custom text</pc:popupDialog>--}%
    <a href="#" onclick="javascript:openParseConfigurationDialog('file1')"/>bestand1</a><br/>
    <a href="#" onclick="javascript:openParseConfigurationDialog('file2')"/>bestand2</a><br/>
    <a href="#" onclick="javascript:openParseConfigurationDialog('file3')"/>bestand3</a><br/>
    <a href="#" onclick="javascript:openParseConfigurationDialog('file4')"/>bestand4</a><br/>
</div>

</body>
</html>
