(function(e){if(typeof exports=="object"&&typeof module=="object")e(require("../../lib/codemirror"));else if(typeof define=="function"&&define.amd)define(["../../lib/codemirror"],e);else e(CodeMirror)})(function(e){"use strict";e.defineMode("properties",function(){return{token:function(e,t){var n=e.sol()||t.afterSection;var r=e.eol();t.afterSection=false;if(n){if(t.nextMultiline){t.inMultiline=true;t.nextMultiline=false}else{t.position="def"}}if(r&&!t.nextMultiline){t.inMultiline=false;t.position="def"}if(n){while(e.eatSpace());}var i=e.next();if(n&&(i==="#"||i==="!"||i===";")){t.position="comment";e.skipToEnd();return"comment"}else if(n&&i==="["){t.afterSection=true;e.skipTo("]");e.eat("]");return"header"}else if(i==="="||i===":"){t.position="quote";return null}else if(i==="\\"&&t.position==="quote"){if(e.next()!=="u"){t.nextMultiline=true}}return t.position},startState:function(){return{position:"def",nextMultiline:false,inMultiline:false,afterSection:false}}}});e.defineMIME("text/x-properties","properties");e.defineMIME("text/x-ini","properties")})