package com.xlmine.netty.client.initializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;

import com.xlmine.netty.client.message.HttpRequestMessage;
import com.xlmine.netty.client.message.HttpResponseMessage;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解码response
 */
public class HttpResponseDecoder extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(HttpResponseDecoder.class);

    private ByteBuf buf = Unpooled.buffer(4096); // 初始化
    private int statuscode;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //TODO 处理statuscode为301的情况
        if (msg instanceof HttpResponse) {
            HttpResponse httpResponse = (HttpResponse) msg;
            statuscode = httpResponse.getStatus().code();
//            for (Entry<String, String> header : httpResponse.headers()) {
//                System.out.println(header.getKey() + ":" + header.getValue());
//            }
        }

        if (msg instanceof HttpContent) {

            HttpContent httpContent = (HttpContent) msg; // 把每次的数据保存到buf中
            ByteBuf byteBuf = httpContent.content();
            byteBuf.readBytes(buf, byteBuf.readableBytes());

            if (msg instanceof LastHttpContent) {
                // bytebuf转成byte[]
                byte[] bytes = new byte[buf.readableBytes()];
                buf.getBytes(buf.readerIndex(), bytes);

                HttpResponseMessage httpResponseMessage = new HttpResponseMessage(statuscode, bytes);
                HttpRequestMessage requestMsg = ctx.pipeline().get(HttpRequestEncoder.class).getHttpRequestMessage();
                if (requestMsg != null && requestMsg.getHttpResponseFuture() != null) {
                    requestMsg.getHttpResponseFuture().complete(httpResponseMessage);
                }
            }
            ctx.flush();
        }

    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        try {
            ctx.channel().close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        try {
            ctx.channel().close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE || event.state() == IdleState.WRITER_IDLE) {
                try {
                    ctx.channel().close();
                } catch (Exception e) {
                    logger.error("channel userEventTriggered:", e.getMessage());
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        try {
            ctx.channel().close();
        } catch (Exception e) {
            logger.error("channel exceptionCaught:", e.getMessage());
        }

    }

}
