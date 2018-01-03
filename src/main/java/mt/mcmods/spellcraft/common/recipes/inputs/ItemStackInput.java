package mt.mcmods.spellcraft.common.recipes.inputs;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStackInput extends BasicProcessingInput<ItemStackInput> {
    private ItemStack mainInput;

    public ItemStackInput() {
        mainInput = ItemStack.EMPTY;
    }

    public ItemStackInput(ItemStack mainInput) {
        this.mainInput = mainInput;
    }

    public ItemStack getMainInput() {
        return mainInput;
    }

    public void setMainInput(ItemStack mainInput) {
        this.mainInput = mainInput;
    }

    @Override
    public boolean isValid() {
        return this.mainInput != null && !this.mainInput.isEmpty();
    }

    @Override
    public ItemStackInput copy() {
        if (mainInput != null)
            return new ItemStackInput(this.mainInput.copy());
        else
            return new ItemStackInput(null);
    }

    @Override
    public void load(NBTTagCompound compound) {
        if (compound.hasKey("Main Input")) {
            mainInput = new ItemStack(compound.getCompoundTag("Main Input"));
        } else {
            mainInput = ItemStack.EMPTY;
        }
    }

    @Override
    public void write(NBTTagCompound compound) {
        if (mainInput != null && !mainInput.isEmpty()) {
            compound.setTag("Main Input", mainInput.serializeNBT());
        }
    }

    @Override
    public boolean testEquality(ItemStackInput other) {
        if (!this.isValid()) {
            return !other.isValid();
        } else if (other.isValid()) {
            return ItemStack.areItemsEqual(other.getMainInput(), getMainInput());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (isValid()) {
            String mainName = mainInput.getUnlocalizedName();
            return mainName.hashCode();
        }
        return -1;
    }

    @Override
    public boolean isInstance(Object other) {
        return other instanceof ItemStackInput;
    }

    public boolean process(List<ItemStack> stacks, int mainIndex, int secondaryIndex, boolean simulate) {
        if (mainIndex < stacks.size() && mainIndex >= 0 && secondaryIndex < stacks.size() && secondaryIndex >= 0) {
            ItemStack mainStack = stacks.get(mainIndex);
            ItemStack secondaryStack = stacks.get(secondaryIndex);
            boolean result = process(mainStack, secondaryStack, simulate);
            if (!simulate && result) {
                stacks.set(mainIndex, mainStack);
                stacks.set(secondaryIndex, secondaryStack);
            }
            return result;
        } else {
            ILoggable.Log.warn("Cannot Process because of invalid index!");
            return false;
        }
    }

    public boolean process(@Nullable ItemStack mainStack, @Nullable ItemStack secondaryStack, boolean simulate) {
        //here the second stack is irrelevant
        if (mainStack != null && !mainStack.isEmpty() && this.isValid()) {
            return consumeStack(mainStack, getMainInput(), simulate);
        } else {
            return false;
        }
    }

    public boolean revert(List<ItemStack> stacks, int mainIndex, int secondaryIndex, boolean simulate) {
        if (mainIndex < stacks.size() && mainIndex >= 0 && secondaryIndex < stacks.size() && secondaryIndex >= 0) {
            ItemStack mainStack = stacks.get(mainIndex);
            ItemStack secondaryStack = stacks.get(secondaryIndex);
            boolean result = revert(mainStack, secondaryStack, simulate);
            if (!simulate && result) {
                stacks.set(mainIndex, mainStack);
                stacks.set(secondaryIndex, secondaryStack);
            }
            return result;
        } else {
            ILoggable.Log.warn("Cannot Revert because of invalid index!");
            return false;
        }
    }

    public boolean revert(@Nullable ItemStack mainStack, @Nullable ItemStack secondaryStack, boolean simulate) {
        if (mainStack != null && !mainStack.isEmpty() && this.isValid()) {
            return revertStack(mainStack, getMainInput(), simulate);
        } else {
            return false;
        }
    }
}
