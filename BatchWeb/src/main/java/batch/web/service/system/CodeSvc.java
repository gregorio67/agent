/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : CodeSvc.java
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
 * 2018. 6. 14.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.system;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import batch.web.base.BaseService;

@Service("codeSvi")
public class CodeSvc extends BaseService implements CodeSvi{

	@Override
	public List<Map<String, Object>> selectBizCode(Map<String, Object> code) throws Exception {
		return baseDao.selectList("deploy.target.selHostNameList", code);
	}

}
