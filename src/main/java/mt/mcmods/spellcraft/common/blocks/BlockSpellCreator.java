package mt.mcmods.spellcraft.common.blocks;

import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.common.gui.helper.GuiID;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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

    /**
     * Called when the block is right clicked by a player.
     *
     * @param worldIn
     * @param pos
     * @param state
     * @param playerIn
     * @param hand
     * @param facing
     * @param hitX
     * @param hitY
     * @param hitZ
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        playerIn.openGui(SpellcraftMod.instance, GuiID.GUISpellCreator.getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
