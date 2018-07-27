package sc.co.cmmn.util;

import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;
import java.sql.SQLException;



public class ResultMap extends CaseInsensitiveLinkedMap {

	private static final long serialVersionUID = 1189210137404623105L;


	public void set(String key, Object value) {
		put(key, value);
	}

	
	public Object get(Object key) {
		Object val = super.get(key);
		
		if (val == null)
			return "";
		
		if(val instanceof Number)
			return String.valueOf(val);
		else if(val instanceof Date)			
			return val;
		else if(val instanceof Blob)
			return (Blob)val;
		
		
		return (String)convertLobData(val);
	}

	public Object convertLobData(Object val) {
		if (val instanceof Clob) {
			Clob clob = (Clob) val;
			try {
				val = clob != null ? ((Object) (clob.getSubString(1L, (int) clob.length()))) : null;
			} catch (SQLException e) {
			}
		} else if (val instanceof Blob) {
			Blob blob = (Blob) val;
			try {
				val = blob != null ? ((Object) (blob.getBytes(1L, (int) blob.length()))) : null;
			} catch (SQLException e) {
			}
		}

		return val;
	}
}
