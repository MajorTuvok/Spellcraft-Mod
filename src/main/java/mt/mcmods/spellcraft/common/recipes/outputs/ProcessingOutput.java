package mt.mcmods.spellcraft.common.recipes.outputs;

import net.minecraft.nbt.NBTTagCompound;

public interface ProcessingOutput<OUTPUT extends ProcessingOutput<OUTPUT>> {
    public boolean isValid();

    public OUTPUT copy();

    public void load(NBTTagCompound compound);

    public void write(NBTTagCompound compound);
}
