<%@page 
  import="java.util.Locale"
  import="java.util.Map"
  import="batch.web.util.SessionUtil"
  import="batch.web.base.BaseConstants"
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String _locale = "ko";
	Map<String, Object> user = SessionUtil.getSession(BaseConstants.DEFAULT_SESSION_NAME);
	String userId = "";
	String userName = "";
	if (user != null) {
		userId = user.get("userId") != null ? String.valueOf(user.get("userId")) : "";
		userName = user.get("userName") != null ? String.valueOf(user.get("userName")) : "";	
		
		System.out.println("userId :: " + userId);
	}
	
	String errCode = BaseConstants.ERROR_CODE;
%>

<script>userId

$(document).ready(function(event){
	
	debugger;
	var v_locale = "<%=_locale %>";
	$("#_locale").val()
	var v_userId = "<%=userId %>";
	$("#_userId").val(v_userId);

	fn_menuCreate();
	menu_effect();

	
	//layer close
	$(document).on("click",function(e){
	    var v1 = $(e.target).closest(".user_info").length;
	    var v2 = $(e.target).closest("#user_info").length;
	    var v3 = $(e.target).closest(".modal_inner").length;
	    //var v4 = $(e.target).closest("#help_info").length;
	    //if(!(v1 == 1 || v2==1 || v3==1 ||v4==1)){
	    if(!(v1 == 1 || v2==1 || v3==1)){
	        $(".user_info").fadeOut('100');
	        //$(".help_info").fadeOut('100');
	        $('#user_info').removeClass('on');	
	        //$('#help_info').removeClass('on');	
	        $('body').css({
				'overflow':'auto'
			});
	    }
	});
	
		
	//After Click menu, create new menu  
	$("#korean").click(function(e) {
		e.preventDefault();
		fn_changeLocale("ko")
	});
	
	$("#english").click(function(e) {
		e.preventDefault();
		fn_changeLocale("en")
	});

	
	$("#logout").click(function(e) {
		e.preventDefault();
		fn_logout();
	});
	$('#plusBtn').click(function() { zoom_page(5, $(this)) });
	$('#minusBtn').click(function() { zoom_page(-5, $(this)) });
	
});//document.ready end

