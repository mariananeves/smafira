(function(e){if(typeof exports=="object"&&typeof module=="object")e(require("../../lib/codemirror"));else if(typeof define=="function"&&define.amd)define(["../../lib/codemirror"],e);else e(CodeMirror)})(function(e){"use strict";function t(e){e.operation(function(){f(e)})}function n(e){if(e.state.markedSelection.length)e.operation(function(){u(e)})}function o(e,t,n,o){if(s(t,n)==0)return;var u=e.state.markedSelection;var a=e.state.markedSelectionStyle;for(var f=t.line;;){var l=f==t.line?t:i(f,0);var c=f+r,h=c>=n.line;var p=h?n:i(c,0);var d=e.markText(l,p,{className:a});if(o==null)u.push(d);else u.splice(o++,0,d);if(h)break;f=c}}function u(e){var t=e.state.markedSelection;for(var n=0;n<t.length;++n)t[n].clear();t.length=0}function a(e){u(e);var t=e.listSelections();for(var n=0;n<t.length;n++)o(e,t[n].from(),t[n].to())}function f(e){if(!e.somethingSelected())return u(e);if(e.listSelections().length>1)return a(e);var t=e.getCursor("start"),n=e.getCursor("end");var i=e.state.markedSelection;if(!i.length)return o(e,t,n);var f=i[0].find(),l=i[i.length-1].find();if(!f||!l||n.line-t.line<r||s(t,l.to)>=0||s(n,f.from)<=0)return a(e);while(s(t,f.from)>0){i.shift().clear();f=i[0].find()}if(s(t,f.from)<0){if(f.to.line-t.line<r){i.shift().clear();o(e,t,f.to,0)}else{o(e,t,f.from,0)}}while(s(n,l.to)<0){i.pop().clear();l=i[i.length-1].find()}if(s(n,l.to)>0){if(n.line-l.from.line<r){i.pop().clear();o(e,l.from,n)}else{o(e,l.to,n)}}}e.defineOption("styleSelectedText",false,function(r,i,s){var o=s&&s!=e.Init;if(i&&!o){r.state.markedSelection=[];r.state.markedSelectionStyle=typeof i=="string"?i:"CodeMirror-selectedtext";a(r);r.on("cursorActivity",t);r.on("change",n)}else if(!i&&o){r.off("cursorActivity",t);r.off("change",n);u(r);r.state.markedSelection=r.state.markedSelectionStyle=null}});var r=8;var i=e.Pos;var s=e.cmpPos})