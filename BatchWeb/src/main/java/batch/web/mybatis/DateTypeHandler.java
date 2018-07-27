/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : DateTypeHandler.java
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
 * 2018. 6. 8.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class DateTypeHandler implements TypeHandler<String>{

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
		Date curDate = new Date();
		Timestamp ts = new Timestamp(curDate.getTime());
		ps.setTimestamp(i, ts);
		
	}

	@Override
	public String getResult(ResultSet rs, String columnName) throws SQLException {
		Date date = rs.getDate(columnName);
		String formatDate = "";
		if (date != null) {
			formatDate = sdf.format(date);			
		}
		
		return formatDate;
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		Date date = rs.getDate(columnIndex);
		String formatDate = "";
		if (date != null) {
			formatDate = sdf.format(date);			
		}
		
		return formatDate;
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
		Timestamp ts = cs.getTimestamp(columnIndex);
		String formatDate = sdf.format(ts.getTime());
		return formatDate;
	}

}
