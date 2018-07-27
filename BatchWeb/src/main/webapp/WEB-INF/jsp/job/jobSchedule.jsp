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
<title>Batch Schedule Register</title>
<script type="text/javascript">

/** When the grid is choosed, send request server to fetch data **/
var vItemSearch="Y";
var isUpdate='N';
var lc_pageIndex = 1;

$(document).ready(function(){
	
	$(document).ajaxStop($.unblockUI);
	
	// click search button 
	$(".main_srch").on("click", function(event){
		event.preventDefault();
		$.blockUI({ message: '<h1><img src="/resources/image/busy.gif" /> Just a moment...</h1>' }); 
		fn_Search();
	});
	
	// search enter event
	$("#searchJobName, #searchJobPath, #searchRegDate").on("keydown", function(e){
		if(e.which == 13){
			$(".main_srch").trigger("click");
		}
	});
	
	// click list 
	$(document).on("click", "#board_list > tbody > tr > td", function(event){
		event.preventDefault();
		debugger;
		/** Get selected row data **/
		var params = $(this).parents("tr");
		
		/** Check job name is null **/
		var jobName = $(params).find("#jobName").text(); 
		if (jobName != '' && jobName != 'undefined') {
			fn_gridClickAction(params);			
		}
	}); 
	
	// New Job insert
	$("#jobInsert").on("click", function(event){
		
		debugger;
		event.preventDefault();
		isUpdate='N';
		$("#board_list tbody tr").removeClass('active');
		$("#board_list").find("input[type=checkbox]").prop("checked", false);
		
		$("#jobDetail #merge input").val("")
						
		$("#jobDetail").show();
		$("#jobDetail #search").hide();
		$("#jobDetail #merge").show();
		$("#jobDetail .list_btn_group").show();
		$("#jobDetail .list_btn_group #btnJobStart").hide();
	});
	
	/** Job Delete **/ 
	$("#jobDelete").on("click", function(event){
		
		event.preventDefault();

		if(!($("#board_list tbody input[type='checkbox']").is(":checked"))){
			var msg = "You should choose the row for delete";
			gfn_messageDialog(msg);
			return;
		}
		
		gfn_answerDialog("Do you want to delete ?", "400", fn_delete);
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
	
	// click save button
	$("#btnSave").on("click", function(event){
		event.preventDefault();
		
		if (isUpdate == 'Y') {
			fn_update();
		}
		else {
			fn_insert();			
		}
	});
	
	/** Start Job button click **/ 
	$("#btnJobStart").on("click", function(event){
		event.preventDefault();
		gfn_answerDialog("Do you want to start job ?", "400", fn_startJob);
	});	
	
	
	// cancel click
	$("#btnCancel").on("click", function(event){
		event.preventDefault();

		$("#board_list tbody tr").removeClass('active');
		$("#board_list tbody tr input[type=checkbox]").prop("checked", false);
		
		$("#jobDetail").hide();
		$("#jobDetail #merge").hide();
		$("#jobDetail .list_btn_group").hide();
	});
	
	// date picker
	$( "#fromDt" ).datepicker({
		dateFormat: "yy-mm-dd",
		showOn: "both",
		buttonImageOnly: true,
	    defaultDate: "-1w",
	    maxDate: "+0d",
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
	});
	
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
/* NAME : fn_Search()														  */
/* DESC : Main list search													  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_Search(pageIndex){

	/** Set json data **/
	var sendParam = {};
	sendParam.jobId = $("#searchJobId").val();
	sendParam.jobName = $("#searchJobName").val();
	sendParam.jobDefaultParam = $("#searchJobPath").val();
	sendParam.fromDt = $( "#fromDt" ).val();
	sendParam.toDt = $( "#toDt" ).val();

	/** Set current page **/
	if (typeof(pageIndex) == "undefined") {
		sendParam.currentPage = 1;
		lc_pageIndex = 1;
	}	
	else {
		sendParam.currentPage = pageIndex;
		lc_pageIndex = pageIndex;
	}

	restAjax.setUrl("<c:url value='/job/schedule/listJob' />");
	restAjax.setCallback("fn_searchCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
	fn_hideDetail();

}
/*----------------------------------------------------------------------------*/
/* NAME : fn_hideDetail()													  */
/* DESC : Hide all attribute for job detail									  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_hideDetail() {
	$("#board_list tbody tr").removeClass('active');
	$("#board_list tbody tr input[type=checkbox]").prop("checked", false);
	
	$("#jobDetail").hide();
	$("#jobDetail #merge").hide();
	$("#jobDetail #search").hide();
	$("#jobDetail .list_btn_group").hide();

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
		$("#board_list tbody").append("<tr class='list_row'><td class='list_td' colspan='10'>" + "There is no data" + "</td></tr>");
		
 		var msg = "There is no data";
		gfn_messageDialog(msg);
		return;
		
	}
	
	drawGrid.setGridField("jobId");
	drawGrid.setGridField("jobName");
	drawGrid.setGridField("jobPath");
	drawGrid.setGridField("jobDefaultParam");
	drawGrid.setGridField("jobExeuctePeriod");
	drawGrid.setGridField("jobShellLoc");
	drawGrid.setGridField("jobShellName");
	drawGrid.setGridField("prevJobId");
	drawGrid.setGridField("nextJobId");
	
//	DrawGrid.setFields(params);
	drawGrid.setGridResult(result.dataList);
	drawGrid.setGridName("board_list");
	drawGrid.setGridCheckBox('Y');
	drawGrid.draw();
/**
	else{

		$.each(result.dataList, function(i, val){
			
			dataList = '<tr class="list_row">';
			
			dataList += '<td class="list_td">' + '<input type="checkbox"/>' + '</td>';
			
			dataList += '<td class="list_td align-left" id="jobId">' + gfn_nvl(val.jobId) + '</td>';
			dataList += '<td class="list_td align-left" id="jobName">' + gfn_nvl(val.jobName) + '</td>';
			dataList += '<td class="list_td align-center" id="jobPath">' + gfn_nvl(val.jobPath) + '</td>';
			dataList += '<td class="list_td align-left" id="jobDefaultParam">' + gfn_nvl(val.jobDefaultParam) + '</td>';
			dataList += '<td class="list_td align-left" id="jobExeuctePeriod">' + gfn_nvl(val.jobExeuctePeriod) + '</td>';
			dataList += '<td class="list_td align-left" id="jobShellLoc">' + gfn_nvl(val.jobShellLoc) + '</td>';
			dataList += '<td class="list_td align-left" id="jobShellName">' + gfn_nvl(val.jobShellName) + '</td>';
			if (val.prevJobId == '0') {
				dataList += '<td class="list_td" id="prevJobId">' + gfn_nvl("") + '</td>';
			}
			else {
				dataList += '<td class="list_td" id="prevJobId">' + gfn_nvl(val.prevJobId) + '</td>';
			}
			if (val.nextJobId == '0') {
				dataList += '<td class="list_td" id="nextJobId">' + gfn_nvl("") + '</td>';				
			}
			else {
				dataList += '<td class="list_td" id="nextJobId">' + gfn_nvl(val.nextJobId) + '</td>';				
			}
			
			dataList += '</tr>';
			
			$("#board_list tbody").append(dataList);
			
		});
	}
**/

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
/* NAME : fn_detailDataSetting()											  */
/* DESC : Setting the detailed information at the bottom of the list		  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_detailDataSetting(params){
	
	debugger;
	/** Seach key **/
	var jobName = $(params).find("#jobName").text(); 
	
	/** Schedule Job Item Search **/
	fn_keySearch(jobName)
	return;
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_keySearch()					         						  */
/* DESC : Setting the detailed information at the bottom of the list		  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_keySearch(jobName) {

	var sendParam = {};
	
	if(typeof(jobName) == 'undefined') {
		var msg = "You should choose the row for detail job information";
		gfn_messageDialog(msg);
		return;
	}
	
	sendParam.jobName = jobName;

	restAjax.setUrl("<c:url value='/job/schedule/selectJob' />");
	restAjax.setCallback("fn_keySearchCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();		
}


/*----------------------------------------------------------------------------*/
/* NAME : fn_keySearchCallBack()					   						  */
/* DESC : Setting the detailed information at the bottom of the list		  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_keySearchCallBack(result) {

	debugger;
	/** When error occurred, show message **/
	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	/** Set job schedule detail **/
	$("#jobDetail").hide();
	$("#jobDetail #search").hide();
	$("#jobDetail #merge").hide();
	$("#jobDetail .list_btn_group").hide();
	
	/** Set data for detail **/
	$("#jobDetail #search #jobId").val(gfn_nvl(result.data.jobId));
	$("#jobDetail #search #jobName").val(gfn_nvl(result.data.jobName));

	$("#jobDetail #search #jobPath").val(gfn_nvl(result.data.jobPath));
	$("#jobDetail #search #jobDefaultParam").val(gfn_nvl(result.data.jobDefaultParam));
	$("#jobDetail #search #jobExeuctePeriod").val(gfn_nvl(result.data.jobExeuctePeriod));
	$("#jobDetail #search #jobShellLoc").val(gfn_nvl(result.data.jobShellLoc));
	$("#jobDetail #search #jobShellName").val(gfn_nvl(result.data.jobShellName));
	$("#jobDetail #search #jobAgentHost").val(gfn_nvl(result.data.jobAgentHost));
	$("#jobDetail #search #jobAgentPort").val(gfn_nvl(result.data.jobAgentPort));

	if (result.data.nextJobId == '0') {
		$("#jobDetail #search #nextJobId").val(gfn_nvl(""));		
	}
	else {
		$("#jobDetail #search #nextJobId").val(gfn_nvl(result.data.nextJobId));				
	}
	if (result.data.prevJobId == '0') {
		$("#jobDetail #search #prevJobId").val(gfn_nvl(""));
	}
	else {
		$("#jobDetail #search #prevJobId").val(gfn_nvl(result.data.prevJobId));		
	}
	if (result.data.useYn != 'Y') {
		$("#jobDetail #search #useYn").prop("checked", false);		
	}
	else {
		$("#jobDetail #search #useYn").prop("checked", true);		
	}

	$("#jobDetail #search #approveStatus").val(gfn_nvl(result.data.approveStatus));
	
	/** UseYn is not Yes, then Job Start Button hide **/
	/** If useYn is 'Y', then can't change job execution period, only change in approve screen **/
	if (result.data.approveStatus != 'Y') {
		$("#jobDetail .list_btn_group #btnJobStart").hide();
		$("#jobDetail .list_btn_group #btnSave").show();

		$("#jobDetail #search #jobId").prop("readonly", false);
		$("#jobDetail #search #jobName").prop("readonly", false);
		$("#jobDetail #search #jobPath").prop("readonly", false);
		$("#jobDetail #search #jobExeuctePeriod").prop("readonly", false);
		$("#jobDetail #search #jobDefaultParam").prop("readonly", false);
		$("#jobDetail #search #jobExeuctePeriod").prop("readonly", false);
		$("#jobDetail #search #jobShellLoc").prop("readonly", false);
		$("#jobDetail #search #jobShellName").prop("readonly", false);
		$("#jobDetail #search #jobAgentHost").prop("readonly", false);
		$("#jobDetail #search #jobAgentPort").prop("readonly", false);
		$("#jobDetail #search #prevJobId").prop("readonly", false);
		$("#jobDetail #search #nextJobId").prop("readonly", false);
		
	}
	else {
		$("#jobDetail .list_btn_group #btnJobStart").show();
		$("#jobDetail .list_btn_group #btnSave").hide();

		$("#jobDetail #search #jobId").prop("readonly", true);
		$("#jobDetail #search #jobName").prop("readonly", true);
		$("#jobDetail #search #jobPath").prop("readonly", true);
		$("#jobDetail #search #jobExeuctePeriod").prop("readonly", true);
		$("#jobDetail #search #jobDefaultParam").prop("readonly", true);
		$("#jobDetail #search #jobExeuctePeriod").prop("readonly", true);
		$("#jobDetail #search #jobShellLoc").prop("readonly", true);
		$("#jobDetail #search #jobShellName").prop("readonly", true);
		$("#jobDetail #search #jobAgentHost").prop("readonly", true);
		$("#jobDetail #search #jobAgentPort").prop("readonly", true);
		$("#jobDetail #search #prevJobId").prop("readonly", true);
		$("#jobDetail #search #nextJobId").prop("readonly", true);
	}
	

	$("#jobDetail").show();
	$("#jobDetail #search").show();
	$("#jobDetail .list_btn_group").show();
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_insert()								   						  */
/* DESC : Setting the detailed information at the bottom of the list		  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_insert() {

	isUpdate = 'N';
	//Check Job Name is null
	if(gfn_isNull($("#jobDetail #merge #jobName").val())){
		var msg = "Job Name is required";
		fn_showMessage("jobName", msg);
		return;
	}

	//Check Job Name is null
	if(gfn_isNull($("#jobDetail #merge #jobExeuctePeriod").val())){
		var msg = "Job Period is required";
		fn_showMessage("jobExeuctePeriod", msg);
		return;
	}
	//Check Job Name is null
	if(gfn_isNull($("#jobDetail #merge #jobShellLoc").val())){
		var msg = "Shell Location is required";
		fn_showMessage("jobShellLoc", msg);
		return;
	}

	//Check Job Name is null
	if(gfn_isNull($("#jobDetail #merge #jobShellName").val())){
		var msg = "Shell Name is required";
		fn_showMessage("jobShellName", msg);
		return;
	}
	
	var sendParam = {};
	sendParam.jobPath = $("#jobDetail #merge #jobPath").val();
	sendParam.jobName = $("#jobDetail #merge #jobName").val();
	sendParam.jobDefaultParam = $("#jobDetail #merge #jobDefaultParam").val();
	sendParam.jobExeuctePeriod = $("#jobDetail #merge #jobExeuctePeriod").val();
	sendParam.jobShellLoc = $("#jobDetail #merge #jobShellLoc").val();
	sendParam.jobShellName = $("#jobDetail #merge #jobShellName").val();
	sendParam.jobAgentHost = $("#jobDetail #merge #jobAgentHost").val();
	sendParam.jobAgentPort = $("#jobDetail #merge #jobAgentPort").val();

	if ($("#jobDetail #merge #nextJobId").val() == '') {
		sendParam.nextJobId = 0;
	}
	else {
		sendParam.nextJobId = $("#jobDetail #merge #nextJobId").val();		
	}
	
	if ($("#jobDetail #merge #prevJobId").val() == '') {
		sendParam.prevJobId = 0;
	}
	else {
		sendParam.prevJobId = $("#jobDetail #merge #prevJobId").val();		
	}

	/** Call Service **/
	restAjax.setUrl("<c:url value='/job/schedule/insertJob' />");
	restAjax.setCallback("fn_insertCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_insertCallBack()											  */
/* DESC : Insert agency end-user data callback function		  				  */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_insertCallBack(result){

	if (gfn_showErrMessage(result.message)) {
		return;
	}

	/** Search current page after insert job schedule **/
	fn_Search(lc_pageIndex);
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_update()														  */
/* DESC : Delete agency end-user data				 						  */
/* DATE : 2016.04.28                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_update(){
	
	debugger;
	var sendParam = {};
	sendParam.jobPath = $("#jobDetail #search #jobPath").val();
	sendParam.jobName = $("#jobDetail #search #jobName").val();
	sendParam.jobDefaultParam = $("#jobDetail #search #jobDefaultParam").val();
	sendParam.jobExeuctePeriod = $("#jobDetail #search #jobExeuctePeriod").val();
	sendParam.jobShellLoc = $("#jobDetail #search #jobShellLoc").val();
	sendParam.jobShellName = $("#jobDetail #search #jobShellName").val();
	sendParam.jobAgentHost = $("#jobDetail #search #jobAgentHost").val();
	sendParam.jobAgentPort = $("#jobDetail #search #jobAgentPort").val();
	
	sendParam.nextJobId = $("#jobDetail #search #nextJobId").val();		
	sendParam.prevJobId = $("#jobDetail #search #prevJobId").val();	
	
	if ($("#jobDetail #search #useYn").prop('checked')) {
		sendParam.useYn='Y';
	}
	else {
		sendParam.useYn='N';		
	}

	restAjax.setUrl("<c:url value='/job/schedule/updateJob' />");
	restAjax.setCallback("fn_updateCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();	
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_updateCallBack()						    					  */
/* DESC :  Job schedule update callback function     		  				  */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_updateCallBack(result){

	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	fn_Search(lc_pageIndex);
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_delete()														  */
/* DESC : Delete agency end-user data				 						  */
/* DATE : 2016.04.28                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_delete(){
	
	debugger;
	var sendParam = {};
	
	$.each($("#board_list tbody tr td").find(":checked").parents("tr"), function(i, val){
		sendParam.jobName = $(val).find("#jobName").text();
		sendParam.jobId =  $(val).find("#jobId").text();
	});
	
	
	restAjax.setUrl("<c:url value='/job/schedule/deleteJob' />");
	restAjax.setCallback("fn_deleteCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_deleteCallbak()												  */
/* DESC : Delete agency administrator data callback function				  */
/* DATE : 2016.04.28                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_deleteCallBack(result){
	
	if (gfn_showErrMessage(result.message)) {
		return;
	}

	gfn_messageDialog(result.message.message);
	fn_Search(lc_pageIndex);
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
	
	if($(params).hasClass("active")){
		isUpdate = 'Y';
		fn_detailDataSetting(params);
	}
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_startJob()	        											  */
/* DESC : Start Job       													  */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_startJob() {
	debugger;
	
	var sendParam = {};
//	sendParam.jobId =  $("#jobDetail #search #jobid").val();
	sendParam.jobName = $("#jobDetail #search #jobName").val();

	restAjax.setUrl("<c:url value='/job/startJob' />");
	restAjax.setCallback("fn_startJobCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
	
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_startJob()	        											  */
/* DESC : Start Job       													  */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_startJobCallBack(result) {
	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	gfn_messageDialog(result.message.message, 400);
}

</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navigation.jsp" %>

	<div class="container_wrap">
		<div class="sub_title">
			<h3>Job Registration</h3>
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
				</colgroup>
				<tbody>
					<tr class="search_row">
						<th>Job ID</th>
						<td>
							<input class="search_input" type="text" id="searchJobId" maxlength="100"/>
						</td>
						<th>Job Name</th>
						<td>
							<input class="search_input" type="text" id="searchJobName" maxlength="100"/>
						</td>
						<th>Job Path</th>
						<td>
							<input class="search_input" type="text" id="searchJobPath" maxlength="100"/>
						</td>
						<th>Registration Date</th>
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
						<th width="10%">Job ID</th>
						<th width="10%">Job Name</th>
						<th width="20%">Job Path</th>
						<th width="20%">Job Parameter</th>
						<th width="12%">Job Period</th>
						<th width="12%">Shell Location</th>
						<th width="10%">Shell Name</th>
						<th width="11%">Prev Job ID</th>
						<th width="11%">Next Job ID</th>
<%-- 						<th width="12%"><spring:message code="label.stat_yn"/></th> --%>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="10">There is no data</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="PAGE_NAVI" class="list_page">
		</div>
		<div class="list_btn_group">
			<button class="btn_default" id="jobInsert">Insert</button>
			<button class="btn_default" id="jobDelete">Delete</button>
		</div>
		
		
<!-- --------------------------------------------------------------------------------- -->
<!--   Job Detail show  Start                                                          -->
<!-- --------------------------------------------------------------------------------- -->
		<div id=jobDetail style="display:none">
			<div class="sub_title">
				<h3>Batch Schedule Registration</h3>
			</div>

<!-- --------------------------------------------------------------------------------- -->
<!--  When click grid show this division                                            -->
<!-- -----------------------------------search----------------------------------------- -->			
			<div id="search" style="display:none">
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
								<th class="list_th">Job ID</th>
								<td class="list_td align-left">
									<input type="text" name="jobId" id="jobId" class="disable" readOnly="readOnly">
								</td>
								<th class="list_th">Job Name</th>
								<td class="list_td align-left">
									<input type="text" name="jobName" id="jobName" class="disable" readOnly="readOnly">
								</td>
							</tr>
						</tbody>
					</table>
				</div>	
				<!-- Change Area -->
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
									<th class="list_th">Job Path</th>
									<td class="list_td align-left">
										<input type="text" name="jobPath" id="jobPath" class="floatL" maxlength="200">
									</td>
								</tr>
								<tr class="detail_view">
									<th class="list_th">Job Parameter</th>
									<td class="list_td align-center">
										<input type="text" name="jobDefaultParam" id="jobDefaultParam" class="floatL" maxlength="50">
									</td>
									<th class="list_th require">Job Period</th>
									<td class="list_td align-left">
										<input type="text" name="jobExeuctePeriod" id="jobExeuctePeriod" class="floatL" maxlength="50">
									</td>
								</tr>
								<tr class="detail_view">
									<th class="list_th require">Shell Location</th>
									<td class="list_td align-left">
										<input type="text" name="jobShellLoc" id="jobShellLoc" class="floatL" maxlength="50">
									</td>
									<th class="list_th require">Shell Name</th>
									<td class="list_td align-left">
										<input type="text" name="jobShellName" id="jobShellName" class="floatL" maxlength="50">
									</td>
								</tr>
								<tr class="detail_view">
									<th class="list_th">Agent Host</th>
									<td class="list_td align-left">
										<input type="text" id="jobAgentHost" class="floatL" maxlength="50">
									</td>
									<th class="list_th">Agent Port</th>
									<td class="list_td align-left">
										<input type="text" id="jobAgentPort" class="floatL" maxlength="50">
									</td>
								</tr>																
								<tr class="detail_view">
									<th class="list_th">Prev Job ID</th>
									<td class="list_td align-left">
										<input type="text" name="prevJobId" id="prevJobId" class="floatL" maxlength="10">
									</td>
									<th class="list_th">Next Job ID</th>
									<td class="list_td align-left">
										<input type="text" name="nextJobId" id="nextJobId" class="floatL" maxlength="10">
									</td>
								</tr>
								<tr class="detail_view">
									<th class="list_th">Job Status</th>
									<td class="list_td align-left">
										<input type="checkbox" name="useYn" id="useYn" class="floatL" maxlength="10">
									</td>
									<th class="list_th">Approve Status</th>
									<td class="list_td align-left">
										<input type="text" name="approveStatus" id="approveStatus" maxlength="10" class="disable" readOnly="readOnly">
									</td>
								</tr>								
						</tbody>
					</table>
				</div>
			</div>
<!-- --------------------------------------------------------------------------------- -->
<!--  When the job insert button is clicked, show this division                        -->
<!-- -----------------------------------merge----------------------------------------- -->			
			<div id="merge" style="display:none">
				<form id="mergeForm">
					<input type="hidden" name="SEQ" class="require">
					<input type="hidden" name="IND_ID" class="require">
					<input type="hidden" id="chkUserPin">
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
									<th class="list_th require">Job Name</th>
									<td class="list_td align-left">
										<input type="text" id="jobName" class="floatL" maxlength="50">
									</td>
									<th class="list_th">Job Path</th>
									<td class="list_td align-left">
										<input type="text" id="jobPath" class="floatL" maxlength="100">
									</td>
								</tr>
								<tr class="detail_view">
									<th class="list_th">Job Parameter</th>
									<td class="list_td align-left">
										<input type="text" id="jobDefaultParam" class="floatL" maxlength="50">
									</td>
									<th class="list_th require">Job Period</th>
									<td class="list_td align-left">
										<input type="text" id="jobExeuctePeriod" class="floatL" maxlength="50">
									</td>
								</tr>
								<tr class="detail_view">
									<th class="list_th require">Shell Location</th>
									<td class="list_td align-left">
										<input type="text" id="jobShellLoc" class="floatL" maxlength="50">
									</td>
									<th class="list_th require">Shell Name</th>
									<td class="list_td align-left">
										<input type="text" id="jobShellName" class="floatL" maxlength="50">
									</td>
								</tr>
								<tr class="detail_view">
									<th class="list_th">Agent Host</th>
									<td class="list_td align-left">
										<input type="text" id="jobAgentHost" class="floatL" maxlength="50">
									</td>
									<th class="list_th require">Agent Port</th>
									<td class="list_td align-left">
										<input type="text" id="jobAgentPort" class="floatL" maxlength="50">
									</td>
								</tr>								
								<tr class="detail_view">
									<th class="list_th">Prev Job ID</th>
									<td class="list_td align-left">
										<input type="text" id="prevJobId" class="floatL" maxlength="10">
									</td>
									<th class="list_th">Next Job ID</th>
									<td class="list_td align-left">
										<input type="text" id="nextJobId" class="floatL" maxlength="10">
									</td>
								</tr>
								<tr class="detail_view">
									<th class="list_th">Job Status</th>
									<td class="list_td align-left">
										<input type="checkbox" name="useYn" id="useYn" class="floatL" maxlength="10">
									</td>
									<th class="list_th">Approve Status</th>
									<td class="list_td align-left">
										<input type="text" name="approveStatus" id="approveStatus" maxlength="10" class="disable" readOnly="readOnly">
									</td>
								</tr>
								
							</tbody>
						</table>
					</div>
				</form>
			</div>
<!-- --------------------------------------------------------------------------------- -->
			<div class="list_btn_group">
				<button class="btn_default red" id="btnJobStart">Job Start</button>
				<button class="btn_default blue" id="btnSave">Save</button>
				<button class="btn_default" id="btnCancel">Cancel</button>
			</div>			
		</div>
	</div>
	<%@ include file="/WEB-INF/include/tailer.jspf" %>
</body>
</html>