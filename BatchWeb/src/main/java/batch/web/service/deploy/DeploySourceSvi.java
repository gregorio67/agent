/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SourceDeploySvi.java
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
 * 2018. 6. 25.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.deploy;

import java.util.Map;

public interface DeploySourceSvi {
	
	public boolean sourceCompile(Map<String, Object> source) throws Exception;
	
	public boolean deploySource(Map<String, Object> source) throws Exception;
	
	public boolean startServer(Map<String, Object> source) throws Exception;

	public boolean stopServer(Map<String, Object> source) throws Exception;

}
