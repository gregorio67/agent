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

package batch.web.controller.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.service.menu.MenuSvi;
import batch.web.util.MessageUtil;
import batch.web.util.SessionUtil;
import batch.web.vo.CamelMap;

@RestController
public class MenuCtr extends BaseController {

	@Resource(name = "menuSvi")
	private MenuSvi menuSvi;
	
	@RequestMapping(value="/menu/activeMenu", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getActiveMenuList(@RequestBody Map<String, Object> menu) throws Exception {
		Map<String, Object> menuMap = new HashMap<String, Object>();
		List<CamelMap> menuList = null;
		
		/** Set User Role **/
		Map<String, Object> session = SessionUtil.getSession(BaseConstants.DEFAULT_SESSION_NAME);
		menu.put("roleId", session.get("userRole"));

		String locale = LocaleContextHolder.getLocale().getLanguage();
		menu.put("locale", locale);
		menuList = menuSvi.getActiveMenuList(menu);
		
		Map<String, Object> msgMap = null;
		if (menuList.isEmpty()) {
			String msg = "Menu is not defined in the database";
			msgMap = MessageUtil.getErrorMessage(msg);
		}
		else {
			msgMap = MessageUtil.getSuccessMessage("Success");			
		}
		menuMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		menuMap.put("menu", menuList);
		
		return menuMap;
	}
}
