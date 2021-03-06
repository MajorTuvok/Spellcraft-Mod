package mt.mcmods.spellcraft.common.tiles;

import com.google.common.collect.Lists;
import mt.mcmods.spellcraft.common.interfaces.ICompatStackHandler;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.interfaces.IMarkDirtyCallback;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BaseTileEntityWithInventory extends BaseTileEntity implements IInventory, IMarkDirtyCallback, ILoggable, ITickable {
    private static final String KEY_INVENTORY = "BaseTileEntityWithInventory_inventoryCompound";
    private ICompatStackHandler inventory;
    private ArrayList<TickingRunnable> tickingRunnables;

    public BaseTileEntityWithInventory(int size) {
        inventory = createInventory(size);
        inventory.setDirtyMarkListener(this);
        tickingRunnables = Lists.newArrayListWithExpectedSize(10);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    /**
     * Returns the stack in the given slot.
     *
     * @param index
     */
    @Override
    public @Nonnull
    ItemStack getStackInSlot(int index) {
        return inventory.getStackInSlot(index);
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
        return inventory.decrStackSize(index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     *
     * @param index
     */
    @Override
    public @Nonnull
    ItemStack removeStackFromSlot(int index) {
        return inventory.removeStackFromSlot(index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     *
     * @param index
     * @param stack
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.setInventorySlotContents(index, stack);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     *
     * @param player
     */
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return inventory.isUsableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        inventory.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        inventory.closeInventory(player);
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
        return inventory.isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return inventory.getFieldCount();
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
    public String getName() {
        return inventory.getName();
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName() {
        return inventory.hasCustomName();
    }

    public List<ItemStack> getInvContent() {
        List<ItemStack> stacks = new ArrayList<>(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) stacks.add(stack);
        }
        return stacks;
    }

    protected ICompatStackHandler getInventory() {
        return inventory;
    }

    protected long getTotalTime() {
        return getWorld().getTotalWorldTime();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag(KEY_INVENTORY));
        super.readFromNBT(compound);
    }

    @Override
    public @Nonnull
    NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag(KEY_INVENTORY, inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public @Nullable
    <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            @SuppressWarnings("unchecked")
            T res = (T) inventory;
            ;
            return res;
        }
        return super.getCapability(capability, facing);
    }

    public List<EntityItem> getItemDrops(World world, BlockPos pos) {
        List<EntityItem> stacks = new ArrayList<>(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                double xVariation = world.rand.nextDouble() * 0.8D + 0.1;
                double yVariation = world.rand.nextDouble() * 0.8D + 0.1;
                double zVariation = world.rand.nextDouble() * 0.8D + 0.1;
                stacks.add(new EntityItem(world, pos.getX() + xVariation, pos.getY() + yVariation, pos.getZ() + zVariation, stack));
            }
        }
        return stacks;
    }

    public void spawnItemDrops(World world, BlockPos pos) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                double xVariation = world.rand.nextDouble() * 0.8D + 0.1;
                double yVariation = world.rand.nextDouble() * 0.8D + 0.1;
                double zVariation = world.rand.nextDouble() * 0.8D + 0.1;
                EntityItem item = new EntityItem(world, pos.getX() + xVariation, pos.getY() + yVariation, pos.getZ() + zVariation, stack);
                if (stack.hasTagCompound()) {
                    item.getItem().setTagCompound(stack.getTagCompound());
                }
                world.spawnEntity(item);
            }
        }
    }

    /**
     * Like the old updateEntity(), except more generic.
     * Called every Tick.
     */
    @Override
    public void update() {
        for (TickingRunnable tickingRunnable :
                tickingRunnables) {
            if (getTotalTime() <= tickingRunnable.getTotalTimeOfStop()) {
                tickingRunnable.onTick(getTotalTime() - tickingRunnable.getStartTime());
            } else {
                tickingRunnable.onFinished();
                tickingRunnables.remove(tickingRunnable);
            }
        }
    }

    protected ICompatStackHandler createInventory(int size) {
        return new CompatStackHandler(size);
    }

    protected void registerForUpdates(TickingRunnable tickingRunnable) {
        tickingRunnable.setStartTime(getTotalTime());
        tickingRunnables.add(tickingRunnable);
    }

    protected void deregisterForUpdates(TickingRunnable tickingRunnable) {
        tickingRunnables.remove(tickingRunnable);
        tickingRunnable.onAborted();
    }

    protected abstract class TickingRunnable {
        private final long totalTimeOfStop;
        private long startTime;

        public TickingRunnable(long totalTimeOfStop) {
            this.totalTimeOfStop = totalTimeOfStop;
            this.startTime = 0;
        }

        public long getTotalTimeOfStop() {
            return totalTimeOfStop;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public abstract void onTick(long ticksElapsed);

        public abstract void onFinished();

        public void onAborted() {

        }
    }
}
