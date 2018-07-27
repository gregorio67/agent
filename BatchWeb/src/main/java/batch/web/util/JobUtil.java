/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : JobUtil.java
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
 * 2018. 5. 24.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.web.vo.CamelMap;

public class JobUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobUtil.class);
	private static final String JOB_PARAM_SPLITER= "\\|";
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Job shell create for execute Shell 
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param jobMap
	 * @return
	 * @throws Exception
	 */
	public static  Map<String, Object> executeShell(CamelMap jobMap) throws Exception {
		return executeShell(jobMap, null);
	}

	public static  Map<String, Object> executeShell(CamelMap jobMap, String option) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String jobName = jobMap.get("jobName") != null ? String.valueOf(jobMap.get("jobName")) : null;
		String jobPath = jobMap.get("jobPath") != null ? String.valueOf(jobMap.get("jobPath")) : null;		
		String jobParameter = jobMap.get("jobDefaultParam") != null ? String.valueOf(jobMap.get("jobDefaultParam")) : null;
		
		String shellLoc = jobMap.get("jobShellLoc") != null ? String.valueOf(jobMap.get("jobShellLoc")) : null;
		String shellName = jobMap.get("jobShellName") != null ? String.valueOf(jobMap.get("jobShellName")) : null;
		
		String[] jobParameters = null;
		StringBuilder tempJobParam  = new StringBuilder("\"");
		if (!NullUtil.isNull(jobParameter)) {
			jobParameters = jobParameter.split(JOB_PARAM_SPLITER);
			for (String s : jobParameters) {
				tempJobParam.append(s).append(" ");
			}
		}
		tempJobParam.append("\"");

		/** Execute Job shell if the job is not running **/
		String execShell = shellLoc + File.separator + shellName;
		resultMap = ShellUtil.runBatchShell(jobPath, jobName, option, execShell, tempJobParam.toString());			
	
		LOGGER.info("{}, {}, {}, {}, {} started", jobPath, jobName, option, shellLoc + File.separator + shellName, jobParameters);
		
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public static<T>  Map<String, Object> executeShell(T paramMap, String option) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (paramMap instanceof Map) {
			Map<String, Object> jobMap = (Map<String, Object>)paramMap;

			String jobName = jobMap.get("jobName") != null ? String.valueOf(jobMap.get("jobName")) : null;
			String jobPath = jobMap.get("jobPath") != null ? String.valueOf(jobMap.get("jobPath")) : null;		
			String jobParameter = jobMap.get("jobDefaultParam") != null ? String.valueOf(jobMap.get("jobDefaultParam")) : null;
			
			String shellLoc = jobMap.get("jobShellLoc") != null ? String.valueOf(jobMap.get("jobShellLoc")) : null;
			String shellName = jobMap.get("jobShellName") != null ? String.valueOf(jobMap.get("jobShellName")) : null;
			
			String[] jobParameters = null;
			StringBuilder tempJobParam  = new StringBuilder("\"");
			if (!NullUtil.isNull(jobParameter)) {
				jobParameters = jobParameter.split(JOB_PARAM_SPLITER);
				for (String s : jobParameters) {
					tempJobParam.append(s).append(" ");
				}
			}
			tempJobParam.append("\"");

			/** Execute Job shell if the job is not running **/
			String execShell = shellLoc + File.separator + shellName;
			resultMap = ShellUtil.runBatchShell(jobPath, jobName, option, execShell, tempJobParam.toString());			
			
			LOGGER.info("{}, {}, {}, {}, {} started", jobPath, jobName, option, shellLoc + File.separator + shellName, jobParameters);
			
		}
		
		return resultMap;
	}
}
