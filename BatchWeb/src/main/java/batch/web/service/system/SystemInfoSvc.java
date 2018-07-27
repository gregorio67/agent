/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : MBeanSvc.java
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
 * 2018. 5. 31.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.system;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import batch.web.base.BaseService;
import batch.web.util.JvmUtil;
import batch.web.util.MapUtil;
import batch.web.util.NetUtil;
import batch.web.vo.CamelMap;

@Service("systemInfoSvi")
public class SystemInfoSvc extends BaseService implements SystemInfoSvi {

	@Override
	public int saveJvmInfo() throws Exception {
		CamelMap camelMap = null;
		
		Map<String, Object> jvmMap = MapUtil.merge(JvmUtil.getJvmMemInfo(), JvmUtil.getThreadInfo());
		jvmMap.put("processId", JvmUtil.getProcessId());
		jvmMap.put("ipAddr", NetUtil.getHostAddr());
		jvmMap.put("hostName", NetUtil.getHostName());
		jvmMap.put("cpuUsage", JvmUtil.getCpuInfo());
		jvmMap.put("memUsage", JvmUtil.getMenInfo());
		jvmMap.put("jvmName", JvmUtil.getJvmName());
		
		/** Processing Host Information **/
		camelMap = baseDao.select("web.sysinfo.selSysInfo", jvmMap);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Thread Info :: {}", jvmMap);
		}
		int cnt = 0;
		if (camelMap == null) {
			cnt = baseDao.insert("web.sysinfo.insSysInfo", jvmMap);
		}
		else {
			cnt = baseDao.update("web.sysinfo.updSysInfo", jvmMap);			
		}
		
		/** Processing JVM Information **/
		camelMap = baseDao.select("web.jvminfo.selJvmInfo", jvmMap);
		if (camelMap == null) {
			cnt = baseDao.insert("web.jvminfo.insJvmInfo", jvmMap);
		}
		else {
			cnt = baseDao.update("web.jvminfo.updJvmInfo", jvmMap);			
		}
		return cnt;
	}

	@Override
	public List<String> selectThreadInfo() throws Exception {
		return JvmUtil.getThredDump();
	}

	@Override
	public List<CamelMap> selectSysInfo(Map<String, Object> sysMap) throws Exception {
		
		return baseDao.selectList("web.sysinfo.selSysInfoList", sysMap);
	}

	@Override
	public int selectSysInfoCount(Map<String, Object> sysMap) throws Exception {
		return baseDao.select("web.sysinfo.selSysInfoCount", sysMap);
	}

	@Override
	public List<CamelMap> selectJvmInfo(Map<String, Object> sysMap) throws Exception {
		
		return baseDao.selectList("web.jvminfo.selJvmInfoList", sysMap);
	}
}
