/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SQLRunable.java
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
 * 2018. 5. 9.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package dymn.sql.future;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import dymn.sql.util.Utils;

public class SQLRunable implements Runnable {

	private SqlSession sqlSession;
	private File f;
	
	public SQLRunable(SqlSession sqlSession, File f) {
		this.sqlSession = sqlSession;
		this.f = f;
	}
	
	@Override
	public void run() {
		try {
//			analysisSql();
			this.sqlSession.commit();															
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

//	public void analysisSql() throws Exception {
//		
//		try {				
//			System.out.printf("-----------------------------------------------------------------------\n");
//			System.out.printf("%s File Analysis \n", f.getAbsolutePath());
//			System.out.printf("-----------------------------------------------------------------------\n");
//
//			/** Get all sql statement from file **/
//			Map<String, Object> sqlMap = Utils.getSqlStatement(f);
//			String serviceName = sqlMap.get("serviceName") != null ? String.valueOf( sqlMap.get("serviceName")) : null;
//			
//			@SuppressWarnings("unchecked")
//			List<String> sqls = (List<String>) sqlMap.get("sqlStatements");
//			
//			List<String> tableNames = null;
//			
//			for (String sql : sqls) {
//				if (sql.contains("SELECT")) {
//					tableNames = Utils.getSelectTableName(sql);
//					insertSvcTable(serviceName, "R", tableNames);
//					
//				}
//				else if (sql.contains("INSERT")) {
//					tableNames = Utils.getInsertTableName(sql);
//					insertSvcTable(serviceName, "C", tableNames);
//					
//				}
//				else if (sql.contains("UPDATE")) {
//					tableNames = Utils.getUpdateTableName(sql);
//					insertSvcTable(serviceName, "U", tableNames);
//					
//				}
//				else if (sql.contains("DELETE")) {
//					tableNames = Utils.getDeleteTableName(sql);					
//					insertSvcTable(serviceName, "D", tableNames);
//				}
//			}
//			System.out.printf("-----------------------------------------------------------------------\n");
//		}
//		catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	/** 
	 * 
	 *<pre>
	 * 1.Description: Insert analysis data to SVC_TABLE
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param sqlSession
	 * @param svcName
	 * @param command
	 * @param tableNames
	 * @throws Exception
	 */
	public void insertSvcTable(String svcName, String command, List<String> tableNames) throws Exception {
		try {
			for (String tableName : tableNames) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("svcName", svcName);
				map.put("tbName", tableName);
				map.put("command", command);
				/** Check Row is already existed **/
				int cnt = sqlSession.selectOne("crudMatrix.selSvcTable", map);
				if (cnt <= 0) {
					this.sqlSession.insert("crudMatrix.insSvcTable", map);							
					System.out.printf("%s table is added %s Service with %s \n", tableName, svcName, command);
				}
				else {
					System.out.printf("%s table is already added in %s Service \n", tableName, svcName);
				}
			}			
		}
		catch(Exception ex) {
			
		}
		
	}
}
