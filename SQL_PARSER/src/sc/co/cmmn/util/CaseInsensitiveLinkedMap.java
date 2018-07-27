package sc.co.cmmn.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class CaseInsensitiveLinkedMap extends LinkedHashMap implements Map{
	
	private static final long serialVersionUID = 3976452468446600292L;

	public Object get(Object key) {
    	return super.get(String.valueOf(key).toLowerCase());
    }

    public Object remove(Object key) {
        return super.remove(String.valueOf(key).toLowerCase());
    }
  
	public Object put(Object key, Object value) {
    	return super.put((Object) String.valueOf(key).toLowerCase(), value);
    }

    public boolean containsKey(Object key) {
        if(key instanceof String){
            key = ((String) key).toLowerCase() ;
        }
        return super.containsKey(key) ;
    }
}
