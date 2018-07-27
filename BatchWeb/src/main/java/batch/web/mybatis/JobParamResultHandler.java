/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : JobParamResultHandler.java
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
 * 2018. 6. 1.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import batch.web.base.BaseDao;
import batch.web.exception.BizException;
import batch.web.util.BeanUtil;
import batch.web.vo.CamelMap;

public class JobParamResultHandler<T> implements ResultHandler<T>{

	private List<T> results = new ArrayList<T>();
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Return Total Result 
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @return
	 */
	public List<T> getResults() {
		return results;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void handleResult(ResultContext<? extends T> resultContext) {
		CamelMap result = (CamelMap) resultContext.getResultObject();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("jobExecutionId", result.get("jobExecutionId"));
		
		try {
			/** Get Job Parameter **/
			BaseDao baseDao = BeanUtil.getBean("baseDao");
			List<String> jobParams = baseDao.selectList("web.jobHist.selJobHistParams", param);
			
			int len = jobParams.size();
			int idx = 0;
			StringBuilder sb = new StringBuilder();
			for (String jobParam : jobParams) {
				if (idx < (len - 1)) {
					sb.append(jobParam).append("|");					
				}
				else {
					sb.append(jobParam);					
				}
				idx++;
			}
			result.put("jobParam", sb.toString());
			results.add((T)result);
			
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
	}
}
