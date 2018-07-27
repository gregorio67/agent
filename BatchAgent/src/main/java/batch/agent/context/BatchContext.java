/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : BatchContext.java
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
 * 2018. 6. 4.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.agent.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BatchContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchContext.class);
	private static final Object synObject = new Object();
	private static ClassPathXmlApplicationContext appContext = null;
	private static final String APP_CONTEXT_LOC = "/spring/context-main.xml";
	
	public static ClassPathXmlApplicationContext getAppContext() throws Exception {
		if (appContext != null) {
			return appContext;
		}
		
		synchronized(synObject) {
			try {
				if (appContext == null) {
					appContext = new ClassPathXmlApplicationContext(APP_CONTEXT_LOC);					
				}
			}
			catch(Exception ex) {
				LOGGER.error("Create Application Context error :: {}", ex.getMessage());
			}
		}
		return appContext;		
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Return Spring bean instance from application context.
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param beanName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) throws Exception {
		/** If application context is null, create application context **/
		if (appContext == null) {
			getAppContext();
		}
		return (T) appContext.getBean(beanName);
	}
}
