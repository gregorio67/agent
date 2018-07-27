/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : ExecuteJobSvc.java
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
 * 2018. 5. 31.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.job;

import javax.annotation.Resource;

import batch.web.base.BaseService;
import batch.web.vo.CamelMap;

public class ExecuteJobSvc extends BaseService implements Runnable{
	
	private JobControlSvi jobControlSvi;
	
	private CamelMap jobMap;
	
	ExecuteJobSvc(JobControlSvi jobControlSvi, CamelMap jobMap) {
		this.jobControlSvi = jobControlSvi;
		this.jobMap = jobMap;
	}
	
	@Override
	public void run() {
		try {
			this.jobControlSvi.starCronJob(this.jobMap);
		} catch (Exception ex) {
			LOGGER.error("Exception :: {}", ex.getMessage());
		}
		
		LOGGER.info("Job Successfully end :: {}", this.jobMap);
	}

}
