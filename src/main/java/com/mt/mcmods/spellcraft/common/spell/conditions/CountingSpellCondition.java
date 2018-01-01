package com.mt.mcmods.spellcraft.common.spell.conditions;

import com.mt.mcmods.spellcraft.SpellcraftMod;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class CountingSpellCondition extends AbsSpellCondition { //TODO make singelton as soon as Component and Condition registries are created
    private static final String KEY_COUNT = "CountingSpellCondition_count";
    private static final String KEY_BORDER = "CountingSpellCondition_count";
    private static final String KEY_UPWARDS = "CountingSpellCondition_upwards";
    private static final CountingSpellCondition INSTANCE = new CountingSpellCondition(0, 10);
    private int count;
    private int border;
    private boolean upwards;

    public static CountingSpellCondition getInstance() {
        return INSTANCE;
    }

    private CountingSpellCondition(int startValue, int border) {
        this.count = startValue;
        this.border = border;
        setUpwards();
        setRegistryName(StringHelper.createResourceLocation(SpellcraftMod.MODID, "spell_condition_counting"));
    }

    /**
     * Tests whether this Condition holds True against the circumstances represented by the conditionCallback
     *
     * @param conditionCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's ISpellType is one of getSupportedTypes(), although this is not guaranteed.
     * @return Whether or not this Condition holds true against given circumstances. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    @Override
    public boolean holdsTrue(ISpellConditionCallback conditionCallback) {
        ILoggable.Log.info("Checking Counting SpellCondition! value=" + count);
        boolean res;
        if (upwards) {
            res = count < border;
            ++count;
        } else {
            res = count > border;
            --count;
        }
        return res;
    }

    private void setUpwards() {
        this.upwards = this.count < this.border;
    }
}
