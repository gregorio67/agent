/*----------------------------------------------------------------------------*/
/* NAME : pushState()            		                            	  	  */
/* DESC : This is for saving the current url and form	 					  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function pushState(stateObject) {
	history.pushState(stateObject, stateObject.formId, stateObject.Url);
	
}
/*--------------------------------------------------------------------------- -*/
/* NAME : sessionTimeout()													   */
/* DESC : Session timeout check										     	   */
/* DATE : 2016.04.22                                                           */
/* AUTH : Gregorio Kim														   */
/*-----------------------------------------------------------------------------*/

function gfn_sessionTimeout(message, warnAfter, redirAfter) {
	$.sessionTimeout({
		message: '<spring:message code="message.sessiontimeout" />',
		keepAliveUrl: '/cmn/keepalive.do',
		keepAliveAjaxRequestType: 'POST',
		logoutUrl: '/logout.do',
	    redirUrl: '/logout.do',
	    appendTime: 'true',
	    warnAfter: '<spring:message code="message.warnAfter" />',
	    redirAfter:'<spring:message code="message.redirAfter" />'
	});
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_isNull()       		                                 	  	  */
/* DESC : This function check whether the variable is null or not			  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_isNull(str) {
    if (str == null) return true;
    if (str == "NaN") return true;
    if (new String(str).valueOf() == "undefined") return true;
    if(typeof(str) == "undefined") return true;
    var chkStr = new String(str);
    if( chkStr.valueOf() == "undefined" ) return true;
    if (chkStr == null) return true;    
    if (chkStr.toString().length == 0 ) return true;   
    return false; 
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_nvl()       			                                 	  	  */
/* DESC : This function set the null value ""								  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_nvl(str, replaceStr){
	if(gfn_isNull(str)){
		if(gfn_isNull(replaceStr)){
			return "";
		}else{
			return replaceStr;
		}
	}else{
		return str;
	}
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_getCookie()       	                                 	  	  */
/* DESC : This function return cookie value with cookie name.				  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
    }
    return "";
}


/*----------------------------------------------------------------------------*/
/* NAME : fn_showMessage()       	                               	  	  	  */
/* DESC : This is show message dialog                            	  		  */
/* DATE : 2018.05.18                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_showMessage(fieldName, msg) {
	gfn_btnDialog(msg, "500", {Close:function(){
		$(this).dialog('close');
		$("#" + fieldName).focus();
		}
	});
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_showErrMessage()                                     	  	  	  */
/* DESC : This is show message dialog                            	  		  */
/* DATE : 2018.05.18                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_showErrMessage(errMsg) {

	if (typeof(errMsg) == 'undefined') {
		return false;
	}
	
	if (errMsg.status == 'F'){
		var msg = errMsg.message;
		gfn_errorDialog(msg, "500", {Close:function(){
			$(this).dialog('close');
			}
		});
		return true;
	}
//	else {
//		var msg = errMsg.message;
//		gfn_btnDialog(msg, "500", {Close:function(){
//			$(this).dialog('close');
//			}
//		});		
//	}
	return false;
}


/*----------------------------------------------------------------------------*/
/* NAME : fileDownload()       	                                 	  	  	  */
/* DESC : This is common java script function for downloading file.			  */
/* 		  To download file, a file name should be set. 				  		  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
var fileDownload = new function() {
	this.uri = "/cmn/filedownload.do";
	this.deleteYn = "";
	this.param = "";
	
	this.setOrgFileName = function setOrgFileName(OrgFileName) {
		this.OrgFileName = OrgFileName;
	}
	
	
	this.setFileName = function setFileName(fileName) {
		this.fileName = fileName;
	}
	
	this.setDelete = function setDelete(deleteYn) {
		this.deleteYn = deleteYn;
	}
	
	this.submit = function submit() {
		if (gfn_isNull(this.fileName)) {
			var message = $("#golabefilenotexist").val() + " : " + this.fileName;
			gfn_messageDialog(message);
			return;
		}
			
		//this.url = this.uri + "?orgfilename=" + this.OrgFileName + "&filename=" + this.fileName + "&delete=" + this.deleteYn;
		
		var arrFileInfo = [];
		var vJsonParam = "";
		vJsonParam = { "FILE_NAME" : this.fileName
				  	 , "ORG_FILE_NAME" : this.OrgFileName
		}
		arrFileInfo.push(vJsonParam);
		this.url = this.uri + "?delete=" + this.deleteYn + "&" + "jsonData=" + encodeURIComponent(JSON.stringify(arrFileInfo));
		
		$('#downloadFrame').remove(); // This shouldn't fail if frame doesn't exist
		$('body').append('<iframe id="downloadFrame" style="display:none"></iframe>');
		$('#downloadFrame').attr('src',this.url);
	}

}

/*----------------------------------------------------------------------------*/
/* NAME : fileUpload()       	                                 	  	  	  */
/* DESC : This is file upload script in common use							  */
/*		  If file upload URI is not set, default URI should be set.			  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
var fileUpload = new function() {
	this.deafultUri = "/cmn/filedownload.do";
	this.uri="";
	var uploadForm;

	this.setURI = function setURI(uri) {
		this.uri = uri;
	}
	
	this.setFormData = function setFormData(form) {
		this.uploadForm = form;
	}
	
	this.setCallback = function setCallback(callBack){
		gfv_ajaxCallback = callBack;
	};
	
	
	this.ajax = function ajax(){	
		if (gfn_isNull(this.uri)) {
			this.uri=this.defaultUri;
		}
		if (gfn_isNull(this.uploadForm)) {
			var message = $("#golabefilenotexist").val() + " : " + this.fileName;
			gfn_messageDialog(message);
			return;
		}
			
		$.ajax({
			url : this.uri,
			type : "POST",   
			data : this.uploadForm,
			dataType : "json",
  	        processData: false,
		    contentType: false,			
			success : function(data, status) {
				if(typeof(gfv_ajaxCallback) == "function"){
					gfv_ajaxCallback(data);
				}
				else {
					eval(gfv_ajaxCallback + "(data);");
				}
		    }
		    ,error: OnError
		});
	};
}

/*----------------------------------------------------------------------------*/
/* NAME : commonSubmit()       	                                 	  	  	  */
/* DESC : This function provide submit form tag, If form tag is not set, 	  */
/* 		  this function create temp form tag.								  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
var commonSubmit = new function() {
	
    this.url = "";
    this.formId =  "";
    this.method = "";
//	$("#commonForm")[0].reset();
//	$("#commonForm").empty();
	  
    this.setUrl = function setUrl(url){
        this.url = url;
    };
    
    /**
     * Set the form tag for submit.
     */
    this.setForm = function setForm(formId) {
    	$(formId).empty();
    	this.formId = formId;
    };
    
    /**
     * Set the parameter for sending to server
     */
    this.addParam = function addParam(key, value){
    	if (gfn_isNull(this.formId)) {
    		$('#TempForm').remove();
        	$('body').append('<form id="TempForm" style="display:none"></form>');
        	this.formId = $("#TempForm");
    	}
        $(this.formId).append($("<input type='hidden' name='"+key+"' id='"+key+"' value='"+value+"' >"));
    };
    
    this.setMethod = function setMethod(method) {
    	this.method = method;
    }
    
    /**
     * Call URL using parameter and form tag.
     */
    this.submit = function submit(){
    	if (gfn_isNull(this.formId)) {
        	$('body').append('<form id="TempForm" style="display:none"></form>');
        	this.formId = $("#TempForm");
    	}
        var frm = $(this.formId)[0];
        frm.action = this.url;
        frm.method = this.method;
        frm.submit(function (event) {
        	debugger;
        	location.reload();
        });   
    };
    
    $('#' + this.formId).remove();
}


