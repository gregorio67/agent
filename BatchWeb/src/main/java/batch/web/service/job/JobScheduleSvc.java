/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : BatchJobSvc.java
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
 * 2018. 5. 15.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.job;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import batch.web.base.BaseConstants;
import batch.web.base.BaseService;
import batch.web.quartz.QuartzHelper;
import batch.web.quartz.job.BatchCronJob;
import batch.web.util.MapUtil;
import batch.web.util.NullUtil;
import batch.web.vo.CamelMap;

@Service("jobScheduleSvi")
public class JobScheduleSvc extends BaseService implements JobScheduleSvi{

	@Resource(name = "quartzHelper")
	private QuartzHelper quartzHelper;
	
	@Override
	public int insertBatchJob(Map<String, Object> job) throws Exception {
		return baseDao.insert("web.batchJobList.insJobItem", job);
	}

	@Override
	public int updateBatchJob(Map<String, Object> job) throws Exception {

		/** Get current job schedule **/
		CamelMap jobMap = selectBatchJob(job);

		int cnt = baseDao.update("web.batchJobList.updJobItem", job);

		String nextJobExeuctePeriod = String.valueOf(job.get("jobExeuctePeriod"));
		String prevJobExeuctePeriod = String.valueOf(jobMap.get("jobExeuctePeriod"));
				
		if (NullUtil.isNull(prevJobExeuctePeriod) || NullUtil.isNull(nextJobExeuctePeriod)) {
			return cnt;
		}

		String jobName = job.get("jobName") != null ? String.valueOf(job.get("jobName")) : null;
		String prevUseYn = job.get("useYn") != null ? String.valueOf(job.get("useYn")) : null;
		String nextUseYn = jobMap.get("useYn") != null ? String.valueOf(jobMap.get("useYn")) : null;
		
		/** If Job execution period is changed, change cron schedule **/
		if ("Y".equals(prevUseYn) && "Y".equals(nextUseYn)) {
			if (!prevJobExeuctePeriod.equals(nextJobExeuctePeriod)) {
				jobMap.remove("jobExeuctePeriod");
				jobMap.put("jobExeuctePeriod", nextJobExeuctePeriod);
				quartzHelper.changeCronSchedule(BaseConstants.CRON_GROUP_NAME, jobName, BaseConstants.CRON_TRIGGER_NAME, jobMap, BatchCronJob.class);
			}
		}
		else if ("Y".equals(prevUseYn) && "N".equals(nextUseYn)){
			quartzHelper.deleteCronSchedule(BaseConstants.CRON_GROUP_NAME, jobName);	
		}
		else if ("N".equals(prevUseYn) && "Y".equals(nextUseYn)){		
			quartzHelper.addCronSchedule(BaseConstants.CRON_GROUP_NAME, BaseConstants.CRON_TRIGGER_NAME, MapUtil.map2Camel(job), BatchCronJob.class);
		}
		
		return cnt;
	}

	@Override
	public int deleteBatchJob(Map<String, Object> job) throws Exception {

		int cnt = baseDao.delete("web.batchJobList.delJobItem", job);

		/** If Job is deleted, delete cron schedule **/
		String jobName = job.get("jobName") != null ? String.valueOf(job.get("jobName")) : null;
		quartzHelper.deleteCronSchedule(BaseConstants.CRON_GROUP_NAME, jobName);
		
		return cnt;

	}

	@Override
	public CamelMap selectBatchJob(Map<String, Object> job) throws Exception {
		return baseDao.select("web.batchJobList.selJobItem", job);

	}

	@Override
	public List<CamelMap> selectBatchJobList(Map<String, Object> job) throws Exception {
		return baseDao.selectList("web.batchJobList.selJobList", job);
	}
	
	@Override
	public List<CamelMap> selectCronJobList(Map<String, Object> job) throws Exception {
		return baseDao.selectList("web.batchJobList.selCronJobList", job);
	}
	

	@Override
	public CamelMap selectBatchJobWithId(Map<String, Object> job) throws Exception {
		return baseDao.selectList("web.batchJobList.selJobListWithId", job);
	}

	@Override
	public int selectBatchJobListCount(Map<String, Object> job) throws Exception {
		return baseDao.select("web.batchJobList.selJobListCount", job);
	}

}
