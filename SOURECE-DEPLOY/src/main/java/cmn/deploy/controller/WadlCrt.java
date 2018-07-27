/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : WadlCrt.java
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
 * 2018. 7. 16.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package cmn.deploy.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamWriter;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.MediaTypeExpression;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import cmn.deploy.base.BaseController;
import cmn.deploy.util.PropertiesUtil;
import cmn.deploy.util.XmlUtil;

@Controller
public class WadlCrt extends BaseController {
	
	@Resource(name = "requestMappingHandler")
	private RequestMappingHandlerMapping requestMappingHandler;
	
	@RequestMapping(value="wadl", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> generateWsdl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<RequestMappingInfo, HandlerMethod> mappings = requestMappingHandler.getHandlerMethods();
		Set<RequestMappingInfo> keys = mappings.keySet();
		Iterator<RequestMappingInfo> iterator = keys.iterator();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLStreamWriter writer = XmlUtil.getWriter(bos);
		XmlUtil.startDocument(writer, "UTF-8", "1.0");
		String namespace = PropertiesUtil.getString("wadl.namespace");
//		XmlUtil.startElement(writer, "application", PropertiesUtil.getString("wadl.namespace"));
		XmlUtil.startElement(writer, "application");
		XmlUtil.addNamespace(writer, "xmlns", "http://wadl.dev.java.net/2009/02");
		XmlUtil.addNamespace(writer, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		XmlUtil.addNamespace(writer, "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		XmlUtil.addNamespace(writer, "xmlns:apigee", "http://api.apigee.com/wadl/2010/07/");
		XmlUtil.addNamespace(writer, "xsi:schemaLocation", "http://wadl.dev.java.net/2009/02 http://apigee.com/schemas/wadl-schema.xsd http://api.apigee.com/wadl/2010/07/ http://apigee.com/schemas/apigee-wadl-extensions.xsd");
		
		XmlUtil.startElement(writer, "resources");
		XmlUtil.writeAttribute(writer,"base", "http://localhost:8080");
		
		
		while(iterator.hasNext()) {
			
			RequestMappingInfo key = iterator.next();
			HandlerMethod value = mappings.get(key);

			XmlUtil.startElement(writer, "resource");
			PatternsRequestCondition condition = key.getPatternsCondition();
			Set<String> patterns = condition.getPatterns();
			if (patterns != null) {
				for (String pattern : patterns) {
					XmlUtil.writeAttribute(writer, "path", pattern);
				}
			}
			
			Map<String, Object> list = new LinkedHashMap<String, Object>();
			
			/** Get parameter type **/
			Method method = value.getMethod();
			XmlUtil.writeAttribute(writer, "id", method.getName());

			/** Get request method **/
			Iterator<RequestMethod> itr = key.getMethodsCondition().getMethods().iterator();
			while(itr.hasNext()) {
				RequestMethod mkey = itr.next();
				XmlUtil.writeAttribute(writer, "name", mkey.toString());
				list.put("method", mkey.toString());				
			}
			
			Class<?>[] paramTypes = method.getParameterTypes();
			int idx = 0;
			if (paramTypes.length > 0) {
				XmlUtil.startElement(writer, "request");			
				for (Class<?> paramType : paramTypes) {
					XmlUtil.startElement(writer, "param");			
					String paramClass = paramType.getName();
					list.put("input" + idx, paramClass);
					XmlUtil.writeAttribute(writer, "type", paramClass);
					XmlUtil.endElement(writer);
				}
				XmlUtil.endElement(writer);
			}
			
			/** Get return type **/
			String returnType = method.getReturnType().getName();
			if (returnType != null) {
				XmlUtil.startElement(writer, "response");			
				list.put("output", returnType);
				XmlUtil.startElement(writer, "param");			
				XmlUtil.writeAttribute(writer, "type", returnType);			
				/** Get media type **/
				Iterator<MediaTypeExpression> itr1 = key.getProducesCondition().getExpressions().iterator();
				String mediaType = null;
				while(itr1.hasNext()) {
					MediaTypeExpression exp = itr1.next();
					mediaType = exp.getMediaType().toString();
					list.put("mediaType", mediaType);
					XmlUtil.writeAttribute(writer, "mediaType", mediaType);	
				}
				/** param **/
				XmlUtil.endElement(writer);
				/** response **/
				XmlUtil.endElement(writer);				
			}
			
			/** Resource **/
			XmlUtil.endElement(writer);	
		}

		/** resources **/
		XmlUtil.endElement(writer);
		/** application **/
		XmlUtil.endElement(writer);
		
		XmlUtil.endDocument(writer);
		LOGGER.debug("Created xml :: {}", bos.toString());
		
		
		String xml = XmlUtil.prettyPrint(bos.toString());
		OutputStream os = response.getOutputStream();
		os.write(xml.getBytes());
		LOGGER.info(xml);
		return resultMap;
	}
	
}
