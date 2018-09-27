package gdcn.igorlo.Network;

import gdcn.igorlo.Connector;
import javafx.application.Platform;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

import java.io.UnsupportedEncodingException;

public class Handler extends SimpleChannelHandler {

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        //TODO сделать так чтобы всё работало с пакетами типа чтобы сообщение не разрывалось на несколько частей как оно скорее всего и будет


        String message = getStringFromBuffer((ChannelBuffer) e.getMessage());

        String[] parsedMessages = message.split("/d/");
        for (String string: parsedMessages) {
            if (!string.equals("")){
                Platform.runLater(() -> {
                    ClientMessage.read(string);
                });
            }
        }

    }

    private String getStringFromBuffer(ChannelBuffer buffer) throws UnsupportedEncodingException {
        int bufSize = buffer.readableBytes();
        byte[] byteBuffer = new byte[bufSize];
        buffer.readBytes(byteBuffer);
        return new String(byteBuffer, "UTF-8");
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelClosed(ctx, e);
        Platform.runLater(() -> {
            Connector.connectionLost();
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        Platform.runLater(() -> {
            Connector.exeptionCaught(e);
        });
    }

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String message = (String) e.getMessage() + "/d/";
        Channels.write(
                ctx,
                e.getFuture(),
                ChannelBuffers.wrappedBuffer(message.getBytes("UTF-8")),
                e.getRemoteAddress()
        );
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
        Connector.connectionSuccess();
    }
}
