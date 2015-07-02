package org.mage.network.messages.requests;

import io.netty.channel.ChannelHandlerContext;
import java.util.UUID;
import org.mage.network.handlers.WriteListener;
import org.mage.network.interfaces.MageServer;
import org.mage.network.messages.responses.UUIDResponse;

/**
 *
 * @author BetaSteward
 */
public class JoinGameRequest extends ServerRequest {
    
    private UUID gameId;
    
    public JoinGameRequest(UUID gameId) {
        this.gameId = gameId;
    }

    @Override
    public void handleMessage(MageServer server, ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new UUIDResponse(server.joinGame(gameId, ctx.channel().id().asLongText()))).addListener(WriteListener.getInstance());
    }

}