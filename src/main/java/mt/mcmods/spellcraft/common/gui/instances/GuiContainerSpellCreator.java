package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import mt.mcmods.spellcraft.common.gui.helper.slots.SingleElementSlot;
import mt.mcmods.spellcraft.common.items.ItemSpellPaper;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import mt.mcmods.spellcraft.common.util.item.ItemHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mt.mcmods.spellcraft.common.gui.helper.GuiResource.GUI_BLANK;
import static mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator.INPUT_SLOT;
import static mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator.OUTPUT_SLOT;

public class GuiContainerSpellCreator extends BaseGuiContainer {
    static final PlayerInventoryOffsets OFFSETS = GUI_BLANK.getSuggestedOffsets();
    static final int X_INPUT = OFFSETS.getInnerXInvOffset();
    private static final int X_OUTPUT = OFFSETS.getSlotXSize() * OFFSETS.getInnerColumnCount() - X_INPUT;
    private static final int Y_INPUT = Math.round((float) OFFSETS.getInnerYInvOffset() / 2 - (float) OFFSETS.getSlotYSize() / 2); //=34
    static final int Y_OUTPUT = Y_INPUT;

    public GuiContainerSpellCreator(GuiContainerSpellCreator containerSpellCreator) {
        this(containerSpellCreator.getPlayerInventory(), containerSpellCreator.getTileEntity());
    }

    public GuiContainerSpellCreator(@Nonnull InventoryPlayer playerInv, @Nonnull TileEntitySpellCreator entity) {
        super(playerInv, entity, OFFSETS, null);
    }

    GuiContainerSpellCreator(@Nonnull InventoryPlayer playerInv, @Nullable TileEntity entity, @Nonnull PlayerInventoryOffsets offsets, @Nullable EnumFacing facing) {
        super(playerInv, entity, offsets, facing);
    }

    @Override
    public TileEntitySpellCreator getTileEntity() {
        return (TileEntitySpellCreator) super.getTileEntity();
    }

    @Override
    protected void createInventoryFromCapability(IItemHandler handler) {
        addSlotToContainer(new InputSlot(handler));
        addSlotToContainer(new OutputSlot(handler));
    }

    private boolean isItemValidForSpellCreation(ItemStack stack) {
        return !ItemHelper.isEmptyOrNull(stack) && stack.getItem() instanceof ItemSpellPaper;
    }

    private class InputSlot extends SlotItemHandler {
        private InputSlot(IItemHandler itemHandler) {
            super(itemHandler, INPUT_SLOT, X_INPUT, Y_INPUT);
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         *
         * @param stack
         */
        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return isItemValidForSpellCreation(stack);
        }
    }

    private class OutputSlot extends SingleElementSlot {
        private OutputSlot(IItemHandler itemHandler) {
            super(itemHandler, OUTPUT_SLOT, X_OUTPUT, Y_OUTPUT);
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         *
         * @param stack
         */
        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return false;
        }
    }
}
