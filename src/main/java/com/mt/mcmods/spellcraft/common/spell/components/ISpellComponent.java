package com.mt.mcmods.spellcraft.common.spell.components;

import com.mt.mcmods.spellcraft.common.spell.types.ISpellType;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.List;

public interface ISpellComponent<T extends ISpellComponent<T>> extends IForgeRegistryEntry<T> {
    /**
     * Return all SpellTypes which are compatible with this component
     *
     * @return A list of compatible SpellTypes. It is recommended to return an ImmutableList.
     */
    public @Nonnull
    List<ISpellType> getSupportedTypes();
}
