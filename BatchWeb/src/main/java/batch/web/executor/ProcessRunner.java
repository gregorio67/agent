/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : ProcessCallable.java
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessRunner implements Callable<Map<String, Object>>{

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessRunner.class);

	
	private static final Long  WATCHDOG_EXIST_VALUE = -999L;
	
	/** Job Path **/
	private String jobPath = null;

	/** Job Name **/
	private String jobName = null;
	
	/** Job Option : -start, -restart **/
	private String jobOption = null;
	
	/** Execute Shell Name **/
	private String execShell = null;
	
	/** Job Parameter **/
	private String jobParams = null;
	
	/**
	 * Default Constructor
	 * @param jobPath job path
	 * @param jobName job name
	 * @param jobOption job option
	 * @param execShell execute shell name
	 * @param jobParams job parameter
	 */
	
	public ProcessRunner(String jobPath, String jobName, String jobOption, String execShell, String jobParams) {
		this.jobPath = jobPath;
		this.jobName = jobName;
		this.jobOption = jobOption;
		this.execShell = execShell;
		this.jobParams = jobParams;
	}
	
	@Override
	public Map<String, Object> call() throws Exception {
		LOGGER.info("Batch Shell is started");
		return runBatchShell(this.jobPath, this.jobName, this.jobOption, this.execShell, this.jobParams);
	}

	/** 
	 * 
	 *<pre>
	 * 1.Description:
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @throws Exception
	 */
	
	public Map<String, Object> runBatchShell(String jobPath, String jobName, String jobOption, String execShell, String jobParamter) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		LOGGER.info("shell parametes :: {}", jobParamter);
		
		/** Initiate Executor **/
		DefaultExecutor executor = new DefaultExecutor();
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60*1000);
		
		executor.setExitValue(1);
		executor.setWatchdog(watchdog);

		
		/** Initiate Command Line **/
		
		CommandLine cmdLine = CommandLine.parse(execShell);
		
		/** Set parameter to command line **/
		cmdLine.addArgument(jobPath);
		cmdLine.addArgument(jobName);
		if (jobOption != null) {
			cmdLine.addArgument(jobOption);
		}

		/** Set job parameter **/
		if (jobParamter != null) {
			cmdLine.addArgument(jobParamter);			
		}
		
		long exitValue = 0;
		/** Execute shell **/
		try {
			executor.execute(cmdLine, resultHandler);

			LOGGER.info("Command is executed and wait for result :: {]", jobName);
			/** Wait for finishing the called program **/
			resultHandler.waitFor();
			
			/** Check Result **/
			if(resultHandler.getException() != null) {
				LOGGER.error("Exceptoon :: {}", resultHandler.getException().getMessage());
				resultHandler.getException().printStackTrace();
				resultMap.put("status", "F");
				resultMap.put("message", String.format("%s is failed %s parameter", execShell, jobParamter));
				return resultMap;
			}
			else {
				exitValue = resultHandler.getExitValue();
				resultMap.put("status", "S");
				resultMap.put("message", String.format("%s successfully started with %s", execShell, jobParamter));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("{} execute failed with {} parameter :: {}", execShell, jobParamter, ex.getMessage());
			resultMap.put("status", "F");
			resultMap.put("message", String.format("%s is failed because of %s", execShell, ex.getMessage()));
		}
		
		if(watchdog.killedProcess()){
            exitValue = WATCHDOG_EXIST_VALUE;
        }
		exitValue = resultHandler.getExitValue();		
		LOGGER.info("Shell Exit Value :: {}", exitValue);
		
		return resultMap;
	}	
}
