package batch.web.controller.deploy;

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
import batch.web.service.deploy.SourceManageSvi;
import batch.web.util.MessageUtil;
import batch.web.util.PropertiesUtil;
import batch.web.vo.CamelMap;

@RestController
public class DeployManageCrt extends BaseController {
	
	@Resource(name = "sourceManageSvi")
	private SourceManageSvi sourceManageSvi;
		
		
	@RequestMapping(value="/deploy/insert", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> insertSource(@RequestBody Map<String, Object> paramMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;	
		
		/** Check input parameter **/
		if (paramMap.get("bizName") == null || paramMap.get("deployDate") == null || 
				paramMap.get("deploySource") == null || paramMap.get("deployPackage") == null) {
			msgMap = MessageUtil.getErrorMessage("Check input parameter");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		
		/** Insert deploy source **/
		int cnt = sourceManageSvi.insertDeploySource(paramMap);
		if (cnt <= 0) {
			msgMap = MessageUtil.getErrorMessage("Insert Fail");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		
		msgMap = MessageUtil.getSuccessMessage("Successfully inserted");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}

	@RequestMapping(value="/deploy/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updateSource(@RequestBody Map<String, Object> paramMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;	
		
		/** Check input parameter **/
		if (paramMap.get("seq") == null ) {
			msgMap = MessageUtil.getErrorMessage("Check input parameter");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		
		
		int cnt = sourceManageSvi.updateDeploySource(paramMap);
		if (cnt <= 0) {
			msgMap = MessageUtil.getErrorMessage("Update Failed");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		
		msgMap = MessageUtil.getSuccessMessage("Successfully updated");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}

	@RequestMapping(value="/deploy/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteSource(@RequestBody Map<String, Object> paramMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;	

		/** Check input parameter **/
		if (paramMap.get("seq") == null ) {
			msgMap = MessageUtil.getErrorMessage("Check input parameter");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
			
		int cnt = sourceManageSvi.deleteDeploySource(paramMap);
		if (cnt <= 0) {
			msgMap = MessageUtil.getErrorMessage("Delete Failed");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		
		msgMap = MessageUtil.getSuccessMessage("Successfully deleted");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		return resultMap;
	}
	
	@RequestMapping(value="/deploy/selectList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteSourceList(@RequestBody Map<String, Object> paramMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;	
		Map<String, Object> pageMap = null;	
		
		/** Get Total Row Count **/
		int totalCount = sourceManageSvi.selectDeploySourceCnt(paramMap);
		int currentPage = 0;
		if (paramMap.get("currentPage") != null) {
			currentPage = Integer.parseInt(String.valueOf(paramMap.get("currentPage")));
		}
		else {
			currentPage = 1;
		}
		
		pageMap = getPageInfo(currentPage, totalCount);
		
		
		/** Get Batch Job List **/
		int pageRowCount = PropertiesUtil.getInt("screen.page.rowcount");
		paramMap.put("pageRowCount", pageRowCount);
		paramMap.put("curRowCount", (currentPage  - 1) * pageRowCount);
		
		/** Call service **/
		List<CamelMap> sourceList = sourceManageSvi.selectDeploySourceList(paramMap);
		if (sourceList == null) {
			msgMap = MessageUtil.getErrorMessage("There is no message");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		
		msgMap = MessageUtil.getSuccessMessage("Successfully Ended");
		resultMap.put("page", pageMap);
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("sourceList", sourceList);
		
		return resultMap;
	}

	@RequestMapping(value="/deploy/selectItem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteSourceItem(@RequestBody Map<String, Object> paramMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msgMap = null;	
		
		CamelMap sourceItem = sourceManageSvi.selectDeploySourceItem(paramMap);
		if (sourceItem == null) {
			msgMap = MessageUtil.getErrorMessage("There is no result");
			resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
			return resultMap;
		}
		
		msgMap = MessageUtil.getSuccessMessage("Successfully enede");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("sourceItem", sourceItem);
		return resultMap;
	}
	
}
