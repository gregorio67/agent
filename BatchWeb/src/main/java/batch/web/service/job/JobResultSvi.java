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
public interface JobResultSvi {

	public int selectJobResultCount(Map<String, Object> job) throws Exception;

	public List<CamelMap> selectJobResult(Map<String, Object> job) throws Exception;
	
	public List<CamelMap> selectHandlerJobResult(Map<String, Object> job) throws Exception;
	
	public List<CamelMap> selectStepResult(Map<String, Object> job) throws Exception;
	
	public List<CamelMap> selectRecentJobResult(Map<String, Object> job) throws Exception;
	
	public List<CamelMap> selectJobParameter(Map<String, Object> job) throws Exception;
	
	
}

