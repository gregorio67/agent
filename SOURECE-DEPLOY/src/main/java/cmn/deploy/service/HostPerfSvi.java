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

package cmn.deploy.service;

import java.util.List;
import java.util.Map;

public interface HostPerfSvi {
	
	public Map<String, Object> savePerfData(Map<String, Object> param) throws Exception;
	
	public int selectCpuListCnt(Map<String, Object> param) throws Exception;
	
	public List<Map<String, Object>> selectCpuList(Map<String, Object> param) throws Exception;

	public Map<String, Object> selectCpuChartList(Map<String, Object> param) throws Exception;

	public int selectMemListCnt(Map<String, Object> param) throws Exception;
	
	public List<Map<String, Object>> selectMemList(Map<String, Object> param) throws Exception;

	public Map<String, Object> selectMemChartList(Map<String, Object> param) throws Exception;
	
}
