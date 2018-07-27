/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : batchInfService.java
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
 * 2018. 6. 4.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.agent.exec.service;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import batch.agent.service.AgentAService;
import batch.agent.util.JsonUtil;
import batch.agent.util.NullUtil;
import batch.agent.util.ShellUtil;

@Service("batchExecService")
public class BatchExecService extends AgentAService<Void, byte[]>{

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchExecService.class);
	
	private static final String JOB_PARAM_SPLITER= "\\|";
	

	@Override
	public Void executeProcess(byte[] param) throws Exception {
		Map<String, Object> jobMap = JsonUtil.json2Map(new String(param));
		LOGGER.debug("received message :: {}", jobMap);
		

		String jobName = jobMap.get("jobName") != null ? String.valueOf(jobMap.get("jobName")) : null;
		String jobPath = jobMap.get("jobPath") != null ? String.valueOf(jobMap.get("jobPath")) : null;		
		String jobParameter = jobMap.get("jobDefaultParam") != null ? String.valueOf(jobMap.get("jobDefaultParam")) : null;
		
		String shellLoc = jobMap.get("jobShellLoc") != null ? String.valueOf(jobMap.get("jobShellLoc")) : null;
		String shellName = jobMap.get("jobShellName") != null ? String.valueOf(jobMap.get("jobShellName")) : null;
		String jobOption = jobMap.get("jobOption") != null ? String.valueOf(jobMap.get("jobOption")) : null;
		
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
		LOGGER.info("Batch Shell is started..");
		ShellUtil.runBatchShell(jobPath, jobName, jobOption, execShell, tempJobParam.toString());			
	
		LOGGER.info("{}, {}, {}, {}, {} started", jobPath, jobName, jobOption, shellLoc + File.separator + shellName, jobParameters);
		return null;
	}
}
