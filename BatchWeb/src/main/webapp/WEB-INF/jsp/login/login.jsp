<%----------------------------------------------------------------------------%>
<%-- NAME : oidUnionLogin.jsp                                               --%>
<%-- DESC : The purpose of SSO Login                                        --%>
<%-- VER  : v1.0                                                            --%>
<%-- Copyright ⓒ 2016 LG CNS Uzbekistan                                    --%>
<%-- All rights reserved.                                                   --%>
<%----------------------------------------------------------------------------%>
<%--                           Change History                               --%>
<%----------------------------------------------------------------------------%>
<%--    DATE     AUTHOR        DESCRIPTION                                  --%>
<%-- ----------  -------------------------------------------------------------%>
<%-- 2018.05.10  Gregorio Kim     Initial Creation                             --%>
<%----------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="true"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/jquery/jquery-ui-1.10.0.custom.min.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/jquery/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common_style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/oid_style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/table_style.css"/>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/json2.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/keydown.js"></script>
<title>LOGIN</title>
<script type="text/javascript">
var mode = "getUsrAuthInfo";
var iMinute = null;
var iSecond = null; 
var timerchecker = null;

$(document).ready(function(){
	$("input[name='userId']").focus();
	$(".tab_header li:first").addClass("active");
	$(".login_box").hide();
	$(".login_box:first").show();
	$("input[name='otp']").numeric();
	$("input[name='secret_code']").numeric();
	
	$(".tab_header li").click(function(e) {
		var index = $(this).index();
		$(".tab_header li").removeClass("active");
		$(this).addClass("active");
		$(".login_box").hide();
		$(".login_box:eq(" + index + ")").show();
		
		return false;
	});
	
	$("#login_btn_idpw").on("click", function(e){
		fn_doLogin();
	});
	

	$("#korean").click(function(e) {
		e.preventDefault();
		fn_changeLocale("ko")
	});
	
	
	$("#english").click(function(e) {
		e.preventDefault();
		fn_changeLocale("en")
	});
});


/*----------------------------------------------------------------------------*/
/* NAME : fn_changeLocale(locale)                                             */
/* DESC : Multiple language support function                                  */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim                                                           */
/*----------------------------------------------------------------------------*/
function fn_changeLocale(locale) {

	var requestURI = "<c:url value='/cmn/localeChange.do' />";
	commonSubmit.setUrl(requestURI);
	commonSubmit.addParam("locale", locale);
	commonSubmit.submit();
	return false;
}
	

