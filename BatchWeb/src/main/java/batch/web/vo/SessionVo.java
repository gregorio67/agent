/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SessionVo.java
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
 * 2018. 5. 14.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.vo;

import java.io.Serializable;

public class SessionVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String userId;
	
	String language;
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	
	

}
