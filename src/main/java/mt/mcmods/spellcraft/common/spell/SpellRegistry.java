package mt.mcmods.spellcraft.common.spell;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.registry.RegistryAdvanced;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import mt.mcmods.spellcraft.common.spell.types.SpellTypes;
import mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Tuple;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@NotThreadSafe
public class SpellRegistry {
    public static final int NO_ID = Integer.MIN_VALUE;
    private static final String KEY_SPELLS = "SpellRegistry_flattenedIdRegistry";
    private static final String KEY_COMPOUND_REGISTRIES = "SpellRegistry_subRegistries";
    private static final String NAME = ILoggable.MODID + " SpellRegistry";
    private static final RegistryAdvanced<Integer, Spell> idRegistry = new RegistryAdvanced<>();
    private static final ArrayList<ISpellRegistryCallback> mRegistries = new ArrayList<>();
    private static int curId = 0;
    private static SpellWorldSaveData lastInstance;
    private static MinecraftServer server;

    public static void addSpellRegistryCallback(ISpellRegistryCallback registry) {
        mRegistries.add(registry);
        registry.onCreate();
    }

    public static void onServerStarting(FMLServerStartingEvent event) {
        server = event.getServer();
    }

    public static void registerSpell(Spell spell) {
        registerSpellWithId(spell, getNextId());
        markDataDirty();
    }

    public static @Nullable
    Spell unregisterSpell(Spell spell) {
        if (spell != null && idRegistry.containsKey(spell.getId())) {
            idRegistry.remove(spell.getId());
            checkAdditionalUnRegistration(spell);
            markDataDirty();
            return spell;
        } else {
            ILoggable.Log.warn("attempted to unregister unknown Spell");
            return null;
        }
    }

    public static @Nullable
    Spell unregisterSpell(int id) {
        if (idRegistry.containsKey(id)) {
            Spell spell = idRegistry.getObject(id);
            if (spell != null) {
                idRegistry.remove(id);
                checkAdditionalUnRegistration(spell);
                markDataDirty();
                return spell;
            }
        }
        ILoggable.Log.warn("Attempted to unregister unknown Spell");
        return null;
    }

