<%@page 
  import="java.util.Locale"
  import="java.util.Map"
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script>

$(document).ready(function(event){
	fn_menu();		
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
/* NAME : fn_menuCallback()													  */
/* DESC : ADP portal menu create callback function							  */
/* DATE : 2016.04.22                                                          */
/* AUTH : Jeongmin Jin														  */
/*----------------------------------------------------------------------------*/
function fn_menu() {
	
	debugger;
	
	var menuhtml="";
	
	//Delete all data under the tr
	$(".lnb_nav li").remove();
		
	menuhtml = "<ul>";
	menuhtml = menuhtml + '<li><a href="/deploy/mainpage.do">Source Deploy</a></li>';
	menuhtml = menuhtml + "</ul><ul>"
	menuhtml = menuhtml + '<li><a href="/system/showPerfPage.do">Resource Monitoring</a>';				
	menuhtml = menuhtml + "</ul>"

	menuhtml = menuhtml + "</ul>"; 
	$(".lnb_nav").append(menuhtml);
	menu_effect();
		
 	return false;
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
			<a href="<c:url value='#' />">
				<p><span>D</span>eploy</p>
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