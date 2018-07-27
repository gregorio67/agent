/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : BaseJob.java
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

package batch.web.quartz;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.web.service.job.JobControlSvi;
import batch.web.service.job.JobScheduleSvi;

public abstract class BaseJob implements Job{

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "jobControlSvi")
	protected JobControlSvi jobControlSvi;
	
	@Resource(name = "jobScheduleSvi")
	protected JobScheduleSvi jobScheduleSvi;
	
	private CountDownLatch latch = new CountDownLatch(1);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Map<String, Object> jobMap = context.getMergedJobDataMap().getWrappedMap();
		String jobName = String.valueOf(jobMap.get("jobName"));
		Date prevFireDate = context.getPreviousFireTime();
		long curDate = context.getJobRunTime();
		Date nextFireDate = context.getNextFireTime();
		
		LOGGER.info("{} Job is running at {} :: {} :: {}", jobName, prevFireDate, new Date(curDate), nextFireDate);
		
		try {
			Map<String, Object> result = executeJob(context);
			context.setResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract <T> T executeJob(JobExecutionContext context) throws Exception;
	
	
	public void countDown() {
		latch.countDown();
	}
}
