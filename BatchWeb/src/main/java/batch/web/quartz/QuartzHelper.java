/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : CronManager.java
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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import batch.web.base.BaseConstants;
import batch.web.base.BaseDao;
import batch.web.quartz.job.BatchCronJob;
import batch.web.util.NullUtil;
import batch.web.vo.CamelMap;

public class QuartzHelper implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzHelper.class);

	@Resource(name = "baseDao")
	private BaseDao baseDao;

	/** Quartz Scheduler **/
	private Scheduler scheduler = null;

	/** Job Listener **/
	private JobListener jobListener = null;
	
	@Required	
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setJobListener(JobListener jobListener) {
		this.jobListener = jobListener;
	}

	/**
	 * 
	 *<pre>
	 * 1.Description: Quartz Scheduler initiate
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @return
	 */
	public void init() throws Exception {
//		scheduler = new StdSchedulerFactory().getScheduler();
		LOGGER.info("Scheduler is initiated");

		List<CamelMap> jobMaps = baseDao.selectList("web.batchJobList.selCronJobList");
		for (CamelMap camelMap : jobMaps) {
			addCronSchedule(BaseConstants.CRON_GROUP_NAME, BaseConstants.CRON_TRIGGER_NAME, camelMap, BatchCronJob.class);
		}

		/** print out all registered job in job scheduler **/
		printCronSchedule(BaseConstants.CRON_GROUP_NAME);
	}
	
	
	/** 
	 * 
	 *<pre>
	 * 1.Description: register job schedule
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param groupName
	 * @param triggerName
	 * @param jobMap
	 * @param jobClass
	 * @throws Exception
	 */
 	public void addCronSchedule(String groupName, String triggerName, CamelMap jobMap, Class<? extends Job> jobClass) throws Exception {
		
		String jobName = jobMap.get("jobName") != null ? String.valueOf(jobMap.get("jobName")) : null;
		String cronExpression = jobMap.get("jobExeuctePeriod") != null ? String.valueOf(jobMap.get("jobExeuctePeriod")) : null;
		
		if (NullUtil.isNull(jobName) || NullUtil.isNull(cronExpression)) {
			LOGGER.error("Can't Create Cron Job because jobName and cron expression is nulll");
			return;
		}
		
		/** Create Scheduler **/
		JobBuilder jobBuilder = JobBuilder.newJob(jobClass);
		
		/** Cron Job Meta Data **/
		JobDataMap cronJobdata = new JobDataMap();
		@SuppressWarnings("unchecked")
		Iterator<String> itrJobMap = jobMap.keySet().iterator();
		while(itrJobMap.hasNext()) {
			String key = itrJobMap.next();
			cronJobdata.put(key, jobMap.get(key));
		}
		
		JobDetail jobDetail = jobBuilder.usingJobData(cronJobdata).withIdentity(jobName, groupName).build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName + "." + jobName, groupName)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();

		/** Add Job Listener **/
		if (jobListener != null) {
			JobKey jobKey = new JobKey(jobName, groupName);
			scheduler.getListenerManager().addJobListener(new SingleJobListener(), KeyMatcher.keyEquals(jobKey));			
		}
		
		/** Scheduler start**/
		scheduler.start();
		scheduler.scheduleJob(jobDetail, trigger);

	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Delete job schedule
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param groupName
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCronSchedule(String groupName, String jobName) throws Exception {
		boolean isDeleted = false;
		
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
		Iterator<JobKey> itrJobKey = jobKeys.iterator();
		
		while(itrJobKey.hasNext()) {
			JobKey jobKey = itrJobKey.next();
			if (jobKey.getName().equals(jobName)) {
				scheduler.deleteJob(jobKey);
				LOGGER.info("The {} at {} group of the scheduler is deleted", jobKey.getName(), jobKey.getGroup());
				isDeleted = true;
				break;
			}
		}
		return isDeleted;
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Change job schedule 
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param groupName
	 * @param jobName
	 * @param triggerName
	 * @param jobMap
	 * @param jobClass
	 * @throws Exception
	 */
	public void changeCronSchedule(String groupName, String jobName, String triggerName, CamelMap jobMap, Class<? extends Job> jobClass) throws Exception {
		
		String compTriggerName = triggerName + "." + jobName;
		String cronExpression = jobMap.get("jobExeuctePeriod") != null ? String.valueOf(jobMap.get("jobExeuctePeriod")) : null;
		
		
		/** Retrieve current trigger **/
		Trigger oldTrigger = scheduler.getTrigger(new TriggerKey(compTriggerName, groupName));
		
		/** Create new trigger **/
		/** When replace trigger, it need to set startNow **/
		Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(compTriggerName, groupName)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.startNow()
				.build();

		/** Rescheduling **/
		scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: print cron job schedule with job name
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param groupName
	 * @param jobName
	 * @throws Exception
	 */
	public void printCronSchedule(String groupName, String jobName) throws Exception {
						
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
		Iterator<JobKey> itrJobKey = jobKeys.iterator();
		
	
		while(itrJobKey.hasNext()) {
			JobKey jobKey = itrJobKey.next();
			if (jobName.equals(jobKey.getName())) {
				LOGGER.info("Register Cron Job :: {} : {}", jobKey.getGroup(), jobKey.getName());
				break;
			}
		}
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: print cron all job schedule
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param groupName
	 * @throws Exception
	 */
	public void printCronSchedule(String groupName) throws Exception {
		
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
		Iterator<JobKey> itrJobKey = jobKeys.iterator();
		
	
		while(itrJobKey.hasNext()) {
			JobKey jobKey = itrJobKey.next();
			LOGGER.info("Register Cron Job :: {} : {}", jobKey.getGroup(), jobKey.getName());
		}
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Schedule shutdown
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @throws Exception
	 */
	public void shutdown() throws Exception {		
		scheduler.shutdown();
		LOGGER.info("{} scheduler is shutdown", scheduler.getMetaData().getSchedulerName());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		
	}
}
