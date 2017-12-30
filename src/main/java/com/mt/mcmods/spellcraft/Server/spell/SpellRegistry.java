package com.mt.mcmods.spellcraft.Server.spell;

import com.mt.mcmods.spellcraft.Server.spell.entity.EntitySpell;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.registry.RegistryAdvanced;
import com.mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Tuple;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class SpellRegistry extends WorldSavedData{
    private static final String NAME = ILoggable.MODID+" SpellRegistry-WorldSavedData";
    private static final String KEY_SPELLS = "SpellRegistry_flattenedIdRegistry";
    private static final RegistryAdvanced<Integer,Spell> idRegistry = new RegistryAdvanced<>();
    private static final RegistryAdvanced<Entity,List<EntitySpell>> entitySpellRegistry = new RegistryAdvanced<>();
    private static int curId = 0;
    private static MinecraftServer server;
    private static SpellRegistry lastInstance;

    public static void onServerStarting(FMLServerStartingEvent event) {
        server = event.getServer();
    }

    public static void registerSpell(Spell spell) {
        registerSpellWithId(spell,getNextId());
        markDataDirty();
    }

    public static void unregisterAllSpells(Entity entity) {
        if (entity!=null && entitySpellRegistry.containsKey(entity)) {
            List<EntitySpell> spells = entitySpellRegistry.getObject(entity);
            if (spells!=null) {
                spells = new ArrayList<>(spells); //id don't want to remove things while i'm iterating over them...
                for (EntitySpell spell:
                     spells) {
                    unregisterSpell(spell); //it has to be unregistered from global map correctly
                }
            }
        }
    }

    public static @Nullable Spell unregisterSpell(Spell spell) {
        if (spell!=null && idRegistry.containsKey(spell.getId())) {
            idRegistry.remove(spell.getId());
            checkAdditionalUnRegistration(spell);
            markDataDirty();
            return spell;
        } else {
            ILoggable.Log.warn("attempted to unregister unknown Spell");
            return null;
        }
    }

    public static @Nullable Spell unregisterSpell(int id) {
        if (idRegistry.containsKey(id)) {
            Spell spell = idRegistry.getObject(id);
            if (spell!=null) {
                idRegistry.remove(id);
                checkAdditionalUnRegistration(spell);
                markDataDirty();
                return spell;
            }
        }
        ILoggable.Log.warn("Attempted to unregister unknown Spell");
        return null;
    }

    public static @Nullable Spell getSpellById(Integer id) {
        return idRegistry.getObject(id);
    }

    public static @Nullable MinecraftServer getServer() {
        return server;
    }

    public static @Nullable SpellRegistry getSaveData() {
        if (getServer()!=null) {
            MapStorage storage = getServer().getEntityWorld().getMapStorage();
            SpellRegistry instance = null;
            if (storage!=null)
                instance = (SpellRegistry) storage.getOrLoadData(SpellRegistry.class, NAME);

            if (instance == null) {
                instance = new SpellRegistry();
                if (storage!=null) {
                    storage.setData(NAME, instance);
                    lastInstance = instance;
                }
            }
            else {
                lastInstance = instance;
            }
            return instance;
        }
        return null;
    }

    public static @Nullable List<EntitySpell> getEntitySpells(UUID uuid) {
        if (uuid!=null) {
            for (Map.Entry<Entity, List<EntitySpell>> entry :
                    entitySpellRegistry.getEntrySet()) {
                if (entry.getKey() != null && entry.getKey().getUniqueID().equals(uuid)) {
                    return new ArrayList<>(entry.getValue());
                }
            }
        }
        return null;
    }

    public static @Nullable List<EntitySpell> getEntitySpells(Entity entity) {
        List<EntitySpell> spells = entitySpellRegistry.getObject(entity);
        return spells !=null ? new ArrayList<>(spells) : null;
    }

    private static void registerSpellWithId(Spell spell, int id) {
        Validate.notNull(spell,"Cannot registerSpell null Spell!");
        Spell spell2 = null;
        if (idRegistry.containsKey(id)) {
            spell2 = unregisterSpell(id);
        }
        idRegistry.putObject(id,spell);
        checkAdditionalRegistration(spell);
        spell.setId(id);
        if ( spell2!=null)
            registerSpell(spell2);
    }

    private static int getNextId() {
        if (lastInstance==null) {
            getSaveData();
        }
        while (idRegistry.containsKey(curId) ||curId==Integer.MIN_VALUE || lastInstance.isUnregisteredId(curId)) {
            ++curId;
        }
        return curId;
    }

    private static void checkAdditionalRegistration(Spell spell) {
        if (spell instanceof EntitySpell) {
            register((EntitySpell) spell);
        }
    }

    private static void register(EntitySpell spell) {
        if (!entitySpellRegistry.containsKey(spell.getEntity())) {
            entitySpellRegistry.putObject(spell.getEntity(),new ArrayList<>(10));
        }
        List<EntitySpell> spells = entitySpellRegistry.getObject(spell.getEntity());
        if (spells!=null) {
            spells.add(spell);
        }
    }

    private static void checkAdditionalUnRegistration(Spell spell) {
        if (spell instanceof EntitySpell) {
            unregister((EntitySpell) spell);
        }
    }

    private static void unregister(EntitySpell spell) {
        if (entitySpellRegistry.containsKey(spell.getEntity())) {
            List<EntitySpell> spells = entitySpellRegistry.getObject(spell.getEntity());
            if (spells!=null ) {
                if (spells.contains(spell))
                    spells.remove(spell);
                else
                    ILoggable.Log.warn("Unregistering incorrectly registered spell");
                if (spells.isEmpty()) {
                    entitySpellRegistry.remove(spell.getEntity(),spells);
                }
            }
        }
    }

    private static void clear() {
        for (Map.Entry<Integer,Spell> entry:
             idRegistry.getEntrySet()) {
            entry.getValue().onPause();
        }
        idRegistry.clear();
        entitySpellRegistry.clear();
    }

    private static void markDataDirty() {
        if (lastInstance!=null) {
            lastInstance.markDirty();
        }
        else {
            SpellRegistry registry = getSaveData();
            if (registry != null) {
                registry.markDirty();
            }
        }
    }

    //--------------------------------non-static Methods ------------------------------------------------

    private List<Tuple<NBTTagCompound,SpellType>> unregisteredSpells;
    private List<Integer> unregisteredIds;

    public SpellRegistry() {
        this(NAME);
    }

    public SpellRegistry(String name) {
        super(name);
        unregisteredSpells = new LinkedList<>();
        unregisteredIds = new LinkedList<>();
    }

    @Override
    public @Nonnull NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        NBTTagList spells = new NBTTagList();
        for (Map.Entry<Integer,Spell> entry:
                idRegistry.getEntrySet()) {
            spells.appendTag(entry.getValue().serializeNBT());
        }
        for (Tuple<NBTTagCompound,SpellType> tuple:
             unregisteredSpells) {
            spells.appendTag(tuple.getFirst());
        }
        compound.setTag(KEY_SPELLS,spells);
        return compound;
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     *
     * @param nbt
     */
    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList spells = (NBTTagList) nbt.getTag(KEY_SPELLS);
        for (NBTBase content:
                spells) {
            if (content instanceof NBTTagCompound) {
                SpellType type = SpellTypes.getType((NBTTagCompound) content);
                if (type!=null) {
                    unregisteredSpells.add(new Tuple<>((NBTTagCompound) content,type));
                    unregisteredIds.add(NBTHelper.getSpellId((NBTTagCompound) content));
                }
            }
        }
    }

    public @Nonnull ArrayList<Tuple<NBTTagCompound,SpellType>> getUnregisteredSpells(){
        return new ArrayList<>(unregisteredSpells);
    }

    public boolean registerInactiveSpell(Tuple<NBTTagCompound,SpellType> toRegister) {
        Spell spell = toRegister.getSecond().instantiate(toRegister.getFirst());
        if (spell!=null) {
            registerSpellWithId(spell,spell.getId());
            unregisteredSpells.remove(toRegister);
            unregisteredIds.remove(new Integer(spell.getId()));
            if (spell.shouldResume())
                spell.onResume();
            markDirty();
        }
        return false;
    }

    public void setToUnregisteredSpell(Spell spell) {
        spell.onPause();
        unregisterSpell(spell);
        unregisteredSpells.add(new Tuple<>(spell.serializeNBT(),spell.getType()));
        unregisteredIds.add(spell.getId());
        markDirty();
    }

    public boolean isUnregisteredId(Integer id) {
        return unregisteredIds.contains(id);
    }
}
