package mt.mcmods.spellcraft.common.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.common.spell.Spell;
import mt.mcmods.spellcraft.common.spell.SpellRegistry;
import mt.mcmods.spellcraft.common.spell.entity.EntitySpell;
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
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public final class NBTHelper {
    public static final String UUID_LEAST_SIG_BITS = "leastSigBits";
    public static final String UUID_MOST_SIG_BITS = "mostSigBits";

    private NBTHelper() {
    }

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

    /*
     * own code starts here...
     */
    public static boolean isSpellEntityInstantiated(NBTTagCompound compound) {
        if (compound.hasKey(EntitySpell.KEY_WORLD) && compound.hasUniqueId(EntitySpell.KEY_ENTITY) && compound.getUniqueId(EntitySpell.KEY_ENTITY) != null) {
            MinecraftServer server = SpellRegistry.getServer();
            if (server != null && DimensionManager.isDimensionRegistered(compound.getInteger(EntitySpell.KEY_WORLD))) {
                WorldServer world = server.getWorld(compound.getInteger(EntitySpell.KEY_WORLD));
                Entity entity = world.getEntityFromUuid(compound.getUniqueId(EntitySpell.KEY_ENTITY));
                return entity != null;
            }
        }
        return false;
    }

    @Nonnull
    public static Integer getSpellId(NBTTagCompound compound) {
        return compound.hasKey(Spell.KEY_ID) ? compound.getInteger(Spell.KEY_ID) : SpellRegistry.NO_ID;
    }

    @Nonnull
    public static String getSpellName(NBTTagCompound compound) {
        return compound.hasKey(Spell.KEY_DISPLAY_NAME) ? compound.getString(Spell.KEY_DISPLAY_NAME) : "";
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

    public static NBTTagByteArray createBooleanList(boolean[] booleans) {
        byte[] bytes = new byte[booleans.length];
        for (int i = 0; i < booleans.length; ++i) {
            bytes[i] = (byte) (booleans[i] ? 0 : 1);
        }
        return new NBTTagByteArray(bytes);
    }

    public static NBTTagByteArray createBooleanList(Boolean[] booleans) {
        byte[] bytes = new byte[booleans.length];
        for (int i = 0; i < booleans.length; ++i) {
            bytes[i] = (byte) (booleans[i] ? 0 : 1);
        }
        return new NBTTagByteArray(bytes);
    }

    public static NBTTagList createShortList(short[] shorts) {
        NBTTagList list = new NBTTagList();
        for (short s : shorts) {
            list.appendTag(new NBTTagShort(s));
        }
        return list;
    }

    public static NBTTagList createShortList(Short[] shorts) {
        NBTTagList list = new NBTTagList();
        for (short s : shorts) {
            list.appendTag(new NBTTagShort(s));
        }
        return list;
    }

    @Nonnull
    public static NBTTagList createFloatList(float[] floats) {
        NBTTagList list = new NBTTagList();
        for (float f : floats) {
            list.appendTag(new NBTTagFloat(f));
        }
        return list;
    }

    @Nonnull
    public static NBTTagList createFloatList(Float[] floats) {
        NBTTagList list = new NBTTagList();
        for (Float f : floats) {
            list.appendTag(new NBTTagFloat(f));
        }
        return list;
    }

    @Nonnull
    public static NBTTagList createDoubleList(double[] doubles) {
        NBTTagList list = new NBTTagList();
        for (double d : doubles) {
            list.appendTag(new NBTTagDouble(d));
        }
        return list;
    }

    @Nonnull
    public static NBTTagList createDoubleList(Double[] doubles) {
        NBTTagList list = new NBTTagList();
        for (Double d : doubles) {
            list.appendTag(new NBTTagDouble(d));
        }
        return list;
    }

    @Nonnull
    public static short[] readShortList(NBTTagList shorts) {
        short[] res = new short[shorts.tagCount()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < shorts.tagCount(); i++) {
            NBTBase nbt = shorts.get(i);
            if (nbt instanceof NBTTagShort) {
                res[i] = ((NBTTagShort) nbt).getShort();
            } else {
                res[i] = 0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            short[] shortened = new short[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i)) {
                    continue;
                }
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static Short[] readBShortList(NBTTagList shorts) {
        Short[] res = new Short[shorts.tagCount()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < shorts.tagCount(); i++) {
            NBTBase nbt = shorts.get(i);
            if (nbt instanceof NBTTagShort) {
                res[i] = ((NBTTagShort) nbt).getShort();
            } else {
                res[i] = 0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            Short[] shortened = new Short[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i)) {
                    continue;
                }
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static NBTTagList createStringList(String[] strings) {
        NBTTagList list = new NBTTagList();
        for (String s : strings) {
            list.appendTag(new NBTTagString(s));
        }
        return list;
    }

    @Nonnull
    public static float[] readFloatList(NBTTagList floats) {
        float[] res = new float[floats.tagCount()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < floats.tagCount(); i++) {
            NBTBase nbt = floats.get(i);
            if (nbt instanceof NBTTagFloat) {
                res[i] = ((NBTTagFloat) nbt).getFloat();
            } else {
                res[i] = 0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            float[] shortened = new float[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i)) {
                    continue;
                }
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static Float[] readBFloatList(NBTTagList floats) {
        Float[] res = new Float[floats.tagCount()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < floats.tagCount(); i++) {
            NBTBase nbt = floats.get(i);
            if (nbt instanceof NBTTagFloat) {
                res[i] = ((NBTTagFloat) nbt).getFloat();
            } else {
                res[i] = 0f;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            Float[] shortened = new Float[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i)) {
                    continue;
                }
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static double[] readDoubleList(NBTTagList doubles) {
        double[] res = new double[doubles.tagCount()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < doubles.tagCount(); i++) {
            NBTBase nbt = doubles.get(i);
            if (nbt instanceof NBTTagDouble) {
                res[i] = ((NBTTagDouble) nbt).getDouble();
            } else {
                res[i] = 0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            double[] shortened = new double[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i)) {
                    continue;
                }
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static Double[] readBDoubleList(NBTTagList doubles) {
        Double[] res = new Double[doubles.tagCount()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < doubles.tagCount(); i++) {
            NBTBase nbt = doubles.get(i);
            if (nbt instanceof NBTTagDouble) {
                res[i] = ((NBTTagDouble) nbt).getDouble();
            } else {
                res[i] = 0.0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            Double[] shortened = new Double[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i)) {
                    continue;
                }
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static String[] readStringList(NBTTagList strings) {
        String[] res = new String[strings.tagCount()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < strings.tagCount(); i++) {
            NBTBase nbt = strings.get(i);
            if (nbt instanceof NBTTagString) {
                res[i] = ((NBTTagString) nbt).getString();
            } else {
                res[i] = "";
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            String[] shortened = new String[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i)) {
                    continue;
                }
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    public static boolean[] readBooleanList(NBTTagByteArray booleans) {
        byte[] bytes = booleans.getByteArray();
        boolean[] res = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; ++i) {
            res[i] = bytes[i] == 0;
        }
        return res;
    }

    public static Boolean[] readBBooleanList(NBTTagByteArray booleans) {
        byte[] bytes = booleans.getByteArray();
        Boolean[] res = new Boolean[bytes.length];
        for (int i = 0; i < bytes.length; ++i) {
            res[i] = bytes[i] == 0;
        }
        return res;
    }

    public static <K, V> NBTTagList serializeMap(Map<K, V> map, Function<K, NBTBase> keySerializer, Function<V, NBTBase> valueSerializer) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("key", keySerializer.apply(entry.getKey()));
            compound.setTag("val", valueSerializer.apply(entry.getValue()));
            list.appendTag(compound);
        }
        return list;
    }

    public static <K, V> Map<K, V> deserializeMap(NBTTagList list, Map<K, V> toAppendTo, Function<NBTBase, K> keyDeserializer, Function<NBTBase, V> valueDeserializer) {
        for (NBTBase nbt : list) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                toAppendTo.put(
                        keyDeserializer.apply(compound.getTag("key")),
                        valueDeserializer.apply(compound.getTag("val"))
                );
            }
        }
        return toAppendTo;
    }
}
