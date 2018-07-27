/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SftpIService.java
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

import java.util.List;

import com.jcraft.jsch.ChannelSftp;

public interface SftpIService {
	/**
	 * 
	 *<pre>
	 * 1.Description: Return sftp channel
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param host sftp host name
	 * @param port sftp port
	 * @param username sftp username
	 * @param password sftp password
	 * @return ChannelSftp sftp channel
	 * @throws Exception
	 */
	public ChannelSftp getChannel(String host, int port, String username, String password) throws Exception;
	
	public ChannelSftp getChannelWithKey(String host, int port, String username, String key) throws Exception;

	public boolean upload(ChannelSftp channel, String localDir, String remoteDir, List<String> srcFileNames,
			List<String> targetFileNames) throws Exception;

	public boolean upload(ChannelSftp channel, String localDir, String remoteDir, List<String> fileNames)
			throws Exception;

	public boolean upload(ChannelSftp channel, String remoteDir, String localDir, String srcFileName,
			String targetFileName) throws Exception;

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
			String localFileName) throws Exception;

	/**
	 * 
	 *<pre>
	 * 1.Description: Close sftp channel
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param channel ChannelSftp channel to close
	 * @throws Exception
	 */
	public void disconnect(ChannelSftp channel) throws Exception;
}
