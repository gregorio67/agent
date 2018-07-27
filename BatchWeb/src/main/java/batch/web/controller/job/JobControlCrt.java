package batch.web.controller.job;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.service.job.JobControlSvi;
import batch.web.util.MessageUtil;
import batch.web.util.NullUtil;

@RestController
public class JobControlCrt extends BaseController {
	
	@Resource(name = "jobControlSvi")
	private JobControlSvi jobControlSvi;
		
	
	/**
	 * 
	 * <pre>
	 * Start Batch
	 * </pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/startJob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> startJob(@RequestBody Map<String, Object> paramMap) throws Exception {

		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("jobName :: " + paramMap.get("jobName"));
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
	
		String jobName = paramMap.get("jobName") != null ? String.valueOf(paramMap.get("jobName")) : null;
		if (NullUtil.isNull(jobName)) {
			resultMap = MessageUtil.getErrorMessage("There are no job name in input parameter");
			LOGGER.error("{}", resultMap);
			return resultMap;
		}
		
		/** Call job start service **/
		Map<String, Object> msgMap = jobControlSvi.startJob(paramMap);
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}

	/**
	 * 
	 *<pre>
	 * 1.Description: Restart job when previous job is abandoned
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/restartJob", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> reStartJob(@RequestBody Map<String, Object> paramMap) throws Exception {

		
		Map<String, Object> msgMap = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("jobName :: {} :: Job Id :: {}" + paramMap.get("jobName"), paramMap.get("jobId"));
		}

	
		/** Check jobId, jobName from parameter **/
		String jobName = paramMap.get("jobName") != null ? String.valueOf(paramMap.get("jobName")) : null;
		Long jobId = paramMap.get("jobId") != null ? Long.parseLong(String.valueOf(paramMap.get("jobId"))) : 0L;


		/** Check Input Parameter **/
		if (NullUtil.isNull(jobName) || jobId <= 0) {
			msgMap = MessageUtil.getErrorMessage("There are no job Id or job Name in input parameter");
			LOGGER.error("{}", resultMap);
			return resultMap;
		}
		
		/** Call job restart service **/
		msgMap = jobControlSvi.restartJob(paramMap);
		
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		
		return resultMap;
	}
	

	/**
	 * 
	 *<pre>
	 * 1.Description: Stop job if the job is running
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/stopJob", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> stopJob(@RequestBody Map<String, Object> paramMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/** Check jobId, jobName from parameter **/
		String jobName = paramMap.get("jobName") != null ? String.valueOf(paramMap.get("jobName")) : null;
		Long jobId = paramMap.get("jobId") != null ? Long.parseLong(String.valueOf(paramMap.get("jobId"))) : 0L;

		/** Check Input Parameter **/
		if (NullUtil.isNull(jobName) || jobId <= 0) {
			resultMap = MessageUtil.getErrorMessage("There are no job Id or job Name in input parameter");
			LOGGER.error("{}", resultMap);
			return resultMap;
		}
		
		/** Call job stop service **/
		Map<String, Object> msgMap = jobControlSvi.stopJob(paramMap);
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Abandon job
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/job/abandonJob", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> abandonJob(@RequestBody Map<String, Object> paramMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		
		/** Check jobId, jobName from parameter **/
		String jobName = paramMap.get("jobName") != null ? String.valueOf(paramMap.get("jobName")) : null;
		Long jobId = paramMap.get("jobId") != null ? Long.parseLong(String.valueOf(paramMap.get("jobId"))) : 0L;

		/** Check Input Parameter **/
		if (NullUtil.isNull(jobName) || jobId <= 0) {
			msgMap = MessageUtil.getErrorMessage("There are no job Id or job Name in input parameter");
			LOGGER.error("{}", resultMap);
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		
		/** Call job abandon service **/
		msgMap = jobControlSvi.abendonJob(paramMap);
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}

}