/*----------------------------------------------------------------------------*/
/* NAME : layerPopup()														  */
/* DESC : User layer popup										 			  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function layerPopup(controll,contents,lock){
	if($(controll).hasClass('on')){
		$(controll).removeClass('on');	
		$(contents).fadeOut('100');
		$(lock).css({
			'overflow':'auto'
		});
	}else{
		$(controll).addClass('on');	
		$(contents).fadeIn('100');
		$(lock).css({
			'overflow':'hidden'
		});
	}
	
}
//zoom level
var zoom_level=100;

/*----------------------------------------------------------------------------*/
/* NAME : zoom_page()														  */
/* DESC : Zoom page										 					  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function zoom_page(step, trigger)
{

    if(zoom_level>=180 && step>0 || zoom_level<=80 && step<0) return;

    if(step==0) zoom_level=100;
    else zoom_level=zoom_level+step;

    $('body').css({
        transform: 'scale('+(zoom_level/100)+')', 
        transformOrigin: '50% 0' 
    });

    if(zoom_level>100) $('body').css({ width: (zoom_level*1)+'%' });
    else $('body').css({ width: '100%' });
}

/*----------------------------------------------------------------------------*/
/* NAME : menu_effect()														  */
/* DESC : Zoom page										 					  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function menu_effect(){

	debugger;
	$('.lnb_2lev > li > a').each(function(){
		if($(this).hasClass('menuactive')){
			$(this).parents('.lnb_nav > ul > li').addClass('active');
		}
	});
	$('.lnb_nav > ul > li >a').on("click",function(){
		//return false;
		$(this).parents('li').children('.lnb_2lev').slideDown(200);
	});
	
	$('.lnb_nav > ul > li').mouseover(function(){		
		$(this).children('.lnb_2lev').slideDown(200);
	});
	
	$('.lnb_nav > ul > li').mouseleave(function(){
		$(this).children('.lnb_2lev').slideUp(200);
		
	});
}
	
var vLocale = "";
/*----------------------------------------------------------------------------*/
/* NAME : fn_changeLocale()													  */
/* DESC : Multiple language support function								  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function fn_changeLocale(locale) {
	
	debugger;
	vLocale = locale;
	var vTitle = "Information";
	var vBtnYes = "Yes";
	var vBtnNo = "No";
	
	var msg = "Do you want to change locale ?, If you change locale, your input data is lost"

	$("#navi_dialog-message").html("");
	$("#navi_dialog-message").append('<div><p class="aleart_normal">' + msg + "</p></div>");
	
	$("#navi_dialog-message").dialog({
	    modal: true,
	    draggable: true,
	    resizable: true,
	    //position: ['center', 'center'],
	    //show: 'blind',
	    //hide: 'blind',
	    width: 500,
	    //dialogClass: 'aleart_normal',
	    closeOnEscape: true,
	    title: vTitle,
	    buttons: [{
	    	text: vBtnYes,
			click: function () {
				$(this).dialog('close');
				fn_goChangeLocale();
				return "Y";
			}},
			{text: vBtnNo,
			click: function () {
				$(this).dialog('close');
			}
		}]
	});
	
	$('.ui-dialog-titlebar').addClass('success');
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_goChangeLocale()												  */
/* DESC : Multiple language support function								  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function fn_goChangeLocale() {
	//var requestURI = location.pathname;
	debugger;
	var requestURI = "<c:url value='/cmn/localeChange.do' />";
	commonSubmit.setUrl(requestURI);
	commonSubmit.addParam("locale", vLocale);
	commonSubmit.submit();
	return false;
}


/*----------------------------------------------------------------------------*/
/* NAME : fn_logout()														  */
/* DESC : ADP portal logout													  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function fn_logout() {
	commonSubmit.setUrl("/login/logout.do");
	commonSubmit.submit();
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_menuCreate()													  */
/* DESC : ADP portal menu create											  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function fn_menuCreate() {

	var vUserId = "<%=userId %>";
	if (vUserId == '') {
		return;
	}
	sendParam = {};
	sendParam.useYn = 'Y';

	restAjax.setUrl("<c:url value='/menu/activeMenu' />");
	restAjax.setCallback("fn_menuCallback");
	restAjax.setAsync(false);
	restAjax.setParam(sendParam);
	restAjax.call();
	
}

/*----------------------------------------------------------------------------*/
/* NAME : fn_menuCallback()													  */
/* DESC : ADP portal menu create callback function							  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function fn_menuCallback(result) {
	
	debugger;
	
	var vUserName = "<%=userName %>"
	var vUserId = "<%=userId %>";

	var menuhtml="";
	
	//Delete all data under the tr
	$(".lnb_nav li").remove();
	var length = result.menu.length;
		
	menuhtml = "<ul>";
	for(var idx = 0; idx < length; idx++) {
		
		var uppderId = result.menu[idx].upperMenuId;
		//alert("pid : " +pid);
		//Level 1 Menu
		if (uppderId == -1) {
			if (idx != 0) {
				menuhtml = menuhtml + '</ul>';
				menuhtml = menuhtml + '</li>';
			}
			if (result.menu[idx].menuAction != null) {
				menuhtml = menuhtml + '<li><a href="' + result.menu[idx].menuAction + '">' + result.menu[idx].menuName + '</a>';				 
//				menuhtml = menuhtml + '<li><a  href="#" onclick="fn_PageView(\'' + result.menu[idx].menuAction + '\')">' + result.menu[idx].menuName + '</a></li>';												
			}
			else {
				menuhtml = menuhtml + '<li><a href="#">' + result.menu[idx].menuName + '</a>';				
			}
			
			menuhtml = menuhtml + '<ul class="lnb_2lev">';
		}
		//Leve 2 Menu
		else {
			if (!gfn_isNull(vUserId)) { 
				menuhtml = menuhtml + '<li><a  class="menuactive" href="' + result.menu[idx].menuAction + '">' + result.menu[idx].menuName + '</a></li>';												
//				menuhtml = menuhtml + '<li><a  href="#" onclick="fn_PageView(\'' + result.menu[idx].menuAction + '\')">' + result.menu[idx].menuName + '</a></li>';												
			}
			else {
				menuhtml = menuhtml + '<li><a href="' + result.menu[idx].menuAction + '">' + result.menu[idx].menuName + '</a></li>';																		
			}
		}
		
		/** Check Last **/
		if (idx == (length - 1)) {
			menuhtml = menuhtml + "</ul>"; 
			menuhtml = menuhtml + "</li>";
		}
	}
	menuhtml = menuhtml + "</ul>"; 
	$(".lnb_nav").append(menuhtml);
	menu_effect();
	
	// Main page variable check
