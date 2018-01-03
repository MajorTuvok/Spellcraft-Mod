package mt.mcmods.spellcraft.common.Particles;


import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.world.World;

public class ParticleEnergizedOsmiumOre extends ParticleRedstone {
    private static final float PARTICLE_SCALE = 1;

    public ParticleEnergizedOsmiumOre(World parWorld,
                                      double parX, double parY, double parZ) {
        super(parWorld, parX, parY, parZ, 0.796f, 0.678f, 0.855f); //because they are Reddust Particles, they don't work like normal Particles would ->last 3 params are Color
        particleScale = PARTICLE_SCALE;
        setParticleTextureIndex(0); //just to make sure it really is what we want
        //setRBGColorF(0.796f,0.678f,0.855f);  //here is the color ref for the above float values:0xCB,0xAD,0xDA
    }
}
