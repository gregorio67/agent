/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : CronRegisterSvc.java
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

package batch.web.service.cron;

import java.util.List;

import batch.web.base.BaseConstants;
import batch.web.base.BaseService;
import batch.web.quartz.CronManager;
import batch.web.quartz.job.BatchCronJob;
import batch.web.vo.CamelMap;

public class CronRegisterSvc extends BaseService implements CronRegisterSvi{
	

	@Override
	public void registerCronSchedule() throws Exception {
		List<CamelMap> jobMaps = baseDao.selectList("web.batchJobList.selCronJobList");
		for (CamelMap camelMap : jobMaps) {
			CronManager.addCronSchedule(BaseConstants.CRON_GROUP_NAME, BaseConstants.CRON_TRIGGER_NAME, camelMap, BatchCronJob.class);
		}

		/** print out all registered job in job scheduler **/
		CronManager.printCronSchedule(BaseConstants.CRON_GROUP_NAME);
		
	}
	
	@Override
	public void shutdownCronSchedule() throws Exception {
		CronManager.shutdown();
	}

}
