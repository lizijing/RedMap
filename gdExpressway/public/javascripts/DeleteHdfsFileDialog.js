      
    $(function() {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $( "#dialog:ui-dialog" ).dialog( "destroy" );
        
       
       $( "#dialog-form2" ).dialog({
            autoOpen: false,
            height: 300,
            width: 350,
            modal: true,
            buttons: {
                "DeleteHdfsFile": function() {
             
                    var bValid = true;
                    allFields.removeClass( "ui-state-error" );

                  
                },
                Cancel: function() {
                    $( this ).dialog( "close" );
                }
            },
            close: function() {
                allFields.val( "" ).removeClass( "ui-state-error" );
            }
        });

        $( "#DeleteHdfsFile" )
            .button()
            .click(function() {
                $( "#dialog-form2" ).dialog( "open" );
               
            });
    });
    