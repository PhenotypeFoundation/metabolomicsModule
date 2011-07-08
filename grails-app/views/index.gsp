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
  <script type="text/javascript">
  $(document).ready(function() {
    <mm:previewDialog id="previewLink"/>
  });
  </script>

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

<div id="preview">
  <a href="#" id="previewLink">Open preview dialog</a>
</div>

</body>
</html>
