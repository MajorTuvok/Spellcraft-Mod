package mt.mcmods.spellcraft.common.spell.types;

import mt.mcmods.spellcraft.common.spell.Spell;
import mt.mcmods.spellcraft.common.spell.entity.PlayerSpellType;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpellTypes {
    public static final ISpellType PLAYER_SPELL_TYPE = PlayerSpellType.INSTANCE;
    private static final ArrayList<ISpellType> types = new ArrayList<>(1);

    static {
        addType(PLAYER_SPELL_TYPE);
    }

    /**
     * There should never be an Instance of this class
     */
    private SpellTypes() {

    }

    public static void addType(ISpellType type) {
        types.add(type);
    }

    public static @Nullable
    Spell instantiate(NBTTagCompound compound) throws InstantiationException {
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

    /**
     * @return An immutable List containing all registered SpellTypes
     */
    public static List<ISpellType> getAll() {
        return Collections.unmodifiableList(types);
    }
}
