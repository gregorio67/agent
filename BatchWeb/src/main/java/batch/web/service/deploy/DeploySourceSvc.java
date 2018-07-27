/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : DeploySourceSvc.java
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

package batch.web.service.deploy;

import java.util.Map;

import javax.annotation.Resource;

import batch.web.base.BaseService;
import batch.web.service.ant.AntService;

public class DeploySourceSvc extends BaseService implements DeploySourceSvi{

	@Resource(name="antService")
	private AntService antService;
	@Override
	public boolean sourceCompile(Map<String, Object> source) throws Exception {
		String srcDir = source.get("sourcePath") != null ? String.valueOf(source.get("srcDir")) : null;
		String buildDir = source.get("buildPath") != null ? String.valueOf(source.get("srcDir")) : null;
		return false;
	}

	@Override
	public boolean deploySource(Map<String, Object> source) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startServer(Map<String, Object> source) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopServer(Map<String, Object> source) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}



}
