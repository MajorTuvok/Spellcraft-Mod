package mt.mcmods.spellcraft.common.blocks.Ores;


import mt.mcmods.spellcraft.common.blocks.BaseBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

abstract class BaseOre extends BaseBlock implements IWorldGenerator {
    protected static final String TOOL_PICKAXE = "pickaxe";
    private static final boolean DEFAULT_GENERATE_SURFACE = true;
    private static final float LIGHT_DEFAULT = 0;
    private static final int OPACITY = 0;
    private static final SoundType SOUND_TYPE = SoundType.STONE;
    private static final int VEIN_SIZE_DEFAULT = 8;
    private WorldGenHelper mGenHelper;
    private boolean mGenerateOnOverworld;
    private int mMaxVeinSize;

    protected BaseOre(Material material, @Nonnull String displayName, int harvestLevel, int oresPerChunk, int lowerGenBound, int upperGenBound) {
        super(material, displayName);
        setSoundType(SOUND_TYPE);
        setLightLevel(LIGHT_DEFAULT);
        setLightOpacity(OPACITY);
        setHarvestLevel(TOOL_PICKAXE, harvestLevel);
        this.mGenHelper = new WorldGenHelper(oresPerChunk, lowerGenBound, upperGenBound);
        this.mMaxVeinSize = VEIN_SIZE_DEFAULT;
        this.mGenerateOnOverworld = DEFAULT_GENERATE_SURFACE;
    }

    public int getOresPerChunk() {
        return mGenHelper.getOresPerChunk();
    }

    public void setOresPerChunk(int oresPerChunk) {
        mGenHelper.setOresPerChunk(oresPerChunk);
    }

    public int getLowerGenBound() {
        return mGenHelper.getLowerGenBound();
    }

    public void setLowerGenBound(int lowerGenBound) {
        mGenHelper.setLowerGenBound(lowerGenBound);
    }

    public int getUpperGenBound() {
        return mGenHelper.getUpperGenBound();
    }

    public void setUpperGenBound(int upperGenBound) {
        mGenHelper.setUpperGenBound(upperGenBound);
    }

    public int getMaxVeinSize() {
        return mMaxVeinSize;
    }

    public void setMaxVeinSize(int maxVeinSize) {
        this.mMaxVeinSize = maxVeinSize;
    }

    protected WorldGenHelper getGenHelper() {
        return mGenHelper;
    }

    /**
     * Generate some world
     *
     * @param random         the chunk specific {@link Random}.
     * @param chunkX         the chunk X coordinate of this chunk.
     * @param chunkZ         the chunk Z coordinate of this chunk.
     * @param world          : additionalData[0] The minecraft {@link World} we're generating for.
     * @param chunkGenerator : additionalData[1] The {@link IChunkProvider} that is generating.
     * @param chunkProvider  : additionalData[2] {@link IChunkProvider} that is requesting the world generation.
     */
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        if (chunkProvider.isChunkGeneratedAt(chunkX, chunkZ)) {
            switch (world.provider.getDimension()) {
                case 0: {
                    generateOverWorld(world, random, x, z);
                    break;
                }
                case 1: {
                    generateNether(world, random, x, z);
                    break;
                }
                case -1: {
                    generateEnd(world, random, x, z);
                    break;
                }
            }
        }
    }

    protected void generateSurface(World world, java.util.Random rand, int chunkX, int chunkZ) {
        Log.info("Generating surface!");
        generateHelper(world, rand, chunkX, chunkZ);
    }

    protected void generateHelper(World world, java.util.Random rand, int chunkX, int chunkZ) {
        WorldGenerator generator = new WorldGenMinable(this.getDefaultState(), mMaxVeinSize);
        generateHelper(world, rand, generator, chunkX, chunkZ);
    }

    protected void generateHelper(World world, java.util.Random rand, WorldGenerator generator, int chunkX, int chunkZ) {
        getGenHelper().generate(world, rand, generator, chunkX, chunkZ);
    }

    protected void generateOverWorld(World world, java.util.Random rand, int chunkX, int chunkZ) {
        if (mGenerateOnOverworld) {
            generateSurface(world, rand, chunkX, chunkZ);
        }
    }

    protected void generateNether(World world, java.util.Random rand, int chunkX, int chunkZ) {

    }

    protected void generateEnd(World world, java.util.Random rand, int chunkX, int chunkZ) {

    }

    public static class WorldGenHelper {
        private int mLowerGenBound;
        private int mOresPerChunk;
        private int mUpperGenBound;

        public WorldGenHelper(int oresPerChunk, int lowerGenBound, int upperGenBound) {
            this.mLowerGenBound = lowerGenBound;
            this.mUpperGenBound = upperGenBound;
            this.mOresPerChunk = oresPerChunk;
        }

        public int getOresPerChunk() {
            return mOresPerChunk;
        }

        public void setOresPerChunk(int oresPerChunk) {
            this.mOresPerChunk = oresPerChunk;
        }

        public int getLowerGenBound() {
            return mLowerGenBound;
        }

        public void setLowerGenBound(int lowerGenBound) {
            this.mLowerGenBound = lowerGenBound;
        }

        public int getUpperGenBound() {
            return mUpperGenBound;
        }

        public void setUpperGenBound(int upperGenBound) {
            this.mUpperGenBound = upperGenBound;
        }

        /**
         * @param world     The world to generate in
         * @param rand      The random to use
         * @param generator the World generator to use
         * @param chunkX    The 'real' X of the Chunk (chunkX*16)
         * @param chunkZ    The 'real' Y of the Chunk (chunkY*16)
         */
        public void generate(World world, java.util.Random rand, WorldGenerator generator, int chunkX, int chunkZ) {
            for (int i = 0; i < mOresPerChunk; i++) {
                int randPosX = chunkX + rand.nextInt(16);
                int randPosY = rand.nextInt(mUpperGenBound - mLowerGenBound) + mLowerGenBound;
                int randPosZ = chunkZ + rand.nextInt(16);
                generator.generate(world, rand, new BlockPos(randPosX, randPosY, randPosZ));
            }
        }
    }
}
