package mt.mcmods.spellcraft.common.gui.helper.slots;

import mt.mcmods.spellcraft.common.interfaces.ISlotChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class MultiChangeSlot extends SlotItemHandler {
    private Set<ISlotChangedListener> mListeners;

    public MultiChangeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        mListeners = new HashSet<>();
    }

    /**
     * if par2 has more items than par1, onCrafting(item,countIncrease) is called
     *
     * @param p_75220_1_
     * @param p_75220_2_
     */
    @Override
    public void onSlotChange(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
        super.onSlotChange(p_75220_1_, p_75220_2_);
        this.onSlotChanged();
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     *
     * @param amount
     */
    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount) {
        this.onSlotChanged();
        return super.decrStackSize(amount);
    }

    public void addListener(ISlotChangedListener listener) {
        if (listener == null) throw new NullPointerException("Cannot have a null Callback");
        mListeners.add(listener);
    }

    public void removeListener(ISlotChangedListener listener) {
        if (listener == null) throw new NullPointerException("Cannot remove a null Callback");
        mListeners.remove(listener);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     *
     * @param stack
     * @param amount
     */
    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        super.onCrafting(stack, amount);
        this.onSlotChanged();
    }

    @Override
    protected void onSwapCraft(int p_190900_1_) {
        super.onSwapCraft(p_190900_1_);
        this.onSlotChanged();
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     *
     * @param stack
     */
    @Override
    protected void onCrafting(ItemStack stack) {
        super.onCrafting(stack);
        this.onSlotChanged();
    }

    /**
     * Called when the stack in a Slot changes
     */
    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        for (ISlotChangedListener listener : mListeners) {
            listener.onSlotChanged();
        }
    }
}
