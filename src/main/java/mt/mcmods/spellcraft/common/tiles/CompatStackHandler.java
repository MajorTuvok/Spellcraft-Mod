package mt.mcmods.spellcraft.common.tiles;

import mcp.MethodsReturnNonnullByDefault;
import mt.mcmods.spellcraft.common.interfaces.ICompatStackHandlerModifiable;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.interfaces.IMarkDirtyCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CompatStackHandler extends ItemStackHandler implements ICompatStackHandlerModifiable, ILoggable {
    private static final String NBT_NAME_HAS_NAME = "Has Name";
    private static final String NBT_NAME_NAME = "Name";
    private ItemStackHandlerListener mItemStackHandlerListener;
    private IMarkDirtyCallback mMarkDirtyCallback;
    private String mName;
    private PlayerInteractionListener mPlayerInteractionListener;
    private PlayerRestrictionProvider mPlayerRestrictionProvider;

    public CompatStackHandler() {
        super();
        init();
    }

    public CompatStackHandler(int size) {
        super(size);
        init();
    }

    public CompatStackHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
        init();
    }

    @Override
    public void setDirtyMarkListener(IMarkDirtyCallback dirtyMarkListener) {
        this.mMarkDirtyCallback = dirtyMarkListener;
    }

    @Override
    public void setPlayerRestrictionProvider(PlayerRestrictionProvider playerRestrictionProvider) {
        this.mPlayerRestrictionProvider = playerRestrictionProvider;
    }

    @Override
    public void setPlayerInteractionListener(PlayerInteractionListener playerInteractionListener) {
        this.mPlayerInteractionListener = playerInteractionListener;
    }

    @Override
    public void setItemStackHandlerListener(ItemStackHandlerListener itemStackHandlerListener) {
        this.mItemStackHandlerListener = itemStackHandlerListener;
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
        for (ItemStack stack :
                stacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    //------------------ItemStackHandler Methods ----------------------------------------------

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
        ItemStack stack = stacks.get(index);
        if (!stack.isEmpty()) {
            stacks.set(index, ItemStack.EMPTY);
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
        setStackInSlot(index, stack);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    @Override
    public int getInventoryStackLimit() {
        return getSlotLimit(0);
    }

    //-------------------------IInventory Methods -------------------------------

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
        if (mPlayerInteractionListener != null) mPlayerInteractionListener.openInventory(player, stacks);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (mPlayerInteractionListener != null) mPlayerInteractionListener.closeInventory(player, stacks);
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
        ItemStack existing = this.stacks.get(index);
        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return false;
        }
        return true;
    }

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
        setSize(stacks.size());
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

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        super.setStackInSlot(slot, stack);
        markDirty();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        markDirty();
        return super.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        markDirty();
        return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        markDirty();
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tagCompound = super.serializeNBT();
        if (hasCustomName()) {
            tagCompound.setString(NBT_NAME_NAME, mName);
        }
        return tagCompound;
    }

//---------------------Custom Methods ---------------------------------

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        if (nbt.hasKey(NBT_NAME_NAME)) {
            mName = nbt.getString(NBT_NAME_NAME);
        } else {
            mName = null;
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (mItemStackHandlerListener != null) mItemStackHandlerListener.onLoad();
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (mItemStackHandlerListener != null) mItemStackHandlerListener.onContentChanged(slot, stacks);
    }

    public @Nonnull
    NonNullList<ItemStack> getSubStack(int singleStack) { //TODO return implementation wrapper
        validateSlotIndex(singleStack);
        NonNullList<ItemStack> inv = NonNullList.create();
        inv.add(stacks.get(singleStack));
        return inv;
    }

    public @Nonnull
    NonNullList<ItemStack> getSubStacks(int from, int to) {
        validateSlotIndex(from);
        validateSlotIndex(to);
        NonNullList<ItemStack> inv = NonNullList.create();
        if (from < to) {
            for (int i = from; i <= to; ++i) {
                inv.add(stacks.get(i));
            }
        } else if (from > to) {
            for (int i = from; i >= to; --i) {
                inv.add(stacks.get(i));
            }
        }
        return inv;
    }

    protected void init() {
        mName = null;
        mMarkDirtyCallback = null;
        mPlayerInteractionListener = null;
        mItemStackHandlerListener = null;
        mPlayerRestrictionProvider = null;
    }
}
