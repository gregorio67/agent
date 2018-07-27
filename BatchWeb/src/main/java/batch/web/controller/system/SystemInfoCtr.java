/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SystemInfoCtr.java
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

package batch.web.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.service.system.SystemInfoSvi;
import batch.web.util.MessageUtil;
import batch.web.util.PropertiesUtil;
import batch.web.vo.CamelMap;

@RestController
public class SystemInfoCtr extends BaseController {
	
	@Resource(name = "systemInfoSvi")
	private SystemInfoSvi systemInfoSvi;
	

	@RequestMapping(value="/system/sysInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getSystemInfo(@RequestBody Map<String, Object> sysMap) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = null;
		Map<String, Object> msgMap = null;		

		int totalCount = systemInfoSvi.selectSysInfoCount(sysMap);
		
		/** Get Total Row Count **/
		int currentPage = 0;
		if (sysMap.get("currentPage") != null) {
			currentPage = Integer.parseInt(String.valueOf(sysMap.get("currentPage")));
		}
		else {
			currentPage = 1;
		}
		
		pageMap = getPageInfo(currentPage, totalCount);
		
		
		/** Get Batch Job List **/
		/** Get Batch Job List **/
		int pageRowCount = PropertiesUtil.getInt("screen.page.rowcount");
		sysMap.put("pageRowCount", pageRowCount);
		sysMap.put("curRowCount", (currentPage  - 1) * pageRowCount);
		
		List<CamelMap > sysList = systemInfoSvi.selectSysInfo(sysMap);
		
		msgMap = MessageUtil.getSuccessMessage("Successfully enede");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("page", pageMap);
		resultMap.put("sysList", sysList);
		
		return resultMap;
	}

	
	/**
	 * 
	 *<pre>
	 * 1.Description: Get JVM Information
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param jvmMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/system/jvmInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getJvmInfo(@RequestBody Map<String, Object> jvmMap) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = null;
		Map<String, Object> msgMap = null;		

		/** Get Batch Job List **/
		List<CamelMap > sysList = systemInfoSvi.selectJvmInfo(jvmMap);
		
		msgMap = MessageUtil.getSuccessMessage("Successfully enede");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("jvmList", sysList);
		
		return resultMap;
	}	
	/**
	 * 
	 *<pre>
	 * 1.Description: Thread Dump
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param sysMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/system/threadDump", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getThreadDump(@RequestBody Map<String, Object> sysMap) throws Exception {
		
		return systemInfoSvi.selectThreadInfo();
	}
}
