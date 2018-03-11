package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotComponent extends ViewComponent {
    private Slot mSlot;

    public SlotComponent(Slot slot) {
        super(slot.xPos, slot.yPos, PlayerInventoryOffsets.DEFAULT_SLOT_X_SIZE, PlayerInventoryOffsets.DEFAULT_SLOT_Y_SIZE);
        mSlot = slot;
    }

    public ItemStack getStack() {
        return mSlot.getStack();
    }

    public boolean getHasStack() {
        return mSlot.getHasStack();
    }

    public int getSlotIndex() {
        return mSlot.getSlotIndex();
    }
}
