/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : PageBean.java
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
 * 2018. 5. 28.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.util;

public class PageBean {

	/** Main Page **/
	private String mainPage;
	
	/** Current Page **/
	private String curPage;
	

	public String getMainPage() {
		return mainPage;
	}

	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}

	public String getCurPage() {
		return curPage;
	}

	public void setCurPage(String curPage) {
		synchronized(this) {
			this.curPage = curPage;			
		}
	}
	
}
