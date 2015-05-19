(function(){function e(e,t,n){if(!t.is||!t.getCustomData("block_processed"))t.is&&CKEDITOR.dom.element.setMarker(n,t,"block_processed",!0),e.push(t)}function t(t,n){function r(){this.foreach(function(e){if(/^(?!vbox|hbox)/.test(e.type)&&(e.setup||(e.setup=function(t){e.setValue(t.getAttribute(e.id)||"",1)}),!e.commit))e.commit=function(t){var n=this.getValue();"dir"==e.id&&t.getComputedStyle("direction")==n||(n?t.setAttribute(e.id,n):t.removeAttribute(e.id))}})}var i=function(){var e=CKEDITOR.tools.extend({},CKEDITOR.dtd.$blockLimit);t.config.div_wrapTable&&(delete e.td,delete e.th);return e}(),s=CKEDITOR.dtd.div,o={},u=[];return{title:t.lang.div.title,minWidth:400,minHeight:165,contents:[{id:"info",label:t.lang.common.generalTab,title:t.lang.common.generalTab,elements:[{type:"hbox",widths:["50%","50%"],children:[{id:"elementStyle",type:"select",style:"width: 100%;",label:t.lang.div.styleSelectLabel,"default":"",items:[[t.lang.common.notSet,""]],onChange:function(){var e=["info:elementStyle","info:class","advanced:dir","advanced:style"],n=this.getDialog(),r=n._element&&n._element.clone()||new CKEDITOR.dom.element("div",t.document);this.commit(r,!0);for(var e=[].concat(e),i=e.length,s,o=0;o<i;o++)(s=n.getContentElement.apply(n,e[o].split(":")))&&s.setup&&s.setup(r,!0)},setup:function(e){for(var n in o)o[n].checkElementRemovable(e,!0,t)&&this.setValue(n,1)},commit:function(e){var n;(n=this.getValue())?o[n].applyToObject(e,t):e.removeAttribute("style")}},{id:"class",type:"text",requiredContent:"div(cke-xyz)",label:t.lang.common.cssClass,"default":""}]}]},{id:"advanced",label:t.lang.common.advancedTab,title:t.lang.common.advancedTab,elements:[{type:"vbox",padding:1,children:[{type:"hbox",widths:["50%","50%"],children:[{type:"text",id:"id",requiredContent:"div[id]",label:t.lang.common.id,"default":""},{type:"text",id:"lang",requiredContent:"div[lang]",label:t.lang.common.langCode,"default":""}]},{type:"hbox",children:[{type:"text",id:"style",requiredContent:"div{cke-xyz}",style:"width: 100%;",label:t.lang.common.cssStyle,"default":"",commit:function(e){e.setAttribute("style",this.getValue())}}]},{type:"hbox",children:[{type:"text",id:"title",requiredContent:"div[title]",style:"width: 100%;",label:t.lang.common.advisoryTitle,"default":""}]},{type:"select",id:"dir",requiredContent:"div[dir]",style:"width: 100%;",label:t.lang.common.langDir,"default":"",items:[[t.lang.common.notSet,""],[t.lang.common.langDirLtr,"ltr"],[t.lang.common.langDirRtl,"rtl"]]}]}]}],onLoad:function(){r.call(this);var e=this,n=this.getContentElement("info","elementStyle");t.getStylesSet(function(r){var i,s;if(r)for(var u=0;u<r.length;u++)s=r[u],s.element&&"div"==s.element&&(i=s.name,o[i]=s=new CKEDITOR.style(s),t.filter.check(s)&&(n.items.push([i,i]),n.add(i,i)));n[1<n.items.length?"enable":"disable"]();setTimeout(function(){e._element&&n.setup(e._element)},0)})},onShow:function(){"editdiv"==n&&this.setupContent(this._element=CKEDITOR.plugins.div.getSurroundDiv(t))},onOk:function(){if("editdiv"==n)u=[this._element];else{var r=[],o={},f=[],l,c=t.getSelection(),h=c.getRanges(),d=c.createBookmarks(),v,g;for(v=0;v<h.length;v++)for(g=h[v].createIterator();l=g.getNextParagraph();)if(l.getName()in i&&!l.isReadOnly()){var y=l.getChildren();for(l=0;l<y.count();l++)e(f,y.getItem(l),o)}else{for(;!s[l.getName()]&&!l.equals(h[v].root);)l=l.getParent();e(f,l,o)}CKEDITOR.dom.element.clearAllMarkers(o);h=[];v=null;for(g=0;g<f.length;g++)l=f[g],y=t.elementPath(l).blockLimit,y.isReadOnly()&&(y=y.getParent()),t.config.div_wrapTable&&y.is(["td","th"])&&(y=t.elementPath(y.getParent()).blockLimit),y.equals(v)||(v=y,h.push([])),h[h.length-1].push(l);for(v=0;v<h.length;v++){y=h[v][0];f=y.getParent();for(l=1;l<h[v].length;l++)f=f.getCommonAncestor(h[v][l]);g=new CKEDITOR.dom.element("div",t.document);for(l=0;l<h[v].length;l++){for(y=h[v][l];!y.getParent().equals(f);)y=y.getParent();h[v][l]=y}for(l=0;l<h[v].length;l++)if(y=h[v][l],!y.getCustomData||!y.getCustomData("block_processed"))y.is&&CKEDITOR.dom.element.setMarker(o,y,"block_processed",!0),l||g.insertBefore(y),g.append(y);CKEDITOR.dom.element.clearAllMarkers(o);r.push(g)}c.selectBookmarks(d);u=r}r=u.length;for(o=0;o<r;o++)this.commitContent(u[o]),!u[o].getAttribute("style")&&u[o].removeAttribute("style");this.hide()},onHide:function(){"editdiv"==n&&this._element.removeCustomData("elementStyle");delete this._element}}}CKEDITOR.dialog.add("creatediv",function(e){return t(e,"creatediv")});CKEDITOR.dialog.add("editdiv",function(e){return t(e,"editdiv")})})()