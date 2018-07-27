/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SftpService.java
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

package batch.web.service.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import batch.web.exception.BizException;
import batch.web.util.CacheMap;
import batch.web.util.FileUtil;
	
@Service("sftpService")
public class SftpService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SftpService.class);

	private static CacheMap<String, ChannelSftp> sftpClients = new CacheMap<String, ChannelSftp>(20);
	

	public ChannelSftp getChannel(String host, int port, String username, String password) throws Exception {

		String sftpKey = host + ":" + port + ":" + username;

		ChannelSftp channelSftp = null;
		channelSftp = sftpClients.get(sftpKey);

		if (channelSftp != null) {
			if (!channelSftp.isClosed() && channelSftp.isConnected()) {
				return channelSftp;				
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SFTP Connection Info :: {}, {}, {}, {}", host, port, username, password);
		}
		
		synchronized(sftpClients) {
			try {
				Channel channel = null;

				JSch jsch = new JSch();
				
				Session session = null;
				session = jsch.getSession(username, host, port);
				session.setPassword(password);

				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
		
				session.setConfig(config);
				session.connect();

				channel = session.openChannel("sftp");
				
				channel.connect();
				channelSftp = (ChannelSftp) channel;
				
				sftpClients.put(sftpKey, channelSftp);
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SFTP Channel is created :: {}, {}, {}, {}", host, port, username, password);
		}
		return channelSftp;
	}

	
	public ChannelSftp getChannelWithKey(String host, int port, String username, String key) throws Exception {

		String sftpKey = host + ":" + port + ":" + key;

		ChannelSftp channelSftp = null;
		channelSftp = sftpClients.get(sftpKey);

		if (channelSftp != null) {
			if (!channelSftp.isClosed() && channelSftp.isConnected()) {
				return channelSftp;				
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SFTP Connection Info :: {}, {}, {}, {}", host, port, username, key);
		}
		
		synchronized(sftpClients) {
			try {

				JSch jsch = new JSch();
				jsch.addIdentity(key);
				
				Session session = null;
				session = jsch.getSession(username, host, port);

				/** Insecure mode **/
				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.connect();

				Channel channel = session.openChannel("sftp");
				
				channel.connect();
				channelSftp = (ChannelSftp) channel;
				
				sftpClients.put(sftpKey, channelSftp);
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SFTP Channel is created :: {}, {}, {}, {}", host, port, username, key);
		}
		return channelSftp;
	}
	
	public boolean upload(ChannelSftp channel, String localDir, String remoteDir, List<String> srcFileNames,
			List<String> targetFileNames) throws Exception {
		if (srcFileNames.size() != targetFileNames.size()) {
			throw new BizException("Soruce File and target File is not match");
		}

		int idx = 0;
		for (String fileName : srcFileNames) {
			upload(channel, remoteDir, localDir, fileName, targetFileNames.get(idx));
		}

		return true;
	}

	public boolean upload(ChannelSftp channel, String localDir, String remoteDir, List<String> fileNames)
			throws Exception {
		for (String fileName : fileNames) {
			upload(channel, remoteDir, localDir, fileName, fileName);
		}
		return true;
	}

	public boolean upload(ChannelSftp channel, String remoteDir, String localDir, String srcFileName,
			String targetFileName) throws Exception {
		boolean result = true;
		FileInputStream in = null;
		try {
			File file = new File(localDir + File.separator + srcFileName);
			if (!file.exists()) {
				throw new BizException(String.format("Local File Not Found :: %s", localDir));
			}

			in = new FileInputStream(file);

			// channel.cd(remoteDir);

			/** Move file from local to remote **/
			channel.put(in, remoteDir + File.separator + targetFileName);
			LOGGER.info("{} is uploaded from {} {}", localDir, remoteDir, targetFileName);

		} catch (Exception e) {
			result = false;
			LOGGER.error(e.getMessage());
			throw new BizException(e.getMessage());
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				throw new BizException(e.getMessage());
			}
		}

		return result;
	}

	/**
	 * 
	 * <pre>
	 * 1.Description: Ftp download file
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 * </pre>
	 * 
	 * @param channel
	 *            SftpChannel for download file
	 * @param remoteDir
	 *            remote directory
	 * @param remoteFileName
	 *            remote file name
	 * @param localPath
	 *            local directory
	 * @param localFileName
	 *            local file name
	 * @throws Exception
	 */
	public boolean download(ChannelSftp channel, String remoteDir, String remoteFileName, String localPath,
			String localFileName) throws Exception {
		InputStream in = null;
		FileOutputStream out = null;

		/** Check local directory **/
		FileUtil.makeDir(localPath);

		try {

			/** Move to remote directory **/
			channel.cd(remoteDir);
			/** Read remote file **/
			in = channel.get(remoteFileName);
			if (in == null) {
				throw new BizException(String.format("File Not Found :: [%s]", remoteFileName));
			}
		} catch (SftpException e) {
			LOGGER.error(e.getMessage());
			throw new BizException(e.getMessage());
		}

		try {

			String downloadFileName = localPath + File.separator + localFileName;
			FileUtil.makeDir(downloadFileName);

			out = new FileOutputStream(new File(downloadFileName));
			/** Write remote file to local file **/
			IOUtils.copy(in, out);

			LOGGER.info("{} is downloaded to {}", remoteDir, localPath);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new BizException(e.getMessage());
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				throw new BizException(e.getMessage());
			}
		}

		return true;
	}

	public void disconnect(ChannelSftp channel) throws Exception {
		if (channel.isConnected()) {
			channel.disconnect();
		}
	}
	
	public void disconnects() throws Exception {
		Iterator<Entry<String, ChannelSftp>> sftpItr = sftpClients.getAll().iterator();
		while(sftpItr.hasNext()) {
			Entry<String, ChannelSftp> entry = sftpItr.next();
			ChannelSftp sftp = entry.getValue();
			if (sftp != null && sftp.isConnected()) {
				sftp.disconnect();
			}
		}
	}

}
