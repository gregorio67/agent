/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : JobControlSvc.java
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

package batch.web.service.deploy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import batch.web.base.BaseService;
import batch.web.util.FileUtil;
import batch.web.vo.CamelMap;

@Service("deployTargetSvi")
public class DeployTargetSvc extends BaseService implements DeployTargetSvi{

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:SS:SSS");
	@Override
	public List<Map<String, Object>> findSource(Map<String, Object> params) throws Exception {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();		

		CamelMap deployTarget = baseDao.select("deploy.target.selDeployTargetItem", params);
		String sourceDir = deployTarget.get("sourceDir") != null ? String.valueOf(deployTarget.get("sourceDir")) : null;
		if (sourceDir == null) {
			
		}
		
		/** Check search source wild card **/
		String sourceType = params.get("sourceType") != null ? String.valueOf(params.get("sourceType")) : "All";
		String sourceName = params.get("sourceName") != null ? String.valueOf(params.get("sourceName")) : null;
		String searchSource = null;
		if (sourceName == null) {
			searchSource = "*" + sourceType;
		}
		else if ("All".equals(sourceType)) {
			searchSource = sourceName + "*"; 
		}
		else {
			searchSource = sourceName + "*" + sourceType;
		}
		Collection<File> files = FileUtil.getFiles(sourceDir, searchSource);
		Iterator<File> itrFile = files.iterator();
		while(itrFile.hasNext()) {
			File f = itrFile.next();
			if (f.isFile()) {
				Map<String, Object> fileMap = new HashMap<String, Object>();
				fileMap.put("sourceName",  f.getName());
				fileMap.put("sourceDir", getPackageName(sourceDir, f.getAbsolutePath()));
				fileMap.put("lastModifiedDate", sdf.format(f.lastModified()));
				resultList.add(fileMap);			
			}
		}
		return resultList;
	}
	@Override
	public CamelMap selectSourceDir(Map<String, Object> source) throws Exception {
		
		return baseDao.select("deploy.target.selDeployTargetItem", source);
	}
	
	private String getPackageName(String sourceDir, String path) throws Exception {
		String extension = FileUtil.getFileExtension(path);
		if ("java".equals(extension) || "class".equals(extension)) {
			sourceDir = sourceDir + File.separator + "java";
		}
		int sLen = sourceDir.length();
		return path.substring(sLen);
	}
}
