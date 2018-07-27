/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : PerfCService.java
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

package batch.agent.perf.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.agent.exception.BizException;
import batch.agent.http.HttpService;
import batch.agent.util.HostPerf;
import batch.agent.util.JsonUtil;
import batch.agent.util.PropertiesUtil;
import batch.agent.vo.CamelMap;

public class PerfCService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PerfCService.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	@Resource(name = "httpService")
	private HttpService httpService;
	
	public void sendPerfData() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		/** current date **/
		String curDate = sdf.format(new Date());
		String hostName = PropertiesUtil.getString("server.host.name");
		if (hostName == null) {
			LOGGER.error("Host name is null, check user configutation");
			throw new BizException("Host name is null, check user configutation");
		}
		
		Map<String, Object> cpuInfo = HostPerf.getCpuUsage();
		cpuInfo.put("updDate", curDate);
		cpuInfo.put("hostName", hostName);
		
		Map<String, Object> memInfo = HostPerf.getMemInfo();
		memInfo.put("updDate", curDate);
		memInfo.put("hostName", hostName);
		
		resultMap.put("cpuInfo", cpuInfo);
		resultMap.put("memInfo", memInfo);
		
		String jsonData = JsonUtil.map2Json(resultMap);
		String url = PropertiesUtil.getString("perf.collector.server.url") != null ? PropertiesUtil.getString("perf.collector.server.url") : null;
		LOGGER.info("collector URL :: {}", url);
		
		if (url == null) {
			LOGGER.error("Collection Server is not set, check your configuration");
			throw new BizException("Collection Server is not set, check your configuration");
		}
		
//		String[] serverNames = PropertiesUtil.getString("was.server.name").split(",");
//		for (String serverName : serverNames) {
//			String shellName = "ps -ef | grep " + serverName + " | grep -v grep";
//			Future<Map<String, Object>> future = ProcessorExcutor.runProcess(shellName, null);
//			resultMap.put(serverName, future.get());
//		}

		/** Call Collection Server **/
		Map<String, Object> returnMap = httpService.callPost(url, jsonData, "application/json", CamelMap.class);
		LOGGER.info("return :: {}", returnMap);
	}

}
