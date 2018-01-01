package com.mt.mcmods.spellcraft.common.spell;

import net.minecraft.nbt.NBTTagCompound;

/**
 * SpellTypes must override the equals Method. It is suggested to use single-Element-Enums, as every SpellType should be made a singleton.
 */
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