/*----------------------------------------------------------------------------*/
/* NAME : fn_doLogin(md)                                                         */
/* DESC : User Authentication                                                 */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim                                                           */
/*----------------------------------------------------------------------------*/
function fn_doLogin() {
	
	debugger;
	if($("#login_btn_idpw").prop("disabled")) {
		return false;
	}
	$("#login_btn_idpw").prop("disabled", true);
	
	
	sendParam = {};
	sendParam.userId = $("#userId").val();
	sendParam.userPassword = $("#userPw").val();
	
	
	restAjax.setUrl("<c:url value='/login/login.do' />");
	restAjax.setCallback("fn_loginCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();	
	
}


/*----------------------------------------------------------------------------*/
/* NAME : Lpad(str, len)                                                      */
/* DESC : Fill in the numbers leading zero as the input length                */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim                                                           */
/*----------------------------------------------------------------------------*/
function fn_loginCallBack(result) {
	debugger;
	$("#login_btn_idpw").prop("disabled", false);
	if (result.message.status == 'S') {
		commonSubmit.setUrl("<c:url value='/main/mainpage.do'/>");
		commonSubmit.setMethod("GET");
		commonSubmit.submit();
	}
	else  {
		gfn_messageDialog(result.message.message, 400);		
	}
}

/*----------------------------------------------------------------------------*/
/* NAME : Lpad(str, len)                                                      */
/* DESC : Fill in the numbers leading zero as the input length                */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim                                                           */
/*----------------------------------------------------------------------------*/
function Lpad(str, len) {
	str = str +"";
	while(str.length < len) {
		str = "0" + str;
	}

	return str;
}

/*----------------------------------------------------------------------------*/
/* NAME : initTimer()                                                         */
/* DESC : It resets the OTP timer.                                            */
/* DATE : 2018.05.10                                                          */
/* AUTH : Gregorio Kim                                                           */
/*----------------------------------------------------------------------------*/
function initTimer() {
	rMinute = parseInt(iSecond / 60);
	rSecond = iSecond % 60;

	if(iSecond > 0) {
		$("#login_otp_area font").html(Lpad(rMinute, 2)+"."+Lpad(rSecond, 2))
		iSecond--;
		timerchecker = setTimeout("initTimer()", 1000);
	} else {
		var msg = 'Authentication time has exceeded';
		
		gfn_messageDialog(msg, 300);
	}
}

/*-------------------------------------------------------------------------------------------*/
/* NAME : doUserIdEnter(e)                                                                   */
/* DESC : The called out function when pressing the Enter key from the ID input box.         */
/* DATE : 2018.05.10                                                                         */
/* AUTH : Gregorio Kim                                                                          */
/*-------------------------------------------------------------------------------------------*/
function doUserIdEnter(e){
	e = e || window.event;
	var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;

	if(charCode == 13){
		$("input[name='userPw']").focus();
	}
}


/*-------------------------------------------------------------------------------------------*/
/* NAME : doUserPwEnter(e)                                                                   */
/* DESC : The called out function when pressing the Enter key from the PW input box.         */
/* DATE : 2018.05.10                                                                         */
/* AUTH : Gregorio Kim                                                                          */
/*-------------------------------------------------------------------------------------------*/
function doUserPwEnter(e) {
	e = e || window.event;
	var charCode = (typeof e.which == "undefined") ? e.keyCode : e.which;

	if(charCode == 13){
		fn_doLogin();
	}
}

/*-------------------------------------------------------------------------------------------*/
/* NAME : fn_resetPassword(e)                                                                */
/* DESC : Password reset															         */
/* DATE : 2018.05.10                                                                         */
/* AUTH : Gregorio Kim                                                                          */
/*-------------------------------------------------------------------------------------------*/
function fn_resetPassword() {
	
	debugger;
	if(gfn_isNull($("#userId").val())){
		var msg = "User ID is required";
		fn_showMessage("userId", msg);
		return;
	}
	sendParam = {};
	sendParam.userId = $("#userId").val();	
	
	/** Call Service **/
	restAjax.setUrl("<c:url value='/user/resetPassword' />");
	restAjax.setCallback("fn_restPasswordCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();	
}

/*-------------------------------------------------------------------------------------------*/
/* NAME : fn_restPasswordCallBack(e)                                                         */
/* DESC : Password reset callback													         */
/* DATE : 2018.05.10                                                                         */
/* AUTH : Gregorio Kim                                                                          */
/*-------------------------------------------------------------------------------------------*/
function fn_restPasswordCallBack(result) {
	debugger;
	if (result.message.status == 'F') {
		gfn_showErrMessage(result.message);
		return;
	}
	
	var msg = "Changed Password<BR>" + result.password;
 	gfn_messageDialog(msg, 400);
}


</script>
</head>
<body class="grey_bg">
<form id="loginFrm">
</form>
	<div class="union_wrap">
		
		<div class="login_title">
			<div style="position: absolute; width: 182px; right: -15px;top: -15px; ">
				<ul>
					<li><a style="float:left; margin-right:8px;" id="korean" href="#" >Korean</a></li>
					<li><a style="float:left; margin-right:8px;" id="english" href="#" >English</a></li>
				</ul>
			</div>
			
			<img style="margin-top:10px;" src="/resources/image/ico_oidmain01.png"/>
			<h1><spring:message code="button.login"/></h1>
		</div>
			<div class="main_login" >
					<!-- login tab1 -->
					<div class="login_box">
						<div class="login_data">
							<input class="id_box" type="text" id="userId" name="userId" placeholder="ID"  onKeyDown="doUserIdEnter(event)" maxlength="20" />
							<input class="pw_box" type="password" id="userPw" name="userPw" placeholder="Password" onKeyDown="doUserPwEnter(event)" maxlength="20" />							<!-- <button class="login_btn" onclick="sign()" type="button" id="login">LOGIN</button> -->

						</div>
							<input class="login_btn" id="login_btn_idpw" type="button" value=<spring:message code="button.login"/>>
						<div class="login_set">
							<a class="register" href="/user/userRegister.do"><i class="fa fa-pencil-square-o"></i><spring:message code="label.user.register" /></a>
							<a href="#" onclick="fn_resetPassword()"><i class="fa fa-cog"></i><spring:message code="label.user.reset.password" /></a>
						</div>	
					</div>
			</div>
	</div>
	<%@ include file="/WEB-INF/include/tailer.jspf" %>
	<font class="validDesc" style="display: none;"></font>

</body>
</html>