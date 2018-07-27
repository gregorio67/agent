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
	$(document).on("click", "#host_list > tbody > tr > td", function(){
		var params = $(this).parents("tr");
		debugger;
		var hostName = $(params).find("#hostName").text();
		if (hostName != '' && hostName != 'undefined') {
			/** Clear Active **/
			$("#host_list tbody tr").removeClass("active");
			$("#host_list tbody tr input[type='checkbox']").prop("checked", false);
			
			fn_gridClickAction(params);			
		}
	});
	
	// check all
	$("#host_list thead tr th input[type='checkbox']").on("click", function(){
		debugger;
		if($(this).prop("checked")){
			$("#host_list tbody tr input[type='checkbox']").prop("checked", true);
			$("#host_list tbody tr").addClass("active");
		}else{
			$("#host_list tbody tr input[type='checkbox']").prop("checked", false);
			$("#host_list tbody tr").removeClass("active");
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
	
});


/*----------------------------------------------------------------------------*/
/* NAME : fn_Search()														  */
/* DESC : Main list search													  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_Search(pageIndex){
	debugger;
	
	$("#jvm_list").hide();
	var sendParam = {};
	sendParam.jobName = $("#searchHostName").val();

	if (typeof pageIndex == "undefined") {
		sendParam.currentPage = 1;
		curPageIndex = 1;
	}	
	else {
		sendParam.currentPage = pageIndex;
		curPageIndex = pageIndex;
	}

	restAjax.setUrl("<c:url value='/system/sysInfo' />");
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
	
	$("#host_list tbody").empty();
	var dataList = "";
	if(result.page.totalRowCount == 0){
		var size = $("#host_list thead tr th").size();
		$("#host_list tbody").append("<tr class='list_row'><td class='list_td' colspan='9'>" + "There is no data" + "</td></tr>");
		
 		var msg = "There is no data";
		gfn_messageDialog(msg);
		return;
		
	}

	$.each(result.sysList, function(i, val){
			
		dataList = '<tr class="list_row" rowId="' + i + '">';
		dataList += '<td class="list_td">' + '<input type="checkbox"/>' + '</td>';
		dataList += '<td class="list_td align-center" id="hostName">' + gfn_nvl(val.hostName) + '</a></td>';
		dataList += '<td class="list_td align-center" id="ipAddr">' + gfn_nvl(val.ipAddr) + '</td>';
		dataList += '<td class="list_td align-center" id="logFileDir">' + gfn_nvl(val.logFileDir) + '</td>';
		dataList += '<td class="list_td align-center" id="appLogFile">' + gfn_nvl(val.appLogFile) + '</td>';
		dataList += '<td class="list_td align-center" id="errLogFile">' + gfn_nvl(val.errLogFile) + '</td>';
		dataList += '<td class="list_td align-center" id="sqlLogFile">' + gfn_nvl(val.sqlLogFile) + '</td>';
		dataList += '<td class="list_td align-center" id="cpuUsage">' + gfn_nvl(val.cpuUsage) + '</td>';
		dataList += '<td class="list_td align-center" id="memUsage">' + gfn_nvl(val.memUsage) + '</td>';

		dataList += '</tr>';

		/** Add data to table **/
		$("#host_list tbody").append(dataList);
		
						
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
	$("#host_list tbody tr").removeClass('active');
	$("#host_list tbody tr input[type=checkbox]").prop("checked", false);
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
function fn_jvmSearch(hostName) {
	debugger;
	
	var sendParam = {};
	sendParam.hostName = hostName;

	restAjax.setUrl("<c:url value='/system/jvmInfo' />");
	restAjax.setCallback("fn_jvmSearchCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();		
}


function fn_jvmSearchCallBack(result) {
	
	debugger;
	if (gfn_showErrMessage(result.message)) {
		return;
	}
		
	/** Delete all previous data from table **/
	$("#jvm_list tbody").empty();

	var dataList = "";

	/** If no search data **/
	if (result.message.status == 'F') {
		var size = $("#jvm_list thead tr th").size();
		$("#jvm_list tbody").append("<tr class='list_row'><td class='list_td' colspan='8'>" + "There is no data" + "</td></tr>");
		
 		var msg = result.message.status;
		gfn_messageDialog(msg);
		return;
	}
	
	$.each(result.jvmList, function(i, val){
			
		dataList = '<tr class="list_row">';
			
		dataList += '<td class="list_td align-center" id="jvmName">' + gfn_nvl(val.jvmName) + '</td>';
		dataList += '<td class="list_td align-center" id="processId">' + gfn_nvl(val.processId) + '</td>';
		dataList += '<td class="list_td align-center" id="heapMax">' + gfn_nvl(val.heapMax) + '</td>';		
		dataList += '<td class="list_td align-center" id="heapUsage">' + gfn_nvl(val.heapUsage) + '</td>';		
		dataList += '<td class="list_td align-center" id="heapFree">' + gfn_nvl(val.heapFree) + '</td>';		
		dataList += '<td class="list_td align-center" id="totalThread">' + gfn_nvl(val.totalThread) + '</td>';		
		dataList += '<td class="list_td align-center" id="currentThread">' + gfn_nvl(val.currentThread) + '</td>';		
		dataList += '<td class="list_td align-center" id="peakThread">' + gfn_nvl(val.peakThread) + '</td>';		
		dataList += '</tr>';
			
		$("#step_list tbody").append(dataList);
			
	});
	
	$("#jvm_list").show();
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
	var hostName = $(params).find("#hostName").text();
	
	fn_jvmSearch(hostName);				
	
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
			<h3>Host Information</h3>
		</div>
		<!-- Search View -->
		<div class="search_Tblock">
			<table class="search_table">
				<colgroup>
					<col width="100px"/>
					<col width="100px"/>
					<!-- Add Below to adjust search field -->
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
				</colgroup>
				<tbody>
					<tr class="search_row">
						<th>Host Name</th>
						<td>
							<input class="search_input" type="text" id="searchHostName" maxlength="100"/>
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
			<table class="list_table colwidth" id="host_list">
				<thead>
					<tr class="list_thead">
						<th width="3%" class="select_check"><input type="checkbox"/></th>
						<th width="10%">Host Name</th>
						<th width="7%">IP</th>
						<th width="10%">LOG DIR</th>
						<th width="10%">APP LOG</th>
						<th width="10%">ERR_LOG</th>
						<th width="10%">SQL LOG</th>
						<th width="7%">CPU Usage</th>
						<th width="7%">MEM Usage</th>
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
		<div id="PAGE_NAVI" class="list_page">
		</div>		
		
<!-- --------------------------------------------------------------------------------- -->
<!--   Step Detail show  Start                                                          -->
<!-- --------------------------------------------------------------------------------- -->
		<BR>
		<div id=jvm_list style="display:none">
			<div class="sub_title">
				<h3>JVM Information</h3>
			</div>
			<div class="block_list">
			<table class="list_table colwidth" id="step_list">
				<thead>
					<tr class="list_thead">
						<th width="15%">JVM NAME</th>
						<th width="15%">PROCESS ID</th>
						<th width="10%">HEAP(MAX)</th>
						<th width="10%">HEAP(USAGE)</th>
						<th width="10%">HEAP(FREE)</th>
						<th width="10%">THREAD(TOTAL)</th>
						<th width="10%">THREAD(CUR)</th>
						<th width="10%">THREAD(PEAK)</th>
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
<!-- --------------------------------------------------------------------------------- -->
<!--  When click grid show this division                                            -->
<!-- -----------------------------------search----------------------------------------- -->			

<!-- --------------------------------------------------------------------------------- -->		
		</div>
	</div>
	<%@ include file="/WEB-INF/include/tailer.jspf" %>
</body>
</html>