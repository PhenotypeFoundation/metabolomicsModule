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
    }
    
    .studyList {

    }

    .studyName {

    }

    .assayList {

    }

    .assayName {

    }

    /*prevent our floating divs from overlapping footer */
    @suppresswarnings
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

    uploader goes here ...

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
