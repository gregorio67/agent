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
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class MybatisUtil {
	
	private static SqlSessionFactory sessionFactory = null;
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
	public static SqlSession getSession(String conf) throws Exception {
		if (conf == null) {
			conf = "mybatis_conf.xml";
		}
//		Reader reader = Resources.getResourceAsReader(conf);
		Reader reader = new InputStreamReader(new FileInputStream(conf));
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		sessionFactory = builder.build(reader);
		SqlSession session = sessionFactory.openSession();

		return session;
	
	}
	
	
	public static String getSqlStatement(String sqlId) throws Exception {
		String sql = null;
		try {
			
			Configuration configuration = sessionFactory.getConfiguration();
			MappedStatement statement = configuration.getMappedStatement(sqlId);
			Map<String, Object> params = new HashMap<String, Object>();
			BoundSql boundSql = statement.getBoundSql(params);
			sql = boundSql.getSql();
			
			sql = sql.replaceAll("\t", "").replaceAll("\n", " ");
			sql = Utils.removeSpace(sql);
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return sql;
	}
	

	
	public static void main(String[] args) throws Exception {
		getSession("C:/STIS/workspace/SQL_PARSER/ibatis_conf.xml");
		String sql = getSqlStatement("apiCm.selApiCmCode");
		System.out.println(sql);
	}
}
