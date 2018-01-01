package com.mt.mcmods.spellcraft.common.spell;

import net.minecraft.nbt.NBTTagCompound;

public interface ISpellType {
    /**
     * Instantiates a Spell
     * @param compound The NBTData containing Information for the Spell
     * @return A fully usable Spell-Instance
     * @throws InstantiationException on any Error
     */
    public Spell instantiate(NBTTagCompound compound)throws InstantiationException;

    public boolean matches(NBTTagCompound compound);

    public void apply(NBTTagCompound compound);

    /**
     *
     * @return A Spell who can be used in a SpellBuilder. This is not a fully usable Instance!
     * @throws InstantiationException on any Error
     */
    public Spell constructableInstance() throws InstantiationException;
}
