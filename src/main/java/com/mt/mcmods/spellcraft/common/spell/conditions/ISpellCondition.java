package com.mt.mcmods.spellcraft.common.spell.conditions;

import com.mt.mcmods.spellcraft.common.spell.ISpellType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

/**
 * Be aware that all Classes of this need to override the equals Method!
 */
public interface ISpellCondition extends INBTSerializable<NBTTagCompound>, IForgeRegistryEntry<ISpellCondition> {
    /**
     * Tests whether this Condition holds True against the circumstances represented by the conditionCallback
     *
     * @param conditionCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's ISpellType is one of getSupportedTypes(), although this is not guaranteed.
     * @return Whether or not this Condition holds true against given circumstances. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    public abstract boolean holdsTrue(ISpellConditionCallback conditionCallback);

    /**
     * Return all SpellTypes which are compatible with this component
     *
     * @return A list of compatible SpellTypes. It is recommended to return an ImmutableList.
     */
    public abstract List<ISpellType> getSupportedTypes();

}
