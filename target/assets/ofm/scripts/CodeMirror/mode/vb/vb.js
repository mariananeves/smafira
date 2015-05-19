(function(e){if(typeof exports=="object"&&typeof module=="object")e(require("../../lib/codemirror"));else if(typeof define=="function"&&define.amd)define(["../../lib/codemirror"],e);else e(CodeMirror)})(function(e){"use strict";e.defineMode("vb",function(e,t){function r(e){return new RegExp("^(("+e.join(")|(")+"))\\b","i")}function N(e,t){t.currentIndent++}function C(e,t){t.currentIndent--}function k(e,t){if(e.eatSpace()){return null}var r=e.peek();if(r==="'"){e.skipToEnd();return"comment"}if(e.match(/^((&H)|(&O))?[0-9\.a-f]/i,false)){var l=false;if(e.match(/^\d*\.\d+F?/i)){l=true}else if(e.match(/^\d+\.\d*F?/)){l=true}else if(e.match(/^\.\d+F?/)){l=true}if(l){e.eat(/J/i);return"number"}var c=false;if(e.match(/^&H[0-9a-f]+/i)){c=true}else if(e.match(/^&O[0-7]+/i)){c=true}else if(e.match(/^[1-9]\d*F?/)){e.eat(/J/i);c=true}else if(e.match(/^0(?![\dx])/i)){c=true}if(c){e.eat(/L/i);return"number"}}if(e.match(y)){t.tokenize=L(e.current());return t.tokenize(e,t)}if(e.match(a)||e.match(u)){return null}if(e.match(o)||e.match(i)||e.match(p)){return"operator"}if(e.match(s)){return null}if(e.match(x)){N(e,t);t.doInCurrentLine=true;return"keyword"}if(e.match(b)){if(!t.doInCurrentLine)N(e,t);else t.doInCurrentLine=false;return"keyword"}if(e.match(w)){return"keyword"}if(e.match(S)){C(e,t);C(e,t);return"keyword"}if(e.match(E)){C(e,t);return"keyword"}if(e.match(g)){return"keyword"}if(e.match(m)){return"keyword"}if(e.match(f)){return"variable"}e.next();return n}function L(e){var r=e.length==1;var i="string";return function(s,o){while(!s.eol()){s.eatWhile(/[^'"]/);if(s.match(e)){o.tokenize=k;return i}else{s.eat(/['"]/)}}if(r){if(t.singleLineStringErrors){return n}else{o.tokenize=k}}return i}}function A(e,t){var r=t.tokenize(e,t);var i=e.current();if(i==="."){r=t.tokenize(e,t);i=e.current();if(r==="variable"){return"variable"}else{return n}}var s="[({".indexOf(i);if(s!==-1){N(e,t)}if(T==="dedent"){if(C(e,t)){return n}}s="])}".indexOf(i);if(s!==-1){if(C(e,t)){return n}}return r}var n="error";var i=new RegExp("^[\\+\\-\\*/%&\\\\|\\^~<>!]");var s=new RegExp("^[\\(\\)\\[\\]\\{\\}@,:`=;\\.]");var o=new RegExp("^((==)|(<>)|(<=)|(>=)|(<>)|(<<)|(>>)|(//)|(\\*\\*))");var u=new RegExp("^((\\+=)|(\\-=)|(\\*=)|(%=)|(/=)|(&=)|(\\|=)|(\\^=))");var a=new RegExp("^((//=)|(>>=)|(<<=)|(\\*\\*=))");var f=new RegExp("^[_A-Za-z][_A-Za-z0-9]*");var l=["class","module","sub","enum","select","while","if","function","get","set","property","try"];var c=["else","elseif","case","catch"];var h=["next","loop"];var p=r(["and","or","not","xor","in"]);var d=["as","dim","break","continue","optional","then","until","goto","byval","byref","new","handles","property","return","const","private","protected","friend","public","shared","static","true","false"];var v=["integer","string","double","decimal","boolean","short","char","float","single"];var m=r(d);var g=r(v);var y='"';var b=r(l);var w=r(c);var E=r(h);var S=r(["end"]);var x=r(["do"]);var T=null;var O={electricChars:"dDpPtTfFeE ",startState:function(){return{tokenize:k,lastToken:null,currentIndent:0,nextLineIndent:0,doInCurrentLine:false}},token:function(e,t){if(e.sol()){t.currentIndent+=t.nextLineIndent;t.nextLineIndent=0;t.doInCurrentLine=0}var n=A(e,t);t.lastToken={style:n,content:e.current()};return n},indent:function(t,n){var r=n.replace(/^\s+|\s+$/g,"");if(r.match(E)||r.match(S)||r.match(w))return e.indentUnit*(t.currentIndent-1);if(t.currentIndent<0)return 0;return t.currentIndent*e.indentUnit}};return O});e.defineMIME("text/x-vb","vb")})