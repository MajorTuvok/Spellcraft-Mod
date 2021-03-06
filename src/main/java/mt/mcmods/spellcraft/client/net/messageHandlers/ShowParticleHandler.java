package mt.mcmods.spellcraft.client.net.messageHandlers;

import mt.mcmods.spellcraft.client.particles.ParticleHandler;
import mt.mcmods.spellcraft.server.net.messages.PacketShowParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ShowParticleHandler implements IMessageHandler<PacketShowParticle, IMessage> {
    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(PacketShowParticle message, MessageContext ctx) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                World world = Minecraft.getMinecraft().world;
                ParticleHandler.spawnCustomParticles(
                        world,
                        world.rand,
                        message.getParticleName(),
                        message.getParticleAmount(),
                        message.getParticleMaxDistance(),
                        message.getVelocityFactor(),
                        message.getParX(),
                        message.getParY(),
                        message.getParZ());
            });
        }
        return null;
    }
}
