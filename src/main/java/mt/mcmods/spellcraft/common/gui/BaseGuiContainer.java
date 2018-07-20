package mt.mcmods.spellcraft.common.gui;

import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseGuiContainer extends Container implements ILoggable {
    private final PlayerInventoryOffsets mOffsets;
    private InventoryPlayer mInventoryPlayer;
    private TileEntity mTileEntity;

    public BaseGuiContainer(@Nonnull InventoryPlayer playerInv, @Nullable final TileEntity entity, @Nonnull PlayerInventoryOffsets offsets, @Nullable EnumFacing facing) {
        super();
        mOffsets = offsets;
        this.mTileEntity = entity;
        this.mInventoryPlayer = playerInv;
        if (entity != null) {
            if (entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
                IItemHandler itemHandler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                createInventoryFromCapability(itemHandler);
            } else if (entity instanceof IInventory) {
                createInventoryFromIInventory((IInventory) entity);
            }
        }
        createPlayerInventory(playerInv, offsets);
    }

    public InventoryPlayer getPlayerInventory() {
        return mInventoryPlayer;
    }

    public TileEntity getTileEntity() {
        assert mTileEntity != null : "Cannot request TileEntity in class that doesn't provide a TileEntity";
        return mTileEntity;
    }

    public int getSlotCount() {
        return this.inventorySlots.size();
    }

    public PlayerInventoryOffsets getInventoryOffsets() {
        return mOffsets;
    }

    @Override
    public @Nonnull
    ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            copyStack = slotStack.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(slotStack, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == copyStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return copyStack;
    }

    /**
     * Determines whether supplied player can use this container
     *
     * @param playerIn ignored by default
     */
    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }

    protected void createInventoryFromCapability(IItemHandler handler) {
        Log.warn("Creating Inventory from Item PacketSyncConfigHandler is not supported in this GUI!");
    }

    protected void createInventoryFromIInventory(IInventory inventory) {
        Log.warn("Creating Inventory from IInventory is not supported in this GUI!");
    }

    protected void createPlayerInventory(InventoryPlayer playerInv, PlayerInventoryOffsets offsets) {
        for (int i = 0; i < offsets.getInnerRowCount(); i++) {
            for (int j = 0; j < offsets.getInnerColumnCount(); j++) {
                addSlotToContainer(new Slot(playerInv,
                        j + i * offsets.getInnerColumnCount() + offsets.getInvBarColumnCount(),
                        offsets.getInnerXInvOffset() + j * offsets.getSlotXSize(),
                        offsets.getInnerYInvOffset() + i * offsets.getSlotYSize()));
            }
        }
        for (int i = 0; i < offsets.getInvBarColumnCount(); i++) {
            addSlotToContainer(new Slot(playerInv, i, offsets.getInvBarXOffset() + i * offsets.getSlotXSize(), offsets.getInvBarYOffset()));
        }
    }
}
