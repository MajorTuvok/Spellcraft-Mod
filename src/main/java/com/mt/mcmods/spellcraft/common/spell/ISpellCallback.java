package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.Server.spell.SpellType;

public interface ISpellCallback {
    /**
     * Extracts Power from this Spells powerProvider
     * @param amount the amount of power to extract
     * @return The actual amount of power consumed
     */
    public float extractPower(float amount);

    public SpellType getType();

    public void onIllegalCallbackDetected();
}