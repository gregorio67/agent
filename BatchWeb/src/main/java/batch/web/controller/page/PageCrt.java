/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : PageViewCrt.java
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
 * 2018. 5. 18.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.controller.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.util.SessionUtil;

@Controller
public class PageCrt extends BaseController {
	
	/** 
	 * 
	 *<pre>
	 * 1.Description: Show main page
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="main/mainpage.do", method = RequestMethod.GET)
	public ModelAndView viewMainPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		pageBean.setCurPage("main/main");
		return new ModelAndView("main/main");
	}	
	
	
	@RequestMapping(value = "/login/loginpage.do")
	public String loginPage() throws Exception {	
		pageBean.setCurPage("login/login");
		return "login/login";
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Show job schedule page
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="job/jobschedule.do", method = RequestMethod.GET)
	public ModelAndView viewJobSchedulePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		pageBean.setCurPage("job/jobSchedule");
		return new ModelAndView("job/jobSchedule");
	}	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Show job approve page
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="job/jobapprove.do", method = RequestMethod.GET)
	public ModelAndView viewJobApprovePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		pageBean.setCurPage("job/jobApprove");
		return new ModelAndView("job/jobApprove");
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Show job result page
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/jobresult.do", method = RequestMethod.GET)
	public ModelAndView viewJobResultPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		pageBean.setCurPage("job/jobResult");
		return new ModelAndView("job/jobResult");
	}
	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Show job result page
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/userRegister.do", method = RequestMethod.GET)
	public ModelAndView viewUserRegisterPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		pageBean.setCurPage("user/userRegister");
		return new ModelAndView("user/userRegister");
	}	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: System Monitoring View page
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/system/monitoring.do", method = RequestMethod.GET)
	public ModelAndView viewMoitoringPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		pageBean.setCurPage("system/sysMonitoring");
		return new ModelAndView("system/sysMonitoring");
	}	

	
	/**
	 * 
	 *<pre>
	 * 1.Description: Thread dump view page
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/system/threadDump.do", method = RequestMethod.GET)
	public ModelAndView viewThreadDumpPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		pageBean.setCurPage("system/threadDump");
		return new ModelAndView("system/threadDump");
	}	
	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Search source from file system
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="source/sourceSearch.do", method = RequestMethod.GET)
	public ModelAndView viewSourceSearchPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionUtil.setSession(BaseConstants.SESSION_CURRENT_PAGE, "source/sourceSearch");
		return new ModelAndView("source/sourceSearch");
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Search for applying for deploy
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="source/deploySearch.do", method = RequestMethod.GET)
	public ModelAndView viewDeploySearchPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionUtil.setSession(BaseConstants.SESSION_CURRENT_PAGE, "source/deploySourceSearch");
		return new ModelAndView("source/deploySourceSearch");
	}		
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Approve source for deploying
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="source/deployApprove.do", method = RequestMethod.GET)
	public ModelAndView viewDeployApprovePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionUtil.setSession(BaseConstants.SESSION_CURRENT_PAGE, "source/deploySourceApprove");
		return new ModelAndView("source/deploySourceApprove");
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Deploy approved source to target server
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="source/deploySource.do", method = RequestMethod.GET)
	public ModelAndView viewDeploySourcePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionUtil.setSession(BaseConstants.SESSION_CURRENT_PAGE, "source/deploySourceApprove");
		return new ModelAndView("source/deploySourceTarget");
	}	

}
