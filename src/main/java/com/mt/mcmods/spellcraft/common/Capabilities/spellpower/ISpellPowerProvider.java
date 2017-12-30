package com.mt.mcmods.spellcraft.common.Capabilities.spellpower;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISpellPowerProvider extends INBTSerializable<NBTTagCompound>{
    /**
     * Returns the currently stored Spellpower
     * @return The current power
     */
    public float getPower();

    /**
     * Set's the current Spellpower.
     * The new Power may be bigger than the maximum Power.
     * Values smaller than 0 will be clamped to 0.
     */
    public void setPower(float newPower);

    /**
     * Returns the current maximum Power Value.
     * @return The current maximum Power level
     */
    public float getMaxPower();

    /**
     * Set's the current Spellpower maximum.
     * Values smaller than 0 will be clamped to 0.
     * @param newMaxPower The new MaxPower
     */
    public void setMaxPower(float newMaxPower);

    /**
     * This method attempts to extract the given amount of power from this Provider.
     * @param amount How much to extract
     * @param simulate Whether or not to actually perform the Extraction. (Setting this to false will prevent power from being extracted, but will still return how much power could be extracted)
     * @return The extracted amount
     */
    public float extractPower(float amount, boolean simulate);

    /**
     * This method attempts to add the given amount of power to this Provider.
     * @param amount How much to receive
     * @param simulate Whether or not to actually perform the receive. (Setting this to false will prevent power from being received, but will still return how much power could be received)
     * @return The received amount
     */
    public float receivePower(float amount, boolean simulate);
}
