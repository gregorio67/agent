/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : TcpInterfaceService.java
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
 * 2018. 4. 27.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.tcp;

import java.util.Map;

public interface TcpClientSvi {
	
	public String sendMessage(String host, int port, final String requestPacket, final boolean rcvWait) throws Exception;

	public byte[] sendMessage(String host, int port, final byte[] requestPacket, final boolean rcvWait) throws Exception;

	public Map<String, Object> sendMessage(String host, int port, final Map<String, Object> requestPacket, final boolean rcvWait) throws Exception;

}
