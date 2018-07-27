/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : DeployIService.java
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

import java.util.List;
import java.util.Map;

public interface DeployIService {

	public Map<String, Object> selCodeList() throws Exception;
	
	public int selDeployTargetListCnt(Map<String, Object> param) throws Exception;
	
	public List<Map<String, Object>> selDeployTargetList(Map<String, Object> param) throws Exception;
}
