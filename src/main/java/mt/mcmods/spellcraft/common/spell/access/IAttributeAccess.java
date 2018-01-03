package mt.mcmods.spellcraft.common.spell.access;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface IAttributeAccess extends INBTSerializable<NBTTagCompound>{
    /**
     * Get the Attributes for the given ResourceLocation
     * @param key The ResourceLocation to look for
     * @return The IAttributeSet for the corresponding key, if found. Null if not.
     * @throws NullPointerException if key was null
     */
    public @Nullable IAttributeSet getAttributes(ResourceLocation key);

    /**
     * Checks if this AttributeAccess contains the given key
     * @param key The key to look for
     * @return True if this AttributeAccess contains the given key, false if not
     * @throws NullPointerException if key was null
     */
    public boolean containsAttributeFor(ResourceLocation key);

    /**
     * Attempts to add the given AttributeSet to this AttributeAccess.
     * Will fail if it already contains an AttributeSet with the ResourceLocation provided by this AttributeSet.
     * Will fail if the AttributeSet doesn't permit being added to this Access (e.g. getSupportedTypes).
     * @param set The set to add
     * @return true if the AttributeSet could be added, false if not
     * @throws NullPointerException if set was null
     */
    public boolean putAttributes(IAttributeSet set);

    /**
     * Attempts to remove the AttributeSet for the given key from this AttributeAccess.
     * @param key The key for the set to remove
     * @return An AttributeSet if Operation was successful, null if not
     * @throws NullPointerException if key was null
     */
    public IAttributeSet removeAttributes(ResourceLocation key);

    /**
     *
     * @return The AccessType of which this AttributeAccess is
     */
    public AccessType getType();
}
