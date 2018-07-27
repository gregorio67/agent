package batch.web.vo;

import org.apache.commons.collections.map.ListOrderedMap;

import batch.web.util.CamelUtil;

public class CamelMap extends ListOrderedMap {

	private static final long serialVersionUID = -8644338222761233955L;
	
	public Object put(Object key, Object value) {
		return super.put(CamelUtil.convert2CamelCase((String)key), value);
	}
}
