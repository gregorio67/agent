/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : UserSvc.java
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

package batch.web.service.user;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import batch.web.base.BaseService;

@Service("userSvi")
public class UserSvc extends BaseService implements UserSvi{

	@Override
	public List<Map<String, Object>> getAllActiveUser(Map<String, Object> user) throws Exception {
		
		return baseDao.selectList("web.user.selUserList", user);
	}

	@Override
	public Map<String, Object> getActiveUser(Map<String, Object> user) throws Exception {
		return baseDao.select("web.user.selUserItem", user);
	}

	@Override
	public int insertUser(Map<String, Object> user) throws Exception {
		return baseDao.insert("web.user.insUser", user);
	}

	@Override
	public int deleteUser(Map<String, Object> user) throws Exception {
		return baseDao.delete("web.user.insUser", user);
	}

	@Override
	public int updateUser(Map<String, Object> user) throws Exception {
		return baseDao.update("web.user.updUser", user);
	}

	@Override
	public int updatePassword(Map<String, Object> user) throws Exception {
		return baseDao.update("web.user.updPassword", user);
	}

	@Override
	public int checkAuthUser(Map<String, Object> user) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}



}
