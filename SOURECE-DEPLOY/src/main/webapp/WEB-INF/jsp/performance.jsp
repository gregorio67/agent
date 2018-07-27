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
<title>System Performance</title>
<script type="text/javascript">

/** When the grid is choosed, send request server to fetch data **/
var lc_pageIndex = 1;
var lc_hostName = '';
var lc_ipAddr = '';
var lc_deployType = '';

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
		var params = $(this).parents("tr");
		fn_gridClickAction(params);				
	});
	
	
	// Delete Source Deploy
	$("#btnDeploy").on("click", function(event){
		
		event.preventDefault();
		$("#board_list tbody tr").removeClass('active');
		$("#board_list").find("input[type=checkbox]").prop("checked", false);
		
		fn_deploy();		
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
	    minDate: "-7d",
	    maxDate: dateToday,
	    changeMonth: true,
	    changeYear: true,
	    numberOfMonths: 1,
	    onClose: function( selectedDate ) {
	    }
	}).datepicker('setDate', "0");
		
	
	$( "#fromTime").timepicker({ 'timeFormat': 'H:i:s' }).timepicker('setTime', new Date());
	$( "#toTime").timepicker({ 'timeFormat': 'H:i:s' }).timepicker('setTime', new Date());
	
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
	restAjax.setUrl("<c:url value='/deploy/searchCodeList' />");
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
	
	$("#hostNameList").empty();	
	
	$.each(result.hostNameList, function(i, val){
		$("#hostNameList").append($('<option/>', {
			value: val.hostName,
			text: val.hostName
		}))
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
	
	sendParam.hostName= $("#hostNameList").val().substring(0,3);		
	
	sendParam.fromDt = $("#fromDt").val();
	sendParam.toDt = $("#fromDt").val();
	sendParam.fromTime = $("#fromTime").val();
	sendParam.toTime = $("#toTime").val();
	
	restAjax.setUrl("<c:url value='/system/chartPerf' />");
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
	$.unblockUI();	
	debugger;
	var cpuCnt = result.cpuInfo.hostCnt;
	for (var i = 0; i< cpuCnt; i++) {
		/** The position of drawing chart **/
		var chartId = "cpu_line_chart" + (i + 1);
		/** Convert result to object **/
		var cpuResult = result.cpuInfo["cpuInfo_" + i];
		if (cpuResult.length != 0) {
			fn_drawCpu(chartId, cpuResult);	
		}		
	}

	var memCnt = result.memInfo.hostCnt;
	for (var i = 0; i< memCnt; i++) {
		/** The position of drawing chart **/
		var chartId = "mem_line_chart" + (i + 1);
		/** Convert result to object **/
		var memResult = result.memInfo["memInfo_" + i];
		if (memResult.length != 0) {
			fn_drawMem(chartId, memResult);
		}
	}
}

function fn_drawCpu(chartId, result) {
	
	debugger;
	var cpuLabels = [];
	var cpuUsed = [];
	var cpuFree = [];
	var sysCpu = [];
	var sysUser = [];
	
	$.each(result, function(i, val){
		var dispTime = gfn_nvl(val.updDate);
		console.log(dispTime);
		dispTime = dispTime.substring(8,14);
		console.log(dispTime);
		cpuLabels.push(dispTime);
		cpuUsed.push(gfn_nvl(val.cpuUsed));
		cpuFree.push(gfn_nvl(val.cpuFree));
		sysCpu.push(gfn_nvl(val.sysCpu));
		sysUser.push(gfn_nvl(val.sysUser));
	});
	
	
	new Chart(document.getElementById(chartId), {
		  type: 'line',
		  data: {
		    labels: cpuLabels,
		    datasets: [{ 
		        data: cpuUsed,
		        label: "Used(%)",
		        borderColor: "#3e95cd",
		        fill: false
		      }, { 
		        data: cpuFree,
		        label: "Free(%)",
		        borderColor: "#8e5ea2",
		        fill: false
		      }, { 
		        data: sysCpu,
		        label: "Sys(%)",
		        borderColor: "#3cba9f",
		        fill: false
		      }, { 
		        data: sysUser,
		        label: "User(%)",
		        borderColor: "#e8c3b9",
		        fill: false
		      }
		    ]
		  },
		  options: {
		    title: {
		      display: true,
		      text: 'CPU Usage'
		    }
		  }
		});	

}

function fn_drawMem(chartId, result) {
	var memLabels = [];
	var memTotal = [];
	var memFree = [];
	var memUsed = [];
	var pageIn = [];
	var pageOut = [];
	var swapTotal = [];
	var swapUsed = [];
	
	
	$.each(result, function(i, val){
		var dispTime = gfn_nvl(val.updDate);
		console.log(dispTime);
		dispTime = dispTime.substring(8,14);
		console.log(dispTime);
		memLabels.push(dispTime);
		memTotal.push(gfn_nvl(val.memTotal));
		memFree.push(gfn_nvl(val.memFree));
		memUsed.push(gfn_nvl(val.memUsed));
		pageIn.push(gfn_nvl(val.pageIn));
		pageOut.push(gfn_nvl(val.pageOut));
		swapTotal.push(gfn_nvl(val.swapTotal));
		swapUsed.push(gfn_nvl(val.swapUsed));
	});		
	
	new Chart(document.getElementById(chartId), {
		  type: 'line',
		  data: {
		    labels: memLabels,
		    datasets: [
		      { 
		        data: memTotal,
		        label: "Mem Total(MB)",
		        borderColor: "#3e95cd",
		        fill: false
		      },
		      { 
		        data: memFree,
		        label: "Mem Free(MB)",
		        borderColor: "#8e5ea2",
		        fill: false
		      },
		      { 
		        data: memUsed,
		        label: "Mem Used(MB)",
		        borderColor: "#3cba9f",
		        fill: false
		      },
		      { 
		        data: swapTotal,
		        label: "Swap Total(MB)",
		        borderColor: "#e8c3b9",
		        fill: false
		      },
		      { 
			    data: swapUsed,
			    label: "Swap Free(MB)",
			    borderColor: "#003333",
			    fill: false
			  }
		    ]
		  },
		  options: {
		    title: {
		      display: true,
		      text: 'Memory Usage'
		    }
		  }
		});
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_memSearch()													  */
/* DESC : Main list search callback			  			 					  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_memSearch() {

	if ($("#hostNameList").val() != 1) {
		sendParam.hostName= $("#hostNameList").val();		
	}
	else {
		sendParam.hostName = "";
	}
	
	sendParam.fromDt = $("#fromDt").val();
	sendParam.toDt = $("#toDt").val();
	
	restAjax.setUrl("<c:url value='/system/searchPerfMem' />");
	restAjax.setCallback("fn_menSearchCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();

}

/*----------------------------------------------------------------------------*/
/* NAME : fn_menSearchCallBack()											  */
/* DESC : Main list search callback			  			 					  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_menSearchCallBack(result) {
	debugger;
	
	if (gfn_showErrMessage(result.message)) {
		return;
	}
	
	$("#mem_list tbody").empty();
	var dataList = "";
	if(result.page.totalRowCount == 0){
		var size = $("#mem_list thead tr th").size();
		$("#mem_list tbody").append("<tr class='list_row'><td class='list_td' colspan='9'>" + "There is no data" + "</td></tr>");
		
 		var msg = "There is no data";
		gfn_messageDialog(msg);
		return;
		
	}
	
	$.each(result.memInfo, function(i, val){
		dataList = '<tr class="list_row">';			
		dataList += '<td class="list_td">' + '<input type="checkbox"/>' + '</td>';
			
		dataList += '<td class="list_td align-center" id="updDate">' + gfn_nvl(val.updDate) + '</td>';
		dataList += '<td class="list_td align-center" id="memTotal">' + gfn_nvl(val.memTotal) + '</td>';
		dataList += '<td class="list_td align-center" id="memFree">' + gfn_nvl(val.memFree) + '</td>';
		dataList += '<td class="list_td align-center" id="memUsed">' + gfn_nvl(val.memUsed) + '</td>';
		dataList += '<td class="list_td align-center" id="memRate">' + gfn_nvl(val.memRate) + '</td>';
		dataList += '<td class="list_td align-center" id="pageIn">' + gfn_nvl(val.pageIn) + '</td>';
		dataList += '<td class="list_td align-center" id="pageOut">' + gfn_nvl(val.pageOut) + '</td>';
		dataList += '<td class="list_td align-center" id="swapRate">' + gfn_nvl(val.swapRate) + '</td>';
		dataList += '</tr>';
		
		$("#mem_list tbody").append(dataList);
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


</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navigation.jsp" %>

	<div class="container_wrap">
		<div class="sub_title">
			<h3>Deploy Target List</h3>
		</div>
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
				</colgroup>
				<tbody>
					<tr class="search_row">
						<th class="list_th">Deploy Target</th>
						<td>
							<select class="select_file" id="hostNameList">
							</select>
						</td>
						<th class="list_th">Search Date</th>
						<td class="align-left date" colspan="2">
							<input type="text" class="search_input" id="fromDt" readonly="readonly"/>
						</td>
						<th class="list_th">Search Time</th>
						<td class="align-left date" colspan="2">
							<input type="text" id="fromTime"/>
							~
							<input type="text" id="toTime"/>
						</td>
						
					</tr>
				</tbody>
			</table>
			<div class="search_submit">
				<input class="main_srch" type="button" value="Search">
			</div>
		</div>
		<!-- Chart Area -->
		<div>
			<table border="0">
				<colgroup>
					<col width="50%"/>
					<col width="50%"/>
				</colgroup>
				<tbody>
					<tr height="150px">
						<td>
							<div>
								<canvas id="cpu_line_chart1"></canvas>
							</div>
						</td>
						<td>
							<div>
								<canvas id="mem_line_chart1"></canvas>
							</div>
						</td>
					</tr>
					<tr height="150px">
						<td>
							<div>
								<canvas id="cpu_line_chart2"></canvas>
							</div>
						</td>
						<td>
							<div>
								<canvas id="mem_line_chart2"></canvas>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
<!-- --------------------------------------------------------------------------------- -->
	</div>
	<%@ include file="/WEB-INF/include/tailer.jspf" %>
</body>
</html>