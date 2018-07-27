/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : AntIService.java
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
 * 2018. 6. 25.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.ant;

public interface AntIService {

	public boolean buildSource(String sourceDir, String targetDir, String libPath) throws Exception;
}