package com.mt.mcmods.spellcraft.common.spell.components;

import com.mt.mcmods.spellcraft.Server.spell.SpellType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

/**
 * Be aware that all Classes of this need to override the equals Method!
 */
public interface ISpellComponent extends INBTSerializable<NBTTagCompound>, IForgeRegistryEntry<ISpellComponent> {

    /**
     * Called by SpellState when execution is required.
     *
     * @param componentCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's SpellType is one of getSupportedTypes(), although this is not guaranteed. Use this to interfere with the outside world.
     * @return Whether or not this Component executed successfully. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    public abstract boolean execute(ISpellComponentCallback componentCallback);

    /**
     * Return all SpellTypes which are compatible with this component
     *
     * @return A list of compatible SpellTypes. It is recommended to return an ImmutableList.
     */
    public abstract List<SpellType> getSupportedTypes();
}
