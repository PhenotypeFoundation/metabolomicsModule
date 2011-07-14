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
      background-color: #bc8f8f;
    }

    /*prevent our floating divs from overlapping footer */
    #footer {
      clear: both;
    }

    .uploadDropArea {
      border: 3px dashed #ccc;
      height: 50px;
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
  %{--<uploadr:add--}%
          %{--name="myUniqueUploadName"--}%
          %{--path="/tmp">--}%
    %{--<uploadr:onSuccess>--}%
      %{--$.ajax('${g.createLink(controller: 'parsedFile', action: 'uploadFinished', plugin: 'dbxpModuleStorage')}'+'?fileName=' + file.fileName)--}%
    %{--</uploadr:onSuccess>--}%
  %{--</uploadr:add>--}%

  %{--<uploadr:demo/>--}%

  <uploadr:add name="uploadDropArea" path="/tmp" class="uploadDropArea" >

    <uploadr:onStart>
      console.log('Upload started');
      %{--add a div to upload list--}%
    </uploadr:onStart>

    <uploadr:onProgress>
      console.log('Progress: ' + percentage);
      %{--advance progress bar--}%
      return false; // to disable default progress handler
    </uploadr:onProgress>

    <uploadr:onSuccess>
      $.ajax('${g.createLink(controller: 'uploadedFile', action: 'uploadFinished', plugin: 'dbxpModuleStorage')}'+'?fileName=' + file.fileName)
      %{--re-render template containing uploaded files--}%
    </uploadr:onSuccess>

    <uploadr:onFailure>
      %{--color upload div red?--}%
    </uploadr:onFailure>

  </uploadr:add>

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
