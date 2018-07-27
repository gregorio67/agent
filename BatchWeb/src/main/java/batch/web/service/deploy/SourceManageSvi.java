/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SourceManageSvi.java
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
 * 2018. 6. 15.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.deploy;

import java.util.List;
import java.util.Map;

import batch.web.vo.CamelMap;

public interface SourceManageSvi {
	
	public int insertDeploySource(Map<String, Object> source) throws Exception;
	
	public int updateDeploySource(Map<String, Object> source) throws Exception;

	public int deleteDeploySource(Map<String, Object> source) throws Exception;
	
	public int selectDeploySourceCnt(Map<String, Object> source) throws Exception;

	public List<CamelMap> selectDeploySourceList(Map<String, Object> source) throws Exception;

	public CamelMap selectDeploySourceItem(Map<String, Object> source) throws Exception;

}
