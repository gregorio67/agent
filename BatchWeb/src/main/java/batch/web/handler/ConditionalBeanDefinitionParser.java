/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : ConditionalBeanDefinitionParser.java
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

import java.util.Properties;


import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;


public class ConditionalBeanDefinitionParser implements BeanDefinitionParser{
	private final Log cLog = LogFactory.getLog(ConditionalBeanDefinitionParser.class);
    private Properties config;


   /** Default placeholder prefix: "${" */
   public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
   /** Default placeholder suffix: "}" */
   public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";


   public ConditionalBeanDefinitionParser() {
         config = new Properties();
   }


   /**
   * Parse the "condition" element and check the mandatory "test" attribute. If
   * the provided resources or the system property named by test is null/empty/false
   * (i.e. not defined) then return null, which is the same as not defining the bean.
   */
   @SuppressWarnings("unchecked")
public BeanDefinition parse(Element element, ParserContext parserContext) {
          try{
            if (DomUtils.nodeNameEquals(element, "condition")) {
               String test = element.getAttribute("test");
               String src = element.getAttribute("src");
               String varnames = element.getAttribute("varnames"); 

               // Check the src attribute is not empty.
                if(StringUtils.isNotEmpty(src)){
                   Resource resource = parserContext.getReaderContext().getResourceLoader().getResource(src.trim());
                   config.load(resource.getInputStream());
                }else{
                   throw new IllegalArgumentException("src attribute not set.");
                } 
               // Check if the varnames is not empty/null
               if(StringUtils.isNotEmpty(test) && StringUtils.isNotEmpty(varnames) && StringUtils.isNotBlank(varnames)){
                  String expression = test.substring(
                             DEFAULT_PLACEHOLDER_PREFIX.length(),
                             test.length() - DEFAULT_PLACEHOLDER_SUFFIX.length()).trim();

                  String[] vars = varnames.split(",");
                  JexlContext jc = JexlHelper.createContext();
                  for(String varname: vars){
                    varname = varname.trim();
                    jc.getVars().put(varname, config.get(varname));
                  }
                  Expression e = ExpressionFactory.createExpression(expression);
                  Object result = e.evaluate(jc);  
                  if( (null != result)){
                    if(result.toString().equalsIgnoreCase("true")){
                       Element beanElement = DomUtils.getChildElementByTagName(element, "bean");
                       return parseAndRegisterBean(beanElement, parserContext);
                    }else if(result.toString().equalsIgnoreCase("false")){
                       return null;
                    }else if ( StringUtils.isNotEmpty(getProperty(test))) {
                       Element beanElement = DomUtils.getChildElementByTagName(element, "bean");
                       return parseAndRegisterBean(beanElement, parserContext);
                    }else{
                       cLog.warn("Condition creation did not happen as test or src attribute is not set.");
                    }
               }
          }
         // Else proceed with non-empty/NULL/Boolean value check
         else{
            if ( StringUtils.isNotEmpty(getProperty(test))) {
               Element beanElement = DomUtils.getChildElementByTagName(element, "bean");
               return parseAndRegisterBean(beanElement, parserContext);
             }else{
               cLog.warn("Condition creation did not happen as test or src attribute is not set.");
             }
        }
     }
  }catch (Exception e) {
      cLog.error("Faiload condition bean.", e);
  }
     return null;
 } 


 /**
  * Get the value of a named resource/system property (it may not be defined).
  *
  * @param strVal The name of a system property. The property may
  * optionally be surrounded in Ant/EL-style brackets. e.g. "${propertyname}" 
  *
  * @return
  */
   private String getProperty(String strVal) {
           cLog.info(strVal);
           if (StringUtils.isEmpty(strVal)) {
               return null;
           } 
           String returnValue = null;
           if (strVal.startsWith(DEFAULT_PLACEHOLDER_PREFIX) && strVal.endsWith(DEFAULT_PLACEHOLDER_SUFFIX)) {
               returnValue = config.getProperty(
                     strVal.substring(DEFAULT_PLACEHOLDER_PREFIX.length(),
                     strVal.length() - DEFAULT_PLACEHOLDER_SUFFIX.length()).trim());
               if(null == returnValue){
                   returnValue = System.getProperty(
                           strVal.substring(DEFAULT_PLACEHOLDER_PREFIX.length(),
                           strVal.length() - DEFAULT_PLACEHOLDER_SUFFIX.length()));
                }
                if( StringUtils.isNotEmpty(returnValue)){
                   if(returnValue.trim().equalsIgnoreCase("false"))
                       returnValue = null;
                    }
                    if(cLog.isDebugEnabled()){
                        cLog.debug("ReturnedSystem.getProperty" + (
                                  strVal.substring(DEFAULT_PLACEHOLDER_PREFIX.length(),
                                  strVal.length() - DEFAULT_PLACEHOLDER_SUFFIX.length())));
                     }
                     return returnValue;
                  }else{
                     return System.getProperty(strVal);
                 }
             } 


      private BeanDefinition parseAndRegisterBean(Element element, ParserContext parserContext) {
             BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
             BeanDefinitionHolder holder = delegate.parseBeanDefinitionElement(element);
             BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry()); 
             return holder.getBeanDefinition();
     }

}
