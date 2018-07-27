/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : MBeanSvi.java
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
 * 2018. 5. 31.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.system;

import java.util.List;
import java.util.Map;

import batch.web.vo.CamelMap;

public interface SystemInfoSvi {
	
	public int saveJvmInfo() throws Exception;
	
	public List<String> selectThreadInfo() throws Exception;
	
	public int selectSysInfoCount(Map<String, Object> sysMap) throws Exception;
	
	public List<CamelMap> selectSysInfo(Map<String, Object> sysMap) throws Exception;

	public List<CamelMap> selectJvmInfo(Map<String, Object> sysMap) throws Exception;
	
}
