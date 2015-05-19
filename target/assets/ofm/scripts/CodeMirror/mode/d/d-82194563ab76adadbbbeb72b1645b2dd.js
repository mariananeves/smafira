(function(e){if(typeof exports=="object"&&typeof module=="object")e(require("../../lib/codemirror"));else if(typeof define=="function"&&define.amd)define(["../../lib/codemirror"],e);else e(CodeMirror)})(function(e){"use strict";function t(e){var t={},n=e.split(" ");for(var r=0;r<n.length;++r)t[n[r]]=true;return t}e.defineMode("d",function(t,n){function p(e,t){var n=e.next();if(f[n]){var r=f[n](e,t);if(r!==false)return r}if(n=='"'||n=="'"||n=="`"){t.tokenize=d(n);return t.tokenize(e,t)}if(/[\[\]{}\(\),;\:\.]/.test(n)){h=n;return null}if(/\d/.test(n)){e.eatWhile(/[\w\.]/);return"number"}if(n=="/"){if(e.eat("+")){t.tokenize=v;return m(e,t)}if(e.eat("*")){t.tokenize=v;return v(e,t)}if(e.eat("/")){e.skipToEnd();return"comment"}}if(c.test(n)){e.eatWhile(c);return"operator"}e.eatWhile(/[\w\$_]/);var i=e.current();if(s.propertyIsEnumerable(i)){if(u.propertyIsEnumerable(i))h="newstatement";return"keyword"}if(o.propertyIsEnumerable(i)){if(u.propertyIsEnumerable(i))h="newstatement";return"builtin"}if(a.propertyIsEnumerable(i))return"atom";return"variable"}function d(e){return function(t,n){var r=false,i,s=false;while((i=t.next())!=null){if(i==e&&!r){s=true;break}r=!r&&i=="\\"}if(s||!(r||l))n.tokenize=null;return"string"}}function v(e,t){var n=false,r;while(r=e.next()){if(r=="/"&&n){t.tokenize=null;break}n=r=="*"}return"comment"}function m(e,t){var n=false,r;while(r=e.next()){if(r=="/"&&n){t.tokenize=null;break}n=r=="+"}return"comment"}function g(e,t,n,r,i){this.indented=e;this.column=t;this.type=n;this.align=r;this.prev=i}function y(e,t,n){var r=e.indented;if(e.context&&e.context.type=="statement")r=e.context.indented;return e.context=new g(r,t,n,null,e.context)}function b(e){var t=e.context.type;if(t==")"||t=="]"||t=="}")e.indented=e.context.indented;return e.context=e.context.prev}var r=t.indentUnit,i=n.statementIndentUnit||r,s=n.keywords||{},o=n.builtin||{},u=n.blockKeywords||{},a=n.atoms||{},f=n.hooks||{},l=n.multiLineStrings;var c=/[+\-*&%=<>!?|\/]/;var h;return{startState:function(e){return{tokenize:null,context:new g((e||0)-r,0,"top",false),indented:0,startOfLine:true}},token:function(e,t){var n=t.context;if(e.sol()){if(n.align==null)n.align=false;t.indented=e.indentation();t.startOfLine=true}if(e.eatSpace())return null;h=null;var r=(t.tokenize||p)(e,t);if(r=="comment"||r=="meta")return r;if(n.align==null)n.align=true;if((h==";"||h==":"||h==",")&&n.type=="statement")b(t);else if(h=="{")y(t,e.column(),"}");else if(h=="[")y(t,e.column(),"]");else if(h=="(")y(t,e.column(),")");else if(h=="}"){while(n.type=="statement")n=b(t);if(n.type=="}")n=b(t);while(n.type=="statement")n=b(t)}else if(h==n.type)b(t);else if((n.type=="}"||n.type=="top")&&h!=";"||n.type=="statement"&&h=="newstatement")y(t,e.column(),"statement");t.startOfLine=false;return r},indent:function(t,n){if(t.tokenize!=p&&t.tokenize!=null)return e.Pass;var s=t.context,o=n&&n.charAt(0);if(s.type=="statement"&&o=="}")s=s.prev;var u=o==s.type;if(s.type=="statement")return s.indented+(o=="{"?0:i);else if(s.align)return s.column+(u?0:1);else return s.indented+(u?0:r)},electricChars:"{}"}});var n="body catch class do else enum for foreach foreach_reverse if in interface mixin "+"out scope struct switch try union unittest version while with";e.defineMIME("text/x-d",{name:"d",keywords:t("abstract alias align asm assert auto break case cast cdouble cent cfloat const continue "+"debug default delegate delete deprecated export extern final finally function goto immutable "+"import inout invariant is lazy macro module new nothrow override package pragma private "+"protected public pure ref return shared short static super synchronized template this "+"throw typedef typeid typeof volatile __FILE__ __LINE__ __gshared __traits __vector __parameters "+n),blockKeywords:t(n),builtin:t("bool byte char creal dchar double float idouble ifloat int ireal long real short ubyte "+"ucent uint ulong ushort wchar wstring void size_t sizediff_t"),atoms:t("exit failure success true false null"),hooks:{"@":function(e,t){e.eatWhile(/[\w\$_]/);return"meta"}}})})