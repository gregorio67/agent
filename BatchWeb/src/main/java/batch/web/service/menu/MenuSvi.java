/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : MenuSvi.java
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

package batch.web.service.menu;

import java.util.List;
import java.util.Map;

import batch.web.vo.CamelMap;

public interface MenuSvi {

	public List<CamelMap> getActiveMenuList(Map<String, Object> menu) throws Exception;

	public List<CamelMap> getMenuList(Map<String, Object> menu) throws Exception;
	
	public CamelMap getMenuItem(Map<String, Object> menu) throws Exception;

	public int insertMenu(Map<String, Object> menu) throws Exception;
	
	public int deleteMenu(Map<String, Object> menu) throws Exception;
	
	public int updateMenu(Map<String, Object> menu) throws Exception;
	
	public int checkAuthMenu(Map<String, Object> menu) throws Exception;

}
