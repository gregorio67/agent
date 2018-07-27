/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : DeployTargetCrt.java
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
 * 2018. 6. 27.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.controller.deploy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import batch.web.base.BaseConstants;
import batch.web.base.BaseController;
import batch.web.util.MessageUtil;

@RestController
public class DeployTargetCrt extends BaseController {

//	@RequestMapping(value="/deploy/target", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public Map<String, Object> deployTarget(@RequestBody Map<String, Object> paramMap) throws Exception {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Map<String, Object> msgMap = null;	
//		
//		/** Check input parameter **/
//		if (paramMap.get("bizName") == null || paramMap.get("deployDate") == null || 
//				paramMap.get("deploySource") == null || paramMap.get("deployPackage") == null) {
//			msgMap = MessageUtil.getErrorMessage("Check input parameter");
//			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
//			return resultMap;
//		}
//		
//		/** Insert deploy source **/
//		int cnt = sourceManageSvi.insertDeploySource(paramMap);
//		if (cnt <= 0) {
//			msgMap = MessageUtil.getErrorMessage("Insert Fail");
//			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
//			return resultMap;
//		}
//		
//		msgMap = MessageUtil.getSuccessMessage("Successfully inserted");
//		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
//		return resultMap;
//	}
}
