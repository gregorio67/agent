<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/include/header.jspf" %>
<title>Batch Management System</title>
<!-- The include is located after body tag but changed because of Login Button Control -->
<%@ include file="/WEB-INF/include/navigation.jsp" %>
<script type="text/javascript">
$(document).ready(function(){
	$('.img_slider').bxSlider({
		  mode: 'horizontal',
		  captions: true,
		  adaptiveHeight: true
		});
	
	debugger;
	if ($("#_userId").val() == 'undefined' || $("#_userId").val() == '') {
		$(".adp_explane").show();		
	}
	else {
		$(".adp_explane").hide();
	}

	$(".adp_notice").show();

	$(".adp_explane button").on("click", function(){
		location.href = "<c:url value='/login/loginpage.do'/>";
	});
	
});//document ready end


/*----------------------------------------------------------------------------*/
/* NAME : fn_recentJobResult()												  */
/* DESC : Rescent Job Search									 			  */
/* DATE : 2018.05.25                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_recentJobResult() {

	debugger;
	sendParam = {};
	/** Row Count **/
	sendParam.rowNum = 10;
	
	restAjax.setUrl("<c:url value='/job/resentJobResult' />");
	restAjax.setCallback("fn_recentJobResultCallback");
	restAjax.setAsync(false);
	restAjax.setParam(sendParam);
	restAjax.call();
	
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_recentJobResult()												  */
/* DESC : Rescent Job Search									 			  */
/* DATE : 2018.05.25                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_recentJobResultCallback(result) {
	
	debugger;
	if (result.message.status == 'F') {
		gfn_showErrMessage(result.messagee);
		return;
	}
	
	$("#recentJob ul").empty();
	var html = '<ul>';
	$.each(result.jobList, function(i, val){
		if (val.jobStatus == 'ABANDONED' || val.jobStatus == 'UNKNOWN' || val.jobStatus == 'FAILED' ) {
			if ($("#_userId").val() == 'undefined' || $("#_userId").val() == '') {
//				html +='<li><span class="job_text">' + gfn_nvl(val.jobName) + '</span>' + '<span class="job_text">' + val.jobStartTime + '</span>' + '<span class="job_status_error">' + val.jobStatus + '</span></li>';							
				html +='<li><span class="job_text">' + gfn_nvl(val.jobName) + '</span>' + '<span class="job_status_error">' + val.jobStatus + '</span></li>';							
			}
			else{
//				html +='<li><a href="#" onclick="moveJobResultPage()" >' + gfn_nvl(val.jobName) + '</a>' + '<span class="job_text">' + val.jobStartTime + '</span>' + '<span class="job_status_error">' + val.jobStatus + '</span></li>';			
				html +='<li><a href="#" onclick="moveJobResultPage()" >' + gfn_nvl(val.jobName) + '</a>' + '</span>' + '<span class="job_status_error">' + val.jobStatus + '</span></li>';			
			}
		}
		else {
			if ($("#_userId").val() == 'undefined' || $("#_userId").val() == '') {
//				html += '<li><span class="job_text" >' + gfn_nvl(val.jobName) +  '</span>'+ '<span class="job_text">' + val.jobStartTime + '</span>' + '</span><span class="job_status_normal">' + val.jobStatus + '</span></li>';			
				html += '<li><span class="job_text" >' + gfn_nvl(val.jobName) +  '</span>'+ '</span><span class="job_status_normal">' + val.jobStatus + '</span></li>';			
			}
			else{
//				html += '<li><a href="#" onclick="moveJobResultPage()" >' + gfn_nvl(val.jobName) + '</a>' + '<span class="job_text">' + val.jobStartTime + '</span>' + '<span class="job_status_normal">' + val.jobStatus + '</span></li>';			
				html += '<li><a href="#" onclick="moveJobResultPage()" >' + gfn_nvl(val.jobName) + '</span>' + '<span class="job_status_normal">' + val.jobStatus + '</span></li>';			
			}
			
		}
	});
	
	html += '</ul>'
	$("#recentJob").append(html);
	
//	$("recentJob").appendTo(recentJob);
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_recentJobResult()												  */
/* DESC : Rescent Job Search									 			  */
/* DATE : 2018.05.25                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function moveJobResultPage() {
	commonSubmit.setUrl("/job/jobresult.do");
	commonSubmit.submit();
}

</script>       
</head>
<body class="grey_bg" onload="fn_recentJobResult();">
	
<div class="wrap">
	<div class="main_box">
		<div class="main_wrap">
			<div class="login_box">
				<h4 class="adp_title"><spring:message code="label.job.result"/></h4>
				<div class="adp_notice" id="recentJob" style="display:none;">
				</div>
				<!-- 로그인 전 -->
				<div class="adp_explane">
					<button><spring:message code="button.login" /></button>
				</div>
				<!-- 로그인 후 -->
			</div>
			<div class="ips_slider">
				<ul class="img_slider">
					<li><img src="/resources/image/spring-batch-reference-model.png"/></li>
				    <li><img src="/resources/image/springbatch1.jpg"/></li>
				</ul>
			</div>
		</div>			
	</div>	
</div>

</body>
</html>