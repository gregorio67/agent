/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : UserSvi.java
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

public interface UserSvi {
	
	public List<Map<String, Object>> getAllActiveUser(Map<String, Object> user) throws Exception;
	
	public Map<String, Object> getActiveUser(Map<String, Object> user) throws Exception;

	public int insertUser(Map<String, Object> user) throws Exception;
	
	public int deleteUser(Map<String, Object> user) throws Exception;
	
	public int updateUser(Map<String, Object> user) throws Exception;

	public int updatePassword(Map<String, Object> user) throws Exception;
	
	public int checkAuthUser(Map<String, Object> user) throws Exception;
}
