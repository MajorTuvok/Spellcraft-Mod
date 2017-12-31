package com.mt.mcmods.spellcraft.common.spell.entity;

import com.mt.mcmods.spellcraft.common.spell.ISpellType;

public interface ISpellCallback {
    /**
     * Extracts Power from this Spells powerProvider
     *
     * @param amount the amount of power to extract
     * @return The actual amount of power consumed
     */
    public float extractPower(float amount);

    public ISpellType getType();

    public void onIllegalCallbackDetected();
}
