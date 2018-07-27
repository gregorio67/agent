/**
 * Project Name :  스마트톨링정보시스템 구축
 * Class Name : BaseBootstrap.java
 * Description : 
 *
 * @author : LGCNS
 * @Since : 2017. 4. 20.
 *
 * @Copyright (c) 2018 EX All rights reserved.
 *-------------------------------------------------------------
 *              Modification Information
 *-------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------
 * 2018. 4. 26.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package cmn.deploy.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmn.deploy.util.PropertiesUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;


public class BaseBootstrap {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseBootstrap.class);
	
	private static final int SOCKET_READ_TIMEOUT = 3000;
	
	public BaseBootstrap() {
		
	}

	public static Bootstrap byteBootstrap(final EventLoopGroup group, final ByteClientHandler clientHandler) {
		Bootstrap bootstrap = new Bootstrap();
		
		final int soTimeout = PropertiesUtil.getInt("tcp.read.timeout") > 0 ? PropertiesUtil.getInt("tcp.read.timeout") : SOCKET_READ_TIMEOUT;			
		final int connectionTimeout = PropertiesUtil.getInt("tcp.connection.timeout") > 0 ? PropertiesUtil.getInt("tcp.connection.timeout") : SOCKET_READ_TIMEOUT;			

		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				ChannelPipeline pipeline = sc.pipeline();
				pipeline.addLast( new ByteArrayDecoder() );
				pipeline.addLast( new ByteArrayEncoder() );
				pipeline.addLast( new IdleStateHandler(20, 10, 0));
				pipeline.addLast(new ReadTimeoutHandler(soTimeout));					
				pipeline.addLast(clientHandler);
				
			}
		})
				// TCP Channel Option
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("byteBootstrap is generated :: {}, {}", connectionTimeout, soTimeout);
		}
		return bootstrap;
	}

}
