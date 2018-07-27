/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : BatchCronJob.java
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
 * 2018. 6. 17.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.quartz.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import batch.web.quartz.BaseJob;
import batch.web.service.job.JobControlSvi;
import batch.web.service.job.JobScheduleSvi;
import batch.web.util.BeanUtil;
import batch.web.util.MessageUtil;
import batch.web.vo.CamelMap;

//@DisallowConcurrentExecution
public class BatchCronJob extends BaseJob {

	public BatchCronJob() {
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T executeJob(JobExecutionContext context) throws Exception {
		
		Map<String, Object> resultMap = null;
		JobDataMap jobData = context.getJobDetail().getJobDataMap();

		LOGGER.info("JobDataMap :: {} ", jobData);	
		
		Map<String, Object> job = new HashMap<String, Object>();
		Map<String, Object> jobParam = jobData.getWrappedMap(); 
		job.put("jobName", jobParam.get("jobName"));
		CamelMap jobMap = null;
		
		/** Get Job Schedule Service Bean **/
		/** If Job default parameter is changed, the changed job parameter is applied **/
		/** So read current job content from data base **/
		JobScheduleSvi jobScheduleSvi = BeanUtil.getBean("jobScheduleSvi");
		try {
			
			jobMap = jobScheduleSvi.selectBatchJob(job);;			
		}
		catch(Exception ex) {
			LOGGER.error("Cron job table read error :: {}", ex.getMessage());
			try {
				resultMap = MessageUtil.getErrorMessage("jobScheduleSvi.selectBatchJob execute error");
			} catch (Exception e) {
			}
			return (T)resultMap;
		}
		
		String jobName = jobMap.get("jobName") != null ? String.valueOf(jobMap.get("jobName")) : null;
		Long prevJobId = jobMap.get("prevJobId") != null ? Long.parseLong(String.valueOf(jobMap.get("prevJobId"))) : 0L;
		
		/** Get Job Control Service Bean **/
		JobControlSvi jobControlSvi = BeanUtil.getBean("jobControlSvi");
		
		try {
			
			/** Check current job is running **/
			if (jobControlSvi.isJobStarted(jobName)) {
				LOGGER.error("Job start failed because %s the job is running", jobName);
				resultMap = MessageUtil.getErrorMessage(String.format("Job start failed because %s the job is running", jobName));
				return (T)resultMap;					
			}	
			
			/** Check previous job is running **/
			if (prevJobId > 0) {
				if (jobControlSvi.isPrevJobStarted(prevJobId)) {
					LOGGER.error("Job start failed because %s previous job is running", String.valueOf(prevJobId));
					resultMap = MessageUtil.getErrorMessage(String.format("Job start failed because %s previous job is running", String.valueOf(prevJobId)));
					return (T)resultMap;	
				}				
			}
			
			resultMap = jobControlSvi.executeJob(jobMap, "-start");
			LOGGER.info("Cron Job started :: {}", resultMap);
			
		}
		catch(Exception ex) {
			LOGGER.error("{} failed at {}.", jobData, new Date());			
		}
		return null;
	}

}
