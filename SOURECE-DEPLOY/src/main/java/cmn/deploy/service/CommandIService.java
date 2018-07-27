/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : CommandIService.java
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
 * 2018. 7. 12.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package cmn.deploy.service;

import java.util.Map;

public interface CommandIService {

	public Map<String, Object> selectCommand(Map<String, Object> params) throws Exception;
}
