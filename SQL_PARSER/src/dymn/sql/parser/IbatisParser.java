/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : IbatisParser.java
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
 * 2018. 5. 16.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package dymn.sql.parser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import dymn.sql.util.IbatisUtil;
import dymn.sql.util.Utils;

public class IbatisParser {

	private static SqlMapClient sqlClient = null;
	
	private static final String ERR_FILE_NAME = "parser_err.text";
	
	public static void iBatisParser(String confDir, String dir) throws Exception {
		sqlClient = IbatisUtil.getSession(confDir);
		
		File errF = new File(Utils.getCurrentPath() + File.separator + ERR_FILE_NAME);
		if (errF.exists()) {
			errF.delete();
		}
		
		/** Get All SQL Files **/
		Collection<File> files = Utils.getFiles(dir);
		Iterator<File> itrFile = files.iterator();
		
		while(itrFile.hasNext()) {
			File f = itrFile.next();
			analysisSql(f);
			
		}
	}
	
	public static void analysisSql(File f) throws Exception {

		System.out.printf("-----------------------------------------------------------------------\n");
		System.out.printf("%s File Analysis \n", f.getAbsolutePath());
		System.out.printf("-----------------------------------------------------------------------\n");
		String sql = null;
		String sqlId = null;
		String curDir = Utils.getCurrentPath();

		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(curDir + File.separator + ERR_FILE_NAME, true));
		/** Get all sql statement from file **/
		try {
			List<String> sqlIds = Utils.getSqlStatement(f);
			List<String> tableNames = null;
			String serviceName = null;
			String command = null;
			
			for (int i = 0; i < sqlIds.size(); i++) {
				int idx = sqlIds.get(i).indexOf(".");
				serviceName = sqlIds.get(i).substring(0, idx);
				sqlId = sqlIds.get(i);
				sql = IbatisUtil.getSqlStatement(sqlId);										
				
				if (sql.contains("SELECT")) {
					tableNames = Utils.getSelectTableName(sql);
					command = "R";

				} else if (sql.contains("INSERT")) {
					tableNames = Utils.getInsertTableName(sql);
					command = "C";

				} else if (sql.contains("UPDATE")) {
					tableNames = Utils.getUpdateTableName(sql);
					command = "U";

				} else if (sql.contains("DELETE")) {
					tableNames = Utils.getDeleteTableName(sql);
					command = "D";
				}
				
				insertSvcTable(serviceName, command, tableNames);	
			}
			System.out.printf("-----------------------------------------------------------------------\n");
		} catch (Exception ex) {
			System.err.printf("Exception File : %s SQL : %s\n", f.getAbsolutePath(), sql);
			String logResult = String.format("%s sqlId in %s file", sqlId, f.getAbsolutePath());
			bos.write(logResult.getBytes());
			bos.write("\n".getBytes());
			bos.flush();
			ex.printStackTrace();
		}
		finally {
			bos.close();
		}
	}
	
	public static void insertSvcTable(String svcName, String command, List<String> tableNames) throws Exception {
		try {
			sqlClient.startTransaction();
			for (String tableName : tableNames) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("svcName", svcName);
				map.put("tbName", tableName);
				map.put("command", command);
				/** Check Row is already existed **/
				List<Integer> cnt = sqlClient.queryForList("crudMatrix.selSvcTable", map);
				
				
				if (cnt.get(0) <= 0) {
					sqlClient.insert("crudMatrix.insSvcTable", map);
					System.out.printf("%s table is added %s Service with %s \n", tableName, svcName, command);
					sqlClient.commitTransaction();
				} else {
					System.out.printf("%s table is already added in %s Service \n", tableName, svcName);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		sqlClient.endTransaction();

	}	
	
}
