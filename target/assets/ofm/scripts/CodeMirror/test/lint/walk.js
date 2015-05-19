(function(e){if(typeof exports=="object"&&typeof module=="object")return e(exports);if(typeof define=="function"&&define.amd)return define(["exports"],e);e((this.acorn||(this.acorn={})).walk={})})(function(e){"use strict";function t(e){if(typeof e=="string")return function(t){return t==e};else if(!e)return function(){return true};else return e}function n(e,t){this.node=e;this.state=t}function r(e,t,n){n(e,t)}function i(e,t,n){}function o(e,t){return{vars:Object.create(null),prev:e,isCatch:t}}function u(e){while(e.isCatch)e=e.prev;return e}e.simple=function(t,n,r,i){function s(e,t,i){var o=i||e.type,u=n[o];r[o](e,t,s);if(u)u(e,t)}if(!r)r=e.base;s(t,i)};e.recursive=function(t,n,r,i){function o(e,t,n){s[n||e.type](e,t,o)}var s=r?e.make(r,i):i;o(t,n)};e.findNodeAt=function(r,i,s,o,u,a){o=t(o);try{if(!u)u=e.base;var f=function(e,t,r){var a=r||e.type;if((i==null||e.start<=i)&&(s==null||e.end>=s))u[a](e,t,f);if(o(a,e)&&(i==null||e.start==i)&&(s==null||e.end==s))throw new n(e,t)};f(r,a)}catch(l){if(l instanceof n)return l;throw l}};e.findNodeAround=function(r,i,s,o,u){s=t(s);try{if(!o)o=e.base;var a=function(e,t,r){var u=r||e.type;if(e.start>i||e.end<i)return;o[u](e,t,a);if(s(u,e))throw new n(e,t)};a(r,u)}catch(f){if(f instanceof n)return f;throw f}};e.findNodeAfter=function(r,i,s,o,u){s=t(s);try{if(!o)o=e.base;var a=function(e,t,r){if(e.end<i)return;var u=r||e.type;if(e.start>=i&&s(u,e))throw new n(e,t);o[u](e,t,a)};a(r,u)}catch(f){if(f instanceof n)return f;throw f}};e.findNodeBefore=function(r,i,s,o,u){s=t(s);if(!o)o=e.base;var a;var f=function(e,t,r){if(e.start>i)return;var u=r||e.type;if(e.end<=i&&(!a||a.node.end<e.end)&&s(u,e))a=new n(e,t);o[u](e,t,f)};f(r,u);return a};e.make=function(t,n){if(!n)n=e.base;var r={};for(var i in n)r[i]=n[i];for(var i in t)r[i]=t[i];return r};var s=e.base={};s.Program=s.BlockStatement=function(e,t,n){for(var r=0;r<e.body.length;++r)n(e.body[r],t,"Statement")};s.Statement=r;s.EmptyStatement=i;s.ExpressionStatement=function(e,t,n){n(e.expression,t,"Expression")};s.IfStatement=function(e,t,n){n(e.test,t,"Expression");n(e.consequent,t,"Statement");if(e.alternate)n(e.alternate,t,"Statement")};s.LabeledStatement=function(e,t,n){n(e.body,t,"Statement")};s.BreakStatement=s.ContinueStatement=i;s.WithStatement=function(e,t,n){n(e.object,t,"Expression");n(e.body,t,"Statement")};s.SwitchStatement=function(e,t,n){n(e.discriminant,t,"Expression");for(var r=0;r<e.cases.length;++r){var i=e.cases[r];if(i.test)n(i.test,t,"Expression");for(var s=0;s<i.consequent.length;++s)n(i.consequent[s],t,"Statement")}};s.ReturnStatement=function(e,t,n){if(e.argument)n(e.argument,t,"Expression")};s.ThrowStatement=function(e,t,n){n(e.argument,t,"Expression")};s.TryStatement=function(e,t,n){n(e.block,t,"Statement");if(e.handler)n(e.handler.body,t,"ScopeBody");if(e.finalizer)n(e.finalizer,t,"Statement")};s.WhileStatement=function(e,t,n){n(e.test,t,"Expression");n(e.body,t,"Statement")};s.DoWhileStatement=s.WhileStatement;s.ForStatement=function(e,t,n){if(e.init)n(e.init,t,"ForInit");if(e.test)n(e.test,t,"Expression");if(e.update)n(e.update,t,"Expression");n(e.body,t,"Statement")};s.ForInStatement=function(e,t,n){n(e.left,t,"ForInit");n(e.right,t,"Expression");n(e.body,t,"Statement")};s.ForInit=function(e,t,n){if(e.type=="VariableDeclaration")n(e,t);else n(e,t,"Expression")};s.DebuggerStatement=i;s.FunctionDeclaration=function(e,t,n){n(e,t,"Function")};s.VariableDeclaration=function(e,t,n){for(var r=0;r<e.declarations.length;++r){var i=e.declarations[r];if(i.init)n(i.init,t,"Expression")}};s.Function=function(e,t,n){n(e.body,t,"ScopeBody")};s.ScopeBody=function(e,t,n){n(e,t,"Statement")};s.Expression=r;s.ThisExpression=i;s.ArrayExpression=function(e,t,n){for(var r=0;r<e.elements.length;++r){var i=e.elements[r];if(i)n(i,t,"Expression")}};s.ObjectExpression=function(e,t,n){for(var r=0;r<e.properties.length;++r)n(e.properties[r].value,t,"Expression")};s.FunctionExpression=s.FunctionDeclaration;s.SequenceExpression=function(e,t,n){for(var r=0;r<e.expressions.length;++r)n(e.expressions[r],t,"Expression")};s.UnaryExpression=s.UpdateExpression=function(e,t,n){n(e.argument,t,"Expression")};s.BinaryExpression=s.AssignmentExpression=s.LogicalExpression=function(e,t,n){n(e.left,t,"Expression");n(e.right,t,"Expression")};s.ConditionalExpression=function(e,t,n){n(e.test,t,"Expression");n(e.consequent,t,"Expression");n(e.alternate,t,"Expression")};s.NewExpression=s.CallExpression=function(e,t,n){n(e.callee,t,"Expression");if(e.arguments)for(var r=0;r<e.arguments.length;++r)n(e.arguments[r],t,"Expression")};s.MemberExpression=function(e,t,n){n(e.object,t,"Expression");if(e.computed)n(e.property,t,"Expression")};s.Identifier=s.Literal=i;e.scopeVisitor=e.make({Function:function(e,t,n){var r=o(t);for(var i=0;i<e.params.length;++i)r.vars[e.params[i].name]={type:"argument",node:e.params[i]};if(e.id){var s=e.type=="FunctionDeclaration";(s?u(t):r).vars[e.id.name]={type:s?"function":"function name",node:e.id}}n(e.body,r,"ScopeBody")},TryStatement:function(e,t,n){n(e.block,t,"Statement");if(e.handler){var r=o(t,true);r.vars[e.handler.param.name]={type:"catch clause",node:e.handler.param};n(e.handler.body,r,"ScopeBody")}if(e.finalizer)n(e.finalizer,t,"Statement")},VariableDeclaration:function(e,t,n){var r=u(t);for(var i=0;i<e.declarations.length;++i){var s=e.declarations[i];r.vars[s.id.name]={type:"var",node:s.id};if(s.init)n(s.init,t,"Expression")}}})})