namespace="comment_";(function(){function e(e,t,n,r,i){return testCM(e,function(e){n(e);eq(e.getValue(),i)},{value:r,mode:t})}var t="function foo() {\n  return bar;\n}";e("block","javascript",function(e){e.blockComment(Pos(0,3),Pos(3,0),{blockCommentLead:" *"})},t+"\n","/* function foo() {\n *   return bar;\n * }\n */");e("blockToggle","javascript",function(e){e.blockComment(Pos(0,3),Pos(2,0),{blockCommentLead:" *"});e.uncomment(Pos(0,3),Pos(2,0),{blockCommentLead:" *"})},t,t);e("line","javascript",function(e){e.lineComment(Pos(1,1),Pos(1,1))},t,"function foo() {\n//   return bar;\n}");e("lineToggle","javascript",function(e){e.lineComment(Pos(0,0),Pos(2,1));e.uncomment(Pos(0,0),Pos(2,1))},t,t);e("fallbackToBlock","css",function(e){e.lineComment(Pos(0,0),Pos(2,1))},"html {\n  border: none;\n}","/* html {\n  border: none;\n} */");e("fallbackToLine","ruby",function(e){e.blockComment(Pos(0,0),Pos(1))},"def blah()\n  return hah\n","# def blah()\n#   return hah\n");e("commentRange","javascript",function(e){e.blockComment(Pos(1,2),Pos(1,13),{fullLines:false})},t,"function foo() {\n  /*return bar;*/\n}");e("indented","javascript",function(e){e.lineComment(Pos(1,0),Pos(2),{indent:true})},t,"function foo() {\n  // return bar;\n  // }");e("singleEmptyLine","javascript",function(e){e.setCursor(1);e.execCommand("toggleComment")},"a;\n\nb;","a;\n// \nb;");e("dontMessWithStrings","javascript",function(e){e.execCommand("toggleComment")},'console.log("/*string*/");','// console.log("/*string*/");');e("dontMessWithStrings2","javascript",function(e){e.execCommand("toggleComment")},'console.log("// string");','// console.log("// string");');e("dontMessWithStrings3","javascript",function(e){e.execCommand("toggleComment")},'// console.log("// string");','console.log("// string");')})()