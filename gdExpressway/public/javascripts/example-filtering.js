// Run the script on DOM ready:
$(function(){
	//filtered chart
 	$('table')
 		.visualize({
	 		rowFilter: ':not(:last)',
	 		colFilter: ':not(:last-child)'
	 	})
	 	.before("<h2>B) Charted with filters to exclude totals data</h2><pre><code>$('table').visualize({<strong>rowFilter: ':not(:last)', colFilter: ':not(:last-child)'</strong>});</code></pre>");
 	
 	
});