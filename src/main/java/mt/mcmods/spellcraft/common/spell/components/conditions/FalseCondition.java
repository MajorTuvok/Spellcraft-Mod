package mt.mcmods.spellcraft.common.spell.components.conditions;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.spell.access.IAttributeProvider;
import mt.mcmods.spellcraft.common.util.StringHelper;

public final class FalseCondition extends AbsSpellCondition {
    private static final FalseCondition INSTANCE = new FalseCondition(StringHelper.createResourceLocation(ILoggable.MODID, "spell_condition", "always_false"));

    private FalseCondition(String location) {
        setRegistryName(location);
    }

    public static FalseCondition getINSTANCE() {
        return INSTANCE;
    }

    /**
     * Tests whether this Condition holds True against the circumstances represented by the conditionCallback
     *
     * @param conditionCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's ISpellType is one of getSupportedTypes(), although this is not guaranteed.
     * @param attributeProvider
     * @return Whether or not this Condition holds true against given circumstances. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    @Override
    public boolean holdsTrue(ISpellConditionCallback conditionCallback, IAttributeProvider attributeProvider) {
        return false;
    }
}
