
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


/** Sharable Annotation create only one handler instance **/
/** The channel handler should be safely shared with channels **/
/** If you don't use this annotation, you should create a new handler instance and add to pipe line**/
@ChannelHandler.Sharable
public class DefaultServerHandler extends SimpleChannelInboundHandler<byte[]> {

	private static final Logger LOGGER = LoggerFactory.getLogger( DefaultServerHandler.class );

	@Override
	protected void channelRead0( ChannelHandlerContext ctx, byte[] msg ) throws Exception {
		LOGGER.info( "[Server][MSG RCV] {}", new String( msg, StandardCharsets.UTF_8 ) );

		Map<String, Object> params = JsonUtil.json2Map(new String(msg));
		String reqService = params.get("service") != null ? String.valueOf(params.get("service")) : null;
		
		if (reqService == null) {
			Map<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("status", "F");
			msgMap.put("message", "Service is null");
			
			ctx.channel().writeAndFlush(JsonUtil.map2Json(msgMap).getBytes());
			return;
		}
		/** Call Service **/
		LOGGER.info("Called {} service ", reqService);
		
		AgentAService<byte[], byte[]> service = BatchContext.getBean( reqService );
		
		byte[] result = service.process(msg);
		if (result != null) {
			ctx.channel().writeAndFlush( result );
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
