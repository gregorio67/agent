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

package batch.web.controller.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import batch.web.base.BaseController;
import batch.web.service.system.HostPerfSvi;

@RestController
public class HostPerfCrt extends BaseController{

	@Resource(name = "hostPerfSvi")
	private HostPerfSvi hostPerfSvi;
	
	@RequestMapping(value = "/system/perf")
	public Map<String, Object> processPerfData(Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap = hostPerfSvi.savePerfData(params);
		return resultMap;
	}
}
