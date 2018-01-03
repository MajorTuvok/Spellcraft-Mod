package mt.mcmods.spellcraft.common.tiles;

import mt.mcmods.spellcraft.common.interfaces.ICompatStackHandler;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import mt.mcmods.spellcraft.common.registry.WandRegistry;
import mt.mcmods.spellcraft.common.util.item.ItemHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class TileEntityWandCraftingTable extends BaseTileEntityWithInventory {
    private static final int INVENTORY_STACK_TIP = 0;
    private static final int INVENTORY_STACK_CORE = 1;
    private static final int INVENTORY_STACK_CRYSTAL = 2;
    private static final int INVENTORY_STACK_WOOD = 3;
    private static final int INVENTORY_STACK_WAND = 4;
    private static final int INVENTORY_SLOTS = 5;

    public TileEntityWandCraftingTable() {
        super(INVENTORY_SLOTS);
    }

    public boolean hasCraftableWand() {
        return hasCompatibleWood() && WandRegistry.INSTANCE.hasWand(getTipCraftingStack(), getCoreCraftingStack());
    }

    public void showCraftableWand() { //TODO remove Bug: cannot find Wand if stacks have different sizes than the recipe
        if (hasCraftableWand()) {
            ItemWand itemWand = WandRegistry.INSTANCE.getWand(getTipCraftingStack(), getCoreCraftingStack());
            if (itemWand != null && !ItemHelper.areItemsAssignable(getWandCraftingStack(), itemWand)) {
                ItemStack stack = new ItemStack(itemWand);
                setWandCraftingStack(stack);
            } else if (itemWand == null) {
                setWandCraftingStack(ItemStack.EMPTY);
            }
        } else {
            setWandCraftingStack(ItemStack.EMPTY);
        }
    }

    public void craftWand() {
        if (hasCraftableWand()) {
            setTipCraftingStack(ItemHelper.decreaseStackSize(getTipCraftingStack(), 1));
            setCoreCraftingStack(ItemHelper.decreaseStackSize(getCoreCraftingStack(), 1));
            setWoodCraftingStack(ItemHelper.decreaseStackSize(getWoodCraftingStack(), 1));
        }
    }

    public ItemStack getTipCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_TIP);
    }

    public ItemStack getCoreCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_CORE);
    }

    public ItemStack getCrystalCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_CRYSTAL);
    }

    public ItemStack getWoodCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_WOOD);
    }

    public ItemStack getWandCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_WAND);
    }

    public void setTipCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_TIP, stack, false);
    }

    public void setCoreCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_CORE, stack, false);
    }

    public void setCrystalCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_CRYSTAL, stack, false);
    }

    public void setWoodCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_WOOD, stack, false);
    }

    public void setWandCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_WAND, stack, false);
    }

    private boolean hasCompatibleWood() {
        return !ItemHelper.isEmptyOrNull(getWoodCraftingStack())
                && getWoodCraftingStack().getItem() instanceof ItemBlock
                && ((ItemBlock) getWoodCraftingStack().getItem()).getBlock() == Blocks.LOG;
    }

    @Override
    protected ICompatStackHandler createInventory(int size) {
        return new WandCraftingStackHandler(size);
    }

    @Override
    protected WandCraftingStackHandler getInventory() {
        return (WandCraftingStackHandler) super.getInventory();
    }

    private class WandCraftingStackHandler extends CompatStackHandler {
        public WandCraftingStackHandler() {
            super();
        }

        public WandCraftingStackHandler(int size) {
            super(size);
        }

        public WandCraftingStackHandler(NonNullList<ItemStack> stacks) {
            super(stacks);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return insertItem(slot, stack, simulate, true);
        }

        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate, boolean showWand) {
            ItemStack result = super.insertItem(slot, stack, simulate);
            if (showWand)
                showCraftableWand();
            return result;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return extractItem(slot, amount, simulate, true);
        }

        public ItemStack extractItem(int slot, int amount, boolean simulate, boolean showWand) {
            ItemStack before = getStackInSlot(slot);
            ItemStack result = super.extractItem(slot, amount, simulate);
            if (showWand)
                showCraftableWand();
            if (slot == INVENTORY_STACK_WAND
                    && ItemHelper.isWand(before))
                craftWand();
            return result;
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            setStackInSlot(slot, stack, true);
        }

        public void setStackInSlot(int slot, @Nonnull ItemStack stack, boolean showWand) {
            ItemStack before = getStackInSlot(slot);
            super.setStackInSlot(slot, stack);
            if (showWand)
                showCraftableWand();
            if (slot == INVENTORY_STACK_WAND
                    && ItemHelper.isWand(before)
                    && stack.isEmpty())
                craftWand();
        }

        /**
         * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
         *
         * @param index
         * @param count
         */
        @Nonnull
        @Override
        public ItemStack decrStackSize(int index, int count) {
            return decrStackSize(index, count, true);
        }

        public ItemStack decrStackSize(int index, int count, boolean showWand) {
            if (showWand && index < INVENTORY_STACK_WAND && !getStackInSlot(index).isEmpty() && getStackInSlot(index).getCount() > count)
                showCraftableWand();
            else if (index == INVENTORY_STACK_WAND
                    && !getStackInSlot(index).isEmpty())
                craftWand();
            return super.decrStackSize(index, count);
        }

        /**
         * Removes a stack from the given slot and returns it.
         *
         * @param index
         */
        @Nonnull
        @Override
        public ItemStack removeStackFromSlot(int index) {
            return removeStackFromSlot(index, true);
        }

        public ItemStack removeStackFromSlot(int index, boolean showWand) {
            if (showWand)
                showCraftableWand();
            if (index == INVENTORY_STACK_WAND && ItemHelper.isWand(getStackInSlot(index)))
                craftWand();
            return super.removeStackFromSlot(index);
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return super.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            super.deserializeNBT(nbt);
        }
    }
}
