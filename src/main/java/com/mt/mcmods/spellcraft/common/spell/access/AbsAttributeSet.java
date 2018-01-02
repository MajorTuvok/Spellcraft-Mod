package com.mt.mcmods.spellcraft.common.spell.access;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Set;

public abstract class AbsAttributeSet implements IAttributeSet {
    private Set<AccessType> mAccessTypes;
    private ResourceLocation mResourceLocation;

    public AbsAttributeSet(ResourceLocation resourceLocation, Set<AccessType> accessTypes) {
        mResourceLocation = resourceLocation;
        mAccessTypes = accessTypes;
        if (accessTypes == null || resourceLocation == null)
            throw new NullPointerException("Cannot construct from null values");
    }

    /**
     * (just use EnumSet.of...)
     *
     * @return The AccessTypes to use this AttributeSet with
     */
    @Override
    public Set<AccessType> getSupportedAccessTypes() {
        return mAccessTypes;
    }

    /**
     * This Method must return the RegistryName of the SpellComponent associated with this AttributeSet.
     * This is necessary so that it is possible to reinstantiate AttributeSets without making any Assumptions via Reflection.
     *
     * @return The identifying ResourceLocation for this AttributeSet
     */
    @Nonnull
    @Override
    public ResourceLocation getComponentRegistryName() {
        return mResourceLocation;
    }
}
