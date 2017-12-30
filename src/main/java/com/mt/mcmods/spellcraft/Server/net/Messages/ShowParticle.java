package com.mt.mcmods.spellcraft.Server.net.Messages;

import com.mt.mcmods.spellcraft.Client.Particles.ParticleHandler.ParticleName;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import static com.mt.mcmods.spellcraft.Client.Particles.ParticleHandler.ID_PARTICLE_MAP;
import static com.mt.mcmods.spellcraft.Client.Particles.ParticleHandler.PARTICLE_ID_MAP;

public class ShowParticle implements IMessage {
    private ParticleName name;
    private int parX;
    private int parY;
    private int parZ;
    private int particleAmount;
    private float particleMaxDistance;
    private float velocityFactor;

    public ShowParticle() {
    }


    public ShowParticle(ParticleName name, int particleAmount, float particleMaxDistance, float velocityFactor, int parX, int parY, int parZ) {
        this.name = name;
        this.parX = parX;
        this.parY = parY;
        this.parZ = parZ;
        this.particleAmount = particleAmount;
        this.particleMaxDistance = particleMaxDistance;
        this.velocityFactor = velocityFactor;
    }

    public ShowParticle(ShowParticle activated) {
        this(activated.getParticleName(),
                activated.getParticleAmount(),
                activated.getParticleMaxDistance(),
                activated.getVelocityFactor(),
                activated.getParX(),
                activated.getParY(),
                activated.getParZ());
    }


    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        this.name = ID_PARTICLE_MAP.get(buf.readInt());
        this.parX = buf.readInt();
        this.parY = buf.readInt();
        this.parZ = buf.readInt();
        this.particleAmount = buf.readInt();
        this.particleMaxDistance = buf.readFloat();
        this.velocityFactor = buf.readFloat();
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(PARTICLE_ID_MAP.get(name));
        buf.writeInt(parX);
        buf.writeInt(parY);
        buf.writeInt(parZ);
        buf.writeInt(particleAmount);
        buf.writeFloat(particleMaxDistance);
        buf.writeFloat(velocityFactor);
    }


    public ParticleName getParticleName() {
        return name;
    }

    public int getParX() {
        return parX;
    }

    public int getParY() {
        return parY;
    }

    public int getParZ() {
        return parZ;
    }

    public int getParticleAmount() {
        return particleAmount;
    }

    public float getParticleMaxDistance() {
        return particleMaxDistance;
    }

    public float getVelocityFactor() {
        return velocityFactor;
    }
}
