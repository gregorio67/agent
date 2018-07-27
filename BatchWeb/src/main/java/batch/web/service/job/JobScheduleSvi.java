package batch.web.service.job;

import java.util.List;
import java.util.Map;

import batch.web.vo.CamelMap;

/**
 * @Project : LAOS Tax Project
 * @Class : BatchResultSvc.java
 * @Description : 
 * @Author : LGCNS
 * @Since : 2017. 9. 14.
 *
 * @Copyright ⓒ LG CNS-HHI Consortium
 *-------------------------------------------------------
 * Modification Information
 *-------------------------------------------------------
 * Date            Modifier             Reason 
 *-------------------------------------------------------
 * 2017. 9. 14.        LGCNS              initial
 *-------------------------------------------------------
 */
public interface JobScheduleSvi {

	public int insertBatchJob(Map<String, Object> job) throws Exception;
	
	public int updateBatchJob(Map<String, Object> job) throws Exception;
	
	public int deleteBatchJob(Map<String, Object> job) throws Exception;

	public CamelMap selectBatchJob(Map<String, Object> job) throws Exception;

	public List<CamelMap> selectBatchJobList(Map<String, Object> job) throws Exception;

	public List<CamelMap> selectCronJobList(Map<String, Object> job) throws Exception;
	
	public int selectBatchJobListCount(Map<String, Object> job) throws Exception;

	public CamelMap selectBatchJobWithId(Map<String, Object> job) throws Exception;
	

}

