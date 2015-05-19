(function(e){if(typeof exports=="object"&&typeof module=="object")e(require("../../lib/codemirror"));else if(typeof define=="function"&&define.amd)define(["../../lib/codemirror"],e);else e(CodeMirror)})(function(e){"use strict";e.defineMode("puppet",function(){function n(t,n){var r=n.split(" ");for(var i=0;i<r.length;i++){e[r[i]]=t}}function r(e,t){var n,r,i=false;while(!e.eol()&&(n=e.next())!=t.pending){if(n==="$"&&r!="\\"&&t.pending=='"'){i=true;break}r=n}if(i){e.backUp(1)}if(n==t.pending){t.continueString=false}else{t.continueString=true}return"string"}function i(n,i){var s=n.match(/[\w]+/,false);var o=n.match(/(\s+)?\w+\s+=>.*/,false);var u=n.match(/(\s+)?[\w:_]+(\s+)?{/,false);var a=n.match(/(\s+)?[@]{1,2}[\w:_]+(\s+)?{/,false);var f=n.next();if(f==="$"){if(n.match(t)){return i.continueString?"variable-2":"variable"}return"error"}if(i.continueString){n.backUp(1);return r(n,i)}if(i.inDefinition){if(n.match(/(\s+)?[\w:_]+(\s+)?/)){return"def"}n.match(/\s+{/);i.inDefinition=false}if(i.inInclude){n.match(/(\s+)?\S+(\s+)?/);i.inInclude=false;return"def"}if(n.match(/(\s+)?\w+\(/)){n.backUp(1);return"def"}if(o){n.match(/(\s+)?\w+/);return"tag"}if(s&&e.hasOwnProperty(s)){n.backUp(1);n.match(/[\w]+/);if(n.match(/\s+\S+\s+{/,false)){i.inDefinition=true}if(s=="include"){i.inInclude=true}return e[s]}if(/(\s+)?[A-Z]/.test(s)){n.backUp(1);n.match(/(\s+)?[A-Z][\w:_]+/);return"def"}if(u){n.match(/(\s+)?[\w:_]+/);return"def"}if(a){n.match(/(\s+)?[@]{1,2}/);return"special"}if(f=="#"){n.skipToEnd();return"comment"}if(f=="'"||f=='"'){i.pending=f;return r(n,i)}if(f=="{"||f=="}"){return"bracket"}if(f=="/"){n.match(/.*\//);return"variable-3"}if(f.match(/[0-9]/)){n.eatWhile(/[0-9]+/);return"number"}if(f=="="){if(n.peek()==">"){n.next()}return"operator"}n.eatWhile(/[\w-]/);return null}var e={};var t=/({)?([a-z][a-z0-9_]*)?((::[a-z][a-z0-9_]*)*::)?[a-zA-Z0-9_]+(})?/;n("keyword","class define site node include import inherits");n("keyword","case if else in and elsif default or");n("atom","false true running present absent file directory undef");n("builtin","action augeas burst chain computer cron destination dport exec "+"file filebucket group host icmp iniface interface jump k5login limit log_level "+"log_prefix macauthorization mailalias maillist mcx mount nagios_command "+"nagios_contact nagios_contactgroup nagios_host nagios_hostdependency "+"nagios_hostescalation nagios_hostextinfo nagios_hostgroup nagios_service "+"nagios_servicedependency nagios_serviceescalation nagios_serviceextinfo "+"nagios_servicegroup nagios_timeperiod name notify outiface package proto reject "+"resources router schedule scheduled_task selboolean selmodule service source "+"sport ssh_authorized_key sshkey stage state table tidy todest toports tosource "+"user vlan yumrepo zfs zone zpool");return{startState:function(){var e={};e.inDefinition=false;e.inInclude=false;e.continueString=false;e.pending=false;return e},token:function(e,t){if(e.eatSpace())return null;return i(e,t)}}});e.defineMIME("text/x-puppet","puppet")})