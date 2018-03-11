package mt.mcmods.spellcraft.common.spell.access;

import mt.mcmods.spellcraft.common.spell.types.ISpellType;

public interface ISpellCallback {
    public ISpellType getSpellType();

    /**
     * Extracts Power from this Spells powerProvider
     *
     * @param amount the amount of power to extract
     * @return The actual amount of power consumed
     */
    public float extractPower(float amount);

    public void onIllegalCallbackDetected();
}
