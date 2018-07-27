/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : DeployCService.java
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
 * 2018. 7. 10.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package cmn.deploy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cmn.deploy.base.BaseService;

@Service("deployIService")
public class DeployCService extends BaseService implements DeployIService{

	@Override
	public Map<String, Object> selCodeList() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> hostNameList = baseDao.selectList("deploy.target.selHostNameList");
		
		List<Map<String, Object>> deployTypeList = baseDao.selectList("deploy.target.selDeployTypeList");
		
		resultMap.put("hostNameList", hostNameList);
		resultMap.put("deployTypeList", deployTypeList);
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> selDeployTargetList(Map<String, Object> param) throws Exception {
		
		return baseDao.selectList("deploy.target.selDeployTargetList", param);
	}

	@Override
	public int selDeployTargetListCnt(Map<String, Object> param) throws Exception {
		
		return baseDao.select("deploy.target.selDeployTargetCnt", param);
	}

}
