package batch.agent.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import batch.agent.context.BatchContext;
import batch.agent.exception.BaseException;


/**
 * @Project : TAXRIS Project
 * @Class : MessageUtil.java
 * @Description : 
 * @Author : KD1
 * @Since : 2017. 7. 11.
 *
 * @Copyright (c) 2017 TAXRIS All rights reserved.
 *----------------------------------------------------------
 * Modification Information
 *----------------------------------------------------------
 * 날짜            수정자             변경사유 
 *----------------------------------------------------------
 *2017. 7. 11.           KD1           최초작성
 *----------------------------------------------------------
 */
public class MessageUtil {
	
	/**LOGGER SET **/
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtil.class);
	
	private static final String DEFAULT_MESSAGE_BEAN_NAME = "messageSource";
	
	private static final String STATUS_SUCCESS = "S";
	
	private static final String STATUS_ERROR = "F";
		
	  
	  /**
	   * Get Message from message file
	   *<pre>
	   *
	   *</pre>
	   * @param messageSource MessageSource
	   * @param msgCode String
	   * @return String
	   * @throws Exception
	   */
	  public static String getMessage(String strMsgCd) throws Exception {
		  return getMessage(strMsgCd, null);
	  }
	  
	  /**
	   *  Get Message from message file
	   *<pre>
	   *
	   *</pre>
	   * @param messageSource MessageSource
	   * @param msgCode String
	   * @param msgParam String
	   * @return String
	   * @throws Exception
	   */
	  public static <U extends Object> String getMessage(String strMsgCd, U arrMsgParam[]) throws Exception {
		  return getMessage(strMsgCd, arrMsgParam, Locale.getDefault());
	  }
	  
	  /**
	   * Get Message from message file
	   *<pre>
	   *
	   *</pre>
	   * @param messageSource MessageSource
	   * @param msgCode String
	   * @param param <U>
	   * @param locale Locale
	   * @return String
	   * @throws Exception
	   */
	  
	  public static <U extends Object> String getMessage(String strMsgCd, U arrParam[], Locale locale) throws Exception {
		  
		  MessageSource messageSource = (MessageSource)BatchContext.getAppContext().getBean(DEFAULT_MESSAGE_BEAN_NAME);
		  
		  if (messageSource == null) {
			  String[] msgParam = {"DefaultConstants.DEFAULT_MESSAGE_BEAN_NAME"};
			  LOGGER.error("Message Bean Creation Error");
			  throw new BaseException("Message Bean Creation Error");
		  }
		  return messageSource.getMessage(strMsgCd, arrParam, locale).trim();
	  }
	  
	  
	  public static Map<String, Object> getSuccessMessage(String msg) throws Exception {
		  Map<String, Object> msgMap = new HashMap<String, Object>();
		  msgMap.put("status", STATUS_SUCCESS);
		  msgMap.put("message", msg);
		  
		  return msgMap;
	  }
	  
	  public static Map<String, Object> getErrorMessage(String msg) throws Exception {
		  Map<String, Object> msgMap = new HashMap<String, Object>();
		  msgMap.put("status", STATUS_ERROR);
		  msgMap.put("message", msg);
		  
		  return msgMap;
	  }
}
