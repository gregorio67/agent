/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : JobControlSvc.java
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
 * 2018. 5. 17.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.job;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Service;

import batch.web.base.BaseService;
import batch.web.service.tcp.TcpClientSvi;
import batch.web.util.JobUtil;
import batch.web.util.JsonUtil;
import batch.web.util.MessageUtil;
import batch.web.util.NullUtil;
import batch.web.vo.CamelMap;

@Service("jobControlSvi")
public class JobControlSvc extends BaseService implements JobControlSvi{

	@Resource(name = "jobRegistry")
	private ListableJobLocator jobRegistry;
	
	@Resource(name = "jobLauncher")
	private JobLauncher jobLauncher;

	@Resource(name = "jobOperator")
	private JobOperator jobOperator;
	
	@Resource(name = "jobExplorer")
	private JobExplorer jobExplorer;
	
	@Resource(name = "jobRepository")
	private JobRepository jobReposotory;
	
	@Resource(name = "jobScheduleSvi")
	private JobScheduleSvi jobScheduleSvi;
	
	@Resource(name = "tcpClientSvi")
	private TcpClientSvi tcpClientSvi;
	
	
	@Override
	public Map<String, Object> startJob(Map<String, Object> job) throws Exception {

		boolean isStarted = false;

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String jobName = job.get("jobName") != null ? String.valueOf(job.get("jobName")) : null;

		CamelMap jobMap = jobScheduleSvi.selectBatchJob(job);
		if (jobMap == null) {
			resultMap = MessageUtil.getErrorMessage(String.format("The %s job is not registered, Please contact to administrator", jobName));
			LOGGER.error("This {} can't start because the job is not registered in the batch schedule table", jobName);
			return resultMap;			
		}
		
		String useYn = jobMap.get("useYn") != null ? String.valueOf(jobMap.get("useYn")) : "";
		if (!"Y".equals(useYn)) {
			resultMap = MessageUtil.getErrorMessage(String.format("This %s job can't restart, Please contact to administrator", jobName));
			LOGGER.error("This {} can't start because the job is not available, Please check the job status", jobName);
			return resultMap;
		}
		

		/** Previous Job Running Check **/
		Long prevJobId = jobMap.get("prevJobId") != null ? Long.parseLong(String.valueOf(jobMap.get("prevJobId"))) : 0L;
		if (isPrevJobStarted(prevJobId)) {
			resultMap = MessageUtil.getErrorMessage(String.format("Job start failed because %s previous job is running", String.valueOf(prevJobId)));
			return resultMap;	
		}
		
		/** Check the same job is running **/
		isStarted = isJobStarted(jobName);
		
		/** If job is running or job is not abandoned **/
		if (isStarted) {
			resultMap = MessageUtil.getErrorMessage(String.format("%s job can't restarted because the same job is already running", jobName));	
			return resultMap;
		}
		
		/** Start Job **/
		resultMap = executeJob(jobMap, "-start");
				
		return resultMap;
	}

	@Override
	public Map<String, Object> restartJob(Map<String, Object> job) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		boolean isStarted = false;
		boolean isRestartable = false;

		String jobName = job.get("jobName") != null ? String.valueOf(job.get("jobName")) : null;
		Long jobId = job.get("jobId") != null ? Long.parseLong(String.valueOf(job.get("jobId"))) : 0L;

		CamelMap jobMap = jobScheduleSvi.selectBatchJob(job);
		if (jobMap == null) {
			resultMap = MessageUtil.getErrorMessage(String.format("The %s job is not registered, Please contact to administrator", jobName));
			LOGGER.error("This {} can't restart because the job is not registered in the batch schedule table", jobName);
			return resultMap;			
		}
		
		String useYn = jobMap.get("useYn") != null ? String.valueOf(jobMap.get("useYn")) : "";
		if (!"Y".equals(useYn)) {
			resultMap = MessageUtil.getErrorMessage(String.format("This %s job can't restart, Please contact to administrator", jobName));
			LOGGER.error("This {} can't restart because the job is not available. Please check the job status", jobName);
			return resultMap;
		}
		

		/** Previous Job Running Check **/
		Long prevJobId = jobMap.get("prevJobId") != null ? Long.parseLong(String.valueOf(jobMap.get("prevJobId"))) : 0L;
		if (isPrevJobStarted(prevJobId)) {
			resultMap = MessageUtil.getErrorMessage(String.format("Job start failed because %s previous job is running", String.valueOf(prevJobId)));
			return resultMap;	
		}
		
