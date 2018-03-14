package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.helper.GuiResource;
import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import mt.mcmods.spellcraft.common.gui.helper.slots.SingleElementSlot;
import mt.mcmods.spellcraft.common.recipes.WandRegistryHelper;
import mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class GuiContainerWandCraftingTable extends BaseGuiContainer {
    static final PlayerInventoryOffsets OFFSETS = GuiResource.GUI_BLANK.getSuggestedOffsets();
    private static final int CRAFTING_X_POS = OFFSETS.getInnerXInvOffset();
    private static final int CRAFTING_X_0 = CRAFTING_X_POS;
    private static final int CRAFTING_Y_POS = 10;
    static final int CRAFTING_Y_0 = CRAFTING_Y_POS;
    private static final int MIN_SLOT_DIS = 2;
    private static final int CRAFTING_X_1 = CRAFTING_X_POS + Math.round(OFFSETS.getSlotXSize() * 0.5f) + MIN_SLOT_DIS;
    static final int CRAFTING_Y_1 = CRAFTING_Y_POS + OFFSETS.getSlotYSize() + MIN_SLOT_DIS;
    static final int CRAFTING_Y_2 = CRAFTING_Y_POS + OFFSETS.getSlotYSize() * 2 + MIN_SLOT_DIS * 2;
    static final int CRAFTING_X_2 = CRAFTING_X_POS + OFFSETS.getSlotXSize() + MIN_SLOT_DIS * 2;
    private static final int RESULT_X_POS = CRAFTING_X_POS + OFFSETS.getSlotXSize() * 8;
    private static final int RESULT_Y_POS = CRAFTING_Y_1;

    public GuiContainerWandCraftingTable(@Nonnull InventoryPlayer playerInv, @Nonnull TileEntityWandCraftingTable entity) {
        super(playerInv, entity, OFFSETS, null);
    }

    @Override
    public TileEntityWandCraftingTable getTileEntity() {
        return (TileEntityWandCraftingTable) super.getTileEntity();
    }

    @Override
    protected void createInventoryFromCapability(IItemHandler handler) {
        addSlotToContainer(new TipCraftingSlot(handler));
        addSlotToContainer(new WoodCraftingSlot(handler));
        addSlotToContainer(new CoreCraftingSlot(handler));
        addSlotToContainer(new CrystalCraftingSlot(handler));
        addSlotToContainer(new WandSlot(handler));
    }

    private class WandSlot extends SingleElementSlot {
        private WandSlot(IItemHandler itemHandler) {
            super(itemHandler, TileEntityWandCraftingTable.INVENTORY_STACK_WAND, RESULT_X_POS, RESULT_Y_POS);
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

    private class CrystalCraftingSlot extends SlotItemHandler {
        private CrystalCraftingSlot(IItemHandler itemHandler) {
            super(itemHandler, TileEntityWandCraftingTable.INVENTORY_STACK_CRYSTAL, CRAFTING_X_1, CRAFTING_Y_2);
        }
    }

    private class TipCraftingSlot extends SlotItemHandler {
        private TipCraftingSlot(IItemHandler itemHandler) {
            super(itemHandler, TileEntityWandCraftingTable.INVENTORY_STACK_TIP, CRAFTING_X_1, CRAFTING_Y_0);
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         *
         * @param stack
         */
        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return super.isItemValid(stack) && WandRegistryHelper.INSTANCE.hasTipPart(stack);
        }
    }

    private class CoreCraftingSlot extends SlotItemHandler {
        private CoreCraftingSlot(IItemHandler itemHandler) {
            super(itemHandler, TileEntityWandCraftingTable.INVENTORY_STACK_CORE, CRAFTING_X_2, CRAFTING_Y_1);
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         *
         * @param stack
         */
        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return super.isItemValid(stack) && WandRegistryHelper.INSTANCE.hasCorePart(stack);
        }
    }

    private class WoodCraftingSlot extends SlotItemHandler {
        private WoodCraftingSlot(IItemHandler itemHandler) {
            super(itemHandler, TileEntityWandCraftingTable.INVENTORY_STACK_WOOD, CRAFTING_X_0, CRAFTING_Y_1);
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         *
         * @param stack
         */
        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return !stack.isEmpty() && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == Blocks.LOG;
        }
    }
}
