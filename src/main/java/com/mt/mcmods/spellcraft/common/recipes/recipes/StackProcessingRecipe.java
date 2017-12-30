package com.mt.mcmods.spellcraft.common.recipes.recipes;

import com.mt.mcmods.spellcraft.common.recipes.inputs.ItemStackInput;
import com.mt.mcmods.spellcraft.common.recipes.outputs.ItemStackOutput;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class StackProcessingRecipe<RECIPE extends StackProcessingRecipe<RECIPE>> extends BasicProcessingRecipe<ItemStackInput, ItemStackOutput, RECIPE> {
    public StackProcessingRecipe(ItemStackInput input, ItemStackOutput output) {
        super(input, output);
    }

    public boolean canOperate(List<ItemStack> inventory, int inputIndex, int secondaryIndex, int outputIndex) {
        return getInput().process(inventory, inputIndex, secondaryIndex, true) && getOutput().applyProcessResult(inventory, outputIndex, true);
    }

    public boolean operate(List<ItemStack> inventory, int inputIndex, int secondaryIndex, int outputIndex) {
        if (getInput().process(inventory, inputIndex, secondaryIndex, false)) {
            boolean applied = getOutput().applyProcessResult(inventory, outputIndex, false);
            if (!applied) {
                getInput().revert(inventory, inputIndex, secondaryIndex, false);
            }
            return applied;
        } else {
            return false;
        }
    }
}
