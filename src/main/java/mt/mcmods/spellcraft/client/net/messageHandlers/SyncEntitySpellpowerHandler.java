package mt.mcmods.spellcraft.client.net.messageHandlers;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.server.net.messages.SyncEntitySpellpower;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SyncEntitySpellpowerHandler implements IMessageHandler<SyncEntitySpellpower, IMessage> {
    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(SyncEntitySpellpower message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                message.apply(Minecraft.getMinecraft().world);
            });
        } else {
            ILoggable.Log.error("Impossible Message (SyncEntitySpellpower) on Server Side! Did you do the registration wrong again?");
        }
        return null;
    }
}
