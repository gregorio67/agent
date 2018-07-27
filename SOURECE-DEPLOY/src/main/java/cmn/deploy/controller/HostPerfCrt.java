/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : HostPerfCrt.java
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

package cmn.deploy.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cmn.deploy.base.BaseController;
import cmn.deploy.service.HostPerfSvi;
import cmn.deploy.util.JsonUtil;
import cmn.deploy.util.PropertiesUtil;

@Controller
public class HostPerfCrt extends BaseController{

	@Resource(name = "hostPerfSvi")
	private HostPerfSvi hostPerfSvi;
	
	
	@RequestMapping(value = "/system/showPerfPage.do", method = RequestMethod.GET)
	public String showPage() throws Exception {
		return "performance";
	}
	
	@RequestMapping(value = "/system/perf", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> processPerfData(HttpServletRequest request, Map<String, Object> params) throws Exception {
		
		InputStream in = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		String output = null;
		
		while((output = br.readLine()) != null) {
			sb.append(output);
		}
		
		Map<String, Object> param = JsonUtil.json2Map(sb.toString());
		
		LOGGER.info("Request :: {}", sb.toString());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap = hostPerfSvi.savePerfData(param);
		return resultMap;
	}

	@RequestMapping(value = "/system/chartPerf", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> chartPerf(@RequestBody Map<String, Object> params) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String fromDt = params.get("fromDt") != null ? String.valueOf(params.get("fromDt")) : null;
		String toDt = null;
		String fromTime = params.get("fromTime") != null ? String.valueOf(params.get("fromTime")) : null;
		String toTime = params.get("toTime") != null ? String.valueOf(params.get("toTime")) : null;
		
		if (fromDt != null) {
			fromDt = fromDt.replaceAll("-", "");
			toDt = fromDt;
			if (fromTime != null) {
				fromTime = fromTime.replaceAll(":", "");
				fromDt = fromDt + fromTime + "000";
			}
			else {
				fromDt = fromDt + "000000000";
			}
			
			if (toTime != null) {
				toTime = toTime.replaceAll(":", "");
				toDt = toDt + toTime + "999";
			}
			else {
				toDt = toDt + "235959999";				
			}
			
			params.remove("fromDt");
			params.put("toDt", toDt);			
			params.put("fromDt", fromDt);
		}
		
		params.put("pageRowCount", PropertiesUtil.getInt("chart.page.rowcount"));
		params.put("curRowCount", 1);
		
		String hostName = params.get("hostName") != null ? String.valueOf(params.get("hostName")) : "";
		Map<String, Object> cpuList = hostPerfSvi.selectCpuChartList(params);
		params.remove("hostName");
		params.put("hostName", hostName);
		Map<String, Object> memList = hostPerfSvi.selectMemChartList(params);
		
		resultMap.put("cpuInfo", cpuList);
		resultMap.put("memInfo", memList);
		
		return resultMap;
	}
	
	
	@RequestMapping(value = "/system/searchPerfCpu", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> searchPerfCpu(@RequestBody Map<String, Object> params) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> pageMap = new HashMap<String, Object>();
		
		String fromDt = params.get("fromDt") != null ? String.valueOf(params.get("fromDt")) : null;
		String toDt = params.get("toDt") != null ? String.valueOf(params.get("toDt")) : null;
		if (fromDt != null) {
			fromDt = fromDt.replace("-", "");
			fromDt = fromDt + "000000000";
			params.remove("fromDt");
			params.put("fromDt", fromDt);
		}
		if (toDt != null) {
			toDt = toDt.replace("-", "");
			toDt = toDt + "235959999";
			params.remove("toDt");
			params.put("toDt", toDt);
		}

		int totalCount = hostPerfSvi.selectCpuListCnt(params);
		
		int currentPage = 0;
		if (params.get("currentPage") != null) {
			currentPage = Integer.parseInt(String.valueOf(params.get("currentPage")));
		}
		else {
			currentPage = 1;
		}
		
		pageMap = getPageInfo(currentPage, totalCount);
		/** Get Batch Job List **/
		int pageRowCount = PropertiesUtil.getInt("screen.page.rowcount");
		params.put("pageRowCount", pageRowCount);
		params.put("curRowCount", (currentPage  - 1) * pageRowCount);
		
		List<Map<String, Object>> cpuList = hostPerfSvi.selectCpuList(params);
		
		resultMap.put("page", pageMap);
		resultMap.put("cpuInfo", cpuList);
		
		return resultMap;	
	}
	
	@RequestMapping(value = "/system/searchPerfMem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> searchPerfMem(@RequestBody Map<String, Object> params) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> pageMap = new HashMap<String, Object>();
		
		String fromDt = params.get("fromDt") != null ? String.valueOf(params.get("fromDt")) : null;
		String toDt = params.get("toDt") != null ? String.valueOf(params.get("toDt")) : null;
		if (fromDt != null) {
			fromDt = fromDt.replace("-", "");
			fromDt = fromDt + "000000000";
			params.remove("fromDt");
			params.put("fromDt", fromDt);
		}
		if (toDt != null) {
			toDt = toDt.replace("-", "");
			toDt = toDt + "235959999";
			params.remove("toDt");
			params.put("toDt", toDt);
		}
		
		int totalCount = hostPerfSvi.selectMemListCnt(params);
		
		int currentPage = 0;
		if (params.get("currentPage") != null) {
			currentPage = Integer.parseInt(String.valueOf(params.get("currentPage")));
		}
		else {
			currentPage = 1;
		}
		
		pageMap = getPageInfo(currentPage, totalCount);
		/** Get Batch Job List **/
		int pageRowCount = PropertiesUtil.getInt("screen.page.rowcount");
		params.put("pageRowCount", pageRowCount);
		params.put("curRowCount", (currentPage  - 1) * pageRowCount);
		
		List<Map<String, Object>> memList = hostPerfSvi.selectMemList(params);
		
		resultMap.put("page", pageMap);
		resultMap.put("memInfo", memList);
		
		return resultMap;	
	}	

}
