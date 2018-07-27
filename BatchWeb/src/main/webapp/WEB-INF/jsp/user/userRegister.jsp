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
<title>Batch Management System</title>
<script type="text/javascript">

$(document).ready(function() {		
	
	$("#userRegister").show();
	
	// click save button
	$("#btnSave").on("click", function(event){
		event.preventDefault();
		fn_insert();			
	});
			
});

/*----------------------------------------------------------------------------*/
/* NAME : fn_insert()														  */
/* DESC : Insert end-user data 								  				  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_insert() {

	debugger;
	//Check User Id is null
	if(gfn_isNull($("#userRegister #userId").val())){
		var msg = "User ID is required";
		fn_showMessage("userId", msg);
		return;
	}

	//Check user name is null
	if(gfn_isNull($("#userRegister #userName").val())){
		var msg = "User Name is required";
		fn_showMessage("userName", msg);
		return;
	}
	//Check password  is null
	if(gfn_isNull($("#userRegister #userPassword").val())){
		var msg = "Password is required";
		fn_showMessage("userPassword", msg);
		return;
	}

	//Check Job Name is null
//	if(gfn_isNull($("#userRegister #email").val())){
//		var msg = "E-mail is required";
//		fn_showMessage($("#userRegister #email"), msg);
//		return;
//	}
	
	var sendParam = {};
	sendParam.userId = $("#userRegister #userId").val();
	sendParam.userName = $("#userRegister #userName").val();
	sendParam.userPassword = $("#userRegister #userPassword").val();
	sendParam.email = $("#userRegister #email").val();

	/** Call Service **/
	restAjax.setUrl("<c:url value='/user/insertUser' />");
	restAjax.setCallback("fn_insertCallBack");
	restAjax.setParam(sendParam);
	restAjax.call();	
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_insertCallBack()											  */
/* DESC : Insert agency end-user data callback function		  				  */
/* DATE : 2016.04.27                                                          */
/* AUTH : Gregorio Kim														  */
/*----------------------------------------------------------------------------*/
function fn_insertCallBack(result){

	debugger;
	if (result.message.status == 'F') {
		gfn_showErrMessage(result.message);
		return;
	}

	gfn_answerDialog("Do you want to move to login page ?", "400", fn_moveLoginPage);
	
}

function fn_moveLoginPage() {
	location.href = "<c:url value='/login/loginpage.do'/>"
}

</script>
</head>
<body>
<%@ include file="/WEB-INF/include/navigation.jsp" %>

	<div class="container_wrap">
		<div class="sub_title">
			<h3>User Register</h3>
		</div>
<!-- Detail View -->
		<div class="block_list">		
<!-- --------------------------------------------------------------------------------- -->
<!--   User Register                                                          -->
<!-- --------------------------------------------------------------------------------- -->
			<div id="userRegister" style="display:none">
				<form id="mergeForm">
					<div class="block_list">
						<table class="list_table">
							<colgroup>
								<col width="25%"/>
								<col width="25%"/>
								<col width="25%"/>
								<col width="25%"/>
							</colgroup>
							<tbody>
								<tr class="detail_view">
									<th class="list_th require">User ID</th>
									<td class="list_td align-left">
										<input type="text" id="userId" class="floatL" maxlength="50">
									</td>
									<th class="list_th require">User Name</th>
									<td class="list_td align-left">
										<input type="text" id="userName" class="floatL" maxlength="100">
									</td>
								</tr>
								<tr class="detail_view">
									<th class="list_th require">Password</th>
									<td class="list_td align-left">
										<input type="text" id="userPassword" class="floatL" maxlength="50">
									</td>
									<th class="list_th">E-mail</th>
									<td class="list_td align-left">
										<input type="text" id="email" class="floatL" maxlength="50">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</form>
			</div>
<!-- --------------------------------------------------------------------------------- -->
			<div class="list_btn_group">
				<button class="btn_default blue" id="btnSave">Save</button>
			</div>			
		</div>
	</div>
	<%@ include file="/WEB-INF/include/tailer.jspf" %>
</body>
</html>