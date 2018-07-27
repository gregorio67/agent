package batch.agent.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.agent.context.BatchContext;

public class ShellUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShellUtil.class);
	/**
	 * Return shell with full path
	 * <pre>
	 *
	 * </pre>
	 * @param groupId String 
	 * @param jobName String
	 * @return String
	 * @throws Exception
	 */
	public String getBatchSheel(String groupId, String jobName) throws Exception {
				
		/** Get Bean in context-jobshell.xml **/
		@SuppressWarnings("unchecked")
		Map<String,String> interfaceMap = BatchContext.getAppContext().getBean( groupId , Map.class );
		
		String shelltDir = interfaceMap.get( "directory" );
		String shellName = interfaceMap != null ? interfaceMap.get( jobName ) : null;
		
		/** Set Shell full name **/
		String execShell = shelltDir + "/" + shellName;
		
		return execShell;
	}
	
	/**
	 * Run Shell with shell and parameter
	 * <pre>
	 *
	 * </pre>
	 * @param execShell String
	 * @param shellParams String
	 * @return int
	 * @throws Exception
	 */
	public static Map<String, Object> runBatchShell(String execShell, String[] shellParams) throws Exception {
		
		Map<String, Object> resultMap = null;
		
		/** Initiate Executor **/
		DefaultExecutor executor = new DefaultExecutor();
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		
		/** Initiate Command Line **/
		CommandLine cmdLine = CommandLine.parse(execShell);
		
		/** Set parameter to command line **/
		StringBuilder sb = new StringBuilder().append("[");
		if (!NullUtil.isNull(shellParams) ) {
			for (String param : shellParams) {
				cmdLine.addArgument(param);
				sb.append(param).append(" ");
			}
		}
		sb.append("]");
		/** Execute shell **/
		try {
			executor.execute(cmdLine, resultHandler);
			resultMap = MessageUtil.getSuccessMessage(String.format("%s is started with %s parameter", execShell, sb.toString()));
			/** No wait **/
			/** if you want to get result remove comment **/
		}
		catch(Exception ex) {
			resultMap = MessageUtil.getErrorMessage(String.format("%s failed to start shell with %s", execShell, sb.toString()));
		}
		
		return resultMap;
	}
	
	/** 
	 * 
	 *<pre>
	 * 1.Description: Execute shell with job path, job name
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param jobPath
	 * @param jobName
	 * @param execShell
	 * @param shellParams
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> runBatchShell(String jobPath, String jobName, String execShell, String[] shellParams) throws Exception {
		return runBatchShell(jobPath, jobName, null, execShell, shellParams);
	}

	public static Map<String, Object> runBatchShell(String jobPath, String jobName, String jobOption, String execShell, String[] shellParams) throws Exception {
		StringBuilder sb = new StringBuilder("\"");
		for (String s : shellParams) {
			sb.append(s).append(" ");
		}
		sb.append("\"");
		return runBatchShell(jobPath, jobName, jobOption, execShell, sb.toString());
	}
	
	public static Map<String, Object> runBatchShell(String jobPath, String jobName, String jobOption, String execShell, String shellParams) throws Exception {
		
		Map<String, Object> resultMap = null;
		
		/** Initiate Executor **/
		DefaultExecutor executor = new DefaultExecutor();
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		
		/** Initiate Command Line **/
		CommandLine cmdLine = CommandLine.parse(execShell);
		
		/** Set parameter to command line **/
		cmdLine.addArgument(jobPath);
		cmdLine.addArgument(jobName);
		if (!NullUtil.isNull(jobOption)) {
			cmdLine.addArgument(jobOption);
		}

		/** Set job parameter **/
		cmdLine.addArgument(shellParams);
		/** Execute shell **/
		try {
			executor.execute(cmdLine, resultHandler);
			resultMap = MessageUtil.getSuccessMessage(String.format("%s is started with %s parameter", execShell, shellParams));
			/** No wait **/
			/** if you want to get result remove comment **/
		}
		catch(Exception ex) {
			resultMap = MessageUtil.getErrorMessage(String.format("%s failed to start shell with %s", execShell, shellParams));
		}
		
		return resultMap;
	}
	
	public static String getCurPath() throws Exception {
		Path path = Paths.get("");
		return path.toAbsolutePath().toString();
	}
	
	
	public static Map<String, Object> execShell(String command, String shellName, List<String> arguments) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		/** Initiate Executor **/
		int watchDogTimeout = PropertiesUtil.getInt("executor.watchdog.timeout");
		
		DefaultExecutor executor = new DefaultExecutor();
		
		ExecuteWatchdog watchDog = new ExecuteWatchdog(watchDogTimeout);
		executor.setWatchdog(watchDog);
		executor.setWorkingDirectory(new File(System.getProperty("user.home")));
		
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		
		final StringBuilder sb = new StringBuilder();
		ExecuteStreamHandler stream = new PumpStreamHandler(new LogOutputStream() {
			@Override
			protected void processLine(String line, int level) {
				LOGGER.info("Result :: {}", line);
				sb.append(line);
			}
		});
		executor.setStreamHandler(stream);
		
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
		
		LOGGER.info("Execute Command :: ", cmdLine.toString());
				
		/** Execute shell **/
		try {
			executor.execute(cmdLine, resultHandler);
			resultHandler.waitFor();
			int exitCode = resultHandler.getExitValue();
			LOGGER.info("Exit Code :: {}", exitCode);
			if (resultHandler.getException() != null) {
				resultMap.put("status", "F");
				resultMap.put("message", resultHandler.getException().getMessage());				
			}
			else {
				resultMap.put("status", "S");
				resultMap.put("message", sb.toString());								
			}
		}
		catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			resultMap.put("status", "F");
			resultMap.put("message", ex.getMessage());
		}
		return resultMap;
	}
}

