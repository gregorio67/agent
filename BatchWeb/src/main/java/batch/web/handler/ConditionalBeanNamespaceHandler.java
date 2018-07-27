/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : ConditionalBeanNamespaceHandler.java
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
 * 2018. 6. 1.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class ConditionalBeanNamespaceHandler extends NamespaceHandlerSupport{

	@Override
	public void init() {
		 super.registerBeanDefinitionParser("cond", new ConditionalBeanDefinitionParser());
	}

}
