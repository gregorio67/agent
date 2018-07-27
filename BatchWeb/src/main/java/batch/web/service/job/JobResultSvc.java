package batch.web.service.job;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import batch.web.base.BaseService;
import batch.web.mybatis.JobParamResultHandler;
import batch.web.vo.CamelMap;

/**
 * @Project : LAOS Tax Project
 * @Class : BatchResultSvcImpl.java
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
@Service("jobResultSvi")
public class JobResultSvc extends BaseService implements JobResultSvi {

	@Override
	public List<CamelMap> selectJobResult(Map<String, Object> job) throws Exception {
		
		return baseDao.selectList("web.jobHist.selJobHist", job);
	}

	public List<CamelMap> selectHandlerJobResult(Map<String, Object> job) throws Exception {
		
		JobParamResultHandler<CamelMap> handler = new  JobParamResultHandler<CamelMap>();
		baseDao.selectListWithHandler("web.jobHist.selJobHist", job, handler);
		
		return (List<CamelMap>)handler.getResults();
	}
	
	@Override
	public List<CamelMap> selectStepResult(Map<String, Object> job) throws Exception {
		return baseDao.selectList("web.jobHist.selStepHist", job);
	}

	@Override
	public int selectJobResultCount(Map<String, Object> job) throws Exception {
		return baseDao.select("web.jobHist.selJobHistCount", job);
	}

	@Override
	public List<CamelMap> selectRecentJobResult(Map<String, Object> job) throws Exception {
		
		return baseDao.selectList("web.jobHist.selRecentJobHist", job);
	}

	@Override
	public List<CamelMap> selectJobParameter(Map<String, Object> job) throws Exception {
		return baseDao.selectList("web.jobHist.selJobHistParams", job);
	}

}

