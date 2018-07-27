/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : PatternMatcher.java
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
 * 2018. 5. 17.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.util;

import java.util.List;

public class PatternUtil {

	/**
	 * 
	 *<pre>
	 * 1.Description: Check pattern is match input path
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param patterns patterns to check
	 * @param path 
	 * @return
	 * @throws Exception
	 */
	public static boolean isPatternMatch(List<String> patterns, String path) throws Exception {
		boolean isMatch = true;
		for (String pattern : patterns) {
			isMatch = isPatternMatch(pattern, path);
			if (isMatch) {
				break;
			}
		}
		return isMatch;
	}

	/**
	 * 
	 *<pre>
	 * 1.Description: Check pattern is match input path
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param patterns patterns to check
	 * @param path 
	 * @return
	 * @throws Exception
	 */
	public static boolean isPatternMatch(String pattern, String path) throws Exception {
		boolean isMatch = true;
		
		if(pattern.equals(path)) {
			return true;
		}
		
		if (pattern.charAt(0) == '/') {
			pattern = pattern.substring(1);
		}
		if (path.charAt(0) == '/') {
			path = path.substring(1);
		}
		
		String[] patterns = pattern.split("/");
		String[] paths = path.split("/");
		int patternLen = patterns.length;
		
		for (int i = 0 ; i < patternLen; i++) {
			if (!"*".equals(patterns[i])) {
				if (!isStringMatch(patterns[i], paths[i])) {
					isMatch = false;
					break;
				}
			}
		}
		
		return isMatch;
	}
	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Check String is match with pattern
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param pattern
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static boolean isStringMatch(String pattern, String source) throws Exception {
		boolean isMatch = true;

		char[] tempPattern = pattern.toCharArray();
		char[] tempPath = source.toCharArray();
		int len = tempPattern.length;
		
		for (int k = 0; k < len; k++) {
			if (tempPattern[k] == '*') {
				break;
			}
			else if (tempPattern[k] == '?') {
				continue;
			}
			else {
				if (tempPattern[k] != tempPath[k]) {
					isMatch = false;
					break;
				}
			}
		}
		/** If the last char is not * Check source length is bigger than pattern **/
		if (tempPattern[len - 1] != '*') {
			if (source.length() > pattern.length()) {
				isMatch = false;
			}
		}
		
		return isMatch;
		
	}
	public static void main(String args[]) throws Exception {
		String pattern = "/batch/j*/b?c";
		String path = "/batch/job/bac";
				
		System.out.println(isPatternMatch(pattern, path));
	}
}
