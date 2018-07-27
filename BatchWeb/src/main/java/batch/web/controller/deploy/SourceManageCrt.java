package batch.web.controller.deploy;

import java.util.ArrayList;
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
import batch.web.service.deploy.DeployTargetSvi;
import batch.web.service.deploy.SourceManageSvi;
import batch.web.util.MessageUtil;
import batch.web.util.PropertiesUtil;

@RestController
public class SourceManageCrt extends BaseController {
	
	@Resource(name = "deployTargetSvi")
	private DeployTargetSvi deployTargetSvi;
	
	@Resource(name = "sourceManageSvi")
	private SourceManageSvi sourceManageSvi;
		
	
	/**
	 * 
	 * <pre>
	 * Start Batch
	 * </pre>
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/source/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> searchSource(@RequestBody Map<String, Object> paramMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pageMap = null;
		Map<String, Object> msgMap = null;		

		/** Check authority administrator **/
		List<Map<String, Object>> sourceList = deployTargetSvi.findSource(paramMap);
		
		/** Get Total Row Count **/
		int currentPage = 0;
		if (paramMap.get("currentPage") != null) {
			currentPage = Integer.parseInt(String.valueOf(paramMap.get("currentPage")));
		}
		else {
			currentPage = 1;
		}
		
		pageMap = getPageInfo(currentPage, sourceList.size());
		
		/** Get Batch Job List **/
		int pageRowCount = PropertiesUtil.getInt("screen.page.rowcount");
		paramMap.put("pageRowCount", pageRowCount);
		paramMap.put("curRowCount", (currentPage - 1) * pageRowCount);

		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		int idx = (currentPage - 1) * pageRowCount;
		for (int i = idx; i < pageRowCount + idx; i++) {
			resultList.add(sourceList.get(i));
		}

		msgMap = MessageUtil.getSuccessMessage("Successfully enede");
		resultMap.put(BaseConstants.DEFAULT_MESSAGE_NAME, msgMap);
		resultMap.put("page", pageMap);
		resultMap.put("sourceList", resultList);
		
		return resultMap;

	}
}
