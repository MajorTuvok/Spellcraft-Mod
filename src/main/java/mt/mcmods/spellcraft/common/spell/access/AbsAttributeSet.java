package mt.mcmods.spellcraft.common.spell.access;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public abstract class AbsAttributeSet implements IAttributeSet {
    private final Set<AccessType> mAccessTypes;
    private final ResourceLocation mResourceLocation;

    public AbsAttributeSet(ResourceLocation resourceLocation) {
        this(resourceLocation, EnumSet.allOf(AccessType.class));
    }

    public AbsAttributeSet(ResourceLocation resourceLocation, Set<AccessType> accessTypes) {
        if (accessTypes == null || resourceLocation == null)
            throw new NullPointerException("Cannot construct from null values");
        mResourceLocation = resourceLocation;
        mAccessTypes = accessTypes;
    }

    /**
     * (just use EnumSet.of...)
     *
     * @return The AccessTypes to use this AttributeSet with
     */
    @Override
    public Set<AccessType> getSupportedAccessTypes() {
        return Collections.unmodifiableSet(mAccessTypes);
    }

    /**
     * This Method must return the RegistryName of the SpellComponent associated with this AttributeSet.
     * This is necessary so that it is possible to reinstantiate AttributeSets without making any Assumptions via Reflection.
     *
     * @return The identifying ResourceLocation for this AttributeSet
     */
    @Nonnull
    @Override
    public ResourceLocation getComponentKey() {
        return mResourceLocation;
    }
}
