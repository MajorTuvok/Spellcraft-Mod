package mt.mcmods.spellcraft.common.recipes.outputs;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ItemStackOutput extends BasicProcessingOutput<ItemStackOutput> {
    private ItemStack outputStack;

    public ItemStackOutput() {
        outputStack = ItemStack.EMPTY;
    }

    public ItemStackOutput(ItemStack outputStack) {
        this.outputStack = outputStack;
    }

    public ItemStack getOutputStack() {
        return outputStack;
    }

    public void setOutputStack(ItemStack outputStack) {
        this.outputStack = outputStack;
    }

    @Override
    public ItemStackOutput copy() {
        return new ItemStackOutput(outputStack);
    }

    @Override
    public void load(NBTTagCompound compound) {
        if (compound.hasKey("outputStack")) {
            setOutputStack(new ItemStack(compound.getCompoundTag("outputStack")));
        } else {
            setOutputStack(ItemStack.EMPTY);
        }
    }

    @Override
    public void write(NBTTagCompound compound) {
        if (outputStack != null && !outputStack.isEmpty()) {
            NBTTagCompound stackCompound = new NBTTagCompound();
            outputStack.writeToNBT(stackCompound);
            compound.setTag("outputStack", stackCompound);
        }
    }

    @Override
    public boolean isValid() {
        return outputStack != null;
    }

    public ItemStack getProcessResult() {
        return getOutputStack().copy();
    }

    public boolean applyProcessResult(List<ItemStack> stacks, int index, boolean simulate) {
        return applyProcessResult(stacks, index, getProcessResult(), simulate);
    }
}
