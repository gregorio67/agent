/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SSHIService.java
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

package batch.web.service.ssh;

import java.util.List;

import net.schmizz.sshj.SSHClient;

public interface SSHIService {
	public SSHClient getChannel(String host, String username, String password) throws Exception;
	
	public List<Boolean> execCommand(SSHClient ssh, List<String> commands) throws Exception;
	
	public boolean execCommand(SSHClient ssh, String command) throws Exception;
	
	public void disconnect(SSHClient ssh) throws Exception;
}
