package mt.mcmods.spellcraft.common.recipes.inputs;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class AdvancedItemStackInput extends ItemStackInput {
    private ItemStack secondaryInput;

    public AdvancedItemStackInput() {
        super();
        secondaryInput = ItemStack.EMPTY;
    }

    public AdvancedItemStackInput(ItemStack mainInput, ItemStack secondaryInput) {
        super(mainInput);
        this.secondaryInput = secondaryInput;
    }

    public ItemStack getSecondaryInput() {
        return secondaryInput;
    }

    public void setSecondaryInput(ItemStack secondaryInput) {
        this.secondaryInput = secondaryInput;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && this.secondaryInput != null && !this.secondaryInput.isEmpty();
    }

    @Override
    public AdvancedItemStackInput copy() {
        return new AdvancedItemStackInput(getMainInput().copy(), secondaryInput.copy());
    }

    @Override
    public void load(NBTTagCompound compound) {
        super.load(compound);
        if (compound.hasKey("Secondary Input")) {
            secondaryInput = new ItemStack(compound);
        } else {
            secondaryInput = ItemStack.EMPTY;
        }
    }

    @Override
    public void write(NBTTagCompound compound) {
        super.write(compound);
        if (secondaryInput != null && !secondaryInput.isEmpty()) {
            compound.setTag("Secondary Input", secondaryInput.serializeNBT());
        }
    }

    @Override
    public boolean testEquality(ItemStackInput other) {
        return other instanceof AdvancedItemStackInput &&
                super.testEquality(other) &&
                ItemStack.areItemsEqual(((AdvancedItemStackInput) other).getSecondaryInput(), getSecondaryInput());
    }

    @Override
    public int hashCode() {
        if (isValid()) {
            String secondaryName = secondaryInput.getUnlocalizedName();
            return secondaryName.hashCode() << 8 | super.hashCode();
        }
        return -1;
    }

    @Override
    public boolean isInstance(Object other) {
        return other instanceof AdvancedItemStackInput;
    }

    @Override
    public boolean process(@Nullable ItemStack mainStack, @Nullable ItemStack secondaryStack, boolean simulate) {
        if (mainStack != null && secondaryStack != null && !mainStack.isEmpty() && !secondaryStack.isEmpty() && this.isValid()) {
            return consumeStack(mainStack, getMainInput(), simulate) && consumeStack(secondaryStack, getSecondaryInput(), simulate); //should always be true
        }
        return false;
    }

    @Override
    public boolean revert(@Nullable ItemStack mainStack, @Nullable ItemStack secondaryStack, boolean simulate) {
        if (mainStack != null && secondaryStack != null && !mainStack.isEmpty() && !secondaryStack.isEmpty() && this.isValid()) {
            return revertStack(mainStack, getMainInput(), simulate) && revertStack(secondaryStack, getSecondaryInput(), simulate);
        } else {
            return false;
        }
    }
}
