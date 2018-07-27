﻿/*
key down
*/
(function($){
	
  $.speObj = new Object();
  $.speObj['192'] = "`";
  $.speObj['49']  = "1";
  $.speObj['50']  = "2";
  $.speObj['51']  = "3";
  $.speObj['52']  = "4";
  $.speObj['53']  = "5";
  $.speObj['54']  = "6";
  $.speObj['55']  = "7";
  $.speObj['56']  = "8";
  $.speObj['57']  = "9";
  $.speObj['48']  = "0";
  $.speObj['189'] = "-";
  $.speObj['187'] = "=";
  $.speObj['220'] = "\\";
  $.speObj['219'] = "[";
  $.speObj['221'] = "]";
  $.speObj['186'] = ";";
  $.speObj['222'] = "'";
  $.speObj['188'] = ",";
  $.speObj['190'] = ".";
  $.speObj['191']   = "/";
  $.speObj['32']   = " ";
  
  $.shSpeObj = new Object();
  $.shSpeObj['192'] = "~";
  $.shSpeObj['49']  = "!";
  $.shSpeObj['50']  = "@";
  $.shSpeObj['51']  = "#";
  $.shSpeObj['52']  = "$";
  $.shSpeObj['53']  = "%";
  $.shSpeObj['54']  = "^";
  $.shSpeObj['55']  = "&";
  $.shSpeObj['56']  = "*";
  $.shSpeObj['57']  = "(";
  $.shSpeObj['48']  = ")";
  $.shSpeObj['189'] = "_";
  $.shSpeObj['187'] = "+";
  $.shSpeObj['220'] = "|";
  $.shSpeObj['219'] = "{";
  $.shSpeObj['221'] = "}";
  $.shSpeObj['186'] = ":";
  $.shSpeObj['222'] = "\"";
  $.shSpeObj['188'] = "<";
  $.shSpeObj['190'] = ">"; 
  $.shSpeObj['191'] = "?";
  $.shSpeObj['32']  = " "; 
  
  // FF
  $.speObj['59']   = ";";
  $.speObj['61']   = "=";
  $.speObj['173']   = "-";
  
  $.shSpeObj['59']  = ":";
  $.shSpeObj['61']  = "+";
  $.shSpeObj['173']  = "_";
  
  // ru
  $.shSpeObj['191'] = ",";
  
  $.fn.prevent = function(selected) {
      $(selected).bind('contextmenu',function () {
        return false; 
      });
      $(selected).bind('paste', function() {
        return false;
      });
      
      $(selected).bind('dragstart',function () {
        return false; 
      });
      
      $(selected).bind('drop', function() {
        return false;
      });
      
      $(selected).bind('dragover', function() {
          return false;
      });
      
      $(selected).bind('selectstart', function() {
        return false;
      });
  }
  
  $.fn.numeric = function(p) {
    p = $.extend({
        allow: ""
      }, p);
    
    $.fn.prevent($(this)[0]);
    
    $(this).blur(function(){
      var str = $(this).val();
      str = str.replace(/[^\x00-\x7F]/g, "");
      
      var charArr = str.split('');
      for(var i=0; i<charArr.length; i++) {
         if("0123456789".indexOf(charArr[i]) != -1) {
            continue;
         } else if(p['allow'].indexOf(charArr[i]) != -1) {
           continue;
         } else {
           charArr[i] = "";
         }
      }
      
      str = charArr.join("");
      
      $(this).val(str);
    });
    
    $(this).keydown (function (e) {
      e = e || window.event;
      var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
      var charStr = String.fromCharCode(charCode);
      
      var isShift = !!e.shiftKey; 
      var isCtrl  = !!e.ctrlKey; 
      
      if(isShift) {
          if(p['allow'].indexOf($.shSpeObj[charCode+""]) != -1) {
            return true;
          }
        } else {
          if(p['allow'].indexOf($.speObj[charCode+""]) != -1) {
            return true;
          }
        }
      
      if(charCode == 229 || charCode == 0) {
        var str = $(this).val();
        str = str.replace(/[^\x00-\x7F]/g, "");
        $(this).val(str);
        e.preventDefault();
        return false;
      }
      
      if(charStr == ".") {
          return true;
      }
      
      if(charStr == "") {
        return true;
      }
      
      if(charStr == "#") {
        return true;
      }
      
      if(charStr == "$") {
        return true;
      }
      
      if(charStr == "%") {
        return true;
      }
      
      if(charStr == "'") {
        return true;
      }
      
      if(charStr == "	") {
        return true;
      }
      
      if(isCtrl && charStr == "A") {
        return true;
      }
      
      if(isShift && charStr == "-") {
        e.preventDefault();
        return false;
      }

      if(isCtrl && charStr == "C") {
          e.preventDefault();
          return false;
      }
      
      if(isCtrl && charStr == "V") {
        e.preventDefault();
        return false;
      }
      
      if ("`abcdefghi".indexOf(charStr) != -1)  {
        return true;
      }
      
      if (!isShift && "1234567890".indexOf(charStr) != -1)  {
        return true;
      } else {
        e.preventDefault();
        return false;
      }
    });
  };
  
  $.fn.alpha = function(p) {
    p = $.extend({
        allow: ""
      }, p);
    
    $.fn.prevent($(this)[0]);
    
    $(this).blur(function(){
      var str = $(this).val();
      str = str.replace(/[^\x00-\x7F]/g, "");
      
      var charArr = str.split('');
      for(var i=0; i<charArr.length; i++) {
         if("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(charArr[i]) != -1) {
            continue;
         } else if(p['allow'].indexOf(charArr[i]) != -1) {
           continue;
         } else {
           charArr[i] = "";
         }
      }
      
      str = charArr.join("");
      
      $(this).val(str);
    });
    
    $(this).keydown (function (e) {
      e = e || window.event;
      var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
      var charStr = String.fromCharCode(charCode);
      
      var isShift = !!e.shiftKey; 
      var isCtrl  = !!e.ctrlKey; 
      
      if(isShift) {
          if(p['allow'].indexOf($.shSpeObj[charCode+""]) != -1) {
            return true;
          }
        } else {
          if(p['allow'].indexOf($.speObj[charCode+""]) != -1) {
            return true;
          }
        }
      
      if(charCode == 229 || charCode == 0) {
        var str = $(this).val();
        str = str.replace(/[^\x00-\x7F]/g, "");
        $(this).val(str);
        e.preventDefault();
        return false;
      }
      
      if(charStr == ".") {
          return true;
      }
      
      if(charStr == "") {
        return true;
      }
      
      if(charStr == "#") {
        return true;
      }
      
      if(charStr == "$") {
        return true;
      }
      
      if(charStr == "%") {
        return true;
      }
      
      if(charStr == "'") {
        return true;
      }
      
      if(charStr == "	") {
        return true;
      }
      
      if(isCtrl && charStr == "A") {
        e.preventDefault();
        return false;
      }
      
      if(isShift && charStr == "-") { 
        e.preventDefault();
        return false;
      }
      
      if(isCtrl && charStr == "C") {
          e.preventDefault();
          return false;
      }
      
      if(isCtrl && charStr == "V") {
        e.preventDefault();
        return false;
      }
      
      
      if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(charStr) != -1)  {
        return true;
      } else {
        e.preventDefault();
        return false;
      }
    });
  };
       
  $.fn.alphanumeric = function(p) {
    p = $.extend({
        allow: ""
      }, p);
    
    $.fn.prevent($(this)[0]);
    
    $(this).blur(function(){
      var str = $(this).val();
      str = str.replace(/[^\x00-\x7F]/g, "");
      
      var charArr = str.split('');
      for(var i=0; i<charArr.length; i++) {
         if("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".indexOf(charArr[i]) != -1) {
            continue;
         } else if(p['allow'].indexOf(charArr[i]) != -1) {
           continue;
         } else {
           charArr[i] = "";
         }
      }
      
      str = charArr.join("");
      
      $(this).val(str);
    });
    
    $(this).keydown(function (e) {
        e = e || window.event;
        var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
        var charStr = String.fromCharCode(charCode);
        
        var isShift = !!e.shiftKey; 
        var isCtrl  = !!e.ctrlKey; 
        
        if(isShift) {
            if(p['allow'].indexOf($.shSpeObj[charCode+""]) != -1) {
              return true;
            }
          } else {
            if(p['allow'].indexOf($.speObj[charCode+""]) != -1) {
              return true;
            }
          }
        
        if(charCode == 229 || charCode == 0) {
          var str = $(this).val();
          str = str.replace(/[^\x00-\x7F]/g, "");
          $(this).val(str);
          e.preventDefault();
          return false;
        }
        
        if(charStr == ".") {
            return true;
        }
        
        if(charStr == "") {
          return true;
        }
        
        if(charStr == "#") {
          return true;
        }
        
        if(charStr == "$") {
          return true;
        }
        
        if(charStr == "%") {
          return true;
        }
        
        if(charStr == "'") {
          return true;
        }
        
        if(charStr == "	") {
          return true;
        }
        
        if(isCtrl && charStr == "A") {
          e.preventDefault();
          return false;
        }
        
        if(isShift && charStr == "-") { 
          e.preventDefault();
          return false;
        }
        
        if(isCtrl && charStr == "C") {
            e.preventDefault();
            return false;
        }
        
        if(isCtrl && charStr == "V") {
          e.preventDefault();
          return false;
        }
        
        if ("`abcdefghi".indexOf(charStr) != -1)  {
          return true;
        }
        
        if (!isShift && "1234567890".indexOf(charStr) != -1)  {
          return true;
        } else if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(charStr) != -1)  {
          return true;
        } else {
          e.preventDefault();
          return false;
        }
    });
  };
  
  $.fn.noSpecialChar = function(p) {
    p = $.extend({
      allow: ""
    }, p);

    $.fn.prevent($(this)[0]);

    $(this).blur(function(){
      var str = $(this).val();
      if(p['allow'] == "") {
        str = str.replace(/[ \{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\(\=]/gi, "");

      } else {
        var charArr = str.split('');
        for(var i=0; i<charArr.length; i++) {

          if(p['allow'].indexOf(charArr[i]) != -1) {
            continue;
          } else if("`~!@#$%^&*()-=_+[]\{}|;':\",./<>?".indexOf(charArr[i]) != -1) {
            charArr[i] = "";
          }
        }

        str = charArr.join("");
      }

      $(this).val(str);
    });

    $(this).keydown(function (e) {
        e = e || window.event;
        var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
        var charStr = String.fromCharCode(charCode);

        var isShift = !!e.shiftKey;
        var isCtrl  = !!e.ctrlKey;

        if(charCode == 229 || charCode == 0) {
        }

        if(charStr == ".") {
            return true;
        }

        if(charStr == "") {
          return true;
        }

        if(charStr == "#") {
          return true;
        }

        if(charStr == "$") {
          return true;
        }

        if(charStr == "%") {
          return true;
        }

        if(charStr == "'") {
          return true;
        }

        if(charStr == "	") {
          return true;
        }
        
        if(charStr == "	") {  
          return true;
        }
        
        if(charStr == "Ý") {  
          return true;
        }
        
        if(charStr == "Û") {  
          return true;
        }
        
        if(charStr == "º") {  
          return true;
        }
        
        if(charStr == ";") {  
          return true;
        }
          
        if(charStr == "Þ") {  
          return true;
        }
        
        if(charStr == "¼") {  
          return true;
        }
        
        if(charStr == "¾") {  
          return true;
        }
        
        if(charStr == "À") {  
          return true;
        }

        if(isShift) {
          if(p['allow'].indexOf($.shSpeObj[charCode+""]) != -1) {
            return true;
          }
        } else {
          if(p['allow'].indexOf($.speObj[charCode+""]) != -1) {
            return true;
          }
        }

        if(charStr == " ") {
          e.preventDefault();
          return false;
        }

        if(isCtrl && charStr == "A") {
          e.preventDefault();
          return false;
        }


        if(isShift && charStr == "-") {
          e.preventDefault();
          return false;
        }

        if(isCtrl && charStr == "C") {
            e.preventDefault();
            return false;
        }
        
        if(isCtrl && charStr == "V") {
          e.preventDefault();
          return false;
        }


        if ("À½»ÜÝÛÞº¿¾¼ojmkn;­=".indexOf(charStr) != -1)  {
          e.preventDefault();
          return false;
        } else if (isShift && "1234567890".indexOf(charStr) != -1)  {
          e.preventDefault();
          return false;
        } else {
          return true;
        }
    });
  };
  
  $.fn.noSpecialCharNum = function(p) {
    p = $.extend({
      allow: ""
    }, p);

    $.fn.prevent($(this)[0]);

    $(this).blur(function(){
      var str = $(this).val();

      var str = $(this).val();
      if(p['allow'] == "") {
        str = str.replace(/[ \{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\(\=]/gi, "");
        str = str.replace(/[0-9]/g, "");

      } else {
        var charArr = str.split('');
        for(var i=0; i<charArr.length; i++) {

          if(p['allow'].indexOf(charArr[i]) != -1) {
            continue;
          } else if("`~!@#$%^&*()-=_+[]\{}|;':\",./<>?1234567890".indexOf(charArr[i]) != -1) {
            charArr[i] = "";
          }
        }

        str = charArr.join("");
      }

      $(this).val(str);
    });

    $(this).keydown(function (e) {
      e = e || window.event;
      var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
      var charStr = String.fromCharCode(charCode);

      var isShift = !!e.shiftKey;
      var isCtrl  = !!e.ctrlKey;


      if(charCode == 229 || charCode == 0) {
      }

      if(charStr == ".") {
          return true;
      }

      if(charStr == "") {
        return true;
      }

      if(charStr == "#") {
        return true;
      }

      if(charStr == "$") {
        return true;
      }

      if(charStr == "%") {
        return true;
      }

      if(charStr == "'") {
        return true;
      }

      if(charStr == "	") {
        return true;
      }

      if(charStr == "Ý") {
        return true;
      }

      if(charStr == "Û") {
        return true;
      }

      if(charStr == "º") {
        return true;
      }
      
      if(charStr == ";") {  
        return true;
      }

      if(charStr == "Þ") {
        return true;
      }

      if(charStr == "¼") {
        return true;
      }

      if(charStr == "¾") {
        return true;
      }

      if(charStr == "À") {
        return true;
      }

      if(isShift) {
        if(p['allow'].indexOf($.shSpeObj[charCode+""]) != -1) {
          return true;
        }
      } else {
        if(p['allow'].indexOf($.speObj[charCode+""]) != -1) {
          return true;
        }
      }

      if(charStr == " ") {
        e.preventDefault();
        return false;
      }

      if(isCtrl && charStr == "A") {
        e.preventDefault();
        return false;
      }

      if(isShift && charStr == "-") {
        e.preventDefault();
        return false;
      }

      if(isCtrl && charStr == "C") {
          e.preventDefault();
          return false;
      }
      
      if(isCtrl && charStr == "V") {
        e.preventDefault();
        return false;
      }

      if ("`abcdefghi".indexOf(charStr) != -1)  {
        return false;
      }

      if ("À½»ÜÝÛÞº¿¾¼ojmkn;­=".indexOf(charStr) != -1)  {
        e.preventDefault();
        return false;
      } else if ("1234567890".indexOf(charStr) != -1)  {
        e.preventDefault();
        return false;
      } else {
        return true;
      }
    });
  };
  
  $.fn.lowalphanumericNoSpace = function(p) {
   p = $.extend({
     allow: ""
   }, p);
   
   $.fn.prevent($(this)[0]);
   
   $(this).blur(function(){
     var str = $(this).val();
     str = str.replace(/[^\x00-\x7F]/g, "");
     str = str.replace(/ /g, "");
     
     var charArr = str.split('');
     for(var i=0; i<charArr.length; i++) {
        if("abcdefghijklmnopqrstuvwxyz1234567890".indexOf(charArr[i]) != -1) {
           continue;
        } else if(p['allow'].indexOf(charArr[i]) != -1) {
          continue;
        } else {
          charArr[i] = "";
        }
     }
     
     str = charArr.join("");
     
     $(this).val(str.toLowerCase());
   });
   
   $(this).keydown(function (e) {
       e = e || window.event;
       var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
       var charStr = String.fromCharCode(charCode);
       
       if(charStr == " ") {
           e.preventDefault();
           return false;
         }
       
       
       if(charCode == 229 || charCode == 0) {
         var str = $(this).val();
         str = str.replace(/[^\x00-\x7F]/g, "");
         $(this).val(str);
         e.preventDefault();
         return false;
       }
       
       return true;
   });
   
   $(this).keypress(function (e) {
       e = e || window.event;
       var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
       var charStr = String.fromCharCode(charCode);
       
       if(("abcdefghijklmnopqrstuvwxyz1234567890"+p['allow']).indexOf(charStr) != -1)  {
           return true;
       } else if("`1234567890-=\~!@#$%^&*()_+|abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[];',./{}:\"<>?".indexOf(charStr) != -1)  {
         e.preventDefault();
         return false;
       } else {
         return true;
       }
   });
 };
  
  $.fn.lowalphanumeric = function(p) {
    p = $.extend({
      allow: ""
    }, p);
    
    $.fn.prevent($(this)[0]);
    
    $(this).blur(function(){
      var str = $(this).val();
      str = str.replace(/[^\x00-\x7F]/g, "");
      
      var charArr = str.split('');
      for(var i=0; i<charArr.length; i++) {
         if("abcdefghijklmnopqrstuvwxyz1234567890".indexOf(charArr[i]) != -1) {
            continue;
         } else if(p['allow'].indexOf(charArr[i]) != -1) {
           continue;
         } else {
           charArr[i] = "";
         }
      }
      
      str = charArr.join("");
      
      $(this).val(str.toLowerCase());
    });
    
    $(this).keydown(function (e) {
        e = e || window.event;
        var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
        var charStr = String.fromCharCode(charCode);
        
        if(charCode == 229 || charCode == 0) {
          var str = $(this).val();
          str = str.replace(/[^\x00-\x7F]/g, "");
          $(this).val(str);
          e.preventDefault();
          return false;
        }
        
        return true;
    });
    
    $(this).keypress(function (e) {
        e = e || window.event;
        var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
        var charStr = String.fromCharCode(charCode);
        
        if(("abcdefghijklmnopqrstuvwxyz1234567890"+p['allow']).indexOf(charStr) != -1)  {
            return true;
        } else if("`1234567890-=\~!@#$%^&*()_+|abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[];',./{}:\"<>?".indexOf(charStr) != -1)  {
          e.preventDefault();
          return false;
        } else {
          return true;
        }
    });
  };
  
  $.fn.upalphanumeric = function(p) {
    p = $.extend({
      allow: ""
    }, p);
    
    $.fn.prevent($(this)[0]);
    
    $(this).blur(function(){
      var str = $(this).val();
      str = str.replace(/[^\x00-\x7F]/g, "");
      
      var charArr = str.split('');
      for(var i=0; i<charArr.length; i++) {
         if("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".indexOf(charArr[i]) != -1) {
            continue;
         } else if(p['allow'].indexOf(charArr[i]) != -1) {
           continue;
         } else {
           charArr[i] = "";
         }
      }
      
      str = charArr.join("");
      
      $(this).val(str.toUpperCase());
    });
    
    $(this).keydown(function (e) {
        e = e || window.event;
        var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
        var charStr = String.fromCharCode(charCode);
        
        if(charCode == 229 || charCode == 0) {
          var str = $(this).val();
          str = str.replace(/[^\x00-\x7F]/g, "");
          $(this).val(str);
          e.preventDefault();
          return false;
        }
        
        return true;
    });
    
    $(this).keypress(function (e) {
        e = e || window.event;
        var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;
        var charStr = String.fromCharCode(charCode);
        
        if(("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"+p['allow']).indexOf(charStr) != -1)  {
            return true;
        } else if("`1234567890-=\~!@#$%^&*()_+|abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[];',./{}:\"<>?".indexOf(charStr) != -1)  {
          e.preventDefault();
          return false;
        } else {
          return true;
        }
    });
  };
})(jQuery);