package com.mt.mcmods.spellcraft.common.spell.access;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Set;

public interface IAttributeSet extends INBTSerializable<NBTTagCompound>{
    /**
     * (just use EnumSet.of...)
     * @return The AccessTypes to use this AttributeSet with
     */
    public Set<AccessType> getSupportedAccessTypes();

    /**
     *
     * @return The identifying ResourceLocation for this AttributeSet
     */
    public @Nonnull ResourceLocation getKey();
}
