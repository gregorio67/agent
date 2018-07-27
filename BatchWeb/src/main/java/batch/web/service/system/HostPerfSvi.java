/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : HostPerfSvi.java
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

package batch.web.service.system;

import java.util.Map;

public interface HostPerfSvi {
	
	public Map<String, Object> savePerfData(Map<String, Object> param) throws Exception;
}
