package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.common.spell.components.AbsSpellComponent;
import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellCondition;
import net.minecraft.nbt.NBTTagCompound;

public final class SpellConstructor {
    private static String ENTRY_SPELL_STATE = "Start_SpellState";
    private NBTTagCompound compound;

    public SpellConstructor() {
        this(new NBTTagCompound());
    }

    public SpellConstructor(NBTTagCompound compound) {
        this.compound = compound;
    }

    public NBTTagCompound construct() {
        return compound.copy();
    }

    public void addCondition(String spellStateName, ISpellCondition spellCondition) {

    }

    public void addComponent(String spellStateName, AbsSpellComponent spellComponent) {

    }

    public static boolean isValid(NBTTagCompound compound) {
        return true;
    }
}