		/** Check the same job is running **/
		isStarted = isJobStarted(jobName);
		
		/** Check job status with input job Id **/
		JobExecution execution = null;
		execution = jobExplorer.getJobExecution(jobId);
		if ((execution.getStatus() == BatchStatus.STOPPED) || (execution.getStatus() == BatchStatus.UNKNOWN) || (execution.getStatus() == BatchStatus.FAILED)) {
			isRestartable = true;
		}
				
		/** If job is running or job is not abandoned **/
		if (isStarted) {
			resultMap = MessageUtil.getErrorMessage(String.format("%s job can't restarted because the same job is already running", jobName));	
			return resultMap;
				
		}
		/** Job restart **/
		if (isRestartable) {
			resultMap = executeJob(jobMap, "-restart");
		}
		else {
			resultMap = MessageUtil.getErrorMessage(String.format("This %s job can't restart, Please check previous job satatus", jobName));
		}

		return resultMap;
	}

	@Override
	public Map<String, Object> stopJob(Map<String, Object> job) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		boolean isStop = false;
		String jobName = job.get("jobName") != null ? String.valueOf(job.get("jobName")) : null;
		Long jobId = job.get("jobId") != null ? Long.parseLong(String.valueOf(job.get("jobId"))) : 0L;
		
		/** Get all Running Job InstanceId **/

		JobExecution execution = null;
		execution = jobExplorer.getJobExecution(jobId);
		if ((execution.getStatus() == BatchStatus.STARTED) || (execution.getStatus() == BatchStatus.STARTING) || 
			 (execution.getStatus() == BatchStatus.ABANDONED) || (execution.getStatus() == BatchStatus.UNKNOWN)) {
    		execution.setStatus(BatchStatus.STOPPING);
    		execution.setExitStatus(new ExitStatus("User stopped the job"));
    		jobReposotory.update(execution);
//    		jobOperator.stop(jobId);
			isStop = true;
			
		}
		
		if (!isStop) {
			LOGGER.error("Can't find {} with {}, the job was not stop", jobName, jobId);
			resultMap = MessageUtil.getErrorMessage(String.format("%s and %s was not found", jobName, jobId));
		}
		else {
			resultMap = MessageUtil.getSuccessMessage(String.format("%s and %s was stopped", jobName, jobId));
			LOGGER.info("{} with {} was stoped by a user", jobName, jobId);
			
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> abendonJob(Map<String, Object> job) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/** Check jobId, jobName from parameter **/
		String jobName = job.get("jobName") != null ? String.valueOf(job.get("jobName")) : null;
		Long jobId = job.get("jobId") != null ? Long.parseLong(String.valueOf(job.get("jobId"))) : 0L;
		
		/** Get all Running Job InstanceId **/
//		Set<Long> jobInstanceIds = jobOperator.getRunningExecutions(jobName);
		
		
		boolean isStop = false;

		JobExecution execution = null;
		execution = jobExplorer.getJobExecution(jobId);
		if ((execution.getStatus() == BatchStatus.STARTED) || (execution.getStatus() == BatchStatus.STARTING)) {
    		execution.setStatus(BatchStatus.ABANDONED);
    		jobReposotory.update(execution);
    		jobOperator.abandon(jobId);
			isStop = true;
			
		}		

		//		Set<JobExecution> executions = jobExplorer.findRunningJobExecutions(jobName);
//		for(JobExecution execution : executions ){
//	    	if (execution.getId() == jobId) {
//		    	if (execution.getStatus() == BatchStatus.STARTED) {
////			        jobOperator.stop(execution.getId());
//		    		execution.setStatus(BatchStatus.ABANDONED);
//		    		jobReposotory.update(execution);
//					isStop = true;
//					resultMap.put("status", "S");
//					LOGGER.info("{} with {} was abandoned by a user", jobName, jobId);
//					break;
//		    	}
//		    }
//		}		
		
		if (!isStop) {
			LOGGER.error("Can't find {} with {}, the job was not stop", jobName, jobId);
			resultMap = MessageUtil.getErrorMessage(String.format("%s and %s was not found", jobName, jobId));
		}
		else {
			resultMap = MessageUtil.getSuccessMessage(String.format("%s and %s was abandoned", jobName, jobId));
			LOGGER.info("{} with {} was stoped by a user", jobName, jobId);
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> starCronJob(CamelMap jobMap) throws Exception {
		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isStarted = false;
		
		String useYn = jobMap.get("useYn") != null ? String.valueOf(jobMap.get("useYn")) : "";
		String jobName = jobMap.get("jobName") != null ? String.valueOf(jobMap.get("jobName")) : null;

		if (!"Y".equals(useYn)) {
			resultMap = MessageUtil.getErrorMessage(String.format("This %s job can't restart, Please contact to administrator", jobName));
			LOGGER.error("This {} can't restart because of job is not available", jobName);
			return resultMap;
		}
		

		/** Previous Job Running Check **/
		Long prevJobId = jobMap.get("prevJobId") != null ? Long.parseLong(String.valueOf(jobMap.get("prevJobId"))) : 0L;
		if (isPrevJobStarted(prevJobId)) {
			resultMap = MessageUtil.getErrorMessage(String.format("Job start failed because %s previous job is running", String.valueOf(prevJobId)));
			return resultMap;	
		}
		
		/** Check the same job is running **/
		isStarted = isJobStarted(jobName);
		
		/** If job is running or job is not abandoned **/
		if (isStarted) {
			resultMap = MessageUtil.getErrorMessage(String.format("%s job can't restarted because the same job is already running", jobName));	
			return resultMap;
		}
		else {
			resultMap = executeJob(jobMap, "-start");
		}
				
		return resultMap;
	}
	
	/**
	 * Check Batch Job running (If jobName is null, return false)
	 * <pre>
	 *
	 * </pre>
	 * @param jobName String
 	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public boolean isJobStarted(String jobName) throws Exception {
		/** jobName is null, return true **/
		if (NullUtil.isNull(jobName)) {
			return false;
		}
		
		boolean isStarted = false;

		Set<JobExecution> executions = jobExplorer.findRunningJobExecutions(jobName);
		for(JobExecution execution : executions ){
			if ((execution.getStatus() == BatchStatus.STARTED) || (execution.getStatus() == BatchStatus.STARTING)) {
				isStarted = true;
				break;
			}
		}
		
		return isStarted;
	}

	/**
	 * 
	 *<pre>
	 * 1.Description: Check previous job is running
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param prevJobId
	 * @return
	 * @throws Exception
	 */
	public boolean isPrevJobStarted(long prevJobId) throws Exception {
		
		boolean isStarted = false;
		if (prevJobId > 0) {
			Map<String, Object> prevMap = new HashMap<String, Object>();
			prevMap.put("jobId", prevJobId);
			
			/** Select Previous job name **/
			Map<String, Object> job = new HashMap<String, Object>();
			job.put("jobId", prevJobId);
			CamelMap prevJobMap = jobScheduleSvi.selectBatchJobWithId(job);
				
			String prevJobName = prevJobMap.get("jobId") != null ? String.valueOf(prevJobMap.get("jobId")) : null;
				
			/** Check previous job is running **/
			if (!NullUtil.isNull(prevJobName)) {
				if (isJobStarted(prevJobName)) {
					LOGGER.error("The {} job is running", prevJobName);
					isStarted = true;
				}					
			}
 		}
		
		return isStarted;
	}	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Execute Job Shell
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param jobMap
	 * @param jobOption
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> executeJob(CamelMap jobMap, String jobOption) throws Exception {
		
		Map<String, Object> resultMap = null;

		if (jobMap == null) {
			resultMap = MessageUtil.getErrorMessage("Job infomation is null");
			return resultMap;
		}
		
		/** Batch run agent **/
		if (jobMap.get("jobAgentHost") != null) {
			jobMap.put("jobOption", jobOption);
				
			@SuppressWarnings("unchecked")
			String sendData = JsonUtil.map2Json(jobMap);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Send Json Data :: {} ", sendData);
			}

			String host = jobMap.get("jobAgentHost") != null ? String.valueOf(jobMap.get("jobAgentHost")) : null;
			int port = jobMap.get("jobAgentPort") != null ? Integer.parseInt(String.valueOf(jobMap.get("jobAgentPort"))) : 0;
			if (port == 0) {
				resultMap = MessageUtil.getErrorMessage("Batch Agent Port is null");
			}
			
			/** No wait for response **/
			/** Batch agent call with parameter(Netty Client) **/
			byte[] ret = tcpClientSvi.sendMessage(host, port, sendData.getBytes(), false);
				
			String recvData = new String(ret);
			resultMap = JsonUtil.json2Map(recvData);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Batch Agent Received Data :: {} ", resultMap);
			}				
		}
		/** Execute shell in the same batch web application **/
		else {
			resultMap = JobUtil.executeShell(jobMap, jobOption);	
		}
		
		return resultMap;
	}
}
