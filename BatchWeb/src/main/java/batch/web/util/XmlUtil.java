/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : XmlUtil.java
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
 * 2018. 6. 22.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.util;

import java.util.Map;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUtil {

	public static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);

	public static Map<String, Object> xml2Map(String xml) throws Exception {

		JSONObject xmlJSONObj = XML.toJSONObject(xml);
		Map<String, Object> result = JsonUtil.json2Map(xmlJSONObj.toString());

		return result;
	}
	
	
	public static <K, V> String  map2Xml(Map<K, V> map) throws Exception {
		
		String jsonData = JsonUtil.map2Json(map);
		
		String xml = XML.toString(new JSONObject(jsonData));
		return xml;
	}
	
	public static String json2Xml(String json) throws Exception {
		JSONObject jsonData = new JSONObject(json);
		String xml = XML.toString(jsonData);
		return xml;
	}
	
	
	public static void main(String args[]) throws Exception {
		String jsonStr = "{\"name\":\"포도\", \"code\":\"1000\"}";

		
		String xml = json2Xml(jsonStr);
		Map<String, Object> map = xml2Map(xml);
		String xml1 = map2Xml(map);
		
		System.out.println("XML =" + xml);
		System.out.println("map = " + map);
		System.out.println("XML1 = " + xml1);
				
	}
}
