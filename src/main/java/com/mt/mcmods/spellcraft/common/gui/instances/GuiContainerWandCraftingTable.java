package com.mt.mcmods.spellcraft.common.gui.instances;

import com.mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import com.mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import com.mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiContainerWandCraftingTable extends BaseGuiContainer {
    private static final PlayerInventoryOffsets OFFSETS = new PlayerInventoryOffsets(10, 84, 10, 142);  //TODO align properly

    public GuiContainerWandCraftingTable(@Nonnull InventoryPlayer playerInv, @Nonnull TileEntityWandCraftingTable entity, @Nullable EnumFacing facing) {
        super(playerInv, entity, OFFSETS, facing);
    }

    @Override
    protected void createInventoryFromCapability(IItemHandler handler) {  //TODO set Slots to better Positions
        for (int i = 0; i < handler.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(handler, i, OFFSETS.getInnerXInvOffset() + i * OFFSETS.getSlotXSize(), 10));
        }
    }

    @Override
    public TileEntityWandCraftingTable getEntity() {
        return (TileEntityWandCraftingTable) super.getEntity();
    }
}
