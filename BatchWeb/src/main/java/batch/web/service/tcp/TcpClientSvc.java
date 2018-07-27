/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : TcpInterfaceServiceImpl.java
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import batch.web.exception.BizException;
import batch.web.tcp.BaseBootstrap;
import batch.web.tcp.ByteClientHandler;
import batch.web.util.PropertiesUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

@Service("tcpClientSvi")
public class TcpClientSvc implements TcpClientSvi {

	private static final Logger LOGGER = LoggerFactory.getLogger(TcpClientSvc.class);

	@Override
	public byte[] sendMessage(String host, int port, byte[] requestPacket, boolean rcvWait) throws Exception {
		/** Check input parameter **/
		if (host == null || requestPacket == null) {
			throw new BizException(" Server Information or send message is null");
		}
		
		long startTime = System.currentTimeMillis();
		
		/** Socket Channel **/
		Channel channel = null;
		final CountDownLatch latch = new CountDownLatch(1);

		/** String Handler **/
		ByteClientHandler clientHandler = null;

		/** Netty Event Loop Group **/
		final EventLoopGroup group = new NioEventLoopGroup();
		try {

			/** Create Client Handler **/
			clientHandler = new ByteClientHandler(requestPacket, rcvWait, latch);

			/** Create Client Bootstrap **/
			Bootstrap bootstrap = BaseBootstrap.byteBootstrap(group, clientHandler);

			int maxRetryCnt = PropertiesUtil.getInt("tcp.max.retry.count");
			
			int rcvTimeout = PropertiesUtil.getInt("tcp.receive.timeout");
			if (LOGGER.isDebugEnabled()) {
				LOGGER.info("Retry Count :: {}, Receive Timeout :: {}", maxRetryCnt, rcvTimeout);
			}

			/** If connect is failed, retry **/
			for (int retryCnt = 0; retryCnt < maxRetryCnt; retryCnt++) {
				try {
					channel = bootstrap.connect(host, port).sync().channel();
					break;
				} catch (Exception connectionError) {
					
					if (retryCnt == maxRetryCnt) {
						// if.tcp.client.maxretry
						throw connectionError;
					} else {
						LOGGER.error("[Client][ERROR] {}:{}Connection error occured. retry again {}/{}", host, port, retryCnt, maxRetryCnt);
						throw new BizException("Can't connect batch agent, Please check batch ageent is running");
					}
				}
			}
			
			/** If needed to get response, wait for response **/
			if (rcvWait) {
				if (!latch.await(rcvTimeout, TimeUnit.MILLISECONDS)) {
					channel.disconnect().sync();
					throw new TimeoutException("[Client][MSG RCV TIMEOUT] TCP 수신 대기 시간을 넘었습니다");
				}				
			}
		} 
		catch (Exception ex) {
			throw new BizException(ex.getMessage());
		} 
		finally {
			if (channel != null) {
				try {
					channel.closeFuture().sync();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Socket Channel is closed");
					}
				} 
				catch (InterruptedException interruptedEx) {
					LOGGER.error("Interrupted: {}", interruptedEx.getMessage());
				}
			}
			group.shutdownGracefully();
		}
		LOGGER.info("Service is successfully done :: {}, {}", host, port, System.currentTimeMillis() - startTime);
		return clientHandler != null ? clientHandler.getResponse() : null;			
	}

	
	@Override
	public Map<String, Object> sendMessage(String host, int port, Map<String, Object> requestPacket, boolean rcvWait) throws Exception {
		return null;
	}
	
	@Override
	public String sendMessage(String host, int port, String requestPacket, boolean rcvWait) throws Exception {
		return null;
	}

}
