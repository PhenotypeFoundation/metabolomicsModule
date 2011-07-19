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

<script>
  $(document).ready(function() {
   <pc:dialog id="dialogLink"/>
});
</script>

</head>
<body>

<div id="uploadedFiles">

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
