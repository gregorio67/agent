/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : StringUtil.java
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
 * 2018. 7. 12.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.agent.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
	
	private static final String DEFAULT_DELIMETER = "\\|";
	
	public static List<String> str2List(String source) throws Exception {
		return str2List(source, DEFAULT_DELIMETER);
	}
	
	public static List<String> str2List(String source, String delimeter) throws Exception {
		
		List<String> result = new ArrayList<String>();
		if (source == null) {
			return result;
		}
		String[] strArray = source.split(delimeter);
		for (String str : strArray) {
			result.add(str);
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		String data = "test|test1222|t";
		System.out.printf("Result : %s", str2List(data));
	}

}
