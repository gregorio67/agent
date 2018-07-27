/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : MybatisUtil.java
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

package dymn.sql.util;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;


public class IbatisUtil {
	
	 private static SqlMapClient sqlClient = null;

	/**
	 * 
	 *<pre>
	 * 1.Description: Get SqlSession
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param conf
	 * @return
	 * @throws Exception
	 */
	public static SqlMapClient getSession(String conf) throws Exception {
		if (conf == null) {
			conf = "ibatis_conf.xml";
		}
//		Reader reader = Resources.getResourceAsReader(conf);
		Reader reader = new InputStreamReader(new FileInputStream(conf));
		sqlClient = SqlMapClientBuilder.buildSqlMapClient(reader);
		return sqlClient;
	}
	

	public static String getSqlStatement(String sqlId) throws Exception {
		String resultSql = "";
	    
	    MappedStatement mappedStatement;
	    StatementScope statementScope;
	    SessionScope sessionScope;
	    Sql sql ;
	   try {
		    mappedStatement = ((ExtendedSqlMapClient)sqlClient).getMappedStatement(sqlId);
			   
		    sessionScope = new SessionScope();
		    statementScope = new StatementScope(sessionScope);	   
		    mappedStatement.initRequest(statementScope);
		    sql = mappedStatement.getSql();
		   
		   
		    resultSql = sql.getSql(statementScope, new HashMap<String, Object>());
			
		    resultSql = resultSql.replaceAll("\t", "").replaceAll("\n", " ");
		    resultSql = Utils.removeSpace(resultSql);		   
	   }
	   catch(Exception ex) {
		   ex.printStackTrace();
	   }
	    
	   return resultSql;
	    
	}
	
	public static void main(String[] args) throws Exception {
		getSession("C:/STIS/workspace/SQL_PARSER/ibatis_conf.xml");
		String sql = getSqlStatement("ViaColInspIADAO.selectViaVerfColInsList");
		System.out.println(sql);
	}
}
