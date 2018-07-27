<%----------------------------------------------------------------------------%>
<%-- NAME : jobschedule.jsp													--%>
<%-- DESC : The purpose of job scheulde registration screen     			--%>
<%-- VER  : v1.0                                                            --%>
<%-- Copyright ⓒ 2018 D.Y KIM                                              --%>
<%-- All rights reserved.                                                   --%>
<%----------------------------------------------------------------------------%>
<%--                           Change History                               --%>
<%----------------------------------------------------------------------------%>
<%--    DATE     AUTHOR                      DESCRIPTION                    --%>
<%-- ----------  ------------  -----------------------------------------------%>
<%-- 2016.04.22  Gregorio Kim  Initial creation                             --%>
<%----------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/include/header.jspf" %>
<title>Source Deploy</title>
<script type="text/javascript">

/** When the grid is choosed, send request server to fetch data **/
var vItemSearch="Y";
var isUpdate='N';
var lc_pageIndex = 1;

/** Serch result local storage **/
var lc_mainSearch = 'N';
var lc_rowIndex = 0;
var lc_colIndex = 0;

var lc_sourceName = '';

$(document).ready(function(){
	
	/** Biz Code Create**/
	fn_init();
	
	$(document).ajaxStop($.unblockUI);
	
	// click search button 
	$(".main_srch").on("click", function(event){
		event.preventDefault();
		$.blockUI({ message: '<h1><img src="/resources/image/busy.gif" /> Just a moment...</h1>' });
		
		/*Set Main Search **/
		lc_mainSearch = 'Y';
		fn_Search();
	});
	
	// search enter event
	$("#searchSource, #searchType").on("keydown", function(e){
		if(e.which == 13){
			$(".main_srch").trigger("click");
		}
	});
	
	// click list 
	$(document).on("click", "#board_list > tbody > tr > td", function(){
		debugger;
		var params = $(this).parents("tr");
		fn_gridClickAction(params);				
	});
	
	
	// New Deploy insert
	$("#btnInsert").on("click", function(event){
		
		debugger;
		event.preventDefault();
		$("#board_list tbody tr").removeClass('active');
		$("#board_list").find("input[type=checkbox]").prop("checked", false);
		
		fn_deployInsert();		
	});
	
	// cancel click
	$("#btnCancel").on("click", function(event){
		event.preventDefault();

		$("#board_list tbody tr").removeClass('active');
		$("#board_list tbody tr input[type=checkbox]").prop("checked", false);
		
		$("#sourceDeploy").hide();
		$("#sourceDeploy #merge").hide();
		$("#sourceDeploy .list_btn_group").hide();
	});
	
	// table width
	var onSampleResized = function(e){  
		var table = $(e.currentTarget);  
	};
	
	// table resizable
	$(".colwidth").colResizable({
	    liveDrag:true,
	    onResize:onSampleResized
	});
		
	var dateToday = new Date();
	$( "#fromDt" ).datepicker({
		dateFormat: "yy-mm-dd",
		showOn: "both",
		buttonImageOnly: true,
	    defaultDate: "-1w",
	    minDate: dateToday,
	    maxDate: "+10d",
	    changeMonth: true,
	    changeYear: true,
	    numberOfMonths: 1,
	    onClose: function( selectedDate ) {
	    	//Set From and To 
	    	if($(this).val() != ""){
		    	$("#toDt").datepicker("option", "minDate", selectedDate );
	    		var vToDt = new Date();
	    		$("#toDt").val($.datepicker.formatDate('yy-mm-dd', vToDt));
	    	}
	    }
	}).datepicker('setDate', "0");
	

	$("#toDt").datepicker({
		dateFormat: "yy-mm-dd",
	    showOn: "both",
	    buttonImageOnly: true,
	    defaultDate: "+0d",
	    maxDate: "+0d",
	    changeMonth: true,
	    changeYear: true,
	    numberOfMonths: 1,
	    onClose: function( selectedDate ) {
		}
	});
});


/*----------------------------------------------------------------------------*/
/* NAME : fn_init()															  */
/* DESC : Main Business Code Search											  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_init() {
	debugger;
	sendParam = {};
	sendParam.command="getBizCode";
	restAjax.setUrl("<c:url value='/code/bizCode' />");
	restAjax.setAsync(false);
	restAjax.setCallback("fn_initCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
	
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_initCallBack()													  */
/* DESC : Main Business Code Search	callback								  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_initCallBack(result) {
	debugger;
	
	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	$("#searchBizNameList").empty();
	
	var bizNameOpt = "";
	$.each(result.bizList, function(i, val){
		debugger;
		$("#searchBizNameList").append($('<option/>', {
			value: val.bizName,
			text: val.bizName
		}))
//		bizNameOpt = $("<option></option>").attr("value", val.hostName).attr("text", val.hostName);
//		bizNameList.append(bizNameOpt);
	});
	
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_Search()														  */
/* DESC : Main list search													  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_Search(pageIndex){

	debugger;

	var sendParam = {};
	if (typeof pageIndex == "undefined") {
		sendParam.currentPage = 1;
		curPageIndex = 1;
	}	
	else {
		sendParam.currentPage = pageIndex;
		curPageIndex = pageIndex;
	}	
	
	sendParam.bizName= $("#searchBizNameList").val();
	sendParam.sourceType=$("#searchSourceType").val();
	sendParam.sourceName=$("#searchSourceName").val();
		
	restAjax.setUrl("<c:url value='/source/search' />");
	restAjax.setCallback("fn_searchCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();

}
/*----------------------------------------------------------------------------*/
/* NAME : fn_searchCallBack()												  */
/* DESC : Main list search callback			  			 					  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_searchCallBack(result){
	
	debugger;
	
	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	$("#board_list tbody").empty();
	var dataList = "";
	if(result.page.totalRowCount == 0){
		var size = $("#board_list thead tr th").size();
		$("#board_list tbody").append("<tr class='list_row'><td class='list_td' colspan='4'>" + "There is no data" + "</td></tr>");
		
 		var msg = "There is no data";
		gfn_messageDialog(msg);
		return;
		
	}
	
	$.each(result.sourceList, function(i, val){
		dataList = '<tr class="list_row">';			
		dataList += '<td class="list_td">' + '<input type="checkbox"/>' + '</td>';
			
		dataList += '<td class="list_td align-left" id="sourceDir">' + gfn_nvl(val.sourceDir) + '</td>';
		dataList += '<td class="list_td align-left" id="sourceName">' + gfn_nvl(val.sourceName) + '</td>';
		dataList += '<td class="list_td align-center" id="lastModifiedDate">' + gfn_nvl(val.lastModifiedDate) + '</td>';
		dataList += '</tr>';
		
		$("#board_list tbody").append(dataList);
	});
	

	$.unblockUI();
	/** Pagination **/
	var params = {
		divId : "PAGE_NAVI",
		pageIndex : result.page.currentPage,
		recordCount : result.page.pageRowCount,
		totalCount : result.page.totalRowCount,
		pageGroupCount : result.page.pageGroupCount,
		eventName : "fn_selectPage"	
	};
	
	gfn_renderPaging(params);
	
}


/*----------------------------------------------------------------------------*/
/* NAME : fn_selectPage()											  */
/* DESC : Click on the bottom of the list page								  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_selectPage(pageIndex){
	fn_Search(pageIndex);
}



/*----------------------------------------------------------------------------*/
/* NAME : fn_insert()								   						  */
/* DESC : Setting the detailed information at the bottom of the list		  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_deployInsert() {
	
	debugger;
	sendParam = {};
	sendParam.bizName = $("#sourceDeploy #merge #bizName").val();
	sendParam.deploySource = $("#sourceDeploy #merge #sourceName").val();
	sendParam.deployPackage = $("#sourceDeploy #merge #sourceDir").val();
	sendParam.deployDate = $("#sourceDeploy #merge #fromDt").val();
	sendParam.deployDescript = $("#sourceDeploy #merge #description").val();
	

	/** Call Service **/
	restAjax.setUrl("<c:url value='/deploy/insert' />");
	restAjax.setCallback("fn_insertCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
}

function fn_insertCallBack(result) {

	/** When error occurred, show message **/
	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	gfn_messageDialog(result.message.message, 400);
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_gridClickAction()												  */
/* DESC : Click the list 													  */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_gridClickAction(params){
	
	debugger;
	/** Checkbox all unchecked **/
	$("#board_list tbody tr input[type='checkbox']").prop("checked", false);
	$("#board_list tbody tr").removeClass("active");
	
	/** Selected Row checked **/
	if($(params).hasClass("active")){
		$(params).removeClass('active');
	}else{
		$(params).addClass('active');
	}


	if($(params).find("input[type=checkbox]").prop("checked") && !($(params).hasClass("active"))){
		$(params).find("input[type=checkbox]").prop("checked", false);
	}
	if(!($(params).find("input[type=checkbox]").prop("checked")) && $(params).hasClass("active")){
		$(params).find("input[type=checkbox]").prop("checked", true);
	}
	

	$("#sourceDeploy #merge #bizName").val($("#searchBizNameList option:selected").text());
	$("#sourceDeploy #merge #sourceDir").val($(params).find("#sourceDir").text());
	$("#sourceDeploy #merge #sourceName").val($(params).find("#sourceName").text());
	
	$("#sourceDeploy").show();
	$("#sourceDeploy #merge").show();
	$("#sourceDeploy .list_btn_group").show();
}


</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navigation.jsp" %>

	<div class="container_wrap">
		<div class="sub_title">
			<h3>Deploy Source Search</h3>
		</div>
		<input type="hidden" id="tempPin"/>
		<!-- Search View -->
		<div class="search_Tblock">
			<table class="search_table">
				<colgroup>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
				</colgroup>
				<tbody>
					<tr class="search_row">
						<th>Biz Code</th>
						<td>
							<select class="select_file" id="searchBizNameList">
							</select>
						</td>
						<th>Source Type</th>
						<td>
							<!-- input type="text" id="searchType" list="provSearchType" maxlength="100"/>
							<datalist id="provSearchType">
							</datalist-->
							<select class="select_file" id="searchSourceType">
								<option value="*">All</option>
								<option value=".java">Java</option>
								<option value=".jsp">JSP</option>
								<option value=".xml">XML</option>
								<option value="*.js">JScript</option>
								<option value="*.css">CSS</option>
								<option value="*.img">Image</option>																
							</select>
						</td>
						<th>Source Name</th>
						<td>
							<input class="search_input" type="text" id="searchSourceName" maxlength="100"/>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="search_submit">
				<input class="main_srch" type="button" value="Search">
			</div>
		</div>
		<!-- Detail View -->
		<div class="block_list">
			<table class="list_table colwidth" id="board_list">
				<thead>
					<tr class="list_thead">
						<th width="3%" class="select_check"><input type="checkbox"/></th>
						<th width="40%">Source Directory</th>
						<th width="20%">Source Name</th>
						<th width="20%">Last Modified Date</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="4">There is no data</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="PAGE_NAVI" class="list_page">
		</div>
		
<!-- --------------------------------------------------------------------------------- -->
<!--   Source deploy registration  Start                                                          -->
<!-- --------------------------------------------------------------------------------- -->
		<div id=sourceDeploy style="display:none">
			<div class="sub_title">
				<h3>Source Deploy Registration</h3>
			</div>

<!-- --------------------------------------------------------------------------------- -->
<!--  When the job insert button is clicked, show this division                        -->
<!-- -----------------------------------merge----------------------------------------- -->			
			<div id="merge" style="display:none">
				<div class="block_list">
					<table class="list_table">
						<colgroup>
							<col width="15%"/>
							<col width="35%"/>
							<col width="15%"/>
							<col width="35%"/>
							<col width="0%"/>
						</colgroup>
						<tbody>
							<tr class="detail_view">
								<th class="list_th">Biz Name</th>
								<td class="list_td align-left">
									<input type="text" name="bizName" id="bizName" class="disable" readOnly="readOnly">
								</td>
								<th class="list_th">Source Name</th>
								<td class="list_td align-left">
									<input type="text" name="sourceName" id="sourceName" class="disable" readOnly="readOnly">
								</td>
							</tr>
							<tr class="detail_view">
								<th class="list_th">Source Dir</th>
								<td class="list_td align-left">
									<input type="text" name="sourceDir" id="sourceDir" class="disable" readOnly="readOnly">
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<form id="mergeForm">
					<div class="block_list">
						<table class="list_table">
							<colgroup>
								<col width="15%"/>
								<col width="35%"/>
								<col width="0%"/>								
							</colgroup>
							<tbody>
								<tr class="detail_view">
									<th class="list_th">Deploy Date</th>
									<td class="list_td align-left date">
										<input type="text" class="search_input" id="fromDt" readonly="readonly"/>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="block_list">
						<table class="list_table">
							<colgroup>
							<col width="15%"/>
							<col width="35%"/>
							<col width="0%"/>							
							</colgroup>
							<tbody>
								<tr class="detail_view">
									<th class="list_th">Description</th>
									<!-- td class="list_td_textarea align-left"-->
									<td class="list_td align-left" colspan="2">
										<textarea id="description" style="margin: 3px; width: 1040px; height: 80px;" class="floatL"></textarea>
									</td>
								</tr>
							</tbody>
						</table>
					</div>					
				</form>
			</div>
<!-- --------------------------------------------------------------------------------- -->
			<div class="list_btn_group">
				<button class="btn_default" id="btnInsert">Save</button>
				<button class="btn_default" id="btnCancel">Cancel</button>
			</div>			
		</div>
	</div>
	<%@ include file="/WEB-INF/include/tailer.jspf" %>
</body>
</html>