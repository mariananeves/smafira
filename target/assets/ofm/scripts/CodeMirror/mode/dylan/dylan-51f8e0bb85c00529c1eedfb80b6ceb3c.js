CodeMirror.defineMode("dylan",function(e){function l(e,t,n){t.tokenize=n;return n(e,t)}function p(e,t,n){c=e;h=n;return t}function d(e,t){var n=e.peek();if(n=="'"||n=='"'){e.next();return l(e,t,m(n,"string","string"))}else if(n=="/"){e.next();if(e.eat("*")){return l(e,t,v)}else if(e.eat("/")){e.skipToEnd();return p("comment","comment")}else{e.skipTo(" ");return p("operator","operator")}}else if(/\d/.test(n)){e.match(/^\d*(?:\.\d*)?(?:e[+\-]?\d+)?/);return p("number","number")}else if(n=="#"){e.next();n=e.peek();if(n=='"'){e.next();return l(e,t,m('"',"symbol","string-2"))}else if(n=="b"){e.next();e.eatWhile(/[01]/);return p("number","number")}else if(n=="x"){e.next();e.eatWhile(/[\da-f]/i);return p("number","number")}else if(n=="o"){e.next();e.eatWhile(/[0-7]/);return p("number","number")}else{e.eatWhile(/[-a-zA-Z]/);return p("hash","keyword")}}else if(e.match("end")){return p("end","keyword")}for(var o in i){if(i.hasOwnProperty(o)){var u=i[o];if(u instanceof Array&&u.some(function(t){return e.match(t)})||e.match(u))return p(o,s[o],e.current())}}if(e.match("define")){return p("definition","def")}else{e.eatWhile(/[\w\-]/);if(a[e.current()]){return p(a[e.current()],f[e.current()],e.current())}else if(e.current().match(r)){return p("variable","variable")}else{e.next();return p("other","variable-2")}}}function v(e,t){var n=false,r;while(r=e.next()){if(r=="/"&&n){t.tokenize=d;break}n=r=="*"}return p("comment","comment")}function m(e,t,n){return function(r,i){var s,o=false;while((s=r.next())!=null){if(s==e){o=true;break}}if(o)i.tokenize=d;return p(t,n)}}var t={unnamedDefinition:["interface"],namedDefinition:["module","library","macro","C-struct","C-union","C-function","C-callable-wrapper"],typeParameterizedDefinition:["class","C-subtype","C-mapped-subtype"],otherParameterizedDefinition:["method","function","C-variable","C-address"],constantSimpleDefinition:["constant"],variableSimpleDefinition:["variable"],otherSimpleDefinition:["generic","domain","C-pointer-type","table"],statement:["if","block","begin","method","case","for","select","when","unless","until","while","iterate","profiling","dynamic-bind"],separator:["finally","exception","cleanup","else","elseif","afterwards"],other:["above","below","by","from","handler","in","instance","let","local","otherwise","slot","subclass","then","to","keyed-by","virtual"],signalingCalls:["signal","error","cerror","break","check-type","abort"]};t["otherDefinition"]=t["unnamedDefinition"].concat(t["namedDefinition"]).concat(t["otherParameterizedDefinition"]);t["definition"]=t["typeParameterizedDefinition"].concat(t["otherDefinition"]);t["parameterizedDefinition"]=t["typeParameterizedDefinition"].concat(t["otherParameterizedDefinition"]);t["simpleDefinition"]=t["constantSimpleDefinition"].concat(t["variableSimpleDefinition"]).concat(t["otherSimpleDefinition"]);t["keyword"]=t["statement"].concat(t["separator"]).concat(t["other"]);var n="[-_a-zA-Z?!*@<>$%]+";var r=new RegExp("^"+n);var i={symbolKeyword:n+":",symbolClass:"<"+n+">",symbolGlobal:"\\*"+n+"\\*",symbolConstant:"\\$"+n};var s={symbolKeyword:"atom",symbolClass:"tag",symbolGlobal:"variable-2",symbolConstant:"variable-3"};for(var o in i)if(i.hasOwnProperty(o))i[o]=new RegExp("^"+i[o]);i["keyword"]=[/^with(?:out)?-[-_a-zA-Z?!*@<>$%]+/];var u={};u["keyword"]="keyword";u["definition"]="def";u["simpleDefinition"]="def";u["signalingCalls"]="builtin";var a={};var f={};["keyword","definition","simpleDefinition","signalingCalls"].forEach(function(e){t[e].forEach(function(t){a[t]=e;f[t]=u[e]})});var c,h;return{startState:function(){return{tokenize:d,currentIndent:0}},token:function(e,t){if(e.eatSpace())return null;var n=t.tokenize(e,t);return n},blockCommentStart:"/*",blockCommentEnd:"*/"}});CodeMirror.defineMIME("text/x-dylan","dylan")