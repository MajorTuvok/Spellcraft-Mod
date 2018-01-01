package com.mt.mcmods.spellcraft.common.spell.attributes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.Callable;

public interface IAttributeProvider extends INBTSerializable<NBTTagCompound>{
    /**
     *
     * @return The AttributeAccess common to the whole Spell
     */
    public @Nonnull IAttributeAccess getGlobalAttributes();

    /**
     *
     * @return The AttributeAccess common to the current SpellState
     */
    public @Nonnull IAttributeAccess getStateAttributes();

    /**
     *
     * @return The AttributeAccess common only to the currently active StateSet.
     */
    public @Nonnull IAttributeAccess getLocalAttributes();

    /**
     * Adds a new Instance provided by this callable to every Access where it fits (e.g. getSupportedTypes).
     * @param callable The callable Object to use as Factory for AttributeSets
     * @return False if an Exception was thrown or if no AttributeSet was added. True otherwise.
     */
    public boolean addCorrespondingToTypes(Callable<? extends IAttributeSet> callable);

    /**
     *
     * @param type The AccessType to look for
     * @return The AttributeAccess for the given AccessType.
     * @throws NullPointerException if type was null
     */
    public @Nonnull IAttributeAccess getAccess(AccessType type);

    /**
     * Tries to find all Access-possibilities for the given ResourceLocation.
     * @param location The Key for the Attributes to find
     * @return A NonNullList containing up to all three Attribute-Access's. May be empty if no matching Access is found.
     */
    public @Nonnull NonNullList<IAttributeAccess> findAccessFor(ResourceLocation location);
}
