package com.mt.mcmods.spellcraft.common.recipes.outputs;

import net.minecraft.nbt.NBTTagCompound;

public interface ProcessingOutput<OUTPUT extends ProcessingOutput<OUTPUT>> {
    public OUTPUT copy();

    public void load(NBTTagCompound compound);

    public boolean isValid();

    public void write(NBTTagCompound compound);
}
