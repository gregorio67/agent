package batch.web.interceptor;
/**
 * @Project : 
 * @Class : SessionInterceptor.java
 *@Description : 
 *@Author : 
 *@Since : 2017. 4. 20.
 *
 * @Copyright (c) 2016 KICS All rights reserved.
 *-------------------------------------------------------------------
 * Modification Information
 *-------------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------------
 *2017. 4. 20.                                 최초작성
 *-------------------------------------------------------------------
 */


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import batch.web.base.BaseConstants;
import batch.web.exception.BizException;
import batch.web.util.NetUtil;
import batch.web.util.NullUtil;
import batch.web.util.PatternUtil;
import batch.web.util.SessionUtil;
import batch.web.vo.SessionVo;


public class SessionInterceptor extends HandlerInterceptorAdapter implements InitializingBean{
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionInterceptor.class);
	
//	private static final String sApiKey = "B69FAF368B184727E89A";
	
	/** Login Check Skip URIs **/
	private List<String> skipUris;
	
	/** LOGIN URL **/
	private String loginURI;
	
	private SessionVo sessionVo;
	
	private String blockServiceName;
	
	private String authCheckServiceName;
			
	public void setSkipUris(List<String> skipUris) {
		this.skipUris = skipUris;
	}

	
	@Required
	public void setLoginURI(String loginURI) {
		this.loginURI = loginURI;
	}
	
	@Required
	public void setSessionVo(SessionVo sessionVo) {
		this.sessionVo = sessionVo;
	}


	public void setBlockServiceName(String blockServiceName) {
		this.blockServiceName = blockServiceName;
	}


	public void setAuthCheckServiceName(String authCheckServiceName) {
		this.authCheckServiceName = authCheckServiceName;
	}


	/**
	 * Return boolean
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param Object handler
	 * @return boolean
	 * @see 
	 */

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();

		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", Long.valueOf(startTime));
		request.setAttribute("requestURI", requestURI);
		request.setAttribute("clientIP", NetUtil.getClinetIP());
		
		/** Session Vo Check if null create new session Vo **/
		boolean isSkipUri = false;
		if (!NullUtil.isNull(skipUris)) {
			isSkipUri = PatternUtil.isPatternMatch(skipUris, requestURI);			
		}
		
		/** Login Check **/
		if (!isSkipUri) {
			/** Get session value from HTTP Session **/
			Map<String, Object> session = SessionUtil.getSession(BaseConstants.DEFAULT_SESSION_NAME);
			
			/** Move to Login Page **/
			if (NullUtil.isNull(session)) {
				String contentType = request.getHeader("Content-Type") != null ? request.getHeader("Content-Type") : "";
				if (!contentType.contains("json")) {
					response.sendRedirect(loginURI);					
				}
				LOGGER.error("You shoud logon the system to use the system(session is null) :: {}", requestURI);
				throw new BizException("You shoud logon the system to use the system(session is null");
			}
			else {
				if (NullUtil.isNull(session.get("userId"))) {
					String contentType = request.getHeader("Content-Type");
					if (!contentType.contains("json")) {
						response.sendRedirect(loginURI);					
					}
					LOGGER.error("You shoud logon the system to use the system(userId is null) :: {}", requestURI);
					throw new BizException("You shoud logon the system to use the system(userId is null)");
				}				
			}
		}
						
		return super.preHandle(request, response, handler);
	}

	/**
	 * Return void
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @paeam Object handler
	 * @return void
	 * @see
	 */
	@Override
	public void postHandle(HttpServletRequest request,	HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {

	}

	/**
	 * 
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

		String requestURI = request.getAttribute("requestURI").toString();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Session Interceptor is called : {}", requestURI);
		}
	
		/** If request URI is login or logout, set host name, host address  to session information 
		 *  When logout, session information is invalidated
		 * */


		long startTime = (Long) request.getAttribute("startTime");
		long endTime = System.currentTimeMillis();
		LOGGER.info("Request URL::{} Request ClinetIP::{} Response Code::{} Start Time::{} End Time::{}  Elapse Time::{}(ms)",
				requestURI, request.getAttribute("clientIP"), response.getStatus(), startTime, endTime, (endTime - startTime));		
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

}
