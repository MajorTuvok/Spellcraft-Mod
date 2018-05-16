package mt.mcmods.spellcraft.server.net.MessageHandlers;

import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.client.net.Messages.RequestSyncEntitySpellpower;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.server.net.Messages.SyncEntitySpellpower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RequestSyncEntitySpellpowerHandler implements IMessageHandler<RequestSyncEntitySpellpower, IMessage> {
    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(RequestSyncEntitySpellpower message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            final EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                Entity entity = message.getEntity(player.world);
                if (entity != null) {
                    SpellcraftMod.CHANNEL_HOLDER.sendTo(new SyncEntitySpellpower(entity), player);
                }
            });
        } else {
            ILoggable.Log.error("Impossible Message (RequestSyncEntitySpellpower) on Client Side! Did you do the registration wrong again?");
        }
        return null;
    }
}
