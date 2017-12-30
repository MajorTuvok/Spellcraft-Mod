package com.mt.mcmods.spellcraft.common.gui.instances;

import com.mt.mcmods.spellcraft.common.gui.BaseGui;
import com.mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import net.minecraft.entity.player.InventoryPlayer;

import static com.mt.mcmods.spellcraft.common.gui.helper.GuiResources.*;

public class GuiWandCraftingTable extends BaseGui {
    private TileEntityWandCraftingTable entityWandCraftingTable;

    public GuiWandCraftingTable(InventoryPlayer inventoryPlayer, GuiContainerWandCraftingTable inventorySlotsIn) {
        this(inventoryPlayer,inventorySlotsIn,inventorySlotsIn.getEntity());
    }

    public GuiWandCraftingTable(InventoryPlayer inventoryPlayer, GuiContainerWandCraftingTable inventorySlotsIn, TileEntityWandCraftingTable entity) {
        super(inventoryPlayer, inventorySlotsIn, GUI_BLANK.getImgXSize(), GUI_BLANK.getImgYSize());
        guiTop = 10;
        entityWandCraftingTable = entity;
    }

    /**
     * Draws the background layer of this container (behind the items).
     *
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        getDelegate().drawGuiBackground(GUI_BLANK);
        getDelegate().drawAllSlotsWithResource(SLOT);
    }
}
