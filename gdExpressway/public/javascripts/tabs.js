function tabs()
{
$(function() {

   
   $('div.tabs').tabs({
      ajaxOptions: {
     
         error: function(xhr, status, index, anchor) {
            $(anchor.hash).html('Error loading tab');
         }
      }
   });
});
}