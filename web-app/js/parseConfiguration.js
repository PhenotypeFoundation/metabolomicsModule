// Global variable holding the datamatrix
var dataMatrixTable;
var parseConfigurationDialog;
var parseConfigurationDialogController = "parseConfiguration";

/**
 * Add control element listeners so when a control is changed the preview is updated
 */
function initParseConfigurationDialogListeners() {
    // Listen for control element changes, if so, submit the form
    $('#delimiter, #sheetIndex, #samplePerRow, #samplePerColumn').change( function() {
        submitForm("update");
    });

    // When the page is ready, read the parameters set in the form and send
    // them to the server. The returned JSON will update the data preview.
    submitForm("init");
}

/**
 * Method to open up a dialog to configure the parsing of the uploaded file
 * @param uploadedFileName filename of uploaded file
 * @param uploadedFileId id of uploaded file
 */
function openParseConfigurationDialog(uploadedFileName, uploadedFileId) {

    // Assign the dialog to the global variable
    parseConfigurationDialog = $("<div><img src='images/spinner.gif'></div>")
        .load(parseConfigurationDialogController + "?uploadedFileId=" + encodeURI(uploadedFileId))
        .dialog({
                modal: true,
                title: "Data Interpretation Settings - " + uploadedFileName,
                buttons: { 'Save': function() { submitForm("save"); }, 'Close': function() {$(this).dialog('close')} },
                width: 680,
                height: 520,
                // necessary to destroy object to make datatables appear after opening dialog for a second time
                beforeClose: function(event, ui) { $(this).remove(); }
        });
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
function updateDialog(jsonDataMatrix) {

    if (jsonDataMatrix.errorMessage != undefined) {
        updateStatus(jsonDataMatrix.errorMessage);
        destroyDataTable();
        return;
    }

    if (jsonDataMatrix.message != undefined) {
        updateStatus(jsonDataMatrix.message);
    }

    if (jsonDataMatrix.aaData == undefined) return

    // show a spinner?
    updateStatus("updating preview...");

    if (dataMatrixTable) {
        destroyDataTable();
    }

    dataMatrixTable = $('#dataMatrix').dataTable({
        "oLanguage": {
            "sInfo": "Showing rows _START_ to _END_ of uploaded file."
        },
        "sScrollX": "100%",
        "bScrollCollapse": false,
        "iDisplayLength": 5,
        "bLengthChange": false,
        "bSort" : false,
        "bFilter" : false,
        "aaData": jsonDataMatrix.aaData,
        "aoColumns": jsonDataMatrix.aoColumns
    });

    // hide a spinner?
    updateStatus("done");

}

function destroyDataTable() {
    $('div.dataMatrixContainer').html('<table cellpadding="0" cellspacing="0" border="0" id="dataMatrix"></table>');
}

/**
 * @param message message to put in the status bar
 */
function updateStatus(message) {
  $('#status').html('Status: ' + message);
}