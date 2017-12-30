package com.mt.mcmods.spellcraft.common.gui.helper;

import com.mt.mcmods.spellcraft.common.gui.BaseGui;
import com.mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import com.mt.mcmods.spellcraft.common.interfaces.IGuiRenderProvider;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class SlotDrawingDelegate extends GuiDrawingDelegate {
    private InventoryPlayer inventoryPlayer;
    private int slotCount;
    private Container container;
    private PlayerInventoryOffsets offsets;

    public SlotDrawingDelegate(InventoryPlayer inventoryPlayer, IGuiRenderProvider renderProvider, Container container, PlayerInventoryOffsets offsets, GUIMeasurements measurements) {
        super(renderProvider, measurements);
        this.inventoryPlayer = inventoryPlayer;
        this.container = container;
        this.slotCount = this.container.inventorySlots.size();
        this.offsets = offsets;
    }

    public SlotDrawingDelegate(InventoryPlayer inventoryPlayer, BaseGui gui, BaseGuiContainer container) {
        this(inventoryPlayer, gui, container, container.getInventoryOffsets(), gui.getGuiMeasurements());
    }

    public PlayerInventoryOffsets getOffsets() {
        return offsets;
    }

    public Container getContainer() {
        return container;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public InventoryPlayer getInventoryPlayer() {
        return inventoryPlayer;
    }

    //----------------------------Inventory Drawing-------------------------------------------------

    public void drawAllSlotsWithResource(@Nonnull ResourceProvider provider) {
        drawAllSlotsWithResource(provider.getResource());
    }

    public void drawNonPlayerInventoryWithResource(@Nonnull ResourceProvider provider) {
        drawNonPlayerInventoryWithResource(provider.getResource());
    }

    public void drawAllSlotsWithResource(@Nonnull ResourceLocation location) {
        drawNonPlayerInventoryWithResource(location);
        drawPlayerInventoryWithResource(location);
    }

    public void drawNonPlayerInventoryWithResource(@Nonnull ResourceLocation location) {
        bindResource(location);
        for (int i = slotCount - inventoryPlayer.mainInventory.size() - 1; i >= 0; i--) {
            drawTexturedSlot(i);
        }
    }

    public void drawPlayerInventoryWithResource(@Nonnull ResourceProvider provider) {
        drawPlayerInventoryWithResource(provider.getResource());
    }

    public void drawPlayerInventoryWithResource(@Nonnull ResourceLocation location) {
        bindResource(location);
        for (int i = slotCount - inventoryPlayer.mainInventory.size(); i < slotCount; i++) {
            drawTexturedSlot(i);
        }
    }

    //----------------------------Slot Drawing-------------------------------------------------

    public void drawSlot(ResourceProvider provider, int index) {
        drawSlot(provider.getResource(), index);
    }

    public void drawSlot(ResourceProvider provider, Slot slot) {
        drawSlot(provider.getResource(), slot);
    }

    public void drawSlot(ResourceLocation location, int index) {
        bindResource(location);
        drawTexturedSlot(index);
    }

    public void drawSlot(ResourceLocation location, Slot slot) {
        bindResource(location);
        drawTexturedSlot(slot);
    }

    public void drawTexturedSlot(int index) {
        drawTexturedSlot(container.getSlot(index));
    }

    public void drawTexturedSlot(Slot slot) {
        drawTexturedImage(slot.xPos, slot.yPos, 0, 0, offsets.getSlotXSize(), offsets.getSlotYSize());
    }
}
