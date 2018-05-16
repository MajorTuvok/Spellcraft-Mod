package mt.mcmods.spellcraft.common.util;

import io.netty.buffer.ByteBuf;
import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.server.ServerProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ChannelHolder {

    private final SimpleNetworkWrapper mChannel;
    private int idCount;

    public ChannelHolder(String modid) {
        mChannel = NetworkRegistry.INSTANCE.newSimpleChannel(modid);
        idCount = 0;
    }

    private int getIdCount() {
        if (idCount < 255) {
            return idCount++;
        } else {
            throw new RuntimeException("Attempted to registerGameOverlayListener more than 255 Messages on a single Channel!");
        }
    }

    public <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
        int count = getIdCount();
        //ILoggable.Log.info("Id count before registration: " + count);
        if (!(SpellcraftMod.proxy instanceof ServerProxy) || side == Side.SERVER) {
            mChannel.registerMessage(messageHandler, requestMessageType, count, side);
        }
    }

    public void sendToAll(IMessage message) {
        mChannel.sendToAll(message);
    }

    public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        mChannel.sendToAllAround(message, point);
    }

    public void sendToDimension(IMessage message, int dimensionId) {
        mChannel.sendToDimension(message, dimensionId);
    }

    public void sendTo(IMessage message, EntityPlayerMP playerMP) {
        mChannel.sendTo(message, playerMP);
    }

    public void sendToServer(IMessage message) {
        mChannel.sendToServer(message);
    }

    public static class StubMessage implements IMessage {
        /**
         * Convert from the supplied buffer into your specific message type
         *
         * @param buf
         */
        @Override
        public void fromBytes(ByteBuf buf) {
            ILoggable.Log.warn("Reading StubMessage");
        }

        /**
         * Deconstruct your message into the supplied byte buffer
         *
         * @param buf
         */
        @Override
        public void toBytes(ByteBuf buf) {
            ILoggable.Log.warn("Writing StubMessage");
        }
    }

    public static final class StubHandler implements IMessageHandler<StubMessage, IMessage> {
        /**
         * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
         * is needed.
         *
         * @param message The message
         * @param ctx
         * @return an optional return message
         */
        @Override
        public IMessage onMessage(StubMessage message, MessageContext ctx) {
            ILoggable.Log.warn("Received Stub message on " + (ctx.side == Side.CLIENT ? "Client Side..." : "Server Side..."));
            return null;
        }
    }
}
