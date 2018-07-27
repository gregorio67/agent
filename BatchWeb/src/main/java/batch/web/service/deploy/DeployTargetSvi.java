/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : JobControlSvi.java
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
 * 2018. 5. 17.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.deploy;

import java.util.List;
import java.util.Map;

import batch.web.vo.CamelMap;

public interface DeployTargetSvi {

	public List<Map<String, Object>> findSource(Map<String, Object> job) throws Exception;
	
	public CamelMap selectSourceDir(Map<String, Object> job) throws Exception;
	
}
