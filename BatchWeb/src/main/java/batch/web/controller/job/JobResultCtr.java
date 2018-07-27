package batch.web.controller.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.service.job.JobResultSvi;
import batch.web.util.MessageUtil;
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
public class JobResultCtr extends BaseController{

	@Resource(name="jobResultSvi")
	private JobResultSvi jobResultSvi;
	

	/**
	 * 
	 *<pre>
	 * 1.Description: Get job result
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/jobResult", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
	public Map<String, Object> jobResultList(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = null;
		Map<String, Object> msgMap = null;		
		List<CamelMap> jobList = null;

		/** Check authority administrator **/
		if (PropertiesUtil.getInt("admin.user.role.id") != SessionUtil.getUserRole()) {
			String userId = SessionUtil.getUserId();
			paramMap.put("regUser", userId);				
		}
		int totalCount = jobResultSvi.selectJobResultCount(paramMap);
		
		/** Get Total Row Count **/
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
		paramMap.put("curRowCount", (currentPage - 1) * pageRowCount);
		jobList = jobResultSvi.selectHandlerJobResult(paramMap);
		
		msgMap = MessageUtil.getSuccessMessage("Successfully enede");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("page", pageMap);
		resultMap.put("jobList", jobList);
		
		return resultMap;
	}

	@RequestMapping(value="/job/allJobResult", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
	public Map<String, Object> allJobResultList(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = null;
		Map<String, Object> msgMap = null;		
		List<CamelMap> jobList = null;

		int totalCount = jobResultSvi.selectJobResultCount(paramMap);
		
		/** Get Total Row Count **/
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
		paramMap.put("curRowCount", (currentPage - 1) * pageRowCount);
		jobList = jobResultSvi.selectHandlerJobResult(paramMap);
		
		msgMap = MessageUtil.getSuccessMessage("Successfully enede");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("page", pageMap);
		resultMap.put("jobList", jobList);
		
		return resultMap;
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Get Step Result
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/stepResult", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> batchStepResult(@RequestBody Map<String, Object> paramMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		List<CamelMap> resultList = null;
		
		/** Input Parameter Check **/
		if (paramMap.get("jobExecutionId") == null) {
		
			msgMap = MessageUtil.getErrorMessage("Job Execution Id is null");
			resultMap.put("errMsge", msgMap);
			return resultMap;
		}
		
		resultList = jobResultSvi.selectStepResult(paramMap);
		if (resultList.size() <= 0) {
			msgMap = MessageUtil.getErrorMessage("There is no data");			
		}
		else {
			msgMap = MessageUtil.getSuccessMessage("Successfully ended");						
		}
		resultMap.put("stepList", resultList);
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}
	
	@RequestMapping(value="/job/resentJobResult", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> batchRecentJobResult(@RequestBody Map<String, Object> paramMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		List<CamelMap> resultList = null;
		
		resultList = jobResultSvi.selectRecentJobResult(paramMap);
		if (resultList.size() <= 0) {
			msgMap = MessageUtil.getErrorMessage("There is no data");			
		}
		else {
			msgMap = MessageUtil.getSuccessMessage("Successfully End");	
		}
		
		resultMap.put("jobList", resultList);
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}	
}

