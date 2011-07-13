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

    .studyName {

    }

    .assayList {

    }

    .assayName {

    }

    /*prevent our floating divs from overlapping footer */
    #footer {
      clear: both;
    }

  </style>

<script>
  $(document).ready(function() {
   <pc:dialog id="dialogLink"/>
});
</script>

</head>
<body>


<div id="uploadedFiles">

  <div id="uploadArea">

    <uploadr:add
            name="myUniqueUploadName"
            path="/tmp">
      <uploadr:onSuccess>
        $.ajax(\'${g.createLink(controller: 'parsedFile', action: 'uploadFinished', plugin: 'dbxpModuleStorage')}\'+'?fileName=' + file.fileName)
      </uploadr:onSuccess>
    </uploadr:add>

    %{--<uploadr:demo/>--}%
  </div>

  <mm:uploadedFileList/>

</div>

<div id="studyOverview">

  <mm:studyList/>

</div>

<div id="configInfo">
  <a href="#" id="dialogLink">Open Parse Configuration dialog</a>
</div>

</body>
</html>
