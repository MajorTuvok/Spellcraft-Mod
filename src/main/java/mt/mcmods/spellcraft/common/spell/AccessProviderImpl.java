package mt.mcmods.spellcraft.common.spell;

import jline.internal.Log;
import mt.mcmods.spellcraft.common.spell.access.AccessType;
import mt.mcmods.spellcraft.common.spell.access.IAttributeAccess;
import mt.mcmods.spellcraft.common.spell.access.IAttributeSet;
import mt.mcmods.spellcraft.common.spell.components.ISpellComponent;
import mt.mcmods.spellcraft.common.spell.components.conditions.SpellcraftConditions;
import mt.mcmods.spellcraft.common.spell.components.executables.SpellcraftExecutables;
import mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class AccessProviderImpl implements IAttributeAccess {
    private static final String KEY_KEYS = "AccessProviderImpl_keys";
    private static final String KEY_VALUES = "AccessProviderImpl_values";
    private AccessType mAccessType;
    private HashMap<ResourceLocation, IAttributeSet> mUnderlyingMap;

    AccessProviderImpl(AccessType accessType) {
        this.mUnderlyingMap = new HashMap<>();
        if (accessType == null) throw new NullPointerException("Cannot have a null AccessType!");
        this.mAccessType = accessType;
    }

    /**
     * @return The AccessType of which this AttributeAccess is
     */
    @Override
    public AccessType getType() {
        return mAccessType;
    }

    /**
     * Get the Attributes for the given ResourceLocation
     *
     * @param key The ResourceLocation to look for
     * @return The IAttributeSet for the corresponding key, if found. Null if not.
     * @throws NullPointerException if key was null
     */
    @Nullable
    @Override
    public IAttributeSet getAttributes(ResourceLocation key) {
        if (key == null) throw new NullPointerException("Cannot retrieve Attributes for null key");
        return mUnderlyingMap.get(key);
    }

    /**
     * Checks if this AttributeAccess contains the given key
     *
     * @param key The key to look for
     * @return True if this AttributeAccess contains the given key, false if not
     * @throws NullPointerException if key was null
     */
    @Override
    public boolean containsAttributeFor(ResourceLocation key) {
        if (key == null) throw new NullPointerException("Cannot check Attributes for null key");
        return mUnderlyingMap.containsKey(key);
    }

    /**
     * Attempts to add the given AttributeSet to this AttributeAccess.
     * Will fail if it already contains an AttributeSet with the ResourceLocation provided by this AttributeSet.
     * Will fail if the AttributeSet doesn't permit being added to this Access (e.g. getSupportedTypes).
     *
     * @param set The set to add
     * @return true if the AttributeSet could be added, false if not
     * @throws NullPointerException if set was null
     */
    @Override
    public boolean putAttributes(IAttributeSet set) {
        if (set == null) throw new NullPointerException("Cannot add null Attributes");
        Set<AccessType> types = set.getSupportedAccessTypes();
        ResourceLocation key = set.getComponentKey();
        if (mUnderlyingMap.containsKey(key)) return false;
        if (!types.contains(getType())) return false;
        mUnderlyingMap.put(key, set);
        return true;
    }

    /**
     * Attempts to remove the AttributeSet for the given key from this AttributeAccess.
     *
     * @param key The key for the set to remove
     * @return An AttributeSet if Operation was successful, null if not
     * @throws NullPointerException if key was null
     */
    @Override
    public IAttributeSet removeAttributes(ResourceLocation key) {
        if (key == null) throw new NullPointerException("Cannot remove attributes for null key");
        return mUnderlyingMap.containsKey(key) ? mUnderlyingMap.remove(key) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList keys = new NBTTagList();
        NBTTagList values = new NBTTagList();
        for (Map.Entry<ResourceLocation, IAttributeSet> entry :
                mUnderlyingMap.entrySet()) {
            keys.appendTag(NBTHelper.serializeResourceLocation(entry.getKey()));
            values.appendTag(entry.getValue().serializeNBT());
        }
        compound.setTag(KEY_KEYS, keys);
        compound.setTag(KEY_VALUES, values);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey(KEY_KEYS) || !nbt.hasKey(KEY_VALUES))
            throw new IllegalArgumentException("Cannot deserialize NBT-Data from NBTTagCompound which wasn't previously created by serializeNBT");
        Iterator<NBTBase> keys = ((NBTTagList) nbt.getTag(KEY_KEYS)).iterator();
        Iterator<NBTBase> values = ((NBTTagList) nbt.getTag(KEY_VALUES)).iterator();
        while (keys.hasNext() && values.hasNext()) {
            ResourceLocation key = NBTHelper.deserializeResourceLocation((NBTTagString) keys.next());
            NBTTagCompound compound = (NBTTagCompound) values.next();
            ISpellComponent<?> component = SpellcraftConditions.getInstance().findCondition(key);
            if (component == null) {
                component = SpellcraftExecutables.getInstance().findExecutable(key);
            }
            if (component == null) {
                Log.warn("Could not deserialize Attributes associated with " + key + " because there is no corresponding SpellComponent registered!");
                continue;
            }
            IAttributeSet attributes = component.createAttributes();
            if (attributes == null) {
                Log.warn("Could not deserialize Attributes associated with " + key +
                        " because the corresponding SpellComponent does not provide any Attributes! This might be a compatibility issue related to updating the " + key.getResourceDomain() + " version.");
                continue;
            }
            attributes.deserializeNBT(compound);
            putAttributes(attributes);
        }
    }

    @Override
    public String toString() {
        return "AccessProviderImpl{" +
                "mAccessType=" + mAccessType +
                '}';
    }
}
