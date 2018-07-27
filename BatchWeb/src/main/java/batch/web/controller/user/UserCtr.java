/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : MenuCtr.java
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
 * 2018. 5. 20.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.controller.user;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.http.HttpService;
import batch.web.service.system.MailSvi;
import batch.web.service.user.UserSvi;
import batch.web.util.JsonUtil;
import batch.web.util.MessageUtil;
import batch.web.util.NullUtil;
import batch.web.util.PropertiesUtil;
import batch.web.util.SessionUtil;

@RestController
public class UserCtr extends BaseController {

	@Resource(name = "userSvi")
	private UserSvi userSvi;
	
	@Resource(name = "mailSvi")
	private MailSvi mailSvi;
	
	@Resource(name = "httpService")
	private HttpService httpService;
	
	
	@RequestMapping(value="/user/insertUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> insertUser(@RequestBody Map<String, Object> user) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		
		/** Check input parameter **/
		if (NullUtil.isNull(user.get("userId")) || NullUtil.isNull(user.get("userName")) || NullUtil.isNull(user.get("userPassword"))) {
			msgMap = MessageUtil.getErrorMessage("User Id, User Name or password is null");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}

		String password = user.get("userPassword") != null ? String.valueOf(user.get("userPassword")) : null;
		String userId = user.get("userId") != null ? String.valueOf(user.get("userId")) : null;
		user.put("userPassword", encPassword(password));
		/** Set User Role **/
		user.put("userRole", 2);
		
		/** Check auto approve **/
		if ("Y".equals(PropertiesUtil.getString("user.auto.approve"))) {
			user.put("useYn", "Y");
		}
		else {
			user.put("useYn", "N");			
		}
		Map<String, Object> session = SessionUtil.getSession(BaseConstants.DEFAULT_SESSION_NAME);
		if (session == null) {
			user.put("regUser", userId);			
		}
		else {
			user.put("regUser", session.get("userId") != null ? String.valueOf(session.get("userId")) : userId);
		}
		
		int cnt = userSvi.insertUser(user);
		if (cnt <= 0) {
			msgMap = MessageUtil.getErrorMessage("Fail to register user");
		}
		else {
			msgMap = MessageUtil.getSuccessMessage("Successfully register user");			
		}
		
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}

	@RequestMapping(value="/user/updateUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updateUser(@RequestBody Map<String, Object> user) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		
		int cnt = userSvi.updateUser(user);
		if (cnt <= 0) {
			msgMap = MessageUtil.getErrorMessage("Fail to update user");
		}
		else {
			msgMap = MessageUtil.getSuccessMessage("Successfully update user");			
		}
		
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}

	@RequestMapping(value="/user/deleteUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteUser(@RequestBody Map<String, Object> user) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		
		int cnt = userSvi.deleteUser(user);
		if (cnt <= 0) {
			msgMap = MessageUtil.getErrorMessage("Fail to delete user");
		}
		else {
			msgMap = MessageUtil.getSuccessMessage("Successfully delete user");			
		}
		
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}

	/**
	 * 
	 *<pre>
	 * 1.Description: Reset Password
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/resetPassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> resetPassword(@RequestBody Map<String, Object> user) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		
		String resetPassword = RandomStringUtils.randomAlphanumeric(10);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Reset Password :: {}", resetPassword);
		}
		/** Encrypt user **/
		user.put("userPassword", encPassword(resetPassword));
		
		int cnt = userSvi.updatePassword(user);
		if (cnt <= 0) {
			msgMap = MessageUtil.getErrorMessage("Fail to reset password");
		}
		else {
			msgMap = MessageUtil.getSuccessMessage("Successfully reset password");
		}
		
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("password", resetPassword);
		
		return resultMap;
	}
	
	@RequestMapping(value="/user/sendEmail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> sendEmail(@RequestBody Map<String, Object> mail) throws Exception {
	
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		
		if (mail.get("recipient") == null || mail.get("message") == null || mail.get("subject") == null) {
			msgMap = MessageUtil.getErrorMessage("recipient, subject or message is null");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		msgMap = mailSvi.sendMail(mail);
		
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		
		return resultMap;
	}
	
	@RequestMapping(value="/user/sessionId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getSession(@RequestBody Map<String, String> param) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String Url = "http://10.255.114.74/portal/ekey/session.do";
		String response = null;
		response = httpService.callPost(Url, null, String.class);
		LOGGER.info("response :: {}", response);
		Map<String, Object> result = JsonUtil.json2Map(response);
		resultMap.put("result", result );
		return resultMap;
	}
}
