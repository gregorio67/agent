/*------------------------------------------------------------------------------
 * PROJ   : UPLUS 해외 송금 프로젝트
 * NAME   : TCPServer.java
 * DESC   : Netty 기반 KB 연계 TCP 서버
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

package batch.agent.tcp;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;

public class TCPServer {

	private static final Logger LOGGER = LoggerFactory.getLogger( TCPServer.class );

	private Channel serverChannel;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

	private ChannelInitializer<Channel> channelInitializer;

	/**
	 * TCP 서버 포트
	 */
	private int port;

	/**
	 * Keepalive 여부
	 */
	private boolean keepAlive;

	/**
	 * TCP Backlog Count
	 */
	private int backlog;

	
	/**
	 * Netty boss group thread count
	 */
	private int bossThreadCount;
	
	
	/**
	 * Netty worker group thread count
	 */
	private int workerThreadCount;

	/**
	 * @param channelInitializer the channelInitializer to set
	 */
	public void setChannelInitializer( ChannelInitializer<Channel> channelInitializer ) {
		this.channelInitializer = channelInitializer;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort( int port ) {
		this.port = port;
	}

	/**
	 * @param keepAlive the keepAlive to set
	 */
	public void setKeepAlive( boolean keepAlive ) {
		this.keepAlive = keepAlive;
	}

	/**
	 * @param backlog the backlog to set
	 */
	public void setBacklog( int backlog ) {
		this.backlog = backlog;
	}

	/**
	 * @param workerThreadCount
	 */
    public void setWorkerThreadCount(int workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
    }

    /**
     * @param bossThreadCount
     */
    public void setBossThreadCount(int bossThreadCount) {
        this.bossThreadCount = bossThreadCount;
    }
    
	/**
	 * <PRE>
	 * TCP 서버 시작
	 * </PRE>
	 */
	public void initialize() {
//		EventLoopGroup bossGroup = null;
//		EventLoopGroup workerGroup = null;

		try {
            bossGroup = new NioEventLoopGroup(bossThreadCount, Executors.defaultThreadFactory());
            workerGroup = new NioEventLoopGroup(workerThreadCount, Executors.defaultThreadFactory());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("boss thread count :: " + bossThreadCount + " - work thread count ::" + workerThreadCount);      
            }

			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group( bossGroup, workerGroup )
					.channel( NioServerSocketChannel.class )
					.handler( new LoggingHandler( TCPServer.class, LogLevel.DEBUG ) )
					.childHandler( channelInitializer )
					// TCP Channel Option
					.option( ChannelOption.SO_BACKLOG, backlog )
					.option( ChannelOption.SO_KEEPALIVE, keepAlive )
					.option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_LINGER, 0)
					.childOption( ChannelOption.SO_KEEPALIVE, true )
					.childOption(ChannelOption.SO_LINGER, 0)
					.childOption(ChannelOption.SO_RCVBUF, 1048576)
					.childOption(ChannelOption.SO_SNDBUF, 1048576);

			LOGGER.info("Server {} port listen ", this.port);
			serverChannel = bootstrap.bind( new InetSocketAddress( port ) ).sync().channel().closeFuture().channel();
			// .sync() 
		} catch ( Exception nettyServerError ) {
			LOGGER.error( "Netty TCP Server Initialize Failed: ", nettyServerError );

			if ( bossGroup != null ) {
				bossGroup.shutdownGracefully();
			}
			if ( workerGroup != null ) {
				workerGroup.shutdownGracefully();
			}
		}
	}

	/**
	 * Bean Destory Event Trigger
	 */
	@PreDestroy
	public void stop() {
	    
	    
        LOGGER.info( "Netty Server Stop starting with port :: "  + port);
        try {
            if (serverChannel != null) {
                serverChannel.close().sync();                            
                LOGGER.info("Server Channel stopped");
                
                if ( serverChannel.parent() != null ) {
                    serverChannel.parent().close(); 
                    LOGGER.info("Parent of server channel stopped");
                }
            }
        }
        catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            Future<?> fc = bossGroup.shutdownGracefully();
            Future<?> fw = workerGroup.shutdownGracefully();
            try {
                fc.await();
                fw.await();                
                LOGGER.info("bossGroup and workGroup stopped successfully");
            }
            catch(InterruptedException ie) {
                throw new RuntimeException(ie);
            }
        }

	    
//		if ( serverChannel != null ) {
//		    serverChannel.closeFuture();
//			serverChannel.close();
//			if ( serverChannel.parent() != null ) {
//				serverChannel.parent().close();	
//			}
//		}
	}
}