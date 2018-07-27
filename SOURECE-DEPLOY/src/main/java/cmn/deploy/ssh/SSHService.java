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

package cmn.deploy.ssh;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cmn.deploy.exception.BizException;
import cmn.deploy.util.CacheMap;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

@Service("sshService")
public class SSHService implements SSHIService{
	private static final Logger LOGGER = LoggerFactory.getLogger(SSHService.class);
	
	private static CacheMap<String, SSHClient> sshClients = new CacheMap<String, SSHClient>(20);

	private static final int DEFAULT_SSH_PORT = 22;

	private int connectionTimeout = 30000;
	
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
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
		return getChannel(host, DEFAULT_SSH_PORT, username, password);
	}
	public SSHClient getChannel(String host, int port, String username, String password) throws Exception {
		
		SSHClient ssh = null;
		String sshKey = host + ":" + username;
		if (sshClients.get(sshKey) != null) {
			ssh = sshClients.get(sshKey);
			if (ssh.isConnected()) {
				return ssh;
			}
		}

		/** Create new SSH Client **/
		synchronized(sshClients) {
			
			ssh = new SSHClient();
			ssh.addHostKeyVerifier(  
				    new HostKeyVerifier() {  
				        public boolean verify(String arg0, int arg1, PublicKey arg2) {  
				            return true;  // don't bother verifying  
				        }  
				    }  
				); 
			ssh.connect(host, port);
			ssh.authPassword(username, password);
			ssh.setConnectTimeout(connectionTimeout);
			sshClients.put(sshKey, ssh);
		}
		return ssh;		
	}
	
	public SSHClient getChannelWithKey(String host, String username, String keyPath) throws Exception {
		return getChannelWithKey(host, DEFAULT_SSH_PORT, username, keyPath);
	}
	public SSHClient getChannelWithKey(String host, int port, String username, String keyPath) throws Exception {
		
		SSHClient ssh = null;
		String sshKey = host + ":" + username;

		if (sshClients.get(sshKey) != null) {
			ssh = sshClients.get(sshKey);
			if (ssh.isConnected()) {
				return ssh;
			}
		}
		
		/** Create new SSH Client **/
		synchronized(sshClients) {			
			ssh = new SSHClient();
			ssh.addHostKeyVerifier(  
				    new HostKeyVerifier() {  
				        public boolean verify(String arg0, int arg1, PublicKey arg2) {  
				            return true;  // don't bother verifying  
				        }  
				    }  
				); 
			
			ssh.setConnectTimeout(connectionTimeout);
			ssh.connect(host, port);

			KeyProvider keyProviders = ssh.loadKeys(keyPath);
			ssh.authPublickey(username, keyProviders);
			sshClients.put(sshKey, ssh);
		}
		return ssh;		
	}	

	public List<String> execCommand(SSHClient ssh, List<String> commands) throws Exception {
		List<String> result = new ArrayList<String>();
		for (String command : commands) {
			result.add(command);
			List<String> lists = execCommand(ssh, command);
			for(String str : lists) {
				result.add(str);
			}
			result.add("--------------------------------------------------------");
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
	public List<String> execCommand(SSHClient ssh, String command) throws Exception {
				
		Session session = null;
		List<String> result = new ArrayList<String>();
		try {
			session = ssh.startSession();			
			Command cmd = session.exec(command);
			String data = readCommandOutput(cmd);
			result.add(data);
//			result = IOUtils.readLines(cmd.getInputStream());
			if (cmd.getExitStatus() != 0) {
				LOGGER.error("{} command failed because {}", command, cmd.getExitErrorMessage());
			}			
		}
		catch(Exception ex) {
			LOGGER.error(ex.getMessage());
//			ex.printStackTrace();
			throw new BizException(ex.getMessage());
		}
		finally {
			if (session != null) session.close();
		}
		
		return result;

	}
	
    private static String readCommandOutput(Command cmd) throws IOException {
        byte[] tmp = new byte[1024];
        InputStream is = cmd.getInputStream();
        StringBuilder sb = new StringBuilder();
        while (true) {
            while (is.available() > 0) {
                int i = is.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                sb.append(new String(tmp, 0, i));
                System.out.print(new String(tmp, 0, i));
            }
            if (!cmd.isOpen()) {
                if (is.available() > 0) {
                    continue;
                }
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
            }
        }
        return sb.toString();
    }	
	
	public void uploadFile(SSHClient ssh, String localPath, String remotePath) throws Exception {
		ssh.useCompression();
		ssh.newSCPFileTransfer().upload(localPath, remotePath);
	}
	
	public void downloadFile(SSHClient ssh, String localPath, String remoteFile) throws Exception {
		ssh.useCompression();
		ssh.newSCPFileTransfer().download(remoteFile, localPath);
	}
	
	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: disconnect ssh client 
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @throws Exception
	 */
	public void disconnect(SSHClient ssh) throws Exception {
		Iterator<Entry<String, SSHClient>> sshItr = sshClients.getAll().iterator();
		while(sshItr.hasNext()) {
			Entry<String, SSHClient> entry = sshItr.next();
			SSHClient ssh1 = entry.getValue();
			if (ssh == ssh1) {
				sshClients.remove(entry.getKey());
				if (ssh != null && ssh.isConnected()) {
					ssh.disconnect();
				}
				break;
			}
		}	
	}
	
	public void disconnects() throws Exception {
		Iterator<Entry<String, SSHClient>> sshItr = sshClients.getAll().iterator();
		while(sshItr.hasNext()) {
			Entry<String, SSHClient> entry = sshItr.next();
			SSHClient ssh = entry.getValue();
			sshClients.remove(entry.getKey());
			if (ssh != null && ssh.isConnected()) {
				ssh.disconnect();
			}
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		SSHService sshService = new SSHService();
		SSHClient ssh1 = sshService.getChannel("34.247.222.144", "developer", "sehati2018!");
		SSHClient ssh2 = sshService.getChannelWithKey("10.255.116.51", "ec2-user", "D:/01.Projects/Bharein/AWS/hira-dev.pem");

		System.out.println(sshService.execCommand(ssh2, "pwd"));
		List<String> list1 = sshService.execCommand(ssh2, "ls -al");
		for (String str : list1) {
			System.out.println(str);
		}
		
		
		sshService.downloadFile(ssh2, "D:/temp/ibm2.war", "/engn001/ciserv/hudson/jobs/SEHATI-IBM-DEV/workspace/ibm.war");
		sshService.uploadFile(ssh1, "D:/temp/ibm2.war", "/home/developer/ibm2.war");
		List<String> list = sshService.execCommand(ssh1, "ls -al");
		for (String str : list) {
			System.out.println(str);
		}
		sshService.disconnect(ssh1);
	}
}
