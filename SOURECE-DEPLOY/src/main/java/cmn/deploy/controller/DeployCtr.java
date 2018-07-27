/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : DeployCtr.java
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
 * 2018. 7. 6.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package cmn.deploy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cmn.deploy.base.BaseController;
import cmn.deploy.http.HttpService;
import cmn.deploy.service.CommandIService;
import cmn.deploy.service.DeployIService;
import cmn.deploy.service.TcpClientSvi;
import cmn.deploy.util.BeanUtil;
import cmn.deploy.util.JsonUtil;
import cmn.deploy.util.PropertiesUtil;

@Controller
public class DeployCtr extends BaseController {
	
	@Resource(name = "deployIService")
	private DeployIService deployIService;
	
	@Resource(name = "commandIService")
	private CommandIService commandIService;
	
	@Resource(name = "tcpClientSvi")
	private TcpClientSvi tcpClientSvi;
	

	@RequestMapping(value = "/deploy/mainpage.do")
	public String mainPageView() throws Exception {
		return "main";
	}

	private static final String DEFAULT_DELIMETER = "\\|";
	
	@RequestMapping(value = "deploy/searchCodeList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> searchCodeList() throws Exception {
		Map<String, Object> codeList =  deployIService.selCodeList();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Search Code :: {}", codeList);
		}
		return codeList;
	}
	
	@RequestMapping(value = "/deploy/selectDeployList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> selectDeployList(@RequestBody Map<String, Object> param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = new HashMap<String, Object>();
		
		int totalCount = deployIService.selDeployTargetListCnt(param);
		
		int currentPage = 0;
		if (param.get("currentPage") != null) {
			currentPage = Integer.parseInt(String.valueOf(param.get("currentPage")));
		}
		else {
			currentPage = 1;
		}
		
		pageMap = getPageInfo(currentPage, totalCount);
		/** Get Batch Job List **/
		int pageRowCount = PropertiesUtil.getInt("screen.page.rowcount");
		param.put("pageRowCount", pageRowCount);
		param.put("curRowCount", (currentPage  - 1) * pageRowCount);
		
		List<Map<String, Object>> deployTarget = deployIService.selDeployTargetList(param);
		
		resultMap.put("page", pageMap);
		resultMap.put("deployTarget", deployTarget);
		
		return resultMap;		
	}
	
	@RequestMapping(value = "/deploy/deployTarget", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody Map<String, Object> deploySource(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String host = params.get("ipAddr") != null ? String.valueOf(params.get("ipAddr")) : null;
		int port = params.get("listenPort") != null ? Integer.parseInt(String.valueOf(params.get("listenPort"))) : 0;
		if (host == null || port <= 0) {
			resultMap.put("status", "F");
			resultMap.put("message", "Host and port is null, check your parameter");
			return resultMap;
		}
		
		/** Search shell name, shell param from command table **/
		Map<String, Object> cmdMap = commandIService.selectCommand(params);
		if (cmdMap == null) {
			resultMap.put("status", "F");
			resultMap.put("message", "Can't find command for deploying source");
			return resultMap;
		}
		
		Map<String, Object> sendMap = new HashMap<String, Object>();
		sendMap.put("service", "shellExecService");
		sendMap.put("responseYn", "Y");
		sendMap.put("command", cmdMap);
		
		byte[] requestPacket = JsonUtil.map2Json(sendMap).getBytes();
		byte[] respData = null;
		try {
			respData = tcpClientSvi.sendMessage(host, port, requestPacket, true);
			resultMap.put("message", new String(respData));
			resultMap.put("status", "S");
		}
		catch(Exception ex) {
			resultMap.put("message", ex.getMessage());
			resultMap.put("status", "F");			
		}
		
		return resultMap;
	}
	
	@RequestMapping(value = "/deploy/jobBuild", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody Map<String, Object> hudsonJobBuild(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> buildPath = BeanUtil.getBean("buildFilePath");
		String hostName = params.get("hostName") != null ? String.valueOf(params.get("hostName")) : null;
		
		String jobConfig = buildPath.get(hostName) != null ? String.valueOf(buildPath.get(hostName)) : null;
		String[] temp = jobConfig.split(DEFAULT_DELIMETER);
		int idx = temp[0].indexOf("/");
		String jobName = temp[0].substring(0, idx);
		String hudsonUrl = PropertiesUtil.getString("hudson.url");
		hudsonUrl = hudsonUrl.replace("${jobName}", jobName);
		
		LOGGER.info("Job Build :: {}", hudsonUrl);
		
		HttpService httpService = BeanUtil.getBean("httpService");
		resultMap = httpService.callPost(hudsonUrl, null, HashMap.class);
		return resultMap;
	}
	
	@RequestMapping(value = "/deploy/uploadFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody Map<String, Object> sourceBuild(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultMap;
	}
}
