package com.mt.mcmods.spellcraft.Server.spell;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface SpellType {
    public Spell instantiate(NBTTagCompound compound);
    public boolean matches(NBTTagCompound compound);
    public void apply(NBTTagCompound compound);
}
