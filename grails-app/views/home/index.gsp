<html>
 <head>
  <meta name="layout" content="main" />
 </head>
 <body>
  <div id="uploadedFiles">
   <mm:uploadedFileList files="${files}"/>
  </div>
  <div id="studyOverview">
   <g:render template="studyList"/>
  </div>
 </body>
</html>