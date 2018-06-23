package mt.mcmods.spellcraft.common.tiles;

import mt.mcmods.spellcraft.common.interfaces.ICompatStackHandler;
import mt.mcmods.spellcraft.common.spell.components.conditions.CountingSpellCondition;
import mt.mcmods.spellcraft.common.spell.components.executables.VoidSpellExecutable;
import mt.mcmods.spellcraft.common.spell.entity.PlayerSpellBuilder;
import mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.Objects;

import static mt.mcmods.spellcraft.common.LocaleKey.GUI_SPELL_CREATOR_NO_SPELL_NAME;

public class TileEntitySpellCreator extends BaseTileEntityWithInventory {
    private static final String KEY_SPELL = "TileEntitySpellCreator_spell";
    private static NBTTagCompound DEFAULT_SPELL;
    private NBTTagCompound mSpellCompound;
    public static final int INPUT_SLOT = 0;
    public static final int INVENTORY_SIZE = 2;
    public static final int OUTPUT_SLOT = 1;

    static {
        try {
            PlayerSpellBuilder builder = new PlayerSpellBuilder();
            boolean res = builder.addSpellState("TestState");
            res &= builder.setStartState("TestState");
            res &= builder.addStateList("TestState");
            res &= builder.addExecutable("TestState", 0, VoidSpellExecutable.getInstance());
            res &= builder.setNextState("TestState", 0, "TestState");
            res &= builder.setCondition("TestState", 0, CountingSpellCondition.getStateInstance(), true);
            DEFAULT_SPELL = builder.constructNBT();
            if (DEFAULT_SPELL == null || !res) {
                Log.error("TileEntitySpellCreator failed to create Default Spell!");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            DEFAULT_SPELL = new NBTTagCompound();
        }
    }

    public TileEntitySpellCreator() {
        super(INVENTORY_SIZE);
        setMayUpdate(true);
        mSpellCompound = null;
    }

    public void setSpellCompound(@Nonnull NBTTagCompound spellCompound) {
        mSpellCompound = Objects.requireNonNull(spellCompound, "Cannot have a null SpellCompound!");
    }

    @Nonnull
    public NBTTagCompound getSpellCompound() {
        if (mSpellCompound == null) {
            Log.warn("TileEntitySpellCreator was questioned for an SpellCompound which doesn't exist yet! Returning DEFAULT_SPELL!");
            return DEFAULT_SPELL;
        }
        return mSpellCompound;
    }

    @Override
    protected ICompatStackHandler createInventory(int size) {
        return super.createInventory(size);
    }

    public String getSpellName() {
        String name = mSpellCompound != null ? NBTHelper.getSpellName(mSpellCompound) : "";
        return name.isEmpty() ? GUI_SPELL_CREATOR_NO_SPELL_NAME.get() : name;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readInternalNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (mSpellCompound != null) {
            compound.setTag(KEY_SPELL, mSpellCompound);
        } else {
            Log.error("TileEntitySpellCreator failed to write NBT-Data because SpellCompound was null!");
        }
        return super.writeToNBT(compound);
    }

    private void readInternalNBT(NBTTagCompound compound) {
        if (compound.hasKey(KEY_SPELL)) {
            compound.getCompoundTag(KEY_SPELL);//mSpellCompound = DEFAULT_SPELL;
        }
    }
}