    @Nullable
    public static Spell getSpellById(Integer id) {
        return idRegistry.getObject(id);
    }

    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }

    @Nullable
    public static SpellWorldSaveData getSaveData() {
        if (getServer() != null) {
            MapStorage storage = getServer().getEntityWorld().getMapStorage();
            SpellWorldSaveData instance = null;
            if (storage != null) {
                instance = (SpellWorldSaveData) storage.getOrLoadData(SpellWorldSaveData.class, NAME);
            }

            if (instance == null) {
                instance = new SpellWorldSaveData();
                if (storage != null) {
                    storage.setData(NAME, instance);
                    lastInstance = instance;
                }
            } else {
                lastInstance = instance;
            }
            return instance;
        }
        return null;
    }

    private static void registerSpellWithId(Spell spell, int id) {
        Validate.notNull(spell, "Cannot registerSpell null Spell!");
        Spell spell2 = null;
        if (idRegistry.containsKey(id)) {
            spell2 = unregisterSpell(id);
        }
        idRegistry.putObject(id, spell);
        checkAdditionalRegistration(spell);
        spell.setId(id);
        spell.activate();
        if (spell2 != null)
            registerSpell(spell2);
    }

    private static void checkAdditionalRegistration(Spell spell) {
        for (ISpellRegistryCallback registry :
                mRegistries) {
            registry.onRegisterSpell(spell);
        }
    }

    private static void checkAdditionalUnRegistration(Spell spell) {
        for (ISpellRegistryCallback registry :
                mRegistries) {
            registry.onUnRegisterSpell(spell);
        }
    }

    private static void clear() {
        for (Map.Entry<Integer, Spell> entry :
                idRegistry.getEntrySet()) {
            entry.getValue().onPause();
        }
        idRegistry.clear();
        for (ISpellRegistryCallback registry :
                mRegistries) {
            registry.onClear();
        }
    }

    private static void markDataDirty() {
        if (lastInstance != null) {
            lastInstance.markDirty();
        } else {
            WorldSavedData data = getSaveData();
            if (data != null) {
                data.markDirty();
            }
        }
    }

    private static int getNextId() {
        if (lastInstance == null) {
            getSaveData();
        }
        while (idRegistry.containsKey(curId) || curId == NO_ID || lastInstance.isUnregisteredId(curId)) {
            ++curId;
        }
        return curId;
    }

    public static final class SpellWorldSaveData extends WorldSavedData {
        private final List<Integer> unregisteredIds;
        private final List<Tuple<NBTTagCompound, ISpellType>> unregisteredSpells;

        public SpellWorldSaveData() {
            this(NAME);
        }

        public SpellWorldSaveData(String name) {
            super(name);
            unregisteredSpells = new LinkedList<>();
            unregisteredIds = new LinkedList<>();
        }

        public @Nonnull
        ArrayList<Tuple<NBTTagCompound, ISpellType>> getUnregisteredSpells() {
            return new ArrayList<>(unregisteredSpells);
        }

        /**
         * reads in data from the NBTTagCompound into this MapDataBase
         *
         * @param nbt
         */
        @Override
        public void readFromNBT(@Nonnull NBTTagCompound nbt) {
            NBTTagList spells = (NBTTagList) nbt.getTag(KEY_SPELLS);
            for (NBTBase content :
                    spells) {
                if (content instanceof NBTTagCompound) {
                    ISpellType type = SpellTypes.getType((NBTTagCompound) content);
                    if (type != null) {
                        unregisteredSpells.add(new Tuple<>((NBTTagCompound) content, type));
                        unregisteredIds.add(NBTHelper.getSpellId((NBTTagCompound) content));
                    }
                }
            }
            if (!mRegistries.isEmpty() && nbt.hasKey(KEY_COMPOUND_REGISTRIES)) {
                for (ISpellRegistryCallback registry :
                        mRegistries) {
                    registry.onReadFromNBT(nbt);
                }
            } else if (!mRegistries.isEmpty() && !nbt.hasKey(KEY_COMPOUND_REGISTRIES)) {
                ILoggable.Log.warn("Could not deserialize Sub-Spell-Registries. This is probably due to updating Spellcraft or adding some other on Spellcraft depending mod.");
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < mRegistries.size(); ++i) {
                    builder.append(mRegistries.get(i).getModId());
                    builder.append(':');
                    builder.append(mRegistries.get(i).getName());
                    if (i < mRegistries.size() - 1) {
                        builder.append(" ,");
                    }
                }
                ILoggable.Log.warn("This should not happen repeatedly! If it does contact developers. Following Sub-Registries were detected: {}.", builder.toString());
            }
        }

        @Override
        @Nonnull
        public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
            NBTTagList spells = new NBTTagList();
            for (Map.Entry<Integer, Spell> entry :
                    idRegistry.getEntrySet()) {
                spells.appendTag(entry.getValue().serializeNBT());
            }
            for (Tuple<NBTTagCompound, ISpellType> tuple :
                    unregisteredSpells) {
                spells.appendTag(tuple.getFirst());
            }
            compound.setTag(KEY_SPELLS, spells);
            if (!mRegistries.isEmpty()) {
                NBTTagCompound subRegistries = new NBTTagCompound();
                for (ISpellRegistryCallback registry :
                        mRegistries) {
                    registry.onWriteToNBT(subRegistries);
                }
                compound.setTag(KEY_COMPOUND_REGISTRIES, subRegistries);
            }
            return compound;
        }

        public boolean registerInactiveSpell(Tuple<NBTTagCompound, ISpellType> toRegister) {
            try {
                Spell spell = toRegister.getSecond().instantiate(toRegister.getFirst());
                if (spell != null) {
                    if (spell.canRegister()) {
                        registerSpellWithId(spell, spell.getId());
                        unregisteredSpells.remove(toRegister);
                        unregisteredIds.remove(Integer.valueOf(spell.getId()));
                        markDirty();
                    }
                    if (spell.shouldResume()) {
                        spell.onResume();
                    }
                    return true;
                }
            } catch (InstantiationException e) {
                ILoggable.Log.error("Could not instantiate Spell! This will result in this Spell being discarded and deleted from the SpellRegistry!", e);
                int id = NBTHelper.getSpellId(toRegister.getFirst());
                if (id != NO_ID) {
                    unregisteredSpells.remove(toRegister);
                    unregisteredIds.remove(id);
                }
            }
            return false;
        }

        public void asUnregisteredSpell(Spell spell) {
            spell.onPause();
            unregisterSpell(spell);
            unregisteredSpells.add(new Tuple<>(spell.serializeNBT(), spell.getType()));
            unregisteredIds.add(spell.getId());
            markDirty();
        }

        public boolean isUnregisteredId(Integer id) {
            return unregisteredIds.contains(id);
        }
    }

}
