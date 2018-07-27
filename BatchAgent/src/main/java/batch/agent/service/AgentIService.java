/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : AgentIService.java
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

package batch.agent.service;

import java.util.Map;

public interface AgentIService {
	
	public Map<String, Object> processMessage(Map<String, Object> params) throws Exception;

}
