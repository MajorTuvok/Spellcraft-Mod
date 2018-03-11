package mt.mcmods.spellcraft.Client.Particles;

import mt.mcmods.spellcraft.common.Particles.ParticleEnergizedOsmiumOre;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Random;

import static mt.mcmods.spellcraft.Client.Particles.ParticleHandler.ParticleName.PARTICLE_ENERGIZED_OSMIUM_ORE;

public class ParticleHandler {
    public enum ParticleName {
        PARTICLE_ENERGIZED_OSMIUM_ORE
    }

    public static final HashMap<Integer, ParticleName> ID_PARTICLE_MAP = new HashMap<>();
    public static final HashMap<ParticleName, Integer> PARTICLE_ID_MAP = new HashMap<>();

    static {
        PARTICLE_ID_MAP.put(PARTICLE_ENERGIZED_OSMIUM_ORE, 0);
        ID_PARTICLE_MAP.put(0, PARTICLE_ENERGIZED_OSMIUM_ORE);
    }

    public static void showParticles(@Nonnull Particle... particles) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc != null && mc.effectRenderer != null) {
                for (Particle particle :
                        particles) {
                    if (particle != null) {
                        mc.effectRenderer.addEffect(particle);
                    }
                }
            } else {
                ILoggable.Log.warn("Unable to show Particles!");
            }
        }
    }

    public static void showParticles(World world, Particle particle) {
        if (world.isRemote) {
            showParticles(particle);
        }
    }

    public static Particle spawnParticle(@Nonnull World parWorld, ParticleName name,
                                         double parX, double parY, double parZ,
                                         float parMotionX, float parMotionY, float parMotionZ) {
        if (parWorld.isRemote && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            switch (name) {
                case PARTICLE_ENERGIZED_OSMIUM_ORE: {
                    return new ParticleEnergizedOsmiumOre(parWorld, parX, parY, parZ);
                }
            }
        }
        return null;
    }

    public static void spawnCustomParticles(@Nonnull World world, Random random, ParticleName name, int particleAmount, float particleMaxDistance, float velocityFactor, int srcX, int srcY, int srcZ) {
        if (world.isRemote && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            Particle[] particles = new Particle[particleAmount];
            for (int i = 0; i < particleAmount; i++) {
                double x = random.nextDouble() + srcX;
                double y = random.nextDouble() + srcY;
                double z = random.nextDouble() + srcZ;
                float vx = (random.nextFloat() - particleMaxDistance) * velocityFactor;
                float vy = (random.nextFloat() - particleMaxDistance) * velocityFactor;
                float vz = (random.nextFloat() - particleMaxDistance) * velocityFactor;
                particles[i] = spawnParticle(world, name, x, y, z, vx, vy, vz);
                if (particles[i] == null) return;
            }
            showParticles(particles);
        }
    }

    public static void spawnParticles(@Nonnull World world, Random random, EnumParticleTypes name, int particleAmount, float particleMaxDistance, float velocityFactor, int srcX, int srcY, int srcZ) {
        if (!world.isRemote) {
            for (int i = 0; i < particleAmount; i++) {
                double x = random.nextDouble() + srcX;
                double y = random.nextDouble() + srcY;
                double z = random.nextDouble() + srcZ;
                double vx = (random.nextDouble() - particleMaxDistance) * velocityFactor;
                double vy = (random.nextDouble() - particleMaxDistance) * velocityFactor;
                double vz = (random.nextDouble() - particleMaxDistance) * velocityFactor;
                world.spawnParticle(name, x, y, z, vx, vy, vz);
            }
        }
    }
}
