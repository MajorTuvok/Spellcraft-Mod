package com.mt.mcmods.spellcraft.common.blocks;


import com.mt.mcmods.spellcraft.SpellcraftMod;
import com.mt.mcmods.spellcraft.common.gui.helper.GuiID;
import com.mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import com.mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWandCraftingTable extends BaseTileEntityBlock<TileEntityWandCraftingTable> {
    private static final String NAME = StringHelper.createUnlocalizedName("wand","crafting","table");
    public BlockWandCraftingTable() {
        super(Material.WOOD, NAME);
        setHardness(2.5f);
        setResistance(2.5f);
        setHarvestLevel("axe",0);
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
        playerIn.openGui(SpellcraftMod.instance, GuiID.GUIWandCraftingTable.ordinal(),worldIn,pos.getX(),pos.getY(),pos.getZ());
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
