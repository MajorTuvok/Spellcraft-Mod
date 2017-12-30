package com.mt.mcmods.spellcraft.Server.spell;

import com.mt.mcmods.spellcraft.Server.spell.entity.PlayerSpellType;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpellTypes {
    private static final ArrayList<SpellType> types = new ArrayList<>(1);

    public static final SpellType PLAYER_SPELL_TYPE = new PlayerSpellType();

    public static void addType(SpellType type) {
        types.add(type);
    }

    public static @Nullable
    Spell instantiate(NBTTagCompound compound) {
        for (SpellType type :
                types) {
            if (type.matches(compound)) {
                return type.instantiate(compound);
            }
        }
        return null;
    }

    public static @Nullable
    SpellType getType(NBTTagCompound compound) {
        for (SpellType type :
                types) {
            if (type.matches(compound)) {
                return type;
            }
        }
        return null;
    }

    public static List<SpellType> getAll() {
        return Collections.unmodifiableList(types);
    }

    static {
        addType(PLAYER_SPELL_TYPE);
    }
}
