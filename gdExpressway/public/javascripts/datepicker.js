function datepicker()
{
    $(function() {
       $(".datepicker").datepicker({dateFormat:"yymmdd"});
        $( ".datepicker" ).datepicker("option","dataFormat","yymmdd");
       
    });
}