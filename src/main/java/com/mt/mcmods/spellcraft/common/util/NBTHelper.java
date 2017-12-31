package com.mt.mcmods.spellcraft.common.util;

import com.mt.mcmods.spellcraft.common.spell.SpellRegistry;
import com.mt.mcmods.spellcraft.SpellcraftMod;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;

import static com.mt.mcmods.spellcraft.common.spell.Spell.KEY_ID;
import static com.mt.mcmods.spellcraft.common.spell.entity.EntitySpell.KEY_ENTITY;
import static com.mt.mcmods.spellcraft.common.spell.entity.EntitySpell.KEY_WORLD;

public class NBTHelper {
    public static final String UUID_MOST_SIG_BITS = "mostSigBits";
    public static final String UUID_LEAST_SIG_BITS = "leastSigBits";

    /*
     * I didn't think of this incredibly good system myself... :(
     * See: https://github.com/HellFirePvP/AstralSorcery/blob/master/src/main/java/hellfirepvp/astralsorcery/common/util/nbt/NBTHelper.java
     */
    @Nonnull
    public static NBTTagCompound getPersistentData(Entity entity) {
        return getPersistentData(getData(entity));
    }

    public static void setPersistentData(Entity entity, NBTTagCompound compound) {
        setPersistentData(getData(entity), compound);
    }

    @Nonnull
    public static NBTTagCompound getPersistentData(ItemStack item) {
        return getPersistentData(getData(item));
    }

    public static void setPersistentData(ItemStack item, NBTTagCompound compound) {
        setPersistentData(getData(item), compound);
    }

    @Nonnull
    public static NBTTagCompound getPersistentData(NBTTagCompound base) {
        NBTTagCompound compound;
        if (hasPersistentData(base)) {
            compound = base.getCompoundTag(SpellcraftMod.MODID);
        } else {
            compound = new NBTTagCompound();
            setPersistentData(base, compound);
        }
        return compound;
    }

    public static void setPersistentData(NBTTagCompound base, NBTTagCompound compound) {
        if (base != null && compound != null)
            base.setTag(SpellcraftMod.MODID, compound);
    }

    public static boolean hasPersistentData(Entity entity) {
        return hasPersistentData(getData(entity));
    }

    public static boolean hasPersistentData(ItemStack item) {
        return item.getTagCompound() != null && hasPersistentData(getData(item));
    }

    public static boolean hasPersistentData(NBTTagCompound base) {
        return base.hasKey(SpellcraftMod.MODID) && base.getTag(SpellcraftMod.MODID) instanceof NBTTagCompound;
    }


    public static void removePersistentData(Entity entity) {
        removePersistentData(getData(entity));
    }

    public static void removePersistentData(ItemStack item) {
        if (item.hasTagCompound()) removePersistentData(getData(item));
    }

    public static void removePersistentData(NBTTagCompound base) {
        base.removeTag(SpellcraftMod.MODID);
    }


    public static NBTTagCompound getData(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            compound = new NBTTagCompound();
            stack.setTagCompound(compound);
        }
        return compound;
    }

    public static NBTTagCompound getData(Entity entity) {
        return entity.getEntityData();
    }


    public static void setStack(NBTTagCompound compound, String tag, ItemStack stack) {
        NBTTagCompound stackCompound = new NBTTagCompound();
        stack.writeToNBT(stackCompound);
        compound.setTag(tag, stackCompound);
    }

    public static void setUUID(NBTTagCompound compound, String tag, UUID uuid) {
        NBTTagCompound uuidComp = new NBTTagCompound();
        uuidComp.setLong(UUID_MOST_SIG_BITS, uuid.getMostSignificantBits());
        uuidComp.setLong(UUID_LEAST_SIG_BITS, uuid.getLeastSignificantBits());
        compound.setTag(tag, uuidComp);
    }

    public static ItemStack getStack(NBTTagCompound compound, String tag) {
        return getStack(compound, tag, ItemStack.EMPTY);
    }

    public static UUID getUUID(NBTTagCompound compound, String tag) {
        return getUUID(compound, tag, null);
    }

    //Get tags with default value
    public static ItemStack getStack(NBTTagCompound compound, String tag, ItemStack defaultValue) {
        if (compound.hasKey(tag)) {
            return new ItemStack(compound.getCompoundTag(tag));
        }
        return defaultValue;
    }

    public static UUID getUUID(NBTTagCompound compound, String tag, UUID defaultValue) {
        if (compound.hasKey(tag)) {
            NBTTagCompound uuidComp = compound.getCompoundTag(tag);
            return new UUID(uuidComp.getLong("mostSigBits"), uuidComp.getLong("leastSigBits"));
        }
        return defaultValue;
    }

    public static boolean getBoolean(NBTTagCompound compound, String tag, boolean defaultValue) {
        return compound.hasKey(tag) ? compound.getBoolean(tag) : defaultValue;
    }

    public static String getString(NBTTagCompound compound, String tag, String defaultValue) {
        return compound.hasKey(tag) ? compound.getString(tag) : defaultValue;
    }

    public static int getInteger(NBTTagCompound compound, String tag, int defaultValue) {
        return compound.hasKey(tag) ? compound.getInteger(tag) : defaultValue;
    }

    public static double getDouble(NBTTagCompound compound, String tag, double defaultValue) {
        return compound.hasKey(tag) ? compound.getDouble(tag) : defaultValue;
    }

    public static float getFloat(NBTTagCompound compound, String tag, float defaultValue) {
        return compound.hasKey(tag) ? compound.getFloat(tag) : defaultValue;
    }

    public static byte getByte(NBTTagCompound compound, String tag, byte defaultValue) {
        return compound.hasKey(tag) ? compound.getByte(tag) : defaultValue;
    }

    public static short getShort(NBTTagCompound compound, String tag, short defaultValue) {
        return compound.hasKey(tag) ? compound.getShort(tag) : defaultValue;
    }

    public static long getLong(NBTTagCompound compound, String tag, long defaultValue) {
        return compound.hasKey(tag) ? compound.getLong(tag) : defaultValue;
    }

    public static boolean isSpellEntityInstantiated(NBTTagCompound compound) {
        if (compound.hasKey(KEY_WORLD) && compound.hasUniqueId(KEY_ENTITY) && compound.getUniqueId(KEY_ENTITY) != null) {
            MinecraftServer server = SpellRegistry.getServer();
            if (server != null && DimensionManager.isDimensionRegistered(compound.getInteger(KEY_WORLD))) {
                WorldServer world = server.getWorld(compound.getInteger(KEY_WORLD));
                Entity entity = world.getEntityFromUuid(compound.getUniqueId(KEY_ENTITY));
                return entity != null;
            }
        }
        return false;
    }

    public static Integer getSpellId(NBTTagCompound compound) {
        return compound.hasKey(KEY_ID) ? compound.getInteger(KEY_ID) : Integer.MIN_VALUE;
    }

    public static @Nonnull
    NBTTagList toNbtList(@Nonnull Collection<? extends INBTSerializable> collection) {
        NBTTagList nbtTagList = new NBTTagList();
        if (!collection.isEmpty()) {
            for (INBTSerializable serializable :
                    collection) {
                if (serializable != null)
                    nbtTagList.appendTag(serializable.serializeNBT());
            }
        }
        return nbtTagList;
    }

    public static @Nonnull
    NBTTagList nbtToNbtList(@Nonnull Collection<? extends NBTBase> collection) {
        NBTTagList nbtTagList = new NBTTagList();
        if (!collection.isEmpty()) {
            for (NBTBase serializable :
                    collection) {
                if (serializable != null)
                    nbtTagList.appendTag(serializable);
            }
        }
        return nbtTagList;
    }

    public static @Nonnull
    NBTTagList stringToNbtList(@Nonnull Collection<String> collection) {
        NBTTagList nbtTagList = new NBTTagList();
        if (!collection.isEmpty()) {
            for (String serializable :
                    collection) {
                if (serializable != null)
                    nbtTagList.appendTag(new NBTTagString(serializable));
            }
        }
        return nbtTagList;
    }

    public static @Nonnull
    NBTTagList resourcesToNbtList(@Nonnull Collection<ResourceLocation> collection) {
        NBTTagList nbtTagList = new NBTTagList();
        if (!collection.isEmpty()) {
            for (ResourceLocation serializable :
                    collection) {
                if (serializable != null)
                    nbtTagList.appendTag(serializeResourceLocation(serializable));
            }
        }
        return nbtTagList;
    }

    public static NBTTagString serializeResourceLocation(ResourceLocation location) {
        if (location == null) throw new NullPointerException("Cannot serialize Resource from null Parameters!");
        return new NBTTagString(location.toString());
    }

    public static void serializeResourceLocation(String key, ResourceLocation location, NBTTagCompound compound) {
        if (compound == null || location == null || key == null)
            throw new NullPointerException("Cannot serialize Resource from null Parameters!");
        compound.setString(key, location.toString());
    }

    public static @Nullable
    ResourceLocation deserializeResourceLocation(String key, NBTTagCompound compound) {
        if (key == null || compound == null) return null;
        return new ResourceLocation(compound.getString(key));
    }

    public static @Nullable
    ResourceLocation deserializeResourceLocation(NBTTagString nbt) {
        if (nbt == null) return null;
        return new ResourceLocation(nbt.getString());
    }

    public static NBTBase booleanToNBT(boolean bool) {
        return new NBTTagByte(bool ? (byte) 0 : (byte) 1);
    }

    public static boolean booleanFromNBT(NBTBase nbt) {
        if (!(nbt instanceof NBTTagByte))
            throw new IllegalArgumentException("Cannot create Boolean from instance, that wasn't previously created by NBTHelper.booleanToNBT!");
        return ((NBTTagByte) nbt).getByte() == 0;
    }
}