/**
 * Global variable for AJAX call back processing
 */
var gfv_ajaxCallback = "";

/*----------------------------------------------------------------------------*/
/* NAME : restAjax()         	                                 	  	  	  */
/* DESC : This function provide calling the AJAX service					  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
var restAjax = new function () {
	this.url ="";
	this.param = "";
	this.async = true;

	this.setUrl = function setUrl(url){
		this.url = url;
	};

	//JSON data setting
	this.setParam = function setParam(param, method) {
		this.param = JSON.stringify(param);
	}

	this.setAsync = function setAsync(async){
		this.async = async;
	};
	
	this.setCallback = function setCallback(callBack){
		gfv_ajaxCallback = callBack;
	};
	
	this.call = function call(){
		$.ajax({
			type : "POST",
			url : this.url,
			data : this.param,
			async : this.async,
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			success : function(data, status) {
				
				if(typeof(gfv_ajaxCallback) == "function") {
					gfv_ajaxCallback(data);
				}
				else {
					eval(gfv_ajaxCallback + "(data);");
				}
			}
		 ,error: OnError
		});
	};	
}
/*----------------------------------------------------------------------------*/
/* NAME : commonAjax()       	                                 	  	  	  */
/* DESC : This function provide calling the AJAX service					  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
var commonAjax = new function () {
	this.url = "";		
	this.param = "";
	this.async = false,
	
	
	this.setUrl = function setUrl(url){
		this.url = url;
	};
	
	this.setDataType = function setDataType(dataType) {
		this.dataType = dataType;
	}
	this.setCallback = function setCallback(callBack){
		gfv_ajaxCallback = callBack;
	};
	
	this.setAsync = function setAsync(async){
		this.async = async;
	};

	this.clearParam = function clearParam() {
		this.param = "";
	}
	
	this.addParam = function addParam(key,value){
		this.param = this.param + "&" + key + "=" + value; 
	}
	
	this.setJsonParam = function setJsonParam(param) {
		this.param = this.param + "&" + "jsonData=" + JSON.stringify(param);
	}
	
	this.setSerializeParam = function setSerializeParam(param){
		this.param = param;
	}
	
	this.ajax = function ajax(){
		if ( gfn_isNull(this.dataType) == true) {
			this.dataType = "json";
		}
			
		$.ajax({
			url : this.url,
			type : "POST",   
			data : this.param,
			async : this.async,
			dataType : this.dataType,
			success : function(data, status) {
				if(typeof(gfv_ajaxCallback) == "function"){
					gfv_ajaxCallback(data);
				}
				else {
					eval(gfv_ajaxCallback + "(data);");
				}
			}
		 ,error: OnError
		});
	};
}

/*----------------------------------------------------------------------------*/
/* NAME : OnError()       	                                 	  	  	 	  */
/* DESC : This function show the error message when error occurred after 	  */
/* 		  AJAX service call. 												  */
/* 		  The message should be genetate by 500.jsp, Exceptio.jsp, 			  */
/* 		  BizException.jsp LRunException.jsp and so on					  	  */
/*		  @param xhr														  */
/*		  @param errorType													  */
/*		  @param exception													  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function OnError(xhr, errorType, exception ) {

	debugger;
	var responseText="";
    $("#errorMessageDialog").html("");

    try {
        responseText = jQuery.parseJSON(xhr.responseText);
        $("#errorMessageDialog").append("<div><b>" + errorType + " " + exception + "</b></div>");
        $("#errorMessageDialog").append("<div><u>Exception</u>:<br /><br />" + responseText.ExceptionType + "</div>");
        $("#errorMessageDialog").append("<div><u>StackTrace</u>:<br /><br />" + responseText.StackTrace + "</div>");
        $("#errorMessageDialog").append("<div><u>Message</u>:<br /><br />" + responseText.Message + "</div>");
    } catch (e) {
        responseText = xhr.responseText;
        if (gfn_isNull(responseText)) {
        	responseText =  $("#globalerrorMessage").val();
        }
    }		
	
    $("#errorMessageDialog").html(responseText);
    $("#errorMessageDialog").dialog({
        title: _ExceptionTitle,
        width: 700,
	    modal: true,
	    draggable: true,
	    resizable: true,
        buttons: [{
            text: _Close,
        	click: function () {
                $(this).dialog('close');
            }
        }]
    });
    $('.ui-dialog-titlebar').addClass('danger');
    
}

var gfv_pageIndex = null;
var gfv_eventName = null;
/*----------------------------------------------------------------------------*/
/* NAME : gfn_renderPaging()                                   	  	  	 	  */
/* DESC : This function create page number on the screen.					  */
/* 		  @param params The params shoud be included div ID, page index, 	  */
/* 		   total count, page group count, current page index.				  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_renderPaging(params){
	
	var divId = params.divId; //div id for page
	gfv_pageIndex = params.pageIndex; //save current page index to global variable
	var totalCount = params.totalCount; //total search count from database
	var pageGroupCount = params.pageGroupCount; //page group count that is number of showing page
	var pageGroupPrev = pageGroupCount - 1;
	var currentIndex = params.pageIndex; //current page

	if(gfn_isNull(currentIndex) == true){
		currentIndex = 1;
	}
	
	var recordCount = params.recordCount; //The row count a page
	if(gfn_isNull(recordCount) == true){
		recordCount = 20;
	}
	var totalIndexCount = Math.ceil(totalCount / recordCount); // total index count
	gfv_eventName = params.eventName;
	
	$("#"+divId).empty();
	var preStr = "";
	var postStr = "";
	var str = "";
	var totalStr = "";
	
	var first = (parseInt((currentIndex-1) / pageGroupCount) * pageGroupCount) + 1;
	var last = (parseInt(totalIndexCount/pageGroupCount) == parseInt((currentIndex-1)/pageGroupCount)) ? totalIndexCount%pageGroupCount : pageGroupCount;
	var prev = (parseInt((currentIndex-1)/pageGroupCount)*pageGroupCount) - pageGroupPrev > 0 ? (parseInt((currentIndex-1)/pageGroupCount)*pageGroupCount) - pageGroupPrev : 1; 
	var next = (parseInt((currentIndex-1)/pageGroupCount)+1) * pageGroupCount + 1 < totalIndexCount ? (parseInt((currentIndex-1)/pageGroupCount)+1) * pageGroupCount + 1 : totalIndexCount;
	
	if(totalIndexCount > pageGroupCount){ //If total index is more than page group count, create tag for the first and the last
		preStr += "<li class='page_btn befor_prev'>"
		preStr += "<a href='javascript:;' onclick='_movePage(1)'><i class='fa fa-angle-double-left'></i></a>"
		preStr += "</li>"
		preStr += "<li class='page_btn prev'>"
		preStr += "<a href='javascript:;' onclick='_movePage("+prev+")'><i class='fa fa-angle-left	'></i></a>";
		preStr += "</li>"
	}
	else if(totalIndexCount <=pageGroupCount && totalIndexCount > 1){ //If total index is less than page group count, crete tag for previous
		preStr += "<li class='page_btn befor_prev'>"
		preStr += "<a href='javascript:;' onclick='_movePage(1)'><i class='fa fa-angle-double-left'></i></a>";
		preStr += "</li>"
	}
	
	if(totalIndexCount > pageGroupCount){ //If total index more tha page groucount, create tag for next tag
		postStr += "<li class='page_btn next'>";
		postStr += "<a href='javascript:;' onclick='_movePage("+next+")'><i class='fa fa-angle-right'></i></a>";
		postStr += "</li>"
		postStr += "<li class='page_btn after_next'>";
		postStr += "<a href='javascript:;' onclick='_movePage("+totalIndexCount+")'><i class='fa fa-angle-double-right'></i></a>";
		postStr += "</li>";
	}
	else if(totalIndexCount <=pageGroupCount && totalIndexCount > 1){ //If total index less than page group count, create tag next
		postStr += "<li class='page_btn after_next'>";
		postStr += "<a href='javascript:;' onclick='_movePage("+totalIndexCount+")'><i class='fa fa-angle-double-right'></i></a>";
		postStr += "</li>";
	}
	
	for(var i=first; i<(first+last); i++){
		
		if(i != currentIndex){
			str += "<li class='page_list'>";
			str += "<a href='javascript:;' onclick='_movePage("+i+")'>"+i+"</a>";
			str += "</li>";
		}
		else{
			str += "<li class='page_list active'>";
			str += "<a href='javascript:;' onclick='_movePage("+i+")'>"+i+"</a>";
			str += "</li>";
		}
	}
	
	totalStr = "<div class='total_num'>"+_Total+" : " + totalCount + "</div>";
	
	$("#"+divId).append("<ul>" + preStr + str + postStr + "</ul>");
	$("#"+divId).append(totalStr);
}

/*----------------------------------------------------------------------------*/
/* NAME : _movePage()      	                                 	  	  	 	  */
/* DESC : move page															  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function _movePage(value){
	$("#"+gfv_pageIndex).val(value);
	if(typeof(gfv_eventName) == "function"){
		gfv_eventName(value);
	}
	else {
		eval(gfv_eventName + "(value);");
	}
}

/*----------------------------------------------------------------------------*/
/* NAME : calcOffset()      	                               	  	  	 	  */
/* DESC : calcurate Offset													  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function calcOffset() {
    var serverTime = getCookie('serverTime');
    serverTime = serverTime==null ? null : Math.abs(serverTime);
    var clientTimeOffset = (new Date()).getTime() - serverTime;
    setCookie('clientTimeOffset', clientTimeOffset);
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_messageDialog()  	                               	  	  	 	  */
/* DESC : This function is for show message diaglog							  */
/*		  @param message The message should be showed for user				  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_messageDialog(message, pOption) {
	
	var option = "700";
	if(!gfn_isNull(pOption)){
		option = pOption;
	}
	$("#successDialog").html("");
	$("#successDialog").append('<div><p class="aleart_normal">' + message + "</p></div>");
	$("#successDialog").dialog({
		title: _InfoTitle,
		width: option,
	    draggable: true,
	    resizable: true,
		modal: true,
		buttons: [{
			text: _Cancel,
			click: function () {
				$(this).dialog('close');
			}
		}]
	});
	$('.ui-dialog-titlebar').addClass('success');
	
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_removeSpecialChar()  	                       	  	  	 	  	  */
/* DESC : This function is remove special character							  */
/*		  @param message data for remove special character					  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_removeSpecialChar(message) {
	var regExpr = /[^a-zA-Z0-9-. ]/g;
	return message.replace(regExpr, " ");
}
/*----------------------------------------------------------------------------*/
/* NAME : gfn_btnDialog()  	                               	  	  	 	  	  */
/* DESC : This function is for show button diaglog							  */
/*		  @param message Title												  */
/*		  @param pOption Postion											  */
/*		  @param btns buttons				  								  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_btnDialog(message, pOption, btns) {
	
	var option = "700";
	if(!gfn_isNull(pOption)){
		option = pOption;
	}
	$("#successDialog").html("");
	$("#successDialog").append('<div><p class="aleart_normal">' + message + "</p></div>");
	$("#successDialog").dialog({
		title: _InfoTitle,
		width: option,
		modal: true,
	    draggable: true,
	    resizable: true,
		buttons: btns
	});
	$('.ui-dialog-titlebar').addClass('success');
	
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_btnDialog()  	                               	  	  	 	  	  */
/* DESC : This function is for show button diaglog							  */
/*		  @param message Title												  */
/*		  @param pOption Postion											  */
/*		  @param btns buttons				  								  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_errorDialog(message, pOption, btns) {
	
	var option = "700";
	if(!gfn_isNull(pOption)){
		option = pOption;
	}
	$("#errorMessageDialog").html("");
	$("#errorMessageDialog").append('<div><p>' + message + "</p></div>");

	$("#errorMessageDialog").dialog({
		title: _ExceptionTitle,
		width: option,
		modal: true,
	    draggable: true,
	    resizable: true,
		buttons: btns
	});
	$('.ui-dialog-titlebar').addClass('danger');
	
}
/*----------------------------------------------------------------------------*/
/* NAME : gfn_answerDialog()                               	  	  	 	  	  */
/* DESC : This function is for show answer diaglog							  */
/*		  @param message Title												  */
/*		  @param pOption Postion											  */
/*		  @param callback Callback function	  								  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_answerDialog(message, pOption, callback) {
	
	$("#dialog-message").html("");
	$("#dialog-message").append('<div><p class="aleart_normal">' + message + "</p></div>");
	
	$("#dialog-message").dialog({
	    modal: true,
	    draggable: true,
	    resizable: true,
	    //position: ['center', 'center'],
	    //show: 'blind',
	    //hide: 'blind',
	    width: pOption,
	    //dialogClass: 'aleart_normal',
	    closeOnEscape: true,
	    title: _InfoTitle,
	    buttons: [{
	    	text: _Yes,
			click: function () {
				$(this).dialog('close');
				if (typeof callback =="function")
				callback();
				return "Y";
			}},
			{text: _No,
			click: function () {
				$(this).dialog('close');
			}
		}]
	});
	$('.ui-dialog-titlebar').addClass('success');
}

/*----------------------------------------------------------------------------*/
/* NAME : isTableChecked()	                               	  	  	 	  	  */
/* DESC : Checkbox checked	  												  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function isTableChecked(selector){
	
	if(selector.find("tbody").find(":checked").size() > 0){
		return true;
	}else{
		return false;
	}
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_print()	 	     	                       	  	  	 	  	  */
/* DESC : print screen	  													  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_print(divName) {
	$("divName").printElement();	
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_printWindow()	     	                       	  	  	 	  	  */
/* DESC : print window	  													  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_printWindow(printArea) {
	var win = window.open('','','left=0,top=0,width=552,height=477,toolbar=0,scrollbars=0,status =0');
    var headstr = "<html><head><title></title></head><body>";
    var footstr = "</body>";
    var newstr = document.all.item(printArea).innerHTML;
    var oldstr = document.body.innerHTML;
    document.body.innerHTML = headstr+newstr+footstr;
    window.print();
    document.body.innerHTML = oldstr;
    win.close();
    return false;
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_pringDiv()	     	                       	  	  	 	  	  */
/* DESC : print div		  													  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_pringDiv(printable) {

	printable.print({
		// Use Global styles
		globalStyles : false,
		 
		// Add link with attrbute media=print
		mediaPrint : false,
		//Custom stylesheet
		stylesheet : "http://fonts.googleapis.com/css?family=Inconsolata",
		//Print in a hidden iframe
		iframe : false,
		// Don't print this
		noPrintSelector : ".avoid-this",
		// Add this on top
		append : "Free jQuery Plugins<br/>",
		// Add this at bottom
		prepend : "<br/>jQueryScript.net",
		 
		// Manually add form values
		manuallyCopyFormValues: true,
		 
		// resolves after print and restructure the code for better maintainability
		deferred: $.Deferred(),
		 
		// timeout
		timeout: 250,
		// Custom title
		title: null,
		// Custom document type
		doctype: '<!doctype html>'
	});
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_grideDraw()	     	                       	  	  	 	  	  */
/* DESC : print div		  													  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
var drawGrid = new function() {
	
	this.gridFields = new Array();
	this.gridResult = "";
	this.gridName = "";
	this.gridCheckBox = "";

	this.setGridField = function setGridField(field) {
		this.gridFields.push(field); 
	}

	this.setGridResult = function setGridResult(result) {
		this.gridResult = result;
	}
	
	this.setGridName = function setGridName(gridName) {
		this.gridName = gridName;
	}

	this.setGridCheckBox = function setGridCheckBox(checkBox) {
		this.gridCheckBox = checkBox;
	}

	this.draw = function draw() {
		debugger;
		var dataList = "";
		/** In the loop, can't access this why ????**/
		var result = this.gridResult;
		var fields = this.gridFields;
		var checkBox = this.gridCheckBox;
		var gridName = this.gridName;
		$.each(result, function(i, val){
			debugger;
			dataList = '<tr class="list_row">';
			if (checkBox == 'Y') {
				dataList += '<td class="list_td">' + '<input type="checkbox"/>' + '</td>';				
			}
			
			$.each(fields, function(k, field) {
				debugger;
				var data = gfn_nvl(val[field]);
				var isNumber = Number.isInteger(data);
				
				if (data == 0 || data == '') {					
					dataList += '<td class="list_td align-left" id="' + field + '">' + gfn_nvl("") + '</td>';				
				}
				else {
					if (isNumber) {
						dataList += '<td class="list_td" id="' + field + '">' + data + '</td>';						
					}
					else {
						dataList += '<td class="list_td align-left" id="' + field + '">' + data + '</td>';						
					}
				}
			});
			dataList += '</tr>';
			
			$("#" + gridName + " tbody").append(dataList);
			
		});		
	}
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_blockUI()	  		   	                       	  	  	 	  	  */
/* DESC : Block UI		  													  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_blockUI(message) {
	$.blockUI(
		{
		message:message,	
		css: { 
		    border: 'none', 
		    padding: '15px', 
		    backgroundColor: '#000', 
		    '-webkit-border-radius': '10px', 
		    '-moz-border-radius': '10px', 
		    opacity: .5, 
		    color: '#fff' 
		} 
	}); 	
}

/*----------------------------------------------------------------------------*/
/* NAME : gfn_releaseBlockUI()	   	                       	  	  	 	  	  */
/* DESC : Release UI block 													  */
/* DATE : 2016.03.30                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function gfn_releaseBlockUI(message) {
	$.unblockUI();
}


