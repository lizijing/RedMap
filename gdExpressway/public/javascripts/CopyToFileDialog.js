$(function() {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $( "#dialog:ui-dialog" ).dialog( "destroy" );
           $( "#tabs" ).tabs();
       
       $( "#dialog-form" ).dialog({
            autoOpen: false,
            height: 300,
            width: 350,
            modal: true,
            buttons: {
                "CopyToHdfs": function() {
             
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
        
          $( "#CopyToHdfs" )
            .button()
            .click(function() {
                $( "#dialog-form" ).dialog( "open" );
               
            });
    });