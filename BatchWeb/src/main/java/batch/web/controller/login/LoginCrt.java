/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : LoginController.java
 * @Description : 
 *
 * @Author : LGCNS
 * @Since : 2017. 4. 20.
 *
 * @Copyright (c) 2018 EX All rights reserved.
 *-------------------------------------------------------------
 *              Modification Information
 *-------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------
 * 2018. 5. 14.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.controller.login;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.exception.BizException;
import batch.web.service.user.UserSvi;
import batch.web.util.MessageUtil;
import batch.web.util.SessionUtil;

@Controller
public class LoginCrt extends BaseController{

	@Resource(name = "userSvi")
	private UserSvi userSvi;
	
	
	@RequestMapping(value = "/login/login.do", method = RequestMethod.POST)
	public ModelAndView login(@RequestBody Map<String, Object> map) throws Exception {
		

		Map<String, Object> msgMap = new HashMap<String, Object>();
		if (map.get("userId") == null || map.get("userPassword") == null) {
			msgMap = MessageUtil.getErrorMessage("User ID or User Password is null");
			ModelAndView mav = new ModelAndView(BaseConstants.DEFAULT_VIEW_NAME);
			mav.addObject("msg", msgMap);
			return mav;
		}
		
		Map<String, Object> user = userSvi.getActiveUser(map);
		if (user == null) {
			throw new BizException("You should register user for using this system");
		}
		/** Check Password **/
		String password = map.get("userPassword") != null ? String.valueOf(map.get("userPassword")) : null;
		
		String encPassword = user.get("userPassword") != null ? String.valueOf(user.get("userPassword")) : null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("encPassword :: {}", encPassword(password)) ;
		}
		if (!passwordEncoder.matches(password, encPassword)) {
			msgMap = MessageUtil.getErrorMessage("User ID or Password is not correct");
			ModelAndView mav = new ModelAndView(BaseConstants.DEFAULT_VIEW_NAME);
			mav.addObject(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return mav;
		}
		
		/** Create Session **/
		SessionUtil.setSession(BaseConstants.DEFAULT_SESSION_NAME, user);
		
		msgMap = MessageUtil.getSuccessMessage("Successfully login the system");
		
		ModelAndView mav = new ModelAndView(BaseConstants.DEFAULT_VIEW_NAME);
		mav.addObject(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		
		return mav;
	}

	@RequestMapping(value = "/login/logout.do")
	public String logout(Map<String, Object> map) throws Exception {
//		SessionUtil.removeSession(BaseConstants.DEFAULT_VIEW_NAME);
		SessionUtil.invalidSession();
		return "main/main";
		
	}
	
	@RequestMapping(value="/cmn/localeChange.do")
	public String localChange(HttpServletRequest request, @RequestParam("locale") String locale) throws Exception {
		Locale tempLocale = RequestContextUtils.getLocale(request);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Current Locale :: {}", tempLocale);
		}
		
		/** If current page is null, return main page **/
		/** The current page is set when page is called **/
		return pageBean.getCurPage() != null ? pageBean.getCurPage() : pageBean.getMainPage();
	}
 	
}
