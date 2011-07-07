<html>
<head>
  <meta name="layout" content="main" />
  <style type="text/css">
    #uploadedFiles {
      float: left;
      width: 50%;
    }

    /*prevent our floating divs from overlapping footer */
    @suppresswarnings
    #footer {
      clear: both;
    }
  </style>
</head>
<body>


<div id="uploadedFiles">

  <div id="uploadArea">

    uploader goes here

  </div>

  <mm:uploadedFileList/>

</div>

<div id="studiesOverview">

  <mm:studyList/>

</div>

</body>
</html>
