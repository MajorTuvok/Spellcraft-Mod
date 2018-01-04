package mt.mcmods.spellcraft.common.blocks;

import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static mt.mcmods.spellcraft.common.items.tools.ToolInfo.AXE;

public class BlockSpellCreator extends BaseTileEntityBlock<TileEntitySpellCreator> {
    private static final String NAME = StringHelper.createUnlocalizedName("spell", "creator");

    public BlockSpellCreator() {
        super(Material.WOOD, NAME);
        setHardness(2.5f);
        setResistance(2.5f);
        setHarvestLevel(AXE.getToolClass(), 0);
    }

    @Override
    public Class<TileEntitySpellCreator> getTileEntityClass() {
        return TileEntitySpellCreator.class;
    }

    @Nullable
    @Override
    public TileEntitySpellCreator createTileEntity(World world, IBlockState state) {
        return new TileEntitySpellCreator();
    }
}
