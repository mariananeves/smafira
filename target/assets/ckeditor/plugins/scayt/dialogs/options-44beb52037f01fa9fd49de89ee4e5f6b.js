CKEDITOR.dialog.add("scaytDialog",function(e){var t=e.scayt,n='<p><img src="'+t.getLogo()+'" /></p><p>'+t.getLocal("version")+t.getVersion()+"</p><p>"+t.getLocal("text_copyrights")+"</p>",r=CKEDITOR.document,i={isChanged:function(){return null===this.newLang||this.currentLang===this.newLang?!1:!0},currentLang:t.getLang(),newLang:null,reset:function(){this.currentLang=t.getLang();this.newLang=null},id:"lang"},n=[{id:"options",label:t.getLocal("tab_options"),onShow:function(){},elements:[{type:"vbox",id:"scaytOptions",children:function(){var e=t.getApplicationConfig(),n=[],r={"ignore-all-caps-words":"label_allCaps","ignore-domain-names":"label_ignoreDomainNames","ignore-words-with-mixed-cases":"label_mixedCase","ignore-words-with-numbers":"label_mixedWithDigits"},i;for(i in e){var s={type:"checkbox"};s.id=i;s.label=t.getLocal(r[i]);n.push(s)}return n}(),onShow:function(){this.getChild();for(var t=e.scayt,n=0;n<this.getChild().length;n++)this.getChild()[n].setValue(t.getApplicationConfig()[this.getChild()[n].id])}}]},{id:"langs",label:t.getLocal("tab_languages"),elements:[{id:"leftLangColumn",type:"vbox",align:"left",widths:["100"],children:[{type:"html",id:"langBox",style:"overflow: hidden; white-space: normal;",html:'<form><div style="float:left;width:45%;margin-left:5px;" id="left-col-'+e.name+'"></div><div style="float:left;width:45%;margin-left:15px;" id="right-col-'+e.name+'"></div></form>',onShow:function(){var t=e.scayt.getLang();r.getById("scaytLang_"+t).$.checked=!0}}]}]},{id:"dictionaries",label:t.getLocal("tab_dictionaries"),elements:[{type:"vbox",id:"rightCol_col__left",children:[{type:"html",id:"dictionaryNote",html:""},{type:"text",id:"dictionaryName",label:t.getLocal("label_fieldNameDic")||"Dictionary name",onShow:function(t){var n=t.sender,r=e.scayt;setTimeout(function(){n.getContentElement("dictionaries","dictionaryNote").getElement().setText("");null!=r.getUserDictionaryName()&&""!=r.getUserDictionaryName()&&n.getContentElement("dictionaries","dictionaryName").setValue(r.getUserDictionaryName())},0)}},{type:"hbox",id:"notExistDic",align:"left",style:"width:auto;",widths:["50%","50%"],children:[{type:"button",id:"createDic",label:t.getLocal("btn_createDic"),title:t.getLocal("btn_createDic"),onClick:function(){var t=this.getDialog(),n=s,r=e.scayt,i=t.getContentElement("dictionaries","dictionaryName").getValue();r.createUserDictionary(i,function(r){r.error||n.toggleDictionaryButtons.call(t,!0);r.dialog=t;r.command="create";r.name=i;e.fire("scaytUserDictionaryAction",r)},function(n){n.dialog=t;n.command="create";n.name=i;e.fire("scaytUserDictionaryActionError",n)})}},{type:"button",id:"restoreDic",label:t.getLocal("btn_restoreDic"),title:t.getLocal("btn_restoreDic"),onClick:function(){var t=this.getDialog(),n=e.scayt,r=s,i=t.getContentElement("dictionaries","dictionaryName").getValue();n.restoreUserDictionary(i,function(n){n.dialog=t;n.error||r.toggleDictionaryButtons.call(t,!0);n.command="restore";n.name=i;e.fire("scaytUserDictionaryAction",n)},function(n){n.dialog=t;n.command="restore";n.name=i;e.fire("scaytUserDictionaryActionError",n)})}}]},{type:"hbox",id:"existDic",align:"left",style:"width:auto;",widths:["50%","50%"],children:[{type:"button",id:"removeDic",label:t.getLocal("btn_deleteDic"),title:t.getLocal("btn_deleteDic"),onClick:function(){var t=this.getDialog(),n=e.scayt,r=s,i=t.getContentElement("dictionaries","dictionaryName"),o=i.getValue();n.removeUserDictionary(o,function(n){i.setValue("");n.error||r.toggleDictionaryButtons.call(t,!1);n.dialog=t;n.command="remove";n.name=o;e.fire("scaytUserDictionaryAction",n)},function(n){n.dialog=t;n.command="remove";n.name=o;e.fire("scaytUserDictionaryActionError",n)})}},{type:"button",id:"renameDic",label:t.getLocal("btn_renameDic"),title:t.getLocal("btn_renameDic"),onClick:function(){var t=this.getDialog(),n=e.scayt,r=t.getContentElement("dictionaries","dictionaryName").getValue();n.renameUserDictionary(r,function(n){n.dialog=t;n.command="rename";n.name=r;e.fire("scaytUserDictionaryAction",n)},function(n){n.dialog=t;n.command="rename";n.name=r;e.fire("scaytUserDictionaryActionError",n)})}}]},{type:"html",id:"dicInfo",html:'<div id="dic_info_editor1" style="margin:5px auto; width:95%;white-space:normal;">'+t.getLocal("text_descriptionDic")+"</div>"}]}]},{id:"about",label:t.getLocal("tab_about"),elements:[{type:"html",id:"about",style:"margin: 5px 5px;",html:'<div><div id="scayt_about_">'+n+"</div></div>"}]}];e.on("scaytUserDictionaryAction",function(e){var t=e.data.dialog,n=t.getContentElement("dictionaries","dictionaryNote").getElement(),r=e.editor.scayt,i;void 0===e.data.error?(i=r.getLocal("message_success_"+e.data.command+"Dic"),i=i.replace("%s",e.data.name),n.setText(i),SCAYT.$(n.$).css({color:"blue"})):(""===e.data.name?n.setText(r.getLocal("message_info_emptyDic")):(i=r.getLocal("message_error_"+e.data.command+"Dic"),i=i.replace("%s",e.data.name),n.setText(i)),SCAYT.$(n.$).css({color:"red"}),null!=r.getUserDictionaryName()&&""!=r.getUserDictionaryName()?t.getContentElement("dictionaries","dictionaryName").setValue(r.getUserDictionaryName()):t.getContentElement("dictionaries","dictionaryName").setValue(""))});e.on("scaytUserDictionaryActionError",function(e){var t=e.data.dialog,n=t.getContentElement("dictionaries","dictionaryNote").getElement(),r=e.editor.scayt,i;""===e.data.name?n.setText(r.getLocal("message_info_emptyDic")):(i=r.getLocal("message_error_"+e.data.command+"Dic"),i=i.replace("%s",e.data.name),n.setText(i));SCAYT.$(n.$).css({color:"red"});null!=r.getUserDictionaryName()&&""!=r.getUserDictionaryName()?t.getContentElement("dictionaries","dictionaryName").setValue(r.getUserDictionaryName()):t.getContentElement("dictionaries","dictionaryName").setValue("")});var s={title:t.getLocal("text_title"),resizable:CKEDITOR.DIALOG_RESIZE_BOTH,minWidth:340,minHeight:260,onLoad:function(){if(0!=e.config.scayt_uiTabs[1]){var t=s,n=t.getLangBoxes.call(this);n.getParent().setStyle("white-space","normal");t.renderLangList(n);this.definition.minWidth=this.getSize().width;this.resize(this.definition.minWidth,this.definition.minHeight)}},onCancel:function(){i.reset()},onHide:function(){e.unlockSelection()},onShow:function(){e.fire("scaytDialogShown",this);if(0!=e.config.scayt_uiTabs[2]){var t=e.scayt,n=this.getContentElement("dictionaries","dictionaryName"),r=this.getContentElement("dictionaries","existDic").getElement().getParent(),i=this.getContentElement("dictionaries","notExistDic").getElement().getParent();r.hide();i.hide();null!=t.getUserDictionaryName()&&""!=t.getUserDictionaryName()?(this.getContentElement("dictionaries","dictionaryName").setValue(t.getUserDictionaryName()),r.show()):(n.setValue(""),i.show())}},onOk:function(){var t=s,n=e.scayt;this.getContentElement("options","scaytOptions");t=t.getChangedOption.call(this);n.commitOption({changedOptions:t})},toggleDictionaryButtons:function(e){var t=this.getContentElement("dictionaries","existDic").getElement().getParent(),n=this.getContentElement("dictionaries","notExistDic").getElement().getParent();e?(t.show(),n.hide()):(t.hide(),n.show())},getChangedOption:function(){var t={};if(1==e.config.scayt_uiTabs[0])for(var n=this.getContentElement("options","scaytOptions").getChild(),r=0;r<n.length;r++)n[r].isChanged()&&(t[n[r].id]=n[r].getValue());i.isChanged()&&(t[i.id]=e.config.scayt_sLang=i.currentLang=i.newLang);return t},buildRadioInputs:function(t,n){var r=new CKEDITOR.dom.element("div");CKEDITOR.document.createElement("div");var s="scaytLang_"+n,o=CKEDITOR.dom.element.createFromHtml('<input id="'+s+'" type="radio"  value="'+n+'" name="scayt_lang" />'),u=new CKEDITOR.dom.element("label"),a=e.scayt;r.setStyles({"white-space":"normal",position:"relative"});o.on("click",function(e){i.newLang=e.sender.getValue()});u.appendText(t);u.setAttribute("for",s);r.append(o);r.append(u);n===a.getLang()&&(o.setAttribute("checked",!0),o.setAttribute("defaultChecked","defaultChecked"));return r},renderLangList:function(n){var r=n.find("#left-col-"+e.name).getItem(0),n=n.find("#right-col-"+e.name).getItem(0),i=t.getLangList(),s={},o=[],u=0,a;for(a in i.ltr)s[a]=i.ltr[a];for(a in i.rtl)s[a]=i.rtl[a];for(a in s)o.push([a,s[a]]);o.sort(function(e,t){var n=0;e[1]>t[1]?n=1:e[1]<t[1]&&(n=-1);return n});s={};for(i=0;i<o.length;i++)s[o[i][0]]=o[i][1];o=Math.round(o.length/2);for(a in s)u++,this.buildRadioInputs(s[a],a).appendTo(u<=o?r:n)},getLangBoxes:function(){return this.getContentElement("langs","langBox").getElement()},contents:function(e,t){var n=[],r=t.config.scayt_uiTabs;if(r){for(var i in r)1==r[i]&&n.push(e[i]);n.push(e[e.length-1])}else return e;return n}(n,e)};return s})