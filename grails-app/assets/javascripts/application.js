// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better 
// to create separate JavaScript files as needed.
//
//= require jquery
//= require_tree .
//= require_self

if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}



function toggleDetails(id) {
	var item = document.getElementById(id);
	var but = document.getElementById('b-' + id);
	if (item.style.display == 'none') {
		item.style.display = ''
		but.innerHTML = "Abstract verbergen";
	} else {
		item.style.display = 'none'
		but.innerHTML = "Abstract anzeigen";
	}
}

function toggleInfoDetails(id) {
	var item = document.getElementById(id);
	var but = document.getElementById('b-' + id);
	if (item.style.display == 'none') {
		item.style.display = ''
		but.innerHTML = "Zusatzinformationen verbergen";
	} else {
		item.style.display = 'none'
		but.innerHTML = "Zusatzinformationen anzeigen";
	}
}
