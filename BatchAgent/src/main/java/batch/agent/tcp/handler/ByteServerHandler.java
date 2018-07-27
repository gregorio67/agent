/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : ByteServerHandler.java
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
 * 2018. 7. 6.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.agent.tcp.handler;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.agent.context.BatchContext;
import batch.agent.service.AgentAService;
import batch.agent.util.JsonUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
@ChannelHandler.Sharable
public class ByteServerHandler extends SimpleChannelInboundHandler<byte[]> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( DefaultServerHandler.class );

	@Override
	protected void channelRead0( ChannelHandlerContext ctx, byte[] msg ) throws Exception {
		LOGGER.info( "[Server][MSG RCV] {}", new String( msg, StandardCharsets.UTF_8 ) );

		Map<String, Object> params = JsonUtil.json2Map(new String(msg));
		String reqService = params.get("service") != null ? String.valueOf(params.get("service")) : null;
		String responseYn = params.get("responseYn") != null ? String.valueOf(params.get("responseYn")) : "Y";
		
		if (reqService == null) {
			Map<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("status", "F");
			msgMap.put("message", "Service is null");
			
			ctx.channel().writeAndFlush(JsonUtil.map2Json(msgMap).getBytes());
			return;
		}
		/** Call Service **/
		LOGGER.info("Called {} service ", reqService);
		AgentAService<byte[], Map<String, Object>> service = BatchContext.getBean( reqService );

		/** Response wait or not **/
		if ("Y".equals(responseYn)) {
			byte[] result = service.process(params);			
			if (result != null) {
				ctx.channel().writeAndFlush( result );
			}
		}
		else {
			service.process(params);
		}
		
//		Method method = ReflectionUtils.findMethod( service.getClass(), methodName, byte[].class );
//
//		/** Call Method **/
//		if ( method.getGenericReturnType().equals( Void.TYPE ) ) {
//			ReflectionUtils.invokeMethod( method, service, msg );
//		} else {
//			byte[] rcvMsg = (byte[])ReflectionUtils.invokeMethod( method, service, msg );
//			ctx.channel().writeAndFlush( rcvMsg );
//		}
	}

	@Override
	public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
		LOGGER.error( "[Server][ERROR] ", cause );
	}
}
