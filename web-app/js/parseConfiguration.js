// Global variable holding the datamatrix
var dataMatrixTable;
var parseConfigurationDialog;
var parseConfigurationDialogController = "parseConfiguration";

/**
 * Add control element listeners so when a control is changed the preview is updated
 */
function initParseConfigurationDialogListeners() {
    // Listen for control element changes, if so, submit the form
    $('#delimiter, #sheetIndex, #samplePerRow, #samplePerColumn, #sampleColumnIndex').change( function() {
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
 * Sends all form values to the controller
 */
function submitSaveForm() {
    var $hiddenInput = $('<input/>',{type:'hidden',id:"save",value:"save"});
    $hiddenInput.appendTo('form');
}

/**
 * Update the datatables datamatrix
 */
function updateDialog(data) {
    var maxSampleColumnIndex=10;

    if (data.errorMessage != undefined) {
        updateStatus(data.errorMessage);
        destroyDataTable();
        return;
    }

    if (data.message != undefined) {
        updateStatus(data.message);
    }

    // save action, so submit the form again in update mode
    if (data.formAction == "update") { submitForm(data.formAction); return }

    // if there is no data source defined, return. Only updating the message was necessary.
    if (data.ajaxSource == undefined) return;

    updateControls(data);

    destroyDataTable();

    dataMatrixTable = $('#dataMatrix').dataTable({
        "fnRowCallback": function( nRow, aData, iDisplayIndex ) {
            // Find the column in the row
			var dataCellObject = $('td:eq(' + data.sampleColumnIndex +')', nRow);
            var sampleName = aData[data.sampleColumnIndex];

            // If the datamatrix is linked to an assay it will contain the assay sample names
            if (data.assaySampleNames.length) {
                // the datamatrix sample name also exists in the assay sample collection?
                if ( $.inArray(sampleName, data.assaySampleNames) != -1 ) {
                    dataCellObject.html('<img src="images/sample_green.png" class="sampleColumnIcon"/>' + dataCellObject.html());
                } else { // the datamatrix samples doesn't exist in the assay sample collection
                    dataCellObject.html('<img src="images/sample_red.png" class="sampleColumnIcon"/>' + dataCellObject.html());
                }
            }

            dataCellObject.addClass('sampleColumnIndex');

			return nRow;
		},

        "fnInitComplete": function() {
            // Amount of columnt in a row
            var columnCount = Math.min(this.fnGetData()[0].length, maxSampleColumnIndex);

            var options = '';

            // Generate a list with options based on the amount of columns
            for (var i = 0; i < columnCount; i++) {
                options += '<option value="' + i + '">' + (i+1) + '</option>';
            }

            // Put the options list in the sampleColumnIndex dropdown box
            $("select#sampleColumnIndex").html(options);
            $("select#sampleColumnIndex option[value='" + data.sampleColumnIndex + "']").attr("selected", "selected");

        },

        "oLanguage": {
            "sInfo": "Showing rows _START_ to _END_ of _TOTAL_.",
            "sInfoFiltered": ''
        },
        "sScrollX": "100%",
        "bScrollCollapse": false,
        "iDisplayLength": 5,
        "bLengthChange": false,
        "bSort" : false,
        "bFilter" : false,
        "bServerSide": true,
        "bProcessing": true,
        "fnServerData": function ( sSource, aoData, fnCallback ) {
			$.ajax( {
				"dataType": 'json',
				"type": "POST",
				"url": sSource,
				"data": aoData,
				"success": fnCallback
			} );
		},
        "sAjaxSource": data.ajaxSource,
        "aoColumns": data.aoColumns
    });
}

/**
* Destroys/removes the datatables from the DOM
*/
function destroyDataTable() {
    $('div.dataMatrixContainer').html('<table id="dataMatrix"></table>');
}

/**
* Read the data object and set the form controls according to it
*
* @param data JSON-object containing information about the parse settings
*/
function updateControls(data) {
    $('#samplePerRow').attr('checked', !data.isColumnOriented);
    $('#samplePerColumn').attr('checked', data.isColumnOriented);
    if (data.parseInfo) $('#delimiter').val(data.parseInfo.delimiter);
}

/**
 * @param message message to put in the status bar
 */
function updateStatus(message) {
  $('#status').html('Status: ' + message);
}