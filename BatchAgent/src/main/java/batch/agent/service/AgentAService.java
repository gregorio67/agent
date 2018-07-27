/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : AgentAService.java
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AgentAService<V, T> {
	
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public V process(T param) throws Exception {
		LOGGER.info("Input Parameter :: ", param);
		
		V result = executeProcess(param);
		
		LOGGER.info("Result :: ", result);
		
		return result;
	}

	public abstract V executeProcess(T param) throws Exception; 
}
