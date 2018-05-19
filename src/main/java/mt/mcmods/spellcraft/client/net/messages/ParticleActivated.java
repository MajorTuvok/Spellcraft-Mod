package mt.mcmods.spellcraft.client.net.messages;

import io.netty.buffer.ByteBuf;
import mt.mcmods.spellcraft.server.net.messages.ShowParticle;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import static mt.mcmods.spellcraft.client.particles.ParticleHandler.ParticleName;

public class ParticleActivated extends ShowParticle {
    private NetworkRegistry.TargetPoint targetPoint;

    public ParticleActivated() {

    }

    public ParticleActivated(ParticleName name, int particleAmount, float particleMaxDistance, float velocityFactor, int parX, int parY, int parZ, NetworkRegistry.TargetPoint targetPoint) {
        super(name, particleAmount, particleMaxDistance, velocityFactor, parX, parY, parZ);
        this.targetPoint = targetPoint;
    }

    public NetworkRegistry.TargetPoint getTargetPoint() {
        return targetPoint;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        int dim = buf.readInt();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        double range = buf.readDouble();
        this.targetPoint = new NetworkRegistry.TargetPoint(dim, x, y, z, range);
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(targetPoint.dimension);
        buf.writeDouble(targetPoint.x);
        buf.writeDouble(targetPoint.y);
        buf.writeDouble(targetPoint.z);
        buf.writeDouble(targetPoint.range);
    }
}
