package mt.mcmods.spellcraft.common.tiles;

import mt.mcmods.spellcraft.common.interfaces.ICompatStackHandler;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import mt.mcmods.spellcraft.common.registry.WandRegistry;
import mt.mcmods.spellcraft.common.util.item.ItemHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityWandCraftingTable extends BaseTileEntityWithInventory {
    public static final int INVENTORY_SLOTS = 5;
    public static final int INVENTORY_STACK_CORE = 1;
    public static final int INVENTORY_STACK_CRYSTAL = 2;
    //better would be an enum - TODO replace with enum
    public static final int INVENTORY_STACK_TIP = 0;
    public static final int INVENTORY_STACK_WAND = 4;
    public static final int INVENTORY_STACK_WOOD = 3;

    public TileEntityWandCraftingTable() {
        super(INVENTORY_SLOTS);
    }

    public boolean hasCraftableWand() {
        return hasCompatibleWood() && WandRegistry.INSTANCE.hasWand(getTipCraftingStack(), getCoreCraftingStack());
    }

    public void setTipCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_TIP, stack);
    }

    public void setCoreCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_CORE, stack);
    }

    public void setCrystalCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_CRYSTAL, stack);
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

    public void setWoodCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_WOOD, stack);
    }

    public void setWandCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_WAND, stack);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandlerForFacing(facing));
        }
        return super.getCapability(capability, facing);
    }

    public void showCraftableWand() {
        if (hasCraftableWand()) {
            ItemWand itemWand = WandRegistry.INSTANCE.getWand(getTipCraftingStack(), getCoreCraftingStack());
            if (itemWand != null && !ItemHelper.areItemsEqual(getWandCraftingStack(), itemWand)) {
                ItemStack stack = new ItemStack(itemWand);
                setWandCraftingStack(stack);
            } else if (itemWand == null) {
                setWandCraftingStack(ItemStack.EMPTY);
            }
        } else {
            setWandCraftingStack(ItemStack.EMPTY);
        }
    }

    private IItemHandler getItemHandlerForFacing(@Nullable EnumFacing facing) {
        if (facing == null) {
            return getInventory();
        } else {
            switch (facing) {
                case NORTH:
                    return new CompatStackHandler(getInventory().getSubStack(INVENTORY_STACK_TIP));
                case EAST:
                    return new CompatStackHandler(getInventory().getSubStack(INVENTORY_STACK_CORE));
                case SOUTH:
                    return new CompatStackHandler(getInventory().getSubStack(INVENTORY_STACK_CRYSTAL));
                case WEST:
                    return new CompatStackHandler(getInventory().getSubStack(INVENTORY_STACK_WOOD));
                default: {
                    return new CompatStackHandler(getInventory().getSubStack(INVENTORY_STACK_WAND));
                }
            }
        }
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
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack before = getStackInSlot(slot);
            ItemStack result = super.extractItem(slot, amount, simulate);
            if (slot == INVENTORY_STACK_WAND
                    && ItemHelper.isWand(before))
                craftWand();
            return result;
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            ItemStack before = getStackInSlot(slot);
            super.setStackInSlot(slot, stack);
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
            if (index == INVENTORY_STACK_WAND
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
            if (index == INVENTORY_STACK_WAND && ItemHelper.isWand(getStackInSlot(index)))
                craftWand();
            return super.removeStackFromSlot(index);
        }
    }
}
