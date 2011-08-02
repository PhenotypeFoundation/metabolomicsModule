// Global variable holding the datamatrix
var dataMatrixTable;
var parseConfigurationDialog;
var parseConfigurationDialogController = "parseConfiguration";

/**
* Add control element listeners so when a control is changed the preview is updated
*/
function initParseConfigurationDialogListeners() {
    // Listen for control element changes, if so, submit the form
    $('#fileType, #samplePerRow, #samplePerColumn').change( function() {
        submitForm("update");
    });

    // When the page is ready, read the parameters set in the form and send
    // them to the server. The returned JSON will update the data preview.
    $(document).ready(function() {
      submitForm("init");
    });
}

/**
* Method to open up a dialog to configure the parsing of the uploaded file
* @param file filename of uploaded file
*/
function openParseConfigurationDialog(uploadedFileName, uploadedFileId) {

    // Assign the dialog to the global variable
    parseConfigurationDialog = $("<div></div>")
        .load(parseConfigurationDialogController + "?uploadedFileName=" + encodeURI(uploadedFileName) + "&uploadedFileId=" + encodeURI(uploadedFileId))
        .dialog({
                autoOpen: false,
                modal: true,
                title: "Data Interpretation Settings - " + uploadedFileName,
                buttons: { 'Save': function() { submitForm("save"); }, 'Close': function() { $(this).remove(); }  },
                width: 680,
                height: 520
        });

    parseConfigurationDialog.dialog("open");
}

/**
* Submit the parser configuration form to the server
*/
function submitForm(formAction) {

    $("#formAction").val(formAction);

    $('#pcform').submit();
}

/**
* Update the datatables datamatrix
*/
function updateDialog(jsonDataMatrix, textStatus) {

    if (jsonDataMatrix.errorMessage != undefined) {
        updateStatus(jsonDataMatrix.errorMessage);
        return;
    }

    if (jsonDataMatrix.message != undefined) {
        updateStatus(jsonDataMatrix.message);
    }

    if (jsonDataMatrix.aaData == undefined) return

    // show a spinner?
    updateStatus("updating preview...");

    // Is the datamatrix table filled already? Then destroy and clean it
    if (dataMatrixTable) dataMatrixTable.fnDestroy();
    $('#datamatrix').html('<table cellpadding="0" cellspacing="0" border="0" class="display" id="datamatrix"></table>');

    // Fill the datamatrix table with new data and properties
    dataMatrixTable = $('#datamatrix').dataTable({
                        "oLanguage": {
                            //"sInfo": "Page _START_ of _END_"
                                "sInfo": "Showing rows _START_ to _END_ of uploaded file."
                                },
                        "sScrollX": "100%",
                        "sScrollY": "120px",
                        "bScrollCollapse": true,
                        "iDisplayLength": 5,
                        "aLengthMenu": [
                          [5, 10, 25, 50],
                          [5, 10, 25, "All"]
                        ],
                        "bSort" : false,
                        "bFilter" : false,
                        "aaData": jsonDataMatrix.aaData,
                        "aoColumns": jsonDataMatrix.aoColumns
                      });

    // hide a spinner?
    updateStatus("done");
}

/**
* @param message message to put in the status bar
*/
function updateStatus(message) {
  $('#status').html('Status: ' + message);
}