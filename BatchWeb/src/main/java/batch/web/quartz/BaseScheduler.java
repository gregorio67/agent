/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : BaseScheduler.java
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
 * 2018. 6. 19.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.quartz;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;

public class BaseScheduler extends StdSchedulerFactory implements FactoryBean {

	private String propertyLocation = null;
	
	public void setPropertyLocation(String propertyLocation) {
		this.propertyLocation = propertyLocation;
	}


	@Override
	public Object getObject() throws Exception {
		if (propertyLocation != null) {
			super.initialize(new ClassPathResource(propertyLocation).getInputStream());			
		}
		else {
			super.initialize();
		}
		Scheduler shceduler = super.getScheduler();
		return shceduler;
	}

	@Override
	public Class getObjectType() {
		return Scheduler.class;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
