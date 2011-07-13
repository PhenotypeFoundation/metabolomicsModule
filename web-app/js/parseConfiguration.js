// Global variable holding the datamatrix
var dataMatrixTable;

function initParseConfigurationDialog() {
 // Init stuff
}

/**
* Submit the parser configuration form to the server
*/
function submitForm() {
    $('#pcform').submit();
}

/**
* Update the datatables datamatrix
*/
function updateDatamatrix(data, textStatus) {
    var jsonDatamatrix = eval(data);

    // show a spinner?

    // Is the datamatrix table filled already? Then destroy and clean it
    if (dataMatrixTable) dataMatrixTable.fnDestroy();
    $('#datamatrix').html('<table cellpadding="0" cellspacing="0" border="0" class="display" id="datamatrix"></table>');

    // Fill the datamatrix table with new data and properties
    dataMatrixTable = $('#datamatrix').dataTable({
                        "oLanguage": {
                            //"sInfo": "Page _START_ of _END_"
                                "sInfo": "Showing rows _START_ to _END_ from uploaded file."
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
                        "aaData": jsonDatamatrix.aaData,
                        "aoColumns": jsonDatamatrix.aoColumns
                      });

    // hide a spinner?
}