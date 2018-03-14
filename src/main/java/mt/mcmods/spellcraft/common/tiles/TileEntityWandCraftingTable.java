package mt.mcmods.spellcraft.common.tiles;

import mt.mcmods.spellcraft.CommonProxy;
import mt.mcmods.spellcraft.common.interfaces.ICompatStackHandlerModifiable;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import mt.mcmods.spellcraft.common.recipes.WandRegistry;
import mt.mcmods.spellcraft.common.util.NBTHelper;
import mt.mcmods.spellcraft.common.util.item.ItemHelper;
import mt.mcmods.spellcraft.common.util.item.StackHandlers;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TileEntityWandCraftingTable extends BaseTileEntityWithInventory {
    public static final int INVENTORY_SLOTS = 5;
    //better would be an enum - TODO replace with enum
    public static final int INVENTORY_STACK_CORE = 1;
    public static final int INVENTORY_STACK_CRYSTAL = 2;
    public static final int INVENTORY_STACK_TIP = 0;
    public static final int INVENTORY_STACK_WAND = 4;
    public static final int INVENTORY_STACK_WOOD = 3;
    private static final String KEY_COMPOUNDS = "TileEntityWandCraftingTable_compound_list";
    private static final String KEY_WANDS = "TileEntityWandCraftingTable_wand_list";
    private Map<ItemWand, NBTTagCompound> mWandNBTTagMap;

    public TileEntityWandCraftingTable() {
        super(INVENTORY_SLOTS);
        mWandNBTTagMap = new HashMap<>();
    }

    public ItemStack getTipCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_TIP);
    }

    public void setTipCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_TIP, stack);
    }

    public ItemStack getCoreCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_CORE);
    }

    public void setCoreCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_CORE, stack);
    }

    public ItemStack getCrystalCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_CRYSTAL);
    }

    public void setCrystalCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_CRYSTAL, stack);
    }

    public ItemStack getWoodCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_WOOD);
    }

    public void setWoodCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_WOOD, stack);
    }

    public ItemStack getWandCraftingStack() {
        return getStackInSlot(INVENTORY_STACK_WAND);
    }

    public void setWandCraftingStack(ItemStack stack) {
        getInventory().setStackInSlot(INVENTORY_STACK_WAND, stack);
    }

    @Override
    protected ICompatStackHandlerModifiable getInventory() {
        return (WandCraftingStackHandler) super.getInventory();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey(KEY_WANDS) && compound.hasKey(KEY_COMPOUNDS)) {
            Iterator<NBTBase> wands = ((NBTTagList) compound.getTag(KEY_WANDS)).iterator();
            Iterator<NBTBase> compounds = ((NBTTagList) compound.getTag(KEY_COMPOUNDS)).iterator();
            while (wands.hasNext() && compounds.hasNext()) {
                ResourceLocation registryName = NBTHelper.deserializeResourceLocation((NBTTagString) wands.next());
                NBTTagCompound compoundTag = (NBTTagCompound) compounds.next();
                if (registryName != null && compoundTag != null && CommonProxy.ITEM_REGISTRY.containsKey(registryName)) {
                    Item wand = CommonProxy.ITEM_REGISTRY.getValue(registryName);
                    if (wand instanceof ItemWand) {
                        ItemWand itemWand = (ItemWand) wand;
                        mWandNBTTagMap.put(itemWand, compoundTag);
                    } else {
                        Log.warn("Could not deserialize TileEntityWandCraftingTable wand-compound map entry. Retrieved Item but item for " + registryName + " was not an ItemWand! Some Mod overrode this with an incompatible Item!");
                    }
                } else {
                    Log.debug("Could not deserialize TileEntityWandCraftingTable wand-compound map entry. This is probably due to updating Mods.");
                }
            }
            if (wands.hasNext()) {
                Log.warn("TileEntityWandCraftingTable:Noticed corrupted NBT-Data! There are too many Wand-Entries for the given Compounds! This may lead to errors further down the line!");
            } else if (compounds.hasNext()) {
                Log.warn("TileEntityWandCraftingTable:Noticed corrupted NBT-Data! There are too many Compound-Entries for the given Compounds! This may lead to errors further down the line!");
            }
        } else {
            Log.debug("Could not deserialize TileEntityWandCraftingTable wand-compound map. This is probably due to updating Spellcraft.");
        }
        super.readFromNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList wands = new NBTTagList();
        NBTTagList compounds = new NBTTagList();
        for (Map.Entry<ItemWand, NBTTagCompound> entry :
                mWandNBTTagMap.entrySet()) {
            wands.appendTag(NBTHelper.serializeResourceLocation(entry.getKey().getRegistryName()));
            compounds.appendTag(entry.getValue());
        }
        compound.setTag(KEY_WANDS, wands);
        compound.setTag(KEY_COMPOUNDS, compounds);
        return super.writeToNBT(compound);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandlerForFacing(facing));
        }
        return super.getCapability(capability, facing);
    }

    /**
     * Like the old updateEntity(), except more generic.
     * Called every Tick.
     */
    @Override
    public void update() {
        super.update();
        showCraftableWand();
    }

    @Override
    protected ICompatStackHandlerModifiable createInventory(int size) {
        return new WandCraftingStackHandler(size);
    }

    private boolean hasCraftableWand() {
        return hasCompatibleWood() && WandRegistry.INSTANCE.hasWand(getTipCraftingStack(), getCoreCraftingStack());
    }

    private void craftWand(ItemWand wand) {
        if (hasCraftableWand()) {
            setTipCraftingStack(ItemHelper.decreaseStackSize(getTipCraftingStack(), 1));
            setCoreCraftingStack(ItemHelper.decreaseStackSize(getCoreCraftingStack(), 1));
            setWoodCraftingStack(ItemHelper.decreaseStackSize(getWoodCraftingStack(), 1));
            mWandNBTTagMap.remove(wand);
        }
    }

    private void showCraftableWand() {
        if (hasCraftableWand()) {
            ItemWand itemWand = WandRegistry.INSTANCE.getWand(getTipCraftingStack(), getCoreCraftingStack());
            if (itemWand != null && !ItemHelper.areItemsEqual(getWandCraftingStack(), itemWand)) {
                ItemStack stack;
                if (mWandNBTTagMap.containsKey(itemWand)) {
                    stack = new ItemStack(itemWand);
                    stack.setTagCompound(mWandNBTTagMap.get(itemWand));
                } else {
                    stack = itemWand.getDefaultInstance();
                }
                setWandCraftingStack(stack);
            } else if (itemWand == null) {
                setWandCraftingStack(ItemStack.EMPTY);
            }
            checkStackCompound(itemWand);
        } else {
            setWandCraftingStack(ItemStack.EMPTY);
        }
    }

    private void checkStackCompound(ItemWand itemWand) {
        if (!getWandCraftingStack().isEmpty()) {
            ItemStack stack = getWandCraftingStack();
            if (itemWand != stack.getItem() && mWandNBTTagMap.containsKey(itemWand)) {
                stack.setTagCompound(mWandNBTTagMap.get(itemWand));
            } else {
                mWandNBTTagMap.put(itemWand, stack.getTagCompound());
                markDirty();
            }
        }
    }

    private IItemHandler getItemHandlerForFacing(@Nullable EnumFacing facing) {
        if (facing == null) {
            return getInventory();
        } else {
            switch (facing) {
                case NORTH:
                    return StackHandlers.asModifiableHandlerOnSpecificSlots(getInventory(), INVENTORY_STACK_TIP, INVENTORY_STACK_TIP);
                case EAST:
                    return StackHandlers.asModifiableHandlerOnSpecificSlots(getInventory(), INVENTORY_STACK_CORE, INVENTORY_STACK_CORE);
                case SOUTH:
                    return StackHandlers.asModifiableHandlerOnSpecificSlots(getInventory(), INVENTORY_STACK_CRYSTAL, INVENTORY_STACK_CRYSTAL);
                case WEST:
                    return StackHandlers.asModifiableHandlerOnSpecificSlots(getInventory(), INVENTORY_STACK_WOOD, INVENTORY_STACK_WOOD);
                default: {
                    return StackHandlers.asModifiableHandlerOnSpecificSlots(getInventory(), INVENTORY_STACK_WAND, INVENTORY_STACK_WAND);
                }
            }
        }
    }

    private boolean hasCompatibleWood() {
        return !ItemHelper.isEmptyOrNull(getWoodCraftingStack())
                && getWoodCraftingStack().getItem() instanceof ItemBlock
                && ((ItemBlock) getWoodCraftingStack().getItem()).getBlock() == Blocks.LOG;
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
                    && ItemHelper.isWand(getStackInSlot(index)))
                craftWand((ItemWand) getStackInSlot(index).getItem());
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
                craftWand((ItemWand) getStackInSlot(index).getItem());
            return super.removeStackFromSlot(index);
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            ItemStack before = getStackInSlot(slot);
            super.setStackInSlot(slot, stack);
            if (slot == INVENTORY_STACK_WAND
                    && ItemHelper.isWand(before)
                    && stack.isEmpty()) {
                craftWand((ItemWand) before.getItem());
            }
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack before = getStackInSlot(slot);
            ItemStack result = super.extractItem(slot, amount, simulate);
            if (slot == INVENTORY_STACK_WAND
                    && ItemHelper.isWand(before)) {
                craftWand((ItemWand) before.getItem());
            }
            return result;
        }
    }
}
