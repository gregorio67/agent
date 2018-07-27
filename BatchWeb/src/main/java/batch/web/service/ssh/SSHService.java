/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SSHService.java
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

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import batch.web.exception.BizException;
import batch.web.util.CacheMap;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

@Service("sshService")
public class SSHService implements SSHIService{
	private static final Logger LOGGER = LoggerFactory.getLogger(SSHService.class);
	
	private static CacheMap<String, SSHClient> sshClients = new CacheMap<String, SSHClient>(20);
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Connect SSH server
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param host host IP address
	 * @param username server account
	 * @param password server password
	 * @return SSHClient
	 * @throws Exception
	 */
	public SSHClient getChannel(String host, String username, String password) throws Exception {
		
		SSHClient ssh = null;
		String sshKey = host + ":" + username;
		if (sshClients.get(sshKey) != null) {
			ssh = sshClients.get(sshKey);
			if (ssh.isConnected()) {
				return ssh;
			}
		}

		synchronized(sshClients) {
			
			ssh = new SSHClient();
			ssh.addHostKeyVerifier(  
				    new HostKeyVerifier() {  
				        public boolean verify(String arg0, int arg1, PublicKey arg2) {  
				            return true;  // don't bother verifying  
				        }  
				    }  
				); 
//			ssh.loadKnownHosts();
			ssh.connect(host);
//			ssh.authPublickey(username);
			ssh.authPassword(username, password);

			sshClients.put(sshKey, ssh);
		}
		return ssh;		
	}
	
	public SSHClient getChannelWithKey(String host, String username, String key) throws Exception {
		
		SSHClient ssh = null;
		String sshKey = host + ":" + username;

		if (sshClients.get(sshKey) != null) {
			ssh = sshClients.get(sshKey);
			if (ssh.isConnected()) {
				return ssh;
			}
		}

		synchronized(sshClients) {			
			ssh = new SSHClient();
			ssh.addHostKeyVerifier(  
				    new HostKeyVerifier() {  
				        public boolean verify(String arg0, int arg1, PublicKey arg2) {  
				            return true;  // don't bother verifying  
				        }  
				    }  
				); 
			
			KeyProvider keyProviders = ssh.loadKeys(new File(key).getPath());
			ssh.authPublickey(username, keyProviders);
//			ssh.loadKnownHosts();
			ssh.connect(host);
//			ssh.authPublickey(username);
//			ssh.authPassword(username, password);

			sshClients.put(sshKey, ssh);
		}
		return ssh;		
	}	

	public List<Boolean> execCommand(SSHClient ssh, List<String> commands) throws Exception {
		List<Boolean> result = new ArrayList<Boolean>();
		for (String command : commands) {
			result.add(execCommand(ssh, command));
		}
		return result;
	}
	/**
	 * 
	 *<pre>
	 * 1.Description: Execute command
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param ssh SSHClient
	 * @param command command 
	 * @return result of command execution
	 * @throws Exception
	 */
	public boolean execCommand(SSHClient ssh, String command) throws Exception {
				
		Session session = null;
		List<String> result = null;
		boolean isSuccess = false;
		try {
			session = ssh.startSession();			
			Command cmd = session.exec(command);

			 result = IOUtils.readLines(cmd.getInputStream());
			 cmd.join();
			 if (cmd.getExitStatus() != 0) {
				 LOGGER.error("{} is failed with message {}", command, cmd.getExitErrorMessage());
			 }
			 isSuccess = true;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("{} : {}", command, result);
			}			
		}
		catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new BizException(ex.getMessage());
		}
		finally {
			if (session != null) session.close();
		}
		
		return isSuccess;

	}
	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: disconnect sshclient 
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @throws Exception
	 */
	public void disconnect(SSHClient ssh) throws Exception {
		if (ssh != null && ssh.isConnected()) {
			ssh.disconnect();
		}
	}
	
	public void disconnects() throws Exception {
		Iterator<Entry<String, SSHClient>> sshItr = sshClients.getAll().iterator();
		while(sshItr.hasNext()) {
			Entry<String, SSHClient> entry = sshItr.next();
			SSHClient ssh = entry.getValue();
			if (ssh != null && ssh.isConnected()) {
				ssh.disconnect();
			}
		}
	}
}
