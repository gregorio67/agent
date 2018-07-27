/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : ProcessExecutor.java
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
 * 2018. 5. 29.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.executor;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.web.util.NullUtil;

public class ProcessExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessExecutor.class);
	
	private static final String JOB_PARAM_SPLITER= "\\|";
	
	/**
	 * 
	 *<pre>
	 * 1.Description:
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param jobMap
	 * @return
	 * @throws IOException
	 */
	public static Future<Map<String, Object>> runProcess(Map<String, Object> jobMap) throws IOException {

		LOGGER.info("Batch Job Executed :: {}", jobMap);
		
		/** Extract Job information from input Map **/
		String jobName = jobMap.get("jobName") != null ? String.valueOf(jobMap.get("jobName")) : null;
		String jobPath = jobMap.get("jobPath") != null ? String.valueOf(jobMap.get("jobPath")) : null;
		String jobParameter = jobMap.get("jobDefaultParam") != null ? String.valueOf(jobMap.get("jobDefaultParam"))	: null;

		String shellLoc = jobMap.get("jobShellLoc") != null ? String.valueOf(jobMap.get("jobShellLoc")) : null;
		String shellName = jobMap.get("jobShellName") != null ? String.valueOf(jobMap.get("jobShellName")) : null;
		String jobOption = jobMap.get("jobOption") != null ? String.valueOf(jobMap.get("jobOption")) : null;

		/** Generate Job Parameter **/
		String[] jobParameters = null;
		StringBuilder tempJobParam = new StringBuilder("\"");
		if (!NullUtil.isNull(jobParameter)) {
			jobParameters = jobParameter.split(JOB_PARAM_SPLITER);
			for (String s : jobParameters) {
				tempJobParam.append(s).append(" ");
			}
		}
		tempJobParam.append("\"");
		
		LOGGER.info("Create Executor..{} {} {} {} {}", jobPath, jobName, jobOption, shellLoc + File.separator + shellName, tempJobParam);
		/** Generate ExecutorService **/
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		/** Asynchronous call **/
		Future<Map<String, Object>> result = executor.submit(new ProcessRunner(jobPath, jobName, jobOption, shellLoc + File.separator + shellName, tempJobParam.toString()));

		executor.shutdown();
		
		return result;
	}
}
