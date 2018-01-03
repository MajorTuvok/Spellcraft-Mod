package mt.mcmods.spellcraft.common.gui.helper.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SingleElementSlot extends SlotItemHandler {
    public SingleElementSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && stack.getCount() >= 1 && getItemHandler().getStackInSlot(getSlotIndex()).isEmpty() ?
                1 : 0;
    }
}
