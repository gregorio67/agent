/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : JobControlSvi.java
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

import java.util.Map;

import batch.web.vo.CamelMap;

public interface JobControlSvi {

	public Map<String, Object> startJob(Map<String, Object> job) throws Exception;
	
	public Map<String, Object> restartJob(Map<String, Object> job) throws Exception;

	public Map<String, Object> stopJob(Map<String, Object> job) throws Exception;
	
	public Map<String, Object> abendonJob(Map<String, Object> job) throws Exception;
	
	public Map<String, Object> starCronJob(CamelMap job) throws Exception;
	
	public boolean isJobStarted(String jobName) throws Exception;
	
	public boolean isPrevJobStarted(long prevJobId) throws Exception;
	
	public Map<String, Object> executeJob(CamelMap jobMap, String jobOption) throws Exception;
}
