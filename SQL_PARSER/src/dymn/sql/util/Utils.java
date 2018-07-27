/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : FileUtils.java
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
 * 2018. 4. 24.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package dymn.sql.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class Utils {
	
	private static String START_CDATA = "<![CDATA[";
	private static String END_CDATA = "]]>";
	private static char SPACE = ' ';
	private static String SQL_FILE_FILTER = "*.xml";
	
	public static Collection<File> getFiles(String dir) throws Exception {
		if (dir == null) {
			return null;
		}
		File f = new File(dir);
		if (!f.isDirectory()) {
			return null;
		}
		
//		WildcardFileFilter wildcardFilter = new WildcardFileFilter(SQL_FILTER);
//		return FileUtils.listFiles(new File(dir), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		return FileUtils.listFiles(new File(dir), new WildcardFileFilter(SQL_FILE_FILTER) , TrueFileFilter.INSTANCE);
	}
	
	
	public static Map<String, Object> getSqlStatementParse(File file) throws Exception {
		
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		List<String> sqlStatements = new ArrayList<String>();
				
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line = null;
		String serviceName = null;
		boolean isSql = false;
		
		while((line = br.readLine()) != null) {
					
			if (line.contains("foreach")) {
				continue;
			}
			/** Service Name **/
			if (line.contains("namespace")) {
				int idx1 = line.lastIndexOf("namespace=");
				int idx2 = line.lastIndexOf("\"");
				serviceName = line.substring(idx1 + 11, idx2);
				serviceName = serviceName.substring(serviceName.lastIndexOf(".") + 1);
			}

			if (line.contains("<select") || line.contains("<update") || line.contains("<delete") || line.contains("<insert")) {				
				isSql = true;
			}
			else if (line.contains("</select") || line.contains("</update") || line.contains("</delete") || line.contains("</insert")) {
				if (sb.length() > 0) {
					String sqlline = removeCDATA(sb.toString());

					sqlStatements.add(removeSpace(sqlline));
					sb.delete(0, sb.length());
				}
				isSql = false;
			}
			else if (isSql) {
				if (!line.contains("<if") && !line.contains("<choose") && !line.contains("<when") && !line.contains("<otherwise") &&
				   !line.contains("</if") && !line.contains("</choose") && !line.contains("</when") && !line.contains("</otherwise") &&
				   !line.contains("<isNotEmpty") && !line.contains("</isNotEmpty") && !line.contains("<isNotEqual") && !line.contains("</isNotEqual") &&
				   !line.contains("<isEqual") && !line.contains("</isEqual") && !line.contains("<isNull") && !line.contains("</isNull") &&
				   !line.contains("<isEmpty") && !line.contains("</isEmpty") && !line.contains("<!--") && !line.contains("-->")){
						line = line.toUpperCase();
						line = removeComment(line);
						String tempLine = line.replaceAll("\n", " ").replaceAll("\t", " ").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\#", " ").replaceAll("\\$", " ");
						sb.append(tempLine);					   
				   }
				if (line.contains(START_CDATA) || line.contains(END_CDATA)) {
					String result = removeCDATA(line);
					if (result != null) {
						sb.append(result);
					}
				}
				
			}
			
			
		}
		if (br != null) {
			br.close();
		}

		sqlMap.put("serviceName", serviceName);
		sqlMap.put("sqlStatements", sqlStatements);
		return sqlMap;
		
	}

	
	public static List<String> getSqlStatement(File file) throws Exception {
		
		List<String> sqlIds = new ArrayList<String>();
				
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line = null;
		String servicename = null;		
		try {
			while((line = br.readLine()) != null) {
				
				/** Service Name **/
				if (line.contains("namespace=")) {
					servicename = getServicename(line); 
				}

				if (line.contains("<select") || line.contains("<update") || line.contains("<delete") || line.contains("<insert")) {
					String tempSqlId = getSqlId(line);
					if (servicename != null) {
						sqlIds.add(servicename + "." + tempSqlId);					
					}
					else {
						sqlIds.add(tempSqlId);		
					}
				}
			}			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			br.close();
		}
		return sqlIds;
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Get service name from namespace(last string)
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param line
	 * @return
	 * @throws Exception
	 */
	public static String getServicename(String line ) throws Exception {
		char tempChar[] = line.toCharArray();
		StringBuilder sb = new StringBuilder();
		/** read char from "namespace=" length + 1 **/
		int idx = line.lastIndexOf("namespace=") + 11;
		
		for (int i = idx ; i< tempChar.length; i++) {
			/** if find last double quotation then break**/
			if (tempChar[i] == '"') {
				break;
			}
			else {
				sb.append(tempChar[i]);
			}
		}
		String namespace = sb.toString();
		idx = namespace.lastIndexOf(".");
		String serviceName = namespace.substring(idx + 1);

		return serviceName;
	}
	
	public static String getSqlId(String line) throws Exception {
		char[] tempChar = line.toCharArray();
		StringBuilder sb = new StringBuilder();
		int idx = line.indexOf("id=");
		boolean isId = false;
		
		for (int i = idx ; i < tempChar.length; i++) {
			if (tempChar[i] == 'i' && tempChar[i + 1] == 'd' && tempChar[i + 2] == '=' && tempChar[i +3] == '"') {
				isId = true;
				i = i + 3;
			}
			else if (isId){
				if (tempChar[i] == '"') {
					isId = false;
					break;
				}
				else {
					sb.append(tempChar[i]);					
				}
			}
		}
		
		return sb.toString();
	}
	
	public static String removeCDATA(String text) throws Exception {
		String result = text.replaceAll("<\\!\\[CDATA\\[","").replaceAll("\\]\\]>",""); 
		return result;
	}
		
	
	
	public static String removeSpace(String text) throws Exception {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		
		char[] tempChar = text.toCharArray();
		int idx = 0;
		int len = tempChar.length;
		for (char ch : tempChar) {
			if (ch == SPACE) {
				if ((idx + 1) < len) {
					if(tempChar[idx + 1] != SPACE) {
						if (!isFirst) {
							sb.append(ch);
						}
					}					
				}
			}
			else {
				sb.append(ch);
				isFirst = false;
			}
			idx++;
		}
		return sb != null ? sb.toString() : "";
	}

	public static String removeComment(String line) throws Exception {
		StringBuilder sb = new StringBuilder();
		char[] tempChar = line.toCharArray();
		boolean isComment = false;
		for(int i = 0; i < tempChar.length; i++) {
			if (tempChar[i] == '/' && tempChar[i + 1] == '*') {
				isComment = true;
				i = i + 1;
			}
			else if (isComment) {
				if (tempChar[i] == '*' && tempChar[i + 1] == '/') {
					isComment = false;
					i = i + 1;
				}				
			}
			else {
				sb.append(tempChar[i]);
			}
		}
		
		return sb.toString();
	}
	public static List<String> getSelectTableName(String sql) throws Exception {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Select selectStatement = (Select) statement;
		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
		
		System.out.printf("SELECT  Statement :: [%s]", sql);
		System.out.printf("\t Tables : %s\n", tableList);

		return tableList;
	}
	
	public static List<String> getInsertTableName(String sql) throws Exception {
		List<String> tableList = null;


		Statement statement = CCJSqlParserUtil.parse(sql);
		Insert insStatement = (Insert) statement;
		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		tableList = tablesNamesFinder.getTableList(insStatement);

		System.out.printf("INSERT Statement :: [%s]", sql);
		System.out.printf("\t Tables : %s\n", tableList);
		
		return tableList;
	}

	public static List<String> getUpdateTableName(String sql) throws Exception {
		List<String> tableList = null;
		

		Statement statement = CCJSqlParserUtil.parse(sql);
		Update updStatement = (Update) statement;
		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		tableList = tablesNamesFinder.getTableList(updStatement);

		System.out.printf("UPDATE Statement :: [%s]", sql);
		System.out.printf("\t Tables : %s\n", tableList);

		return tableList;
	}

	public static List<String> getDeleteTableName(String sql) throws Exception {
		List<String> tableList = null;

		Statement statement = CCJSqlParserUtil.parse(sql);
		Delete delStatement = (Delete) statement;
		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		tableList = tablesNamesFinder.getTableList(delStatement);

		System.out.printf("UPDATE Statement :: [%s]", sql);
		System.out.printf("\t Tables : %s\n", tableList);

		
		return tableList;
	}
	
	public static String getCurrentPath() throws Exception {
		Path currentRelativePath = Paths.get("");
		String curDir = currentRelativePath.toAbsolutePath().toString();
		return curDir;
	}	
	
	public static void main(String[] args) throws Exception {
		String temp = getSqlId("<select id=\"DcProcInsOraDAO.selectDcProcInsGrap\" resultClass=\"sc.co.cmmn.util.ResultMap\">");
		String temp1 = getServicename("<mapper namespace=\"web.batch.user\">");
		System.out.println(temp1);
	}
	


}
