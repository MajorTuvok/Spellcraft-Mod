package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.common.spell.entity.PlayerSpellType;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpellTypes {
    private static final ArrayList<ISpellType> types = new ArrayList<>(1);

    public static final ISpellType PLAYER_SPELL_TYPE = new PlayerSpellType();

    public static void addType(ISpellType type) {
        types.add(type);
    }

    public static @Nullable
    Spell instantiate(NBTTagCompound compound) throws InstantiationException{
        for (ISpellType type :
                types) {
            if (type.matches(compound)) {
                return type.instantiate(compound);
            }
        }
        return null;
    }

    public static @Nullable
    ISpellType getType(NBTTagCompound compound) {
        for (ISpellType type :
                types) {
            if (type.matches(compound)) {
                return type;
            }
        }
        return null;
    }

    public static List<ISpellType> getAll() {
        return Collections.unmodifiableList(types);
    }

    static {
        addType(PLAYER_SPELL_TYPE);
    }
}
