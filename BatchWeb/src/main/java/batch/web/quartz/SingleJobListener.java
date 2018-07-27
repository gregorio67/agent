/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : BaseJobListener.java
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
 * 2018. 6. 19.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.web.exception.BaseException;

public class SingleJobListener implements JobListener{

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private static String LISTENER_NAME = "BASE_LISTENER";
	
	@Override
	public String getName() {
		return LISTENER_NAME;
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		 List<JobExecutionContext> jobs = null;
		 String curJobName = context.getJobDetail().getKey().getName();
		try {
			/** Get current running jobs **/
			jobs = context.getScheduler().getCurrentlyExecutingJobs();
			
			/** Check the same job name **/
			/** The job name is unique to be registered in quartz scheduler **/
	         for (JobExecutionContext job : jobs) {
	        	 String runJobName = job.getJobDetail().getKey().getName();
	             if (curJobName.equals(runJobName)) {
	                 LOGGER.info("There's another instance running, so leaving {}", this);
	                 throw new BaseException("The {} same job is running ", curJobName);
	             }
	         }
		} catch (SchedulerException ex) {
			LOGGER.error("Job Check error :: {}", ex);
			throw new BaseException("The {} same job can't start because of {}", ex.getMessage());
		}
		
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
		String jobName = context.getJobDetail().getKey().getName();
		
		if (exception != null) {
			LOGGER.error("Exception throws by {} \nException :: {}", jobName, exception);
		}
		else {
			LOGGER.info("{} job is successfully completed", jobName);
		}
	}

}
