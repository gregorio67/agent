/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : HostPerfSvc.java
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

package cmn.deploy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cmn.deploy.base.BaseService;

@Service("hostPerfSvi")
public class HostPerfSvc extends BaseService implements HostPerfSvi{

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> savePerfData(Map<String, Object> param) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> memInfo = (Map<String, Object>) param.get("memInfo");
		Map<String, Object> cpuInfo = (Map<String, Object>) param.get("cpuInfo");
		
		LOGGER.info("CPU info :: {}", cpuInfo);
		LOGGER.info("Memory Info : {}", memInfo);
		
		int cnt = baseDao.insert("host.perf.insCpuInfo", cpuInfo);
		if (cnt <= 0) {
			resultMap.put("status", "F");
		}
		cnt = baseDao.insert("host.perf.insMemInfo", memInfo);
		if (cnt <= 0) {
			if (resultMap.get("status") != null) {
				resultMap.put("message", "CPU, and Memory information insert failed.");
			}
		}
		resultMap.put("status", "S");
		resultMap.put("message", "Successfully ended.");
		return resultMap;
	}

	@Override
	public int selectCpuListCnt(Map<String, Object> param) throws Exception {
		
		return baseDao.select("host.perf.selCpuInfoListCnt", param);
	}

	@Override
	public List<Map<String, Object>> selectCpuList(Map<String, Object> param) throws Exception {
		return baseDao.selectList("host.perf.selCpuInfoList", param);
	}

	@Override
	public int selectMemListCnt(Map<String, Object> param) throws Exception {
		return baseDao.select("host.perf.selMemInfoListCnt", param);
	}

	@Override
	public List<Map<String, Object>> selectMemList(Map<String, Object> param) throws Exception {
		return baseDao.selectList("host.perf.selMemInfoList", param);
	}

	@Override
	public Map<String, Object> selectCpuChartList(Map<String, Object> param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> hostList = baseDao.selectList("host.perf.selCpuHostList", param);
		resultMap.put("hostCnt", hostList.size());

		int idx = 0;
		for (String host : hostList) {
			param.remove("hostName");
			param.put("hostName", host);
			List<Map<String, Object>> cpuList = baseDao.selectList("host.perf.selCpuInfoChartList", param);
			String name = "cpuInfo_" + idx;
			resultMap.put(name, cpuList);
			idx++;
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> selectMemChartList(Map<String, Object> param) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> hostList = baseDao.selectList("host.perf.selMemHostList", param);
		int idx = 0;
		resultMap.put("hostCnt", hostList.size());
		for (String host : hostList) {
			param.remove("hostName");
			param.put("hostName", host);
			List<Map<String, Object>> memList = baseDao.selectList("host.perf.selMemInfoChartList", param);
			String name = "memInfo_" + idx;
			resultMap.put(name, memList);
			idx++;
		}
		
		return resultMap;
	}

}
