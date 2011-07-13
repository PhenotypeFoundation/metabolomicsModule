// Global variable holding the datamatrix
var dataMatrixTable;

function initParseConfigurationDialog() {
 // Init stuff
}

function submitForm() {
    $('#pcform').submit();
}

function updateParseConfiguration() {

   // show the please wait spinner
   //   showSpinner();

          // Perform an AJAX call to send configuration parameters to the controller. The controller will
          // use the parameters to process the data server side and finally return it back as a JSON object.
          $.ajax({
                    type: "POST",
                    data: "param1=" + $("#param1").val() + "&param2="+ $("#param2").val(),
                    url: "parseConfiguration/getDatamatrixAsJSON",
                    success: function(msg) {
                        // Evaluate the returned JSON data to create objects of it
                        var jsonDatamatrix = eval(msg);

                        // Is the datamatrix table filled already? Then destroy and clean it
                        if (dataMatrixTable) dataMatrixTable.fnDestroy();
                        $('#datamatrix').html('<table cellpadding="0" cellspacing="0" border="0" class="display" id="datamatrix"></table>');

                        // Fill the datamatrix table with new data and properties
                        dataMatrixTable = $('#datamatrix').dataTable({
                                "oLanguage": {
                                    //"sInfo": "Page _START_ of _END_"
                                    "sInfo": "Showing rows _START_ to _END_ (as read from Excel, including the header)"
                                },
                                "sScrollX": "100%",
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
                    },
                    complete: function() {
                      // hide the spinner
                      //hideSpinner();
                    }
                  });
}