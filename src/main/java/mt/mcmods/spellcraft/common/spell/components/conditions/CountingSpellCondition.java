package mt.mcmods.spellcraft.common.spell.components.conditions;

import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.common.spell.access.AbsAttributeSet;
import mt.mcmods.spellcraft.common.spell.access.AccessType;
import mt.mcmods.spellcraft.common.spell.access.IAttributeProvider;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Set;

public final class CountingSpellCondition extends AbsSpellCondition {
    private static final CountingSpellCondition GLOBAL_INSTANCE =
            new CountingSpellCondition(StringHelper.createResourceLocation(SpellcraftMod.MODID, "spell_condition", "counting_global"), AccessType.GLOBAL);
    private static final CountingSpellCondition LOCAL_INSTANCE =
            new CountingSpellCondition(StringHelper.createResourceLocation(SpellcraftMod.MODID, "spell_condition", "counting_local"), AccessType.LOCAL);
    private static final CountingSpellCondition STATE_INSTANCE =
            new CountingSpellCondition(StringHelper.createResourceLocation(SpellcraftMod.MODID, "spell_condition", "counting_state"), AccessType.STATE);
    private final AccessType mAccessType;

    private CountingSpellCondition(String location, AccessType supportedType) {
        super();
        setRegistryName(location);
        mAccessType = supportedType;
    }

    public static CountingSpellCondition getGlobalInstance() {
        return GLOBAL_INSTANCE;
    }

    public static CountingSpellCondition getStateInstance() {
        return STATE_INSTANCE;
    }

    public static CountingSpellCondition getLocalInstance() {
        return LOCAL_INSTANCE;
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
        CountingAttributeSet attributeSet = getOrCreateAttributes(attributeProvider);
        //ILoggable.Log.info("Checking "+this+"! value=" + attributeSet.getCount());
        return attributeSet.count();
    }

    @Nonnull
    @Override
    public CountingAttributeSet createAttributes() {
        return createAttributes(0, 10);
    }

    @Override
    public String toString() {
        return "CountingSpellCondition{" +
                "mAccessType=" + mAccessType +
                (getRegistryName() != null ? "registryName=" + getRegistryName() + '}' : '}');
    }

    private CountingAttributeSet getOrCreateAttributes(IAttributeProvider attributeProvider) {
        return (CountingAttributeSet) super.getOrCreateAttributes(attributeProvider, mAccessType);
    }

    private CountingAttributeSet createAttributes(int count, int border) {
        return new CountingAttributeSet(getRegistryName(), EnumSet.of(mAccessType), count, border);
    }

    public static final class CountingAttributeSet extends AbsAttributeSet {
        private static final String KEY_BORDER = "CountingAttributeSet_border";
        private static final String KEY_COUNT = "CountingAttributeSet_count";
        private static final String KEY_UPWARDS = "CountingAttributeSet_upwards";
        private int border;
        private int count;
        private boolean upwards;

        private CountingAttributeSet(ResourceLocation location, Set<AccessType> types, int count, int border) {
            super(location, types);
            this.count = count;
            this.border = border;
            setUpwards();
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getBorder() {
            return border;
        }

        public void setBorder(int border) {
            this.border = border;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger(KEY_COUNT, count);
            compound.setInteger(KEY_BORDER, border);
            compound.setBoolean(KEY_UPWARDS, upwards);
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            if (nbt.hasKey(KEY_COUNT))
                this.count = nbt.getInteger(KEY_COUNT);
            if (nbt.hasKey(KEY_BORDER))
                this.border = nbt.getInteger(KEY_BORDER);
            if (nbt.hasKey(KEY_UPWARDS))
                this.upwards = nbt.getBoolean(KEY_UPWARDS);
        }

        public boolean count() {
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

        @Override
        public String toString() {
            return "CountingAttributeSet{" +
                    "border=" + border +
                    ", count=" + count +
                    '}';
        }

        private void setUpwards() {
            this.upwards = this.count < this.border;
        }


    }
}
