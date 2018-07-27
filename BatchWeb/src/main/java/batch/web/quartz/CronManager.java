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
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.web.util.NullUtil;
import batch.web.vo.CamelMap;

public class CronManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(CronManager.class);
//	private static Map<String, Scheduler> schedulers = new HashMap<String, Scheduler>();
	private static Scheduler scheduler = null;
	private static Object syncObject = new Object();
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Quartz Scheduler initiate
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @return
	 */
	public static Scheduler getSCheduler() {
		if (scheduler != null) {
			return scheduler;
		}
		
		try {
			synchronized(syncObject) {
				scheduler = new StdSchedulerFactory().getScheduler();				
			}
		} catch (SchedulerException ex) {
			LOGGER.error("Scheduler init error :: {} ", ex.getMessage());
		}
		return scheduler;
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
 	public static void addCronSchedule(String groupName, String triggerName, CamelMap jobMap, Class<? extends Job> jobClass) throws Exception {
		
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
		JobKey jobKey = new JobKey(jobName, groupName);
		getSCheduler().getListenerManager().addJobListener(new SingleJobListener(), KeyMatcher.keyEquals(jobKey));
		
		/** Scheduler start**/
		getSCheduler().start();
		getSCheduler().scheduleJob(jobDetail, trigger);

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
	public static boolean deleteCronSchedule(String groupName, String jobName) throws Exception {
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
	public static void changeCronSchedule(String groupName, String jobName, String triggerName, CamelMap jobMap, Class<? extends Job> jobClass) throws Exception {
		
		String compTriggerName = triggerName + "." + jobName;
		String cronExpression = jobMap.get("jobExeuctePeriod") != null ? String.valueOf(jobMap.get("jobExeuctePeriod")) : null;
		
		
		/** Retrieve current trigger **/
		Trigger oldTrigger = getSCheduler().getTrigger(new TriggerKey(compTriggerName, groupName));
		
		/** Create new trigger **/
		/** When replace trigger, it need to set startNow **/
		Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(compTriggerName, groupName)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.startNow()
				.build();

		/** Rescheduling **/
		getSCheduler().rescheduleJob(oldTrigger.getKey(), newTrigger);

		
		
//		Set<TriggerKey> triggerKeys = getSCheduler().getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName));
//		Iterator<TriggerKey> itrTriggerKey = triggerKeys.iterator();
//		
//		while(itrTriggerKey.hasNext()) {
//			TriggerKey triggerKey = itrTriggerKey.next();
//			if (compTriggerName.equals(triggerKey.getName())) {
//				String cronExpression = jobMap.get("jobExeuctePeriod") != null ? String.valueOf(jobMap.get("jobExeuctePeriod")) : null;
//				Trigger oldTriger = getSCheduler().getTrigger(triggerKey);
//				Trigger newtrigger = TriggerBuilder.newTrigger().withIdentity(compTriggerName, groupName)
//																.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
//																.build();
//				
//				/** Reschedule job **/
//				getSCheduler().rescheduleJob(oldTriger.getKey(), newtrigger);
//				break;
//			}
//		}		
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
	public static void printCronSchedule(String groupName, String jobName) throws Exception {
						
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
	public static void printCronSchedule(String groupName) throws Exception {
		
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
	public static void shutdown() throws Exception {		
		getSCheduler().shutdown();
		LOGGER.info("{} scheduler is shutdown", scheduler.getMetaData().getSchedulerName());
	}
}
