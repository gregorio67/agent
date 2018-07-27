/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : JobScheduleChkSvc.java
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

package batch.web.service.cron;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import batch.web.base.BaseService;
import batch.web.service.job.JobControlSvi;
import batch.web.service.job.JobScheduleSvi;
import batch.web.util.CronUtil;
import batch.web.util.PropertiesUtil;
import batch.web.vo.CamelMap;

@Service("cronScheduleSvi")
public class  CronScheduleSvc <T> extends BaseService implements CronScheduleSvi {

	@Resource(name = "jobScheduleSvi")
	private JobScheduleSvi jobScheduleSvi;
	
	@Resource(name = "jobControlSvi")
	private JobControlSvi jobControlSvi;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd:HHmmSS");
			
	@Override
	public void cronJobCheck() throws Exception {
		
		/** Don't know how to run job, so using cached thread pool **/
		ExecutorService execService = Executors.newCachedThreadPool(); 
		Date curDate = new Date();
		LOGGER.info("Scheduler is strated :: {}", sdf.format(curDate));
		
		Map<String, Object> job = new HashMap<String, Object>();
		
		/** Get All Job List **/
		List<CamelMap> jobLists = jobScheduleSvi.selectCronJobList(job);
		
		try {
			for (final CamelMap map : jobLists) {
				
				String cronExpression = map.get("jobExeuctePeriod") != null ? String.valueOf(map.get("jobExeuctePeriod")) : null;
				
				if (cronExpression != null) {
					final String jobName = String.valueOf(map.get("jobName"));
					/** Scheduler Next Date, if current date is compared with next job, it is not match**/
					Date scheduleNextDate = CronUtil.getNextDate(PropertiesUtil.getString("batch.cron.expression"), CronUtil.previousScheduleDate);
					
					/** Batch Job Next Date **/
					/** '0 0/1 * * * ?' when second is 0, working well, but '* 0/1 * * * ?' is not working **/
					Date nextJobDate = CronUtil.getNextDate(cronExpression, CronUtil.previousScheduleDate);
					long scheduleTime = scheduleNextDate.getTime();
					long nextTime = nextJobDate.getTime();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("{} scheduleTime is {} ", map.get("jobName"), nextJobDate);
					}
					
					/** Job is runnable **/
					if ((scheduleTime - nextTime) == 0) {
						/** Create Job Service **/
						/** Multiple Thread **/
						Runnable task = new Runnable() {
							@Override
							public void run() {
								try {
									jobControlSvi.starCronJob(map);
								} catch (Exception e) {
									LOGGER.error("{} jobName started witn {} parameter", map.get("jobName"), map.get("jobDefautlParam"));
									LOGGER.error("");
								}
							}
						};
						execService.execute(task);						
						LOGGER.info("{} jobName started witn {} parameter", map.get("jobName"), map.get("jobDefautlParam"));
					}
				}
			}
		}
		/** When exception is occurred, nothing happened **/
		catch(Exception ex) {
			LOGGER.error(ex.getMessage());
		}

		/** Executor Service shutdown **/
		execService.shutdown();
		/** Current Date is set previous date **/
		CronUtil.previousScheduleDate = curDate;
		LOGGER.info("Scheduler is ended :: {}", sdf.format(new Date()));
	}
}
