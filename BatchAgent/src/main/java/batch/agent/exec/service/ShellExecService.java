/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : ShellExecService.java
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

package batch.agent.exec.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import batch.agent.service.AgentAService;
import batch.agent.task.ProcessorExcutor;
import batch.agent.util.JsonUtil;
import batch.agent.util.ShellUtil;
import batch.agent.util.StringUtil;

@Service("shellExecService")
public class ShellExecService extends AgentAService<byte[], Map<String, Object>>{

	@Override
	public byte[] executeProcess(Map<String, Object> paramMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/** Check shell command **/
		@SuppressWarnings("unchecked")		
		Map<String, Object> commandMap = (Map<String, Object>) paramMap.get("command");
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Command Map :: {}", commandMap);
		}
		
		/** Check shell command **/
		String command = commandMap.get("shellCommand") != null ? String.valueOf(commandMap.get("shellCommand")) : null;
		if (command == null) {
			resultMap.put("status", "F");
			resultMap.put("message", "Shell command is not set");
			String jsonData = JsonUtil.map2Json(resultMap);
			return jsonData.getBytes();
		}
		/** Check shell name **/
		String shellName = commandMap.get("shellName") != null ? String.valueOf(commandMap.get("shellName")) : null;
		if (shellName == null) {
			resultMap.put("status", "F");
			resultMap.put("message", "Shell name is not set");
			String jsonData = JsonUtil.map2Json(resultMap);
			return jsonData.getBytes();
		}
		
		String shellParams = commandMap.get("shellParams") != null ? String.valueOf(commandMap.get("shellParams")) : null;
		List<String> arguments = StringUtil.str2List(shellParams);

		/** Asynchronous shell execute **/
		Future<Map<String, Object>> future = ProcessorExcutor.runProcess(command, shellName, arguments);
		resultMap = future.get();
		resultMap = ShellUtil.execShell(command, shellName, arguments);
		
		String jsonData = JsonUtil.map2Json(resultMap);
		return jsonData.getBytes();
	}
}
