package cmn.deploy.base;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseService{
	
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	/** 영업정보 데이터베이스를 위한 기본 Mapper **/
	@Resource(name = "baseDao")
	protected BaseDao baseDao;
			

}
