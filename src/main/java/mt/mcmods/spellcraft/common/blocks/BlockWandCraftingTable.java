package mt.mcmods.spellcraft.common.blocks;


import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.common.gui.helper.GuiID;
import mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import mt.mcmods.spellcraft.common.util.NetworkUtils;
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

public class BlockWandCraftingTable extends BaseTileEntityBlock<TileEntityWandCraftingTable> {
    private static final String NAME = StringHelper.createUnlocalizedName("wand", "crafting", "table");

    public BlockWandCraftingTable() {
        super(Material.WOOD, NAME);
        setHardness(2.5f);
        setResistance(2.5f);
        setHarvestLevel(AXE.getToolClass(), 0);
    }

    @Override
    public Class<TileEntityWandCraftingTable> getTileEntityClass() {
        return TileEntityWandCraftingTable.class;
    }

    @Nullable
    @Override
    public TileEntityWandCraftingTable createTileEntity(World world, IBlockState state) {
        return new TileEntityWandCraftingTable();
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
        playerIn.openGui(SpellcraftMod.instance, GuiID.GUI_WAND_CRAFTING_TABLE.getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     *
     * @param worldIn
     * @param pos
     * @param state
     * @param player
     */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        if (NetworkUtils.isServer(worldIn)) {
            TileEntityWandCraftingTable wandCraftingTable = (TileEntityWandCraftingTable) worldIn.getTileEntity(pos);
            if (wandCraftingTable != null) {
                wandCraftingTable.spawnItemDrops(worldIn, pos);
            }
        }
    }
}
