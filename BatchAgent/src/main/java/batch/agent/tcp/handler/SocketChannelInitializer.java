/*------------------------------------------------------------------------------
 * PROJ   : UPLUS 해외 송금 프로젝트
 * NAME   : SocketChannelInitializer.java
 * DESC   : TCP Socket 채널 초기화 클래스
 * Author : 윤순혁
 * VER    : 1.0
 * Copyright 2016 LG CNS All rights reserved
 *------------------------------------------------------------------------------
 *                  변         경         사         항                       
 *------------------------------------------------------------------------------
 *    DATE       AUTHOR                      DESCRIPTION                        
 * ----------    ------  --------------------------------------------------------- 
 * 2016. 12. 6.  윤순혁  최초 프로그램 작성                                     
 */

package batch.agent.tcp.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * <PRE>
 * TCP Socket 채널 초기화 클래스
 * </PRE>
 *
 * @author    윤순혁
 * @version   1.0
 * @see       ChannelInitializer
 */
public class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {

	private ChannelInboundHandlerAdapter handlerAdapter;

	public void setHandlerAdapter(ChannelInboundHandlerAdapter handlerAdapter) {
		this.handlerAdapter = handlerAdapter;
	}

	/**
	 * @param defaultServerHandler the defaultServerHandler to set
	 */

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	@Override
	protected void initChannel( SocketChannel socketChannel ) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();

		// the encoder and decoder (byte array : not sharable)
		pipeline.addLast( new ByteArrayDecoder() );
		pipeline.addLast( new ByteArrayEncoder() );

		pipeline.addLast( handlerAdapter );
	}
}
