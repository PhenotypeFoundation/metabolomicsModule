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

  </style>
</head>
<body>

<div id="uploadedFiles">
  <mm:uploadedFileList files="${files}" />
</div>

<div id="studyOverview">
      <mm:studyList/>
</div>

<br/>

</body>
</html>
