String.prototype.score=function(b,g){g=g||0;if(0==b.length)return 0.9;if(b.length>this.length)return 0;for(var a=b.length;0<a;a--){var c=b.substring(0,a),d=this.indexOf(c);if(!(0>d||d+b.length>this.length+g)){var c=this.substring(d+c.length),e=null,e=a>=b.length?"":b.substring(a),e=c.score(e,g+d);if(0<e){a=this.length-c.length;if(0!=d){var h=0,f=this.charCodeAt(d-1);if(32==f||9==f)for(h=d-2;0<=h;h--)f=this.charCodeAt(h),a-=32==f||9==f?1:0.15;else a-=d}a+=e*c.length;return a/=this.length}}}return 0};
jQuery.fn.liveUpdate=function(b){function g(){var b=jQuery.trim(jQuery(this).val().toLowerCase()),e=[];(b=b.split(" ").join(""))?(a.hide(),jQuery("#fileinfo ul#contents li").hide(),jQuery("#fileinfo table#contents td").parent().hide(),c.each(function(a){var f=this.score(b);0<f&&e.push([f,a])}),jQuery.each(e.sort(function(a,b){return b[0]-a[0]}),function(){jQuery(a[this[1]]).show()}),jQuery("#filetree ul").find("li > a").each(function(){var a=$(this).attr("rel");"none"!=$(this).css("display")&&(jQuery('#fileinfo ul#contents li[title="'+
a+'"]').show(),jQuery('#fileinfo table#contents td[title="'+a+'"]').parent().show())})):(a.show(),jQuery("#fileinfo ul#contents li").show(),jQuery("#fileinfo table#contents td").parent().show())}b=jQuery(b);if(b.length){var a=b.find("li > a"),c=a.map(function(){return this.innerHTML.toLowerCase()});this.keyup(g).keyup().parents("form").submit(function(){return!1})}return this};
$(document).ready(function(){$("#q").focus(function(){$("#search span.q-inactive").css("display","none")});$("#search span.q-inactive").click(function(){$(this).css("display","none");$("#q").focus()});$("#q").bind("blur keyup",function(){""==$(this).val()?($("#search span.q-inactive").css("display","inline"),$("#search a.q-reset").css("display","none")):$("#search a.q-reset").css("display","inline-table")});$("#search a.q-reset").click(function(){$("#q").val("");$("#q").liveUpdate("#filetree ul").blur();
return!1});$("#search").submit(function(){return!1});$("#q").blur()});

