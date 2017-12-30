package com.mt.mcmods.spellcraft.common.spell.conditions;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class CountingSpellCondition extends AbsSpellCondition {
    private static final String KEY_COUNT = "CountingSpellCondition_count";
    private static final String KEY_BORDER = "CountingSpellCondition_count";
    private static final String KEY_UPWARDS = "CountingSpellCondition_upwards";
    private int count;
    private int border;
    private boolean upwards;

    public CountingSpellCondition() {
        this.count = 0;
        this.border = 0;
        this.upwards = true;
    }

    public CountingSpellCondition(int startValue, int border) {
        this.count = startValue;
        this.border = border;
        setUpwards();
    }

    /**
     * Tests whether this Condition holds True against the circumstances represented by the conditionCallback
     *
     * @param conditionCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's SpellType is one of getSupportedTypes(), although this is not guaranteed.
     * @return Whether or not this Condition holds true against given circumstances. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    @Override
    public boolean holdsTrue(ISpellConditionCallback conditionCallback) {
        boolean res = false;
        if (upwards) {
            res = count < border;
            ++count;
        } else {
            res = count > border;
            --count;
        }
        return res;
    }

    @Override
    public @Nonnull
    NBTTagCompound serializeNBT() {
        NBTTagCompound compound = super.serializeNBT();
        compound.setInteger(KEY_COUNT, count);
        compound.setInteger(KEY_BORDER, border);
        compound.setBoolean(KEY_UPWARDS, upwards);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        if (nbt.hasKey(KEY_COUNT))
            this.count = nbt.getInteger(KEY_COUNT);
        if (nbt.hasKey(KEY_BORDER))
            this.border = nbt.getInteger(KEY_BORDER);
        if (nbt.hasKey(KEY_UPWARDS))
            this.upwards = nbt.getBoolean(KEY_UPWARDS);
        else setUpwards();
    }

    private void setUpwards() {
        this.upwards = this.count < this.border;
    }
}
