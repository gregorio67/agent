
package cmn.deploy.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmn.deploy.base.CamelMap;


public class MapUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapUtil.class);

	/**
	 * 
	 *<pre>
	 * 1.Description: Convert Map to String
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param map Map for converting String
	 * @return String 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String toStringMap(Map map) {
		StringBuffer sb = new StringBuffer();
		
		if (map != null && map.size() > 0) {
			Set<String> keySet = map.keySet();
			
			Iterator<String> iter = keySet.iterator();
			
			int cnt = 1;
			
			while (iter.hasNext()) {
				String key = iter.next();
				Object value = map.get(key);
				
				sb.append(cnt + " : " + "Column Name=" + key + ", " 
						+ "Value=" + value.toString() 
						+ ", DataType=" + value.getClass().getName() 
						+ "\n");
				
				cnt++;
			}
		} else {
			sb.append("정보가 없습니다.");
		}
		
		return sb.toString();
	}
	

	/**
	 * 
	 *<pre>
	 * 1.Description: Copy input map to new map
	 * 2.Biz Logic: 
	 * 3.Author : LGCNS
	 *</pre>
	 * @param sourceMap map for copied
	 * @return Copied map is same the input map
	 * @throws Exception
	 */
	public static Map<String, Object> setMapToMap(Map<String, Object> sourceMap) throws Exception {
		Map<String, Object> targetMap = new HashMap<String, Object>();
		
		if (sourceMap != null && sourceMap.size() > 0) {
			Set<String> keySet = sourceMap.keySet();
			
			Iterator<String> iter = keySet.iterator();
			
			while (iter.hasNext()) {
				String key = iter.next();
				Object value = sourceMap.get(key);
				
				targetMap.put(key, value.toString());
			}
		}
		
		return targetMap != null && targetMap.size() > 0 ? targetMap : new HashMap<String, Object>();
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Merge target map with source map
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param source source map for merge
	 * @param target target map for result
	 * @return map
	 * @throws Exception
	 */
	public static Map<String, Object> merge(Map<String, Object> source, Map<String, Object> target) throws Exception {
		return merge(source, target, null);
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Merge target map with source map if key is matched with args
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param source source map for merge
	 * @param target target map for result
	 * @param args group of keys for merging 
	 * @return target map
	 * @throws Exception
	 */
	public static Map<String, Object> merge(Map<String, Object> source, Map<String, Object> target, String[] args) throws Exception {
		if (target == null) {
			target = new HashMap<String, Object>();
		}
		
		Set<Entry<String, Object>> mapSet = source.entrySet();
		Iterator<Entry<String, Object>> mapItr = mapSet.iterator();
		String key = null;
		while(mapItr.hasNext()) {
			key = mapItr.next().getKey();
			if (args != null) {
				for (String arg : args) {
					if (arg.equals(key)) {
						target.put(key, source.get(key));						
					}
				}
			}
			else {
				target.put(key, source.get(key));				
			}
		}
		return target;
	}
	
	/**
	 * 
	 * <pre>
	 * Read Request JSON Data
	 * </pre>
	 * @param request HttpServletRequest
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readRequestData(HttpServletRequest request) throws Exception {
		/** Get Inputstream from HttpServletRequest **/
		InputStream is = request.getInputStream();

		/** Read data from InputStream **/
		byte[] requestData = IOUtils.toByteArray(is);
		
		String strParam = requestData != null ? new String(requestData) : null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Request Payload Data :: {}", strParam);
		}
		return (Map<String, Object>) (strParam != null ? JsonUtil.json2Map(strParam) : new HashMap<String, Object>());

	}
	
	
	public static CamelMap map2Camel(Map<String, Object> map) throws Exception {
		if (map == null) {
			return new CamelMap();
		}
		
		CamelMap camelMap = new CamelMap();
		Iterator<String> itrMap = map.keySet().iterator();
		while(itrMap.hasNext()) {
			String key = itrMap.next();
			camelMap.put(key, map.get(key));
		}
		return camelMap;
	}
}
