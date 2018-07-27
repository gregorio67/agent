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
			
	// Delete Source Deploy
	$("#btnDeploy").on("click", function(event){
		
		debugger;
		event.preventDefault();
		$("#board_list tbody tr").removeClass('active');
		$("#board_list").find("input[type=checkbox]").prop("checked", false);
		
		fn_deploySource();		
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
function fn_deploySource(){

	debugger;
	
	var sendParam = {};
	
	sendParam.bizName= $("#searchBizNameList").val();	
	
	restAjax.setUrl("<c:url value='/deploy/target' />");
	restAjax.setCallback("fn_deploySourceCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();

}
/*----------------------------------------------------------------------------*/
/* NAME : fn_searchCallBack()												  */
/* DESC : Main list search callback			  			 					  */
/* DATE : 2018.05.11                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_deploySourceCallBack(result){
	
	debugger;
	
	if (gfn_showErrMessage(result.message)) {
		return;
	}

	
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
		<div>
			<div class="list_btn_group">
				<button class="btn_default" id="btnDeploy">Deploy</button>
			</div>				
		</div>
	</div>
	<%@ include file="/WEB-INF/include/tailer.jspf" %>
</body>
</html>