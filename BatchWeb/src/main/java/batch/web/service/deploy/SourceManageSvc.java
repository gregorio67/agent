/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SourceManageSvc.java
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
 * 2018. 6. 15.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.deploy;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import batch.web.base.BaseService;
import batch.web.vo.CamelMap;

@Service("sourceManageSvi")
public class SourceManageSvc extends BaseService implements SourceManageSvi{

	@Override
	public int insertDeploySource(Map<String, Object> source) throws Exception {
		return baseDao.insert("deploy.source.insDeploySource", source);
	}

	@Override
	public int updateDeploySource(Map<String, Object> source) throws Exception {
		return baseDao.update("deploy.source.updDeploySource", source);
	}

	@Override
	public int deleteDeploySource(Map<String, Object> source) throws Exception {
		return baseDao.delete("deploy.source.delDeploySource", source);
	}

	@Override
	public int selectDeploySourceCnt(Map<String, Object> source) throws Exception {
		return baseDao.select("deploy.source.selDeploySourceListCnt", source);
	}

	@Override
	public List<CamelMap> selectDeploySourceList(Map<String, Object> source) throws Exception {
		return baseDao.selectList("deploy.source.selDeploySourceList", source);
	}

	@Override
	public CamelMap selectDeploySourceItem(Map<String, Object> source) throws Exception {
		return baseDao.select("deploy.source.selDeploySourceItem", source);
	}

}
