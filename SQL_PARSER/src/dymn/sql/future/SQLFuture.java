/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SQLFuture.java
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
import java.util.function.Supplier;

import org.apache.ibatis.session.SqlSession;

import dymn.sql.util.MybatisUtil;
import dymn.sql.util.Utils;

public class SQLFuture implements Supplier<Void> {

	private SqlSession sqlSession;
	private File f;

	public SQLFuture(SqlSession sqlSession, File f) {
		this.sqlSession = sqlSession;
		this.f = f;
	}

	@Override
	public Void get() {
		try {
			analysisSql();
			this.sqlSession.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void analysisSql() throws Exception {

		System.out.printf("-----------------------------------------------------------------------\n");
		System.out.printf("%s File Analysis \n", f.getAbsolutePath());
		System.out.printf("-----------------------------------------------------------------------\n");
		String sql = null;

		/** Get all sql statement from file **/
		try {
			List<String> sqlIds = Utils.getSqlStatement(f);
			List<String> tableNames = null;
			String serviceName = null;
			
			for (int i = 0; i < sqlIds.size(); i++) {
				int idx = sqlIds.get(i).indexOf("\\.");
				serviceName = sqlIds.get(i).substring(0, idx);
				sql = MybatisUtil.getSqlStatement(sqlIds.get(i));
				
				if (sql.contains("SELECT")) {
					tableNames = Utils.getSelectTableName(sql);
					insertSvcTable(serviceName, "R", tableNames);

				} else if (sql.contains("INSERT")) {
					tableNames = Utils.getInsertTableName(sql);
					insertSvcTable(serviceName, "C", tableNames);

				} else if (sql.contains("UPDATE")) {
					tableNames = Utils.getUpdateTableName(sql);
					insertSvcTable(serviceName, "U", tableNames);

				} else if (sql.contains("DELETE")) {
					tableNames = Utils.getDeleteTableName(sql);
					insertSvcTable(serviceName, "D", tableNames);
				}
			}
			System.out.printf("-----------------------------------------------------------------------\n");
		} catch (Exception ex) {
			System.err.printf("Exception File : %s SQL : %s\n", f.getAbsolutePath(), sql);
			ex.printStackTrace();
		}
	}
	
//	public void analysisSql() throws Exception {
//
//		System.out.printf("-----------------------------------------------------------------------\n");
//		System.out.printf("%s File Analysis \n", f.getAbsolutePath());
//		System.out.printf("-----------------------------------------------------------------------\n");
//		String sql = null;
//
//		/** Get all sql statement from file **/
//		try {
////			Map<String, Object> sqlMap = Utils.getSqlStatement(f);
//			List<String> sqlMap = Utils.getSqlStatement(f);
//			String serviceName = sqlMap.get("serviceName") != null ? String.valueOf(sqlMap.get("serviceName")) : null;
//
//			@SuppressWarnings("unchecked")
//			List<String> sqls = (List<String>) sqlMap.get("sqlStatements");
//
//			List<String> tableNames = null;
//			
//			for (int i = 0; i < sqls.size(); i++) {
//				sql = sqls.get(i);
//				if (sql.contains("SELECT")) {
//					tableNames = Utils.getSelectTableName(sql);
//					insertSvcTable(serviceName, "R", tableNames);
//
//				} else if (sql.contains("INSERT")) {
//					tableNames = Utils.getInsertTableName(sql);
//					insertSvcTable(serviceName, "C", tableNames);
//
//				} else if (sql.contains("UPDATE")) {
//					tableNames = Utils.getUpdateTableName(sql);
//					insertSvcTable(serviceName, "U", tableNames);
//
//				} else if (sql.contains("DELETE")) {
//					tableNames = Utils.getDeleteTableName(sql);
//					insertSvcTable(serviceName, "D", tableNames);
//				}
//			}
//			System.out.printf("-----------------------------------------------------------------------\n");
//		} catch (Exception ex) {
//			System.err.printf("Exception File : %s SQL : %s\n", f.getAbsolutePath(), sql);
//			ex.printStackTrace();
//		}
//	}

	/**
	 * 
	 * <pre>
	 * 1.Description: Insert analysis data to SVC_TABLE
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 * </pre>
	 * 
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
				} else {
					System.out.printf("%s table is already added in %s Service \n", tableName, svcName);
				}
			}
		} catch (Exception ex) {

		}

	}
}
