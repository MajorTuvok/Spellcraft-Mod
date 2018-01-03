package mt.mcmods.spellcraft.Server.net.MessageHandlers;


import mt.mcmods.spellcraft.Client.net.Messages.ParticleActivated;
import mt.mcmods.spellcraft.Server.net.Messages.ShowParticle;
import mt.mcmods.spellcraft.SpellcraftMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ParticleActivatedHandler implements IMessageHandler<ParticleActivated, IMessage> {
    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(ParticleActivated message, MessageContext ctx) {
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
        // The value that was sent

        // Execute the action on the main Server thread by adding it as a scheduled task
        serverPlayer.getServerWorld().addScheduledTask(() -> {
            StructureBoundingBox boundingBox = new StructureBoundingBox();
            NetworkRegistry.TargetPoint targetPoint = message.getTargetPoint();
            boundingBox.minX = (int) Math.round(targetPoint.x - targetPoint.range);
            boundingBox.minY = (int) Math.round(targetPoint.y - targetPoint.range);
            boundingBox.minZ = (int) Math.round(targetPoint.z - targetPoint.range);
            boundingBox.maxX = (int) Math.round(targetPoint.x + targetPoint.range);
            boundingBox.maxY = (int) Math.round(targetPoint.y + targetPoint.range);
            boundingBox.maxZ = (int) Math.round(targetPoint.z + targetPoint.range);
            if (serverPlayer.getServerWorld().isAreaLoaded(boundingBox))
                SpellcraftMod.CHANNEL_HOLDER.sendToAllAround(new ShowParticle(message), message.getTargetPoint());
        });
        return null;
    }
}
