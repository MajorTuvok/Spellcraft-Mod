package mt.mcmods.spellcraft.common.util.item;

import mcp.MethodsReturnNonnullByDefault;
import mt.mcmods.spellcraft.common.interfaces.ICompatStackHandler;
import mt.mcmods.spellcraft.common.interfaces.ICompatStackHandlerModifiable;
import mt.mcmods.spellcraft.common.interfaces.IMarkDirtyCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public final class StackHandlers {
    private StackHandlers() {
        throw new AssertionError("This class may never be instantiated!");  //prevent Reflection
    }

    public static void setStackInNonModifiableHandler(@Nonnull IItemHandler handler, @Nonnull ItemStack stack, int slot) {
        ItemStack stackInSlot = handler.getStackInSlot(slot);
        handler.extractItem(slot, stackInSlot.getCount(), false);
        if (!stack.isEmpty()) {
            handler.insertItem(slot, stack, false);
        }
    }

    public static @Nonnull
    ICompatStackHandlerModifiable asModifiableCompatHandler(@Nonnull IItemHandler handler) {
        if (handler instanceof IItemHandlerModifiable) {
            return new ModifiableHandlerWrappingModifiableStackHandler((IItemHandlerModifiable) handler);
        } else {
            return new ModifiableHandlerWrappingStackHandler(handler);
        }
    }

    public static @Nonnull
    ICompatStackHandler asNonModifiableCompatHandler(@Nonnull IItemHandler handler) {
        if (handler instanceof IItemHandlerModifiable) {
            return new HandlerWrappingModifiableStackHandler((IItemHandlerModifiable) handler);
        } else {
            return new HandlerWrappingStackHandler(handler);
        }
    }

    public static @Nonnull
    ICompatStackHandlerModifiable asModifiableHandlerOnSpecificSlots(@Nonnull IItemHandler handler, int startIndex, int endIndex) {
        if (handler instanceof IItemHandlerModifiable) {
            return new ModifiablePartialHandlerWrappingModifiableStackHandler((IItemHandlerModifiable) handler, startIndex, endIndex);
        } else {
            return new ModifiablePartialHandlerWrappingStackHandler(handler, startIndex, endIndex);
        }
    }

    public static @Nonnull
    ICompatStackHandler asNonModifiableHandlerOnSpecificSlots(@Nonnull IItemHandler handler, int startIndex, int endIndex) {
        if (handler instanceof IItemHandlerModifiable) {
            return new PartialHandlerWrappingModifiableStackHandler((IItemHandlerModifiable) handler, startIndex, endIndex);
        } else {
            return new PartialHandlerWrappingStackHandler(handler, startIndex, endIndex);
        }
    }

    public static @Nonnull
    ICompatStackHandler asImmutableHandlerOnSpecificSlots(@Nonnull IItemHandler handler, int startIndex, int endIndex) {
        if (handler instanceof IItemHandlerModifiable) {
            return new ImmutablePartialHandlerWrappingModifiableStackHandler((IItemHandlerModifiable) handler, startIndex, endIndex);
        } else {
            return new ImmutablePartialHandlerWrappingStackHandler(handler, startIndex, endIndex);
        }
    }

    public static @Nonnull
    NonNullList<ItemStack> getStacksFromHandler(@Nonnull IItemHandler handler) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < handler.getSlots(); ++i) {
            stacks.add(handler.getStackInSlot(i));
        }
        return stacks;
    }

    private static abstract class BaseCompatStackHandler implements ICompatStackHandler {
        private static final String NBT_NAME = "Name";
        private ItemStackHandlerListener mItemStackHandlerListener;
        private IMarkDirtyCallback mMarkDirtyCallback;
        private String mName;
        private PlayerInteractionListener mPlayerInteractionListener;
        private PlayerRestrictionProvider mPlayerRestrictionProvider;

        protected BaseCompatStackHandler() {
            mName = null;
            mMarkDirtyCallback = null;
            mPlayerInteractionListener = null;
            mItemStackHandlerListener = null;
            mPlayerRestrictionProvider = null;
        }

        @Override
        public void setDirtyMarkListener(IMarkDirtyCallback dirtyMarkListener) {
            mMarkDirtyCallback = dirtyMarkListener;
        }

        @Override
        public void setPlayerRestrictionProvider(PlayerRestrictionProvider playerRestrictionProvider) {
            mPlayerRestrictionProvider = playerRestrictionProvider;
        }

        @Override
        public void setPlayerInteractionListener(PlayerInteractionListener playerInteractionListener) {
            mPlayerInteractionListener = playerInteractionListener;
        }

        @Override
        public void setItemStackHandlerListener(ItemStackHandlerListener itemStackHandlerListener) {
            mItemStackHandlerListener = itemStackHandlerListener;
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public abstract int getSlots();

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            markDirty();
            return getStack(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }

            validateSlotIndex(slot);

            ItemStack existing = this.getStack(slot);

            int limit = getStackLimit(slot, stack);

            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                    return stack;
                }

                limit -= existing.getCount();
            }

            if (limit <= 0) {
                return stack;
            }

            boolean reachedLimit = stack.getCount() > limit;

            if (!simulate) {
                markDirty();
                if (existing.isEmpty()) {
                    this.setStack((reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack), slot);
                } else {
                    existing.grow(reachedLimit ? limit : stack.getCount());
                }
                onContentsChanged(slot);
            }

            return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount == 0) {
                return ItemStack.EMPTY;
            }

            validateSlotIndex(slot);

            ItemStack existing = this.getStack(slot);

            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            }

            int toExtract = Math.min(amount, existing.getMaxStackSize());

            if (existing.getCount() <= toExtract) {
                if (!simulate) {
                    markDirty();
                    this.setStack(ItemStack.EMPTY, slot);
                    onContentsChanged(slot);
                }
                return existing;
            } else {
                if (!simulate) {
                    markDirty();
                    this.setStack(ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract), slot);
                    onContentsChanged(slot);
                }

                return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
            }
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            return getStackLimit(slot, getStack(slot));
        }

        /**
         * Returns the number of slots in the inventory.
         */
        @Override
        public int getSizeInventory() {
            return getSlots();
        }

        @Override
        public boolean isEmpty() {
            for (int i = 0; i < getSlots(); ++i) {
                if (!getStack(i).isEmpty()) return false;
            }
            return true;
        }

        /**
         * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
         *
         * @param index
         * @param count
         */
        @Override
        public @Nonnull
        ItemStack decrStackSize(int index, int count) {
            return extractItem(index, count, false);
        }

        /**
         * Removes a stack from the given slot and returns it.
         *
         * @param index
         */
        @Override
        public @Nonnull
        ItemStack removeStackFromSlot(int index) {
            validateSlotIndex(index);
            ItemStack stack = getStack(index);
            if (!stack.isEmpty()) {
                setStack(ItemStack.EMPTY, index);
                markDirty();
                onContentsChanged(index);
            }
            return stack;
        }

        /**
         * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
         *
         * @param index
         * @param stack
         */
        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
            setStack(stack, index);
        }

        /**
         * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
         */
        @Override
        public int getInventoryStackLimit() {
            return getSlotLimit(0);
        }

        //------------------ItemStackHandler Methods ----------------------------------------------

        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
         * hasn't changed and skip it.
         */
        @Override
        public void markDirty() {
            if (mMarkDirtyCallback != null) mMarkDirtyCallback.markDirty();
        }

        /**
         * Don't rename this method to canInteractWith due to conflicts with Container
         *
         * @param player
         */
        @Override
        public boolean isUsableByPlayer(EntityPlayer player) {
            return (mPlayerRestrictionProvider != null && mPlayerRestrictionProvider.isUsableByPlayer(player)) || mPlayerInteractionListener == null || mPlayerInteractionListener.isUsableByPlayer(player);
        }

        @Override
        public void openInventory(EntityPlayer player) {
            if (mPlayerInteractionListener != null) mPlayerInteractionListener.openInventory(player, getStacks());
        }

        @Override
        public void closeInventory(EntityPlayer player) {
            if (mPlayerInteractionListener != null) mPlayerInteractionListener.closeInventory(player, getStacks());
        }

        /**
         * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
         * guis use Slot.isItemValid
         *
         * @param index
         * @param stack
         */
        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            ItemStack existing = this.getStack(index);
            return existing.isEmpty() || ItemHandlerHelper.canItemStacksStack(stack, existing);
        }

        //-------------------------IInventory Methods -------------------------------

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {
            for (int i = 0; i < getSlots(); ++i) {
                setStack(ItemStack.EMPTY, i);
            }
        }

        /**
         * Get the name of this object. For players this returns their username
         */
        @Override
        @MethodsReturnNonnullByDefault
        public String getName() {
            return mName == null ? "" : mName;
        }

        public void setName(String name) {
            this.mName = name;
        }

        /**
         * Returns true if this thing is named
         */
        @Override
        public boolean hasCustomName() {
            return this.mName != null && !this.mName.equals("");
        }

        /**
         * Get the formatted ChatComponent that will be used for the sender's username in chat
         */
        @Override
        public ITextComponent getDisplayName() {
            return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
        }

        protected IMarkDirtyCallback getMarkDirtyCallback() {
            return mMarkDirtyCallback;
        }

        protected PlayerRestrictionProvider getPlayerRestrictionProvider() {
            return mPlayerRestrictionProvider;
        }

        protected PlayerInteractionListener getPlayerInteractionListener() {
            return mPlayerInteractionListener;
        }

        protected ItemStackHandlerListener getItemStackHandlerListener() {
            return mItemStackHandlerListener;
        }

        protected List<ItemStack> getStacks() {
            return getStacksFromHandler(this);
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tagCompound = new NBTTagCompound();
            if (hasCustomName()) {
                tagCompound.setString(NBT_NAME, mName);
            }
            return tagCompound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            if (nbt.hasKey(NBT_NAME)) {
                mName = nbt.getString(NBT_NAME);
            } else {
                mName = null;
            }
        }

        protected abstract void setStack(ItemStack stack, int slot);

        protected abstract ItemStack getStack(int slot);

        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
        }

        //---------------------Custom Methods ---------------------------------

        protected void onLoad() {
            if (mItemStackHandlerListener != null) mItemStackHandlerListener.onLoad();
        }

        protected void onContentsChanged(int slot) {
            if (mItemStackHandlerListener != null) mItemStackHandlerListener.onContentChanged(slot, getStacks());
        }

        protected void validateSlotIndex(int index) {
            if (index < 0 || index >= getSlots()) {
                throw new IndexOutOfBoundsException("Index of " + index + " is too large!");
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------------------------------


    private static class ModifiableHandlerWrappingModifiableStackHandler extends BaseCompatStackHandler implements ICompatStackHandlerModifiable {
        private IItemHandlerModifiable mItemHandler;

        private ModifiableHandlerWrappingModifiableStackHandler(@Nonnull IItemHandlerModifiable itemHandler) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mItemHandler.getSlots();
        }

        /**
         * Overrides the stack in the given slot. This method is used by the
         * standard Forge helper methods and classes. It is not intended for
         * general use by other mods, and the handler may throw an error if it
         * is called unexpectedly.
         *
         * @param slot  Slot to modify
         * @param stack ItemStack to set slot to (may be null)
         * @throws RuntimeException if the handler is called in a way that the handler
         *                          was not expecting.
         **/
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            setStack(stack, slot);
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {
            mItemHandler.setStackInSlot(slot, stack);
        }

        @Override
        protected ItemStack getStack(int slot) {
            return mItemHandler.getStackInSlot(slot);
        }


        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            markDirty();
            return mItemHandler.extractItem(slot, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            markDirty();
            return mItemHandler.insertItem(slot, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            return mItemHandler.getSlotLimit(slot);
        }
    }

    private static class ModifiableHandlerWrappingStackHandler extends BaseCompatStackHandler implements ICompatStackHandlerModifiable {
        private IItemHandler mItemHandler;

        private ModifiableHandlerWrappingStackHandler(@Nonnull IItemHandler itemHandler) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {
            setStackInNonModifiableHandler(this, stack, slot);
        }

        @Override
        protected ItemStack getStack(int slot) {
            return mItemHandler.getStackInSlot(slot);
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mItemHandler.getSlots();
        }

        /**
         * Overrides the stack in the given slot. This method is used by the
         * standard Forge helper methods and classes. It is not intended for
         * general use by other mods, and the handler may throw an error if it
         * is called unexpectedly.
         *
         * @param slot  Slot to modify
         * @param stack ItemStack to set slot to (may be null)
         * @throws RuntimeException if the handler is called in a way that the handler
         *                          was not expecting.
         **/
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            setStack(stack, slot);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            markDirty();
            return mItemHandler.extractItem(slot, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            markDirty();
            return mItemHandler.insertItem(slot, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            return mItemHandler.getSlotLimit(slot);
        }
    }

    private static class HandlerWrappingModifiableStackHandler extends BaseCompatStackHandler {
        private IItemHandlerModifiable mItemHandler;

        private HandlerWrappingModifiableStackHandler(@Nonnull IItemHandlerModifiable itemHandler) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {
            mItemHandler.setStackInSlot(slot, stack);
        }

        @Override
        protected ItemStack getStack(int slot) {
            return mItemHandler.getStackInSlot(slot);
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mItemHandler.getSlots();
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            markDirty();
            return mItemHandler.extractItem(slot, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            markDirty();
            return mItemHandler.insertItem(slot, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            return mItemHandler.getSlotLimit(slot);
        }
    }

    private static class HandlerWrappingStackHandler extends BaseCompatStackHandler {
        private IItemHandler mItemHandler;

        private HandlerWrappingStackHandler(@Nonnull IItemHandler itemHandler) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {
            setStackInNonModifiableHandler(this, stack, slot);
        }

        @Override
        protected ItemStack getStack(int slot) {
            return mItemHandler.getStackInSlot(slot);
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mItemHandler.getSlots();
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            markDirty();
            return mItemHandler.extractItem(slot, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            markDirty();
            return mItemHandler.insertItem(slot, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            return mItemHandler.getSlotLimit(slot);
        }
    }

    private static class ModifiablePartialHandlerWrappingModifiableStackHandler extends BaseCompatStackHandler implements ICompatStackHandlerModifiable {
        private int mEndIndex;
        private IItemHandlerModifiable mItemHandler;
        private int mSize;
        private int mStartIndex;

        private ModifiablePartialHandlerWrappingModifiableStackHandler(@Nonnull IItemHandlerModifiable itemHandler, int startIndex, int endIndex) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
            if (startIndex > endIndex || startIndex < 0 || endIndex >= mItemHandler.getSlots()) {
                throw new IndexOutOfBoundsException("ItemHandler Index [" + startIndex + "; " + endIndex + "] are out of bounds [0; " + mItemHandler.getSlots() + "]!");
            }
            mStartIndex = startIndex;
            mEndIndex = endIndex;
            mSize = mEndIndex - mStartIndex + 1;
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {
            validateSlotIndex(slot);
            mItemHandler.setStackInSlot(slot + mStartIndex, stack);
        }

        @Override
        protected ItemStack getStack(int slot) {
            validateSlotIndex(slot);
            return mItemHandler.getStackInSlot(slot + mStartIndex);
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mSize;
        }

        /**
         * Overrides the stack in the given slot. This method is used by the
         * standard Forge helper methods and classes. It is not intended for
         * general use by other mods, and the handler may throw an error if it
         * is called unexpectedly.
         *
         * @param slot  Slot to modify
         * @param stack ItemStack to set slot to (may be null)
         * @throws RuntimeException if the handler is called in a way that the handler
         *                          was not expecting.
         **/
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            setStack(stack, slot);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.extractItem(slot + mStartIndex, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.insertItem(slot + mStartIndex, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            return mItemHandler.getSlotLimit(slot + mStartIndex);
        }
    }

    private static class ModifiablePartialHandlerWrappingStackHandler extends BaseCompatStackHandler implements ICompatStackHandlerModifiable {
        private int mEndIndex;
        private IItemHandler mItemHandler;
        private int mSize;
        private int mStartIndex;

        private ModifiablePartialHandlerWrappingStackHandler(@Nonnull IItemHandler itemHandler, int startIndex, int endIndex) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
            if (startIndex > endIndex || startIndex < 0 || endIndex >= mItemHandler.getSlots()) {
                throw new IndexOutOfBoundsException("ItemHandler Index [" + startIndex + "; " + endIndex + "] are out of bounds [0; " + mItemHandler.getSlots() + "]!");
            }
            mStartIndex = startIndex;
            mEndIndex = endIndex;
            mSize = mEndIndex - mStartIndex + 1;
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {
            validateSlotIndex(slot);
            setStackInNonModifiableHandler(this, stack, slot);
        }

        @Override
        protected ItemStack getStack(int slot) {
            validateSlotIndex(slot);
            return mItemHandler.getStackInSlot(slot + mStartIndex);
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mSize;
        }

        /**
         * Overrides the stack in the given slot. This method is used by the
         * standard Forge helper methods and classes. It is not intended for
         * general use by other mods, and the handler may throw an error if it
         * is called unexpectedly.
         *
         * @param slot  Slot to modify
         * @param stack ItemStack to set slot to (may be null)
         * @throws RuntimeException if the handler is called in a way that the handler
         *                          was not expecting.
         **/
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            setStack(stack, slot);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.extractItem(slot + mStartIndex, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.insertItem(slot + mStartIndex, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            return mItemHandler.getSlotLimit(slot + mStartIndex);
        }
    }

    private static class PartialHandlerWrappingModifiableStackHandler extends BaseCompatStackHandler {
        private int mEndIndex;
        private IItemHandlerModifiable mItemHandler;
        private int mSize;
        private int mStartIndex;

        private PartialHandlerWrappingModifiableStackHandler(@Nonnull IItemHandlerModifiable itemHandler, int startIndex, int endIndex) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
            if (startIndex > endIndex || startIndex < 0 || endIndex >= mItemHandler.getSlots()) {
                throw new IndexOutOfBoundsException("ItemHandler Index [" + startIndex + "; " + endIndex + "] are out of bounds [0; " + mItemHandler.getSlots() + "]!");
            }
            mStartIndex = startIndex;
            mEndIndex = endIndex;
            mSize = mEndIndex - mStartIndex + 1;
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {
            validateSlotIndex(slot);
            mItemHandler.setStackInSlot(slot + mStartIndex, stack);
        }

        @Override
        protected ItemStack getStack(int slot) {
            validateSlotIndex(slot);
            return mItemHandler.getStackInSlot(slot + mStartIndex);
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mSize;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.extractItem(slot + mStartIndex, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.insertItem(slot + mStartIndex, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            return mItemHandler.getSlotLimit(slot + mStartIndex);
        }
    }

    private static class PartialHandlerWrappingStackHandler extends BaseCompatStackHandler {
        private int mEndIndex;
        private IItemHandler mItemHandler;
        private int mSize;
        private int mStartIndex;

        private PartialHandlerWrappingStackHandler(@Nonnull IItemHandler itemHandler, int startIndex, int endIndex) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
            if (startIndex > endIndex || startIndex < 0 || endIndex >= mItemHandler.getSlots()) {
                throw new IndexOutOfBoundsException("ItemHandler Index [" + startIndex + "; " + endIndex + "] are out of bounds [0; " + mItemHandler.getSlots() + "]!");
            }
            mStartIndex = startIndex;
            mEndIndex = endIndex;
            mSize = mEndIndex - mStartIndex + 1;
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {
            validateSlotIndex(slot);
            setStackInNonModifiableHandler(this, stack, slot);
        }

        @Override
        protected ItemStack getStack(int slot) {
            validateSlotIndex(slot);
            return mItemHandler.getStackInSlot(slot + mStartIndex);
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mSize;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.extractItem(slot + mStartIndex, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.insertItem(slot + mStartIndex, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            validateSlotIndex(slot);
            return mItemHandler.getSlotLimit(slot + mStartIndex);
        }
    }

    private static class ImmutablePartialHandlerWrappingModifiableStackHandler extends BaseCompatStackHandler {
        private int mEndIndex;
        private IItemHandlerModifiable mItemHandler;
        private int mSize;
        private int mStartIndex;

        private ImmutablePartialHandlerWrappingModifiableStackHandler(@Nonnull IItemHandlerModifiable itemHandler, int startIndex, int endIndex) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
            if (startIndex > endIndex || startIndex < 0 || endIndex >= mItemHandler.getSlots()) {
                throw new IndexOutOfBoundsException("ItemHandler Index [" + startIndex + "; " + endIndex + "] are out of bounds [0; " + mItemHandler.getSlots() + "]!");
            }
            mStartIndex = startIndex;
            mEndIndex = endIndex;
            mSize = mEndIndex - mStartIndex + 1;
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {

        }

        @Override
        protected ItemStack getStack(int slot) {
            validateSlotIndex(slot);
            return mItemHandler.getStackInSlot(slot + mStartIndex);
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mSize;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (!simulate) return ItemStack.EMPTY;
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.extractItem(slot + mStartIndex, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!simulate) return ItemStack.EMPTY;
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.insertItem(slot + mStartIndex, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            return mItemHandler.getSlotLimit(slot + mStartIndex);
        }

        /**
         * No effect
         */
        @Nonnull
        @Override
        public ItemStack removeStackFromSlot(int index) {
            return ItemStack.EMPTY;
        }

        /**
         * No effect
         */
        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
        }
    }

    private static class ImmutablePartialHandlerWrappingStackHandler extends BaseCompatStackHandler {
        private int mEndIndex;
        private IItemHandler mItemHandler;
        private int mSize;
        private int mStartIndex;

        private ImmutablePartialHandlerWrappingStackHandler(@Nonnull IItemHandler itemHandler, int startIndex, int endIndex) {
            super();
            Objects.requireNonNull(itemHandler);
            mItemHandler = itemHandler;
            if (startIndex > endIndex || startIndex < 0 || endIndex >= mItemHandler.getSlots()) {
                throw new IndexOutOfBoundsException("ItemHandler Index [" + startIndex + "; " + endIndex + "] are out of bounds [0; " + mItemHandler.getSlots() + "]!");
            }
            mStartIndex = startIndex;
            mEndIndex = endIndex;
            mSize = mEndIndex - mStartIndex + 1;
        }

        @Override
        protected void setStack(ItemStack stack, int slot) {

        }

        @Override
        protected ItemStack getStack(int slot) {
            validateSlotIndex(slot);
            return mItemHandler.getStackInSlot(slot + mStartIndex);
        }

        /**
         * Returns the number of slots available
         *
         * @return The number of slots available
         **/
        @Override
        public int getSlots() {
            return mSize;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (!simulate) return ItemStack.EMPTY;
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.extractItem(slot + mStartIndex, amount, simulate);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!simulate) return ItemStack.EMPTY;
            validateSlotIndex(slot);
            markDirty();
            return mItemHandler.insertItem(slot + mStartIndex, stack, simulate);
        }

        /**
         * Retrieves the maximum stack size allowed to exist in the given slot.
         *
         * @param slot Slot to query.
         * @return The maximum stack size allowed in the slot.
         */
        @Override
        public int getSlotLimit(int slot) {
            validateSlotIndex(slot);
            return mItemHandler.getSlotLimit(slot + mStartIndex);
        }

        /**
         * No effect
         */
        @Nonnull
        @Override
        public ItemStack removeStackFromSlot(int index) {
            return ItemStack.EMPTY;
        }

        /**
         * No effect
         */
        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
        }
    }
}
