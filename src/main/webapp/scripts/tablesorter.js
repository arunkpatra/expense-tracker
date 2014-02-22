$(document).ready(function() {
	$.extend($.tablesorter.themes.bootstrap, {
		table : 'table table-bordered',
		caption : 'caption',
		header : 'bootstrap-header',
		footerRow : '',
		footerCells : '',
		icons : '',
		sortNone : 'bootstrap-icon-unsorted',
		sortAsc : 'icon-chevron-up glyphicon glyphicon-chevron-up', 
		sortDesc : 'icon-chevron-down glyphicon glyphicon-chevron-down', 
		active : '', 
		hover : '',
		filterRow : '', 
		even : '',
		odd : '' 
	});
	$(function() {
		$("table").tablesorter({
			theme : "bootstrap",
			widthFixed : true,
			headerTemplate : '{content} {icon}', 
			widgets : [ "uitheme" ]
		});
	});
});