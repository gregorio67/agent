<%----------------------------------------------------------------------------%>
<%-- NAME : adpUserReg.jsp													--%>
<%-- DESC : The purpose of adpUserReg screen is  							--%>
<%-- 		registration of Legal Entity End User							--%>
<%-- VER  : v1.0                                                            --%>
<%-- Copyright ⓒ 2016 LG CNS Uzbekistan                                    --%>
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
<title>Batch Management System</title>
<script type="text/javascript">

/** When the grid is choosed, send request server to fetch data **/
var vItemSearch="Y";
var isUpdate='N';
var curPageIndex = 1;
var isStepSearch = 'true';

$(document).ready(function(){
	
	// click search button 
	$(".main_srch").on("click", function(){
		fn_Search();
	});
	
	// search enter event
	$("#searchJobName, #fromDt").on("keydown", function(e){		
		if(e.which == 13){
			$(".main_srch").trigger("click");
		}
	});
	
	// click list 
	$(document).on("click", "#job_list > tbody > tr > td", function(){
		var params = $(this).parents("tr");

		/** Clear Active **/
		$("#job_list tbody tr").removeClass("active");
		$("#job_list tbody tr input[type='checkbox']").prop("checked", false);
		
		fn_gridClickAction(params);
	});
	
	// check all
	$("#job_list thead tr th input[type='checkbox']").on("click", function(){
		debugger;
		if($(this).prop("checked")){
			$("#job_list tbody tr input[type='checkbox']").prop("checked", true);
			$("#job_list tbody tr").addClass("active");
		}else{
			$("#job_list tbody tr input[type='checkbox']").prop("checked", false);
			$("#job_list tbody tr").removeClass("active");
		}
		
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
	$("#btnSave").on("click", function(e){
		if (isUpdate == 'Y') {
			fn_update();
		}
		else {
			fn_insert();			
		}
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
	debugger;
	
	$("#stepDetail").hide();
	var sendParam = {};
	sendParam.jobName = $("#searchJobName").val();
	sendParam.fromDt = $( "#fromDt" ).val();
	sendParam.toDt = $( "#toDt" ).val();

	if (typeof pageIndex == "undefined") {
		sendParam.currentPage = 1;
		curPageIndex = 1;
	}	
	else {
		sendParam.currentPage = pageIndex;
		curPageIndex = pageIndex;
	}
	if ($("#searchUseYn").prop('checked')) {
		sendParam.useYn = 'Y';
	}

	restAjax.setUrl("<c:url value='/job/jobResult' />");
	restAjax.setCallback("fn_searchCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
	fn_hideDetail();

}

/*----------------------------------------------------------------------------*/
/* NAME : fn_searchCallBack()												  */
/* DESC : Main list search callback			  			 					  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_searchCallBack(result){
	
	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	$("#job_list tbody").empty();
	var dataList = "";
	if(result.page.totalRowCount == 0){
		var size = $("#job_list thead tr th").size();
		$("#job_list tbody").append("<tr class='list_row'><td class='list_td' colspan='8'>" + "There is no data" + "</td></tr>");
		
 		var msg = "There is no data";
		gfn_messageDialog(msg);
		return;
		
	}

	$.each(result.jobList, function(i, val){
			
		dataList = '<tr class="list_row" rowId="' + i + '">';
		dataList += '<td class="list_td">' + '<input type="checkbox"/>' + '</td>';
		dataList += '<td class="list_td align-center" id="jobName">' + gfn_nvl(val.jobName) + '</a></td>';
		dataList += '<td class="list_td align-center" id="jobExecutionId">' + gfn_nvl(val.jobExecutionId) + '</td>';
		dataList += '<td class="list_td align-center" id="jobStartTime">' + gfn_nvl(val.jobStartTime) + '</td>';
		dataList += '<td class="list_td align-center" id="jobEndTime">' + gfn_nvl(val.jobEndTime) + '</td>';

		/** Check Job Status **/
		if (val.jobStatus == 'STARTING' || val.jobStatus == 'STARTED') {
			dataList += '<td class="list_td align-center job_status_run" id="jobStatus">' + gfn_nvl(val.jobStatus) + '</td>';				
		}
		else if (val.jobStatus == 'ABANDONED' || val.jobStatus == 'UNKNOWN' || val.jobStatus == 'FAILED' ) {
			dataList += '<td class="list_td align-center job_status_error" id="jobStatus">' + gfn_nvl(val.jobStatus) + '</td>';								
		}
		else {
			dataList += '<td class="list_td align-center job_status_normal" id="jobStatus">' + gfn_nvl(val.jobStatus) + '</td>';				
		}
			
		var jobParam = gfn_nvl(val.jobParam);
		var dispJobParam = jobParam.substring(0,30) + "...";
			
		if (jobParam.length > 0) {
			dataList += '<td class="list_td" id="exitMessage">' + '<a href="#" onclick="fn_showJobMessage(\'' + gfn_nvl(val.jobParam) + '\')">' +  dispJobParam + '</a></td>';			
		}
		else {
			dataList += '<td class="list_td" id="exitMessage">' + jobParam + '</td>';						
		}
		/** Create Action Buttion **/
		/** If current job status STRATING or STARTED, the job can stop **/
		/** If current job status ABNADON, UNKNOWN or FAILED, the job can restart **/ 
		/** If current job status COMPLETED, there is no action **/
		if (val.jobStatus == 'STARTING' || val.jobStatus == 'STARTED') {
			dataList += '<td class="list_td">' + '<input class="btn_default red" id="btnAction" type="button" value="STOP" onclick="fn_jobStop(\'' +  gfn_nvl(val.jobName) + "','" +  gfn_nvl(val.jobExecutionId) + '\')"></td>';				
		}
		else if (val.jobStatus == 'FAILED' || val.jobStatus == 'STOPPED' || val.jobStatus == 'UNKNOWN'){
			dataList += '<td class="list_td">' + '<input class="btn_default blue" id="btnAction" type="button" value="RESTART" onclick="fn_jobRestart(\'' +  gfn_nvl(val.jobName) + "','" +  gfn_nvl(val.jobExecutionId) + '\')"></td>';				
		}
		else {
			dataList += '<td lass="list_td  id="noaction">' + gfn_nvl("") + '</td>';								
		}		
		dataList += '</tr>';

		/** Add data to table **/
		$("#job_list tbody").append(dataList);
						
	});
	

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
/* NAME : fn_hideDetail()													  */
/* DESC : Hide all attribute for job detail									  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_hideDetail() {
	$("#job_list tbody tr").removeClass('active');
	$("#job_list tbody tr input[type=checkbox]").prop("checked", false);
	
	$("#jobDetail").hide();
	$("#jobDetail #merge").hide();
	$("#jobDetail #search").hide();
	$("#jobDetail .list_btn_group").hide();

}
/*----------------------------------------------------------------------------*/
/* NAME : fn_selectPage()											  */
/* DESC : Click on the bottom of the list page								  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_selectPage(pageIndex){
	debugger;
	fn_Search(pageIndex);
}


/*----------------------------------------------------------------------------*/
/* NAME : fn_stepSearch()					         						  */
/* DESC : Search batch job step result										  */
/* DATE : 2016.04.26                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_stepSearch(jobExecId) {
	debugger;
	
	var sendParam = {};
	sendParam.jobExecutionId = jobExecId;

	restAjax.setUrl("<c:url value='/job/stepResult' />");
	restAjax.setCallback("fn_setpSearchCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();		
}


function fn_setpSearchCallBack(result) {
	
	debugger;
	if (gfn_showErrMessage(result.message)) {
		return;
	}
		
	/** Delete all previous data from table **/
	$("#step_list tbody").empty();

	var dataList = "";

	/** If no search data **/
	if (result.message.status == 'F') {
		var size = $("#step_list thead tr th").size();
		$("#step_list tbody").append("<tr class='list_row'><td class='list_td' colspan='8'>" + "There is no data" + "</td></tr>");
		
 		var msg = result.message.status;
		gfn_messageDialog(msg);
		return;
	}
	
	$.each(result.stepList, function(i, val){
			
		dataList = '<tr class="list_row">';
			
		dataList += '<td class="list_td align-left" id="stepName">' + gfn_nvl(val.stepName) + '</td>';
		dataList += '<td class="list_td align-left" id="stepStartTime">' + gfn_nvl(val.stepStartTime) + '</td>';
		dataList += '<td class="list_td align-center" id="stepEndTime">' + gfn_nvl(val.stepEndTime) + '</td>';
		if (val.exitCode == 'STARTING' || val.exitCode == 'STARTED') {
			dataList += '<td class="list_td job_status_run" id="stepStatus">' + gfn_nvl(val.status) + '</td>';
		}
		
		else if (val.exitCode == 'ABANDONED' || val.exitCode == 'UNKNOWN' || val.exitCode == 'FAILED' ) {
			dataList += '<td class="list_td job_status_error" id="stepStatus">' + gfn_nvl(val.status) + '</td>';
		}
		else {
			dataList += '<td class="list_td job_status_normal" id="stepStatus">' + gfn_nvl(val.status) + '</td>';
		}

		dataList += '<td class="list_td id="readCount">' + gfn_nvl(val.readCount) + '</td>';
		dataList += '<td class="list_td" id="writeCount">' + gfn_nvl(val.writeCount) + '</td>';
		if (val.exitCode == 'STARTING' || val.exitCode == 'STARTED') {
			dataList += '<td class="list_td job_status_run" id="exitCode">' + gfn_nvl(val.exitCode) + '</td>';
		}
		
		else if (val.exitCode == 'ABANDONED' || val.exitCode == 'UNKNOWN' || val.exitCode == 'FAILED' ) {
			dataList += '<td class="list_td job_status_error" id="exitCode">' + gfn_nvl(val.exitCode) + '</td>';
		}
		else {
			dataList += '<td class="list_td job_status_normal" id="exitCode">' + gfn_nvl(val.exitCode) + '</td>';
		}
		debugger;
		var exitMessage = gfn_nvl(val.exitMessage);
		var dispExitMessage = exitMessage.substring(0,20) + "...";

		if (exitMessage.length > 0) {
			dataList += '<td class="list_td" id="exitMessage">' + '<a href="#" onclick="fn_showExitMessage(\'' + gfn_removeSpecialChar(gfn_nvl(val.exitMessage)) + '\')">' +  dispExitMessage + '</a></td>';			
		}
		else {
			dataList += '<td class="list_td" id="exitMessage">' + exitMessage + '</td>';						
		}
		
		dataList += '</tr>';
			
		$("#step_list tbody").append(dataList);
			
	});
	
	$("#stepDetail").show();
}


/*----------------------------------------------------------------------------*/
/* NAME : fn_jobRestart()													  */
/* DESC : Click the list 													  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_jobRestart(jobName, jobId) {
	sendParam = {};
	sendParam.jobName = jobName;
	sendParam.jobId = jobId;
	
	restAjax.setUrl("<c:url value='/job/restartJob' />");
	restAjax.setCallback("fn_jobActionCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();	
		
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_jobActionCallBack()											  */
/* DESC : Start, Restart Button Call back									  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_jobActionCallBack(result) {

	isStepSearch = 'false';	

	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	var msg = result.message.message;
	gfn_messageDialog(msg);
	
	
	/** Search Job Result **/
	fn_Search(curPageIndex);
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_jobStop()														  */
/* DESC : Click the list 													  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_jobStop(jobName, jobId) {
	debugger;
	
	sendParam = {};
	sendParam.jobName = jobName;
	sendParam.jobId = jobId;
	
	restAjax.setUrl("<c:url value='/job/stopJob' />");
	restAjax.setCallback("fn_jobActionCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_jobAbandon()													  */
/* DESC : Click the list 													  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_jobAbandon(jobName, jobId) {
	debugger;
	
	sendParam = {};
	sendParam.jobName = jobName;
	sendParam.jobId = jobId;
	
	restAjax.setUrl("<c:url value='/job/abandonJob' />");
	restAjax.setCallback("fn_jobActionCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_showJobMessage()												  */
/* DESC : Click the list 													  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_showJobMessage(message) {
	isStepSearch = 'false';	
	message = '<p class="job_text">Job&nbsp;Parameter</p>' + "<BR>" + message;
	gfn_messageDialog(message);
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_showJobMessage()												  */
/* DESC : Click the list 													  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_showExitMessage(message) {
	message = '<p class="job_text">Exit&nbsp;Message</p>' + "<BR>" + message;
	gfn_messageDialog(message);
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_gridClickAction()												  */
/* DESC : Click the list 													  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_gridClickAction(params){
	
	debugger;
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
	
	/** Get Job Execution Id **/
	var jobExecId = $(params).find("#jobExecutionId").text();
	
	/** If Action Button is not clicked **/
//	var isButtonClicked = $(params).find("#btnAction").data('status');
	if (isStepSearch == 'true') {
		/** Search Step Result **/
		fn_stepSearch(jobExecId);				
	}
	
	isStepSearch = 'true';
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_gridClickAction()												  */
/* DESC : Click the list 													  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/

</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navigation.jsp" %>

	<div class="container_wrap">
		<div class="sub_title">
			<h3>Job Result</h3>
		</div>
		<!-- Search View -->
		<div class="search_Tblock">
			<table class="search_table">
				<colgroup>
					<col width="100px"/>
					<col width="100px"/>
				</colgroup>
				<tbody>
					<tr class="search_row">
						<th>Job Name</th>
						<td>
							<input class="search_input" type="text" id="searchJobName" maxlength="100"/>
						</td>
						<th style="width:300px; padding:5;">Job Execution Date</th>
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
			<table class="list_table colwidth" id="job_list">
				<thead>
					<tr class="list_thead">
						<th width="3%" class="select_check"><input type="checkbox"/></th>
						<th width="10%">Job Name</th>
						<th width="7%">Exec ID</th>
						<th width="15%">Start Time</th>
						<th width="15%">End Time</th>
						<th width="10%">Job Status</th>
						<th width="20%">Job Parameter</th>
						<th width="10%">Job Action</th>
<%-- 						<th width="12%"><spring:message code="label.stat_yn"/></th> --%>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="8">There is no data</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="PAGE_NAVI" class="list_page">
		</div>		
		
<!-- --------------------------------------------------------------------------------- -->
<!--   Step Detail show  Start                                                          -->
<!-- --------------------------------------------------------------------------------- -->
		<BR>
		<div id=stepDetail style="display:none">
			<div class="sub_title">
				<h3>Step Result</h3>
			</div>
			<div class="block_list">
			<table class="list_table colwidth" id="step_list">
				<thead>
					<tr class="list_thead">
						<th width="15%">Step Name</th>
						<th width="15%">Start Time</th>
						<th width="15%">End Time</th>
						<th width="10%">Step Status</th>
						<th width="10%">Read Count</th>
						<th width="10%">Write Count</th>
						<th width="15%">Exit Code</th>
						<th width="20%">Exit Message</th>
<%-- 						<th width="12%"><spring:message code="label.stat_yn"/></th> --%>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="9">There is no data</td>
					</tr>
				</tbody>
			</table>
			</div>		
<!-- --------------------------------------------------------------------------------- -->
<!--  When click grid show this division                                            -->
<!-- -----------------------------------search----------------------------------------- -->			

<!-- --------------------------------------------------------------------------------- -->		
		</div>
	</div>
	<!-- 
	<div>
		<input type="hidden" id="showExitMessage">
		<input type="hidden" id="showJobMessage">
	</div>
	 -->
	<%@ include file="/WEB-INF/include/tailer.jspf" %>
</body>
</html>