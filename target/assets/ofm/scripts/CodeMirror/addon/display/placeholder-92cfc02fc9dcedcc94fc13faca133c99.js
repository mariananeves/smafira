(function(e){if(typeof exports=="object"&&typeof module=="object")e(require("../../lib/codemirror"));else if(typeof define=="function"&&define.amd)define(["../../lib/codemirror"],e);else e(CodeMirror)})(function(e){function t(e){if(e.state.placeholder){e.state.placeholder.parentNode.removeChild(e.state.placeholder);e.state.placeholder=null}}function n(e){t(e);var n=e.state.placeholder=document.createElement("pre");n.style.cssText="height: 0; overflow: visible";n.className="CodeMirror-placeholder";n.appendChild(document.createTextNode(e.getOption("placeholder")));e.display.lineSpace.insertBefore(n,e.display.lineSpace.firstChild)}function r(e){if(s(e))n(e)}function i(e){var r=e.getWrapperElement(),i=s(e);r.className=r.className.replace(" CodeMirror-empty","")+(i?" CodeMirror-empty":"");if(i)n(e);else t(e)}function s(e){return e.lineCount()===1&&e.getLine(0)===""}e.defineOption("placeholder","",function(n,s,o){var u=o&&o!=e.Init;if(s&&!u){n.on("blur",r);n.on("change",i);i(n)}else if(!s&&u){n.off("blur",r);n.off("change",i);t(n);var a=n.getWrapperElement();a.className=a.className.replace(" CodeMirror-empty","")}if(s&&!n.hasFocus())r(n)})})