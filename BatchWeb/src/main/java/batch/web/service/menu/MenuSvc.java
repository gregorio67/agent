/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : MenuSvc.java
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

import org.springframework.stereotype.Service;

import batch.web.base.BaseService;
import batch.web.vo.CamelMap;

@Service("menuSvi")
public class MenuSvc extends BaseService implements MenuSvi{


	@Override
	public List<CamelMap> getActiveMenuList(Map<String, Object> menu) throws Exception {
		return baseDao.selectList("web.menu.selActiveMenuList", menu);
	}
	@Override
	public List<CamelMap> getMenuList(Map<String, Object> menu) throws Exception {
		return baseDao.selectList("web.menu.selMenuList", menu);
	}

	@Override
	public CamelMap getMenuItem(Map<String, Object> menu) throws Exception {
		return null;
	}
	@Override
	public int insertMenu(Map<String, Object> menu) throws Exception {
		return 0;
	}

	@Override
	public int deleteMenu(Map<String, Object> menu) throws Exception {
		return 0;
	}

	@Override
	public int updateMenu(Map<String, Object> menu) throws Exception {
		return 0;
	}

	@Override
	public int checkAuthMenu(Map<String, Object> menu) throws Exception {
		return 0;
	}



}