//	var isParentRegIp = typeof(regIpYn) == "undefined" ? false : true;
	
 	if(!gfn_isNull(vUserId)) {
 		$('#user_info').on("click",function(){
 			controll = $(this)
 			contents = $('.user_info');
 			layerPopup(controll,contents);
 			return false;
 		});
 		
 		var str = "<span style='color:#676a6c; margin-right:15px'>" + vUserName + "</span>";
		$('.login_statue').prepend(str);
		$(".login_statue").show();
		
		str = "<p>"+ vUserId +"</p><p>" + vUserName +"</p>";
		$(".user_info").prepend(str);
	}else{
		$(".login_statue").hide();
	}
	
 	return false;
}
/*----------------------------------------------------------------------------*/
/* NAME : fn_menuCallback()													  */
/* DESC : ADP portal menu create callback function							  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function fn_PageView(uri) {
	debugger;
	
	commonSubmit.setUrl(uri);
	commonSubmit.submit();
	menu_effect();
	$(this).parents('.lnb_nav > ul > li').addClass('active');
}
</script>
<body>

<div class="header_wrap">
		<div class="header_group">
		<h1 class="logo">
			<a href="<c:url value='/main/mainpage.do' />">
				<p><span>B</span>atch</p>
				<p><span class="logo_second_$('#_locale').val()">M</span>anagement</p>
			</a>
		</h1>
		<div class="gnb_nav">			
			<div class="login_statue" style="display:none">			
				<span><i class="fa fa-sign-out"></i></span><a href="#" id="logout">Logout</a>
			</div>
			<ul>
				<!-- Ser -->
				<li><a id="korean" href="#">Korean</a></li>
				<li><a id ="english" href="#">English</a></li>				
				<li class="gnb_ico">
					<a id="user_info" href="#">
						<i class="fa fa-user"></i>
					</a>	
						<div class="user_info">							
							<canvas id="triangle" width="26" height="26"></canvas>
							<script>
								var ctx = document.getElementById("triangle").getContext("2d");
								ctx.beginPath();
							    ctx.moveTo(13,0);
							    ctx.lineTo(26,13);  
							    ctx.lineTo(0,13);
							    ctx.closePath();
							    ctx.fillStyle = '#fff';
							    ctx.fill();
							    ctx.beginPath();
							    ctx.moveTo(13,0);
							    ctx.lineTo(26,13); 
							    ctx.lineWidth = 1;
							    ctx.strokeStyle = '#cdcdcd';
							    ctx.stroke();
							    ctx.beginPath();
							    ctx.moveTo(13,0);
							    ctx.lineTo(0,13);
							    ctx.lineWidth = 1;
							    ctx.strokeStyle = '#cdcdcd';
							    ctx.stroke();
							</script>
						</div>			
				</li>
				<li class="gnb_ico"><a id="plusBtn" ><i class="fa fa-plus"></i></a></li>
				<li class="gnb_ico"><a id="minusBtn"><i class="fa fa-minus"></i></a></li>
			</ul>
		</div>
	</div>
	<div class="lnb_nav"></div>
		
</div>
<input type="hidden" id="_locale" />
<input type="hidden" id="globalerrorMessage" value='There are some errors while a service is running' />
<input type="hidden" id="golabefilenotexist" value='message.file.notexists' />
<input type="hidden" id="_userId"/>
<div style="display:none" id="navi_dialog-message"></div>

<div style="display:none" id="logoutDialog"></div>
</body>