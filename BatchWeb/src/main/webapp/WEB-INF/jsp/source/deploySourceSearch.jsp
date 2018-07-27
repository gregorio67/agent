<%----------------------------------------------------------------------------%>
<%-- NAME : jobschedule.jsp													--%>
<%-- DESC : The purpose of search deploy source screen		     			--%>
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
var lc_pageIndex = 1;


$(document).ready(function(){
	
	/** Biz Code Create**/
	fn_init();
	
	$(document).ajaxStop($.unblockUI);
	
	// click search button 
	$(".main_srch").on("click", function(event){
		event.preventDefault();
		$.blockUI({ message: '<h1><img src="/resources/image/busy.gif" /> Just a moment...</h1>' });
		
		/*Set Main Search **/
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
	
	
	// Update Source Deploy Detail
	$("#btnUpdate").on("click", function(event){
		
		debugger;
		event.preventDefault();
		$("#board_list tbody tr").removeClass('active');
		$("#board_list").find("input[type=checkbox]").prop("checked", false);
		
		fn_deployUpdate();		
	});
	
	// Delete Source Deploy
	$("#btnDelete").on("click", function(event){
		
		debugger;
		event.preventDefault();
		$("#board_list tbody tr").removeClass('active');
		$("#board_list").find("input[type=checkbox]").prop("checked", false);
		
		fn_deployDelete();		
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
	    maxDate: "+10d",
	    changeMonth: true,
	    changeYear: true,
	    numberOfMonths: 1,
	    onClose: function( selectedDate ) {
		}
	}).datepicker('setDate', "0");;
	
	$( "#deployDt" ).datepicker({
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
	restAjax.setCallback("fn_initCallBack");
	restAjax.setAsync(false);
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
		lc_pageIndex = 1;
	}	
	else {
		sendParam.currentPage = pageIndex;
		lc_pageIndex = pageIndex;
		
	}	
	
	sendParam.bizName= $("#searchBizNameList").val();
	sendParam.deploySource=$("#searchSourceName").val();
	sendParam.fromDt=$("#fromDt").val();	
	sendParam.toDt=$("#toDt").val();		
	
	
	restAjax.setUrl("<c:url value='/deploy/selectList' />");
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
		$("#board_list tbody").append("<tr class='list_row'><td class='list_td' colspan='7'>" + "There is no data" + "</td></tr>");
		
 		var msg = "There is no data";
		gfn_messageDialog(msg);
		return;
		
	}
	
	$.each(result.sourceList, function(i, val){
		dataList = '<tr class="list_row">';			
		dataList += '<td class="list_td">' + '<input type="checkbox"/>' + '</td>';
			
		dataList += '<td class="list_td align-left" id="seq">' + gfn_nvl(val.deploySourceSeq) + '</td>';
		dataList += '<td class="list_td align-left" id="deployPackage">' + gfn_nvl(val.deployPackage) + '</td>';
		dataList += '<td class="list_td align-center" id="deploySource">' + gfn_nvl(val.deploySource) + '</td>';
		dataList += '<td class="list_td align-center" id="deployDate">' + gfn_nvl(val.deployDate) + '</td>';
		dataList += '<td class="list_td align-center" id="deployDescript">' + gfn_nvl(val.deployDescript) + '</td>';
		dataList += '<td class="list_td align-center" id="approveStatus">' + gfn_nvl(val.approveStatus) + '</td>';
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
/* NAME : fn_deployUpdate()							   						  */
/* DESC : Setting the detailed information at the bottom of the list		  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_deployUpdate() {
	
	debugger;
	sendParam = {};
	sendParam.seq = $("#sourceDeploy #merge #seq").val();	
	sendParam.deployDate = $("#sourceDeploy #merge #deployDt").val();
	sendParam.deployDescript = $("#sourceDeploy #merge #description").val();
	

	/** Call Service **/
	restAjax.setUrl("<c:url value='/deploy/update' />");
	restAjax.setCallback("fn_messageCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_deployDelete()							   						  */
/* DESC : Setting the detailed information at the bottom of the list		  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_deployDelete() {
	
	debugger;
	sendParam = {};
	sendParam.seq = $("#sourceDeploy #merge #seq").val();	

	/** Call Service **/
	restAjax.setUrl("<c:url value='/deploy/delete' />");
	restAjax.setCallback("fn_messageCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_messageCallBack()								   				  */
/* DESC : Insert, Update, Delete call back									  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_messageCallBack(result) {

	/** When error occurred, show message **/
	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	fn_Search(lc_pageIndex);
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
	
	var seq = $(params).find("#seq").text();
	fn_searchItem(seq);
	
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_searchItem()													  */
/* DESC : Search Source Item												  */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_searchItem(seq) {
	debugger;
	
	var sendParam = {};
	sendParam.seq = seq;
	/** Call Service **/
	restAjax.setUrl("<c:url value='/deploy/selectItem' />");
	restAjax.setCallback("fn_searchItemCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
	
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_searchItemCallBack()											  */
/* DESC : Search Source Item call back										  */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_searchItemCallBack(result) {
	/** When error occurred, show message **/
	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	debugger;
	$("#sourceDeploy #merge #seq").val(result.sourceItem.deploySourceSeq);
	$("#sourceDeploy #merge #bizName").val(result.sourceItem.bizName);
	$("#sourceDeploy #merge #sourceDir").val(result.sourceItem.deployPackage);
	$("#sourceDeploy #merge #sourceName").val(result.sourceItem.deploySource);
	$("#sourceDeploy #merge #deployDt").val(result.sourceItem.deployDate);
	$("#sourceDeploy #merge #description").val(result.sourceItem.deployDescript);
	
	
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
			<h3>Deploy Source List</h3>
		</div>
		<input type="hidden" id="tempPin"/>
		<!-- Search View -->
		<div class="search_Tblock">
			<table class="search_table">
				<colgroup>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
				</colgroup>
				<tbody>
					<tr class="search_row">
						<th class="list_th">Biz Name</th>
						<td>
							<select class="select_file" id="searchBizNameList">
							</select>
						</td>
						<th class="list_th">Source Name</th>
						<td>
							<input class="search_input" type="text" id="searchSourceName" maxlength="100"/>
						</td>
						<th class="list_th">Deploy Date</th>
						<td class="align-left date" colspan="2">
							<input type="text" class="search_input" id="fromDt" readonly="readonly"/>
							<label class="mid_date">~</label>
							<input type="text" class="search_input" id="toDt" readonly="readonly"/>
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
						<th width="10%">SEQ</th>
						<th width="20%">Source Directory</th>
						<th width="20%">Source Name</th>
						<th width="20%">Deploy Date</th>						
						<th width="20%">Description</th>
						<th width="20%">Approve Status</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="7">There is no data</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="PAGE_NAVI" class="list_page">
		</div>
		
<!-- --------------------------------------------------------------------------------- -->
<!--   Source deploy registration  Start                                               -->
<!-- --------------------------------------------------------------------------------- -->
		<div id=sourceDeploy style="display:none">
			<div class="sub_title">
				<h3>Source Deploy Detail</h3>
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
								<th class="list_th">SEQ</th>
								<td class="list_td align-center">
									<input type="text" name="seq" id="seq" class="disable" readOnly="readOnly">
								</td>
								<th class="list_th">Biz Name</th>
								<td class="list_td align-left">
									<input type="text" name="bizName" id="bizName" class="disable" readOnly="readOnly">
								</td>
							</tr>
							<tr class="detail_view">
								<th class="list_th">Source Name</th>
								<td class="list_td align-left">
									<input type="text" name="sourceName" id="sourceName" class="disable" readOnly="readOnly">
								</td>
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
							<col width="15%"/>
							<col width="35%"/>
							<col width="0%"/>							
							</colgroup>
							<tbody>
								<tr class="detail_view">
									<th class="list_th">Deploy Date</th>
									<td class="list_td align-left date">
										<input type="text" class="search_input" id="deployDt" readonly="readonly"/>
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
				<button class="btn_default" id="btnUpdate">Update</button>
				<button class="btn_default" id="btnDelete">Delete</button>
				<button class="btn_default" id="btnCancel">Cancel</button>
			</div>			
		</div>
	</div>
	<%@ include file="/WEB-INF/include/tailer.jspf" %>
</body>
</html>