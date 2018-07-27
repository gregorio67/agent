/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : CallableProcess.java
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
 * 2018. 7. 9.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.agent.task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.agent.util.PropertiesUtil;

public class TaskProcess<T> implements Callable<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskProcess.class);
	
	public static final Long  WATCHDOG_EXIST_VALUE = -999L;
	
	private String command = null;
	
	private String shellName = null;
	
	private List<String> arguments = null;
	
	
	TaskProcess(String command, String shellName, List<String> arguments) {
		this.shellName = shellName;
		this.arguments = arguments;
		this.command = command;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public T call() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		/** Initiate Executor **/
		DefaultExecutor executor = new DefaultExecutor();
		executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
		executor.setWorkingDirectory(new File(System.getProperty("user.home")));
		
		int watchDogTimeout = PropertiesUtil.getInt("executor.watchdog.timeout");
		ExecuteWatchdog watchDog = new ExecuteWatchdog(watchDogTimeout);
		executor.setWatchdog(watchDog);
		
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		final StringBuilder sb = new StringBuilder();
		ExecuteStreamHandler streamHandler = new PumpStreamHandler( new LogOutputStream() {
			@Override
			protected void processLine(String line, int level) {
				LOGGER.info("Result :: {}", line);
				sb.append(line);
			}
		});
		executor.setStreamHandler(streamHandler);
		
		/** Initiate Command Line **/
		CommandLine cmdLine = CommandLine.parse(command);
		
		if (shellName != null) {
			cmdLine.addArgument(shellName);
		}
		/** Set parameter to command line **/
		if (arguments != null) {
			for (String argument : arguments) {
				cmdLine.addArgument(argument);				
			}
		}
		
		LOGGER.info("Execute Command :: {}", cmdLine.toString());
		long exitValue = 0;		
		/** Execute shell **/
		try {
			executor.execute(cmdLine, resultHandler);
			resultHandler.waitFor();
			exitValue = resultHandler.getExitValue();
			LOGGER.info("Shell exit value :: {}", exitValue);

			if (resultHandler.getException() != null) {
				LOGGER.error("Shell execute result", resultHandler.getException().getMessage());
				resultMap.put("status", "S");
				resultMap.put("message", resultHandler.getException().getMessage());				
			}
			else {
				resultMap.put("status", "S");
				resultMap.put("message", sb.toString());								
			}
		}
		catch(ExecuteException eex) {
			exitValue = eex.getExitValue();
			LOGGER.error(eex.getMessage());
			resultMap.put("status", "F");
			resultMap.put("message", eex.getMessage());
			
		}
		catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			resultMap.put("status", "F");
			resultMap.put("message", ex.getMessage());
		}
		finally {
			bos.close();
		}
		
	    if(watchDog.killedProcess()){
	    	exitValue = WATCHDOG_EXIST_VALUE;
	    	resultMap.put("status", "F");
	    	resultMap.put("message", "Process is killed by user");
	    }
	    
		return (T) resultMap;
	}
}
