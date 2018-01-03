package mt.mcmods.spellcraft.common.recipes.inputs;

import net.minecraft.nbt.NBTTagCompound;

public interface ProcessingInput<INPUT extends ProcessingInput<INPUT>> {
    public boolean isValid();

    public INPUT copy();

    public void load(NBTTagCompound var1);

    public boolean testEquality(INPUT var1);

    public int hashCode();

    public boolean equals(Object other);

    public boolean isInstance(Object var1);

    public void write(NBTTagCompound compound);
}
