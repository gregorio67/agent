package batch.web.controller.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.service.job.JobScheduleSvi;
import batch.web.util.CronUtil;
import batch.web.util.MessageUtil;
import batch.web.util.NullUtil;
import batch.web.util.PropertiesUtil;
import batch.web.util.SessionUtil;
import batch.web.vo.CamelMap;

/**
 * @Project : LAOS Tax Project
 * @Class : BatchJobHistController.java
 * @Description : 
 * @Author : LGCNS
 * @Since : 2017. 9. 14.
 *
 * @Copyright ⓒ LG CNS-HHI Consortium
 *-------------------------------------------------------
 * Modification Information
 *-------------------------------------------------------
 * Date            Modifier             Reason 
 *-------------------------------------------------------
 * 2017. 9. 14.        LGCNS              initial
 *-------------------------------------------------------
 */
@RestController
public class JobScheduleCrt extends BaseController{

	@Resource(name="jobScheduleSvi")
	private JobScheduleSvi jobScheduleSvi;
	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Insert job item
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/schedule/insertJob", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> insertJob(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> msgMap = null;
		String cronExpression = paramMap.get("jobExeuctePeriod") != null ? String.valueOf(paramMap.get("jobExeuctePeriod")) : null;
		String jobName = paramMap.get("jobName") != null ? String.valueOf(paramMap.get("jobName")) : null;
		
		if (NullUtil.isNull(jobName)) {
			msgMap = MessageUtil.getErrorMessage("Job Name is null");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		
		/** Check cron expression validation **/
		if (!CronUtil.isValidate(cronExpression)) {
			msgMap = MessageUtil.getErrorMessage("The Job Execute Period is not match Quartz cron expression");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		/** Get User id from session **/
		String userId = SessionUtil.getUserId();
		paramMap.put("regUser", userId);
		
		/** Call Job schedule insert service **/
		int cnt = jobScheduleSvi.insertBatchJob(paramMap);
		if (cnt > 0) {
			msgMap = MessageUtil.getSuccessMessage("Successfully added");
		}
		else {
			msgMap = MessageUtil.getErrorMessage("Failed to add job");
		}

		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		
		return resultMap;
	}

	/**
	 * 
	 *<pre>
	 * 1.Description: Update job item
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/schedule/updateJob", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updateJob(@RequestBody Map<String, Object> paramMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		String cronExpression = paramMap.get("jobExeuctePeriod") != null ? String.valueOf(paramMap.get("jobExeuctePeriod")) : null;
		String jobName = paramMap.get("jobName") != null ? String.valueOf(paramMap.get("jobName")) : null;
		if (NullUtil.isNull(jobName)) {
			msgMap = MessageUtil.getErrorMessage("Job Name is null");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}

		if (!CronUtil.isValidate(cronExpression)) {
			msgMap = MessageUtil.getErrorMessage("The Job Execute Period is not match Quartz cron expression");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}

		/** Get User id from session **/
		String userId = SessionUtil.getUserId();
		paramMap.put("regUser", userId);
		
		int cnt = jobScheduleSvi.updateBatchJob(paramMap);
		if (cnt > 0) {
			msgMap = MessageUtil.getSuccessMessage("Successfully updated");
		}
		else {
			msgMap = MessageUtil.getErrorMessage("Failed to update job schedule");
		}
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
		
	}

	
	/**
	 * 
	 *<pre>
	 * 1.Description: Delete job item
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/schedule/deleteJob", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteJob(@RequestBody Map<String, Object> paramMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		String jobName = paramMap.get("jobName") != null ? String.valueOf(paramMap.get("jobName")) : null;
		if (NullUtil.isNull(jobName)) {
			msgMap = MessageUtil.getErrorMessage("Job name is null. Check your paramter");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
			
		}
		int cnt = jobScheduleSvi.deleteBatchJob(paramMap);
		if (cnt > 0) {
			msgMap = MessageUtil.getSuccessMessage("Successfully deleted");			
		}
		else {
			msgMap = MessageUtil.getErrorMessage("Failed to delete job schedule");
		}

		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}

	/**
	 * 
	 *<pre>
	 * 1.Description: Get job list
	 * 2.Biz Logic:
	 * 3.Author : LGCNSs
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/schedule/listJob", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> listJob(@RequestBody Map<String, Object> paramMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = null;
		Map<String, Object> msgMap = null;
		List<CamelMap> jobList = null;
		
		/** If user dose't have administrator role,  search for user registered job **/
		if (PropertiesUtil.getInt("admin.user.role.id") != SessionUtil.getUserRole()) {
			String userId = SessionUtil.getUserId();
			paramMap.put("regUser", userId);				
		}
		
		/** Get Total Row Count **/
		int totalCount = jobScheduleSvi.selectBatchJobListCount(paramMap);
		int currentPage = 0;
		if (paramMap.get("currentPage") != null) {
			currentPage = Integer.parseInt(String.valueOf(paramMap.get("currentPage")));
		}
		else {
			currentPage = 1;
		}
		
		pageMap = getPageInfo(currentPage, totalCount);
		
		/** Get Batch Job List **/
		int pageRowCount = PropertiesUtil.getInt("screen.page.rowcount");
		paramMap.put("pageRowCount", pageRowCount);
		paramMap.put("curRowCount", (currentPage  - 1) * pageRowCount);
		
		/** Call service **/
		jobList = jobScheduleSvi.selectBatchJobList(paramMap);
		
		msgMap = MessageUtil.getSuccessMessage("Successfully Ended");
		resultMap.put("page", pageMap);
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("dataList", jobList);
		
		return resultMap;
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Get job item
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/schedule/selectJob", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectJob(@RequestBody Map<String, Object> paramMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/** Call Service **/
		CamelMap dataMap = jobScheduleSvi.selectBatchJob(paramMap);
		
		Map<String, Object> msgMap = MessageUtil.getSuccessMessage("");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("data", dataMap);
		return resultMap;
	}

}
