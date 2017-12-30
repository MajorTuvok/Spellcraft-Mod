package com.mt.mcmods.spellcraft.common.recipes.recipes;

import com.mt.mcmods.spellcraft.common.recipes.inputs.ProcessingInput;
import com.mt.mcmods.spellcraft.common.recipes.outputs.ProcessingOutput;

public interface ProcessingRecipe<IN extends ProcessingInput<IN>, OUT extends ProcessingOutput<OUT>, RECIPE extends ProcessingRecipe<IN, OUT, RECIPE>> {

    public IN getInput();

    public OUT getOutput();

    public RECIPE copy();

    public long processingTime();
}
