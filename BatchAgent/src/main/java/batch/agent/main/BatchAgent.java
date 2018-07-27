/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : BatchAgent.java
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
 * 2018. 6. 4.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.agent.main;

import batch.agent.context.BatchContext;

public class BatchAgent {

	
	public static void main(String args[]) throws Exception {
		BatchContext.getAppContext();
	}
}
