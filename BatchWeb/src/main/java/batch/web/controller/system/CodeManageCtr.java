/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : CodeManageCtr.java
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

package batch.web.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.service.system.CodeSvi;
import batch.web.util.MessageUtil;

@RestController
public class CodeManageCtr extends BaseController{

	@Resource(name = "codeSvi")
	private CodeSvi codeSvi;
	
	@RequestMapping(value="/code/bizCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> searchSource(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;
		List<Map<String, Object>> bizList = codeSvi.selectBizCode(paramMap);
		if (bizList.size() <= 0) {
			msgMap = MessageUtil.getErrorMessage("There is no data");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		msgMap = MessageUtil.getSuccessMessage("Successfully Ended");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("bizList", bizList);
		return resultMap;

	}
	
}
