package com.mt.mcmods.spellcraft.common.blocks;


import com.mt.mcmods.spellcraft.Client.Particles.ParticleHandler;
import com.mt.mcmods.spellcraft.Client.net.Messages.ParticleActivated;
import com.mt.mcmods.spellcraft.Server.net.Messages.ShowParticle;
import com.mt.mcmods.spellcraft.SpellcraftMod;
import com.mt.mcmods.spellcraft.common.CTabs;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.interfaces.INamed;
import com.mt.mcmods.spellcraft.common.interfaces.IOreDictNamed;
import com.mt.mcmods.spellcraft.common.interfaces.IRenderable;
import com.mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BaseBlock extends Block implements INamed, IOreDictNamed, ILoggable, IRenderable {
    private static final int DEFAULT_TICKRATE = 10;
    private static final float DEFAULT_PARTICLE_VELOCITY_FACTOR = 0.1f;
    private static final float DEFAULT_PARTICLE_MAX_DISTANCE = 0.4f;

    private String mName;

    public BaseBlock(Material material, @Nonnull String displayName) {
        super(material);
        mName = displayName;
        setCreativeTab(CTabs.TAB_MAIN);
        setUnlocalizedName(displayName);
        setRegistryName(getName());
    }

    @Override
    public int tickRate(World p_149738_1_) {
        return DEFAULT_TICKRATE;
    }

    protected void spawnParticles(World world, Random random, EnumParticleTypes particleType, int particleAmount, float particleMaxDistance, float velocityFactor, int blockX, int blockY, int blockZ) {
        ParticleHandler.spawnParticles(world, random, particleType, particleAmount, particleMaxDistance, velocityFactor, blockX, blockY, blockZ);
    }

    protected void spawnParticles(World world, Random random, EnumParticleTypes particleType, int particleAmount, float particleMaxDistance, int blockX, int blockY, int blockZ) {
        spawnParticles(world, random, particleType, particleAmount, particleMaxDistance, DEFAULT_PARTICLE_VELOCITY_FACTOR, blockX, blockY, blockZ);
    }

    protected void spawnParticles(World world, Random random, EnumParticleTypes particleType, int particleAmount, int blockX, int blockY, int blockZ) {
        spawnParticles(world, random, particleType, particleAmount, DEFAULT_PARTICLE_MAX_DISTANCE, blockX, blockY, blockZ);
    }

    protected void spawnCustomParticles(World world, Random random, ParticleHandler.ParticleName particleType, int particleAmount, float particleMaxDistance, float velocityFactor, int blockX, int blockY, int blockZ) {
        ParticleHandler.spawnCustomParticles(world, random, particleType, particleAmount, particleMaxDistance, velocityFactor, blockX, blockY, blockZ);
    }

    protected void spawnCustomParticles(World world, Random random, ParticleHandler.ParticleName particleType, int particleAmount, float particleMaxDistance, int blockX, int blockY, int blockZ) {
        spawnCustomParticles(world, random, particleType, particleAmount, particleMaxDistance, DEFAULT_PARTICLE_VELOCITY_FACTOR, blockX, blockY, blockZ);
    }

    protected void spawnCustomParticles(World world, Random random, ParticleHandler.ParticleName particleType, int particleAmount, int blockX, int blockY, int blockZ) {
        spawnCustomParticles(world, random, particleType, particleAmount, DEFAULT_PARTICLE_MAX_DISTANCE, blockX, blockY, blockZ);
    }

    protected void spawnCustomSyncParticles(World world, ParticleHandler.ParticleName name, int particleAmount, float particleMaxDistance, float velocityFactor, int parX, int parY, int parZ) {
        NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(world.provider.getDimension(), parX, parY, parZ, 8);
        if (world.isRemote) {
            Log.info("Sending to Server Spawn contract");
            SpellcraftMod.CHANNEL_HOLDER.sendToServer(new ParticleActivated(name, particleAmount, particleMaxDistance, velocityFactor, parX, parY, parZ, targetPoint));
        } else {
            Log.info("Sending to Clients Spawn contract");
            SpellcraftMod.CHANNEL_HOLDER.sendToAllAround(new ShowParticle(name, particleAmount, particleMaxDistance, velocityFactor, parX, parY, parZ), targetPoint);
        }
    }

    protected void spawnCustomSyncParticles(World world, ParticleHandler.ParticleName name, int particleAmount, float particleMaxDistance, int parX, int parY, int parZ) {
        spawnCustomSyncParticles(world, name, particleAmount, particleMaxDistance, DEFAULT_PARTICLE_VELOCITY_FACTOR, parX, parY, parZ);
    }

    protected void spawnCustomSyncParticles(World world, ParticleHandler.ParticleName name, int particleAmount, int parX, int parY, int parZ) {
        spawnCustomSyncParticles(world, name, particleAmount, DEFAULT_PARTICLE_MAX_DISTANCE, parX, parY, parZ);
    }

    @Override
    public String getOreDictName() {
        return StringHelper.createOreDictNameFromUnlocalized(getName());
    }

    @Override
    public String getName() {
        return mName;
    }

    public BaseBlock setName(String mName) {
        this.mName = mName;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getLocation() {
        return getRegistryName();
    }

    @Override
    public boolean registerRenderer() {
        return true;
    }
}
