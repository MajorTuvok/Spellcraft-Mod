package mt.mcmods.spellcraft.common.spell;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.spell.access.AccessType;
import mt.mcmods.spellcraft.common.spell.access.IAttributeAccess;
import mt.mcmods.spellcraft.common.spell.access.IAttributeProvider;
import mt.mcmods.spellcraft.common.spell.access.IAttributeSet;
import mt.mcmods.spellcraft.common.spell.components.ISpellComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.concurrent.Callable;

class AttributeProviderImpl implements IAttributeProvider {
    private IAttributeAccess mGlobalAccess;
    private IAttributeAccess mLocalAccess;
    private IAttributeAccess mStateAccess;

    /**
     * @return The AttributeAccess common to the whole Spell
     * @throws IllegalStateException if an internalError occurred
     */
    @Nonnull
    @Override
    public IAttributeAccess getGlobalAttributes() {
        checkState();
        return mGlobalAccess;
    }

    /**
     * @return The AttributeAccess common to the current SpellState
     * @throws IllegalStateException if an internalError occurred
     */
    @Nonnull
    @Override
    public IAttributeAccess getStateAttributes() {
        checkState();
        return mStateAccess;
    }

    /**
     * @return The AttributeAccess common only to the currently active StateSet.
     * @throws IllegalStateException if an internalError occurred
     */
    @Nonnull
    @Override
    public IAttributeAccess getLocalAttributes() {
        checkState();
        return mLocalAccess;
    }

    /**
     * Adds a new Instance provided by this callable to every Access where it fits (e.g. getSupportedTypes).
     *
     * @param callable The callable Object to use as Factory for AttributeSets
     * @return False if an Exception was thrown or if no AttributeSet was added. True otherwise.
     * @throws IllegalStateException if an internalError occurred
     */
    @Override
    public boolean addCorrespondingToTypes(Callable<? extends IAttributeSet> callable) {
        checkState();
        try {
            IAttributeSet set = callable.call();
            Set<AccessType> types = set.getSupportedAccessTypes();
            if (types.isEmpty()) return false;
            int count = 0;
            for (AccessType type :
                    types) {
                getAccess(type).putAttributes(set);
                if (++count < types.size()) {
                    set = callable.call();
                }
            }
            return true;
        } catch (Exception e) {
            ILoggable.Log.error("Failed to create AttributeSet", e);
        }
        return false;
    }

    /**
     * Adds a new Instance provided by this component to every Access where it fits (e.g. getSupportedTypes).
     *
     * @param component The component Object to use as Factory for AttributeSets
     * @return False if an Exception was thrown or if no AttributeSet was added. True otherwise.
     * @throws IllegalStateException if an internalError occurred
     */
    @Override
    public <T extends ISpellComponent<T>> boolean addCorrespondingToTypes(ISpellComponent<T> component) {
        checkState();
        try {
            IAttributeSet set = component.getAttributes();
            if (set == null) return false;
            Set<AccessType> types = set.getSupportedAccessTypes();
            if (types.isEmpty()) return false;
            int count = 0;
            for (AccessType type :
                    types) {
                getAccess(type).putAttributes(set);
                if (++count < types.size()) {
                    set = component.getAttributes();
                }
            }
            return true;
        } catch (Exception e) {
            ILoggable.Log.error("Failed to create AttributeSet", e);
        }
        return false;
    }

    /**
     * @param type The AccessType to look for
     * @return The AttributeAccess for the given AccessType.
     * @throws NullPointerException  if type was null
     * @throws IllegalStateException if an internalError occurred
     */
    @Nonnull
    @Override
    public IAttributeAccess getAccess(AccessType type) {
        checkState();
        if (type == AccessType.LOCAL)
            return getLocalAttributes();
        if (type == AccessType.STATE)
            return getStateAttributes();
        return getGlobalAttributes();
    }

    /**
     * Tries to find all Access-possibilities for the given ResourceLocation.
     *
     * @param location The Key for the Attributes to find
     * @return A NonNullList containing up to all three Attribute-Access's. May be empty if no matching Access is found.
     * @throws IllegalStateException if an internalError occurred
     */
    @Nonnull
    @Override
    public NonNullList<IAttributeAccess> findAccessFor(ResourceLocation location) {
        checkState();
        NonNullList<IAttributeAccess> list = NonNullList.create();
        IAttributeAccess attributeAccess = getGlobalAttributes().containsAttributeFor(location) ? getGlobalAttributes() : null;
        if (attributeAccess != null) list.add(attributeAccess);
        attributeAccess = getStateAttributes().containsAttributeFor(location) ? getStateAttributes() : null;
        if (attributeAccess != null) list.add(attributeAccess);
        attributeAccess = getLocalAttributes().containsAttributeFor(location) ? getLocalAttributes() : null;
        if (attributeAccess != null) list.add(attributeAccess);
        return list;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }

    AttributeProviderImpl with(IAttributeAccess globalAccess, IAttributeAccess stateAccess, IAttributeAccess localAccess) {
        //does not check whether they are not Null so that it cannot fail silently when there is some null Access
        this.mGlobalAccess = globalAccess;
        this.mStateAccess = stateAccess;
        this.mLocalAccess = localAccess;
        return this;
    }

    private void checkState() throws IllegalStateException {
        if (mGlobalAccess == null)
            throw new IllegalStateException("Cannot perform Provider actions without an Spell-specific AttributeAccess");
        if (mStateAccess == null)
            throw new IllegalStateException("Cannot perform Provider actions without an SpellState-specific AttributeAccess");
        if (mLocalAccess == null)
            throw new IllegalStateException("Cannot perform Provider actions without an SpellStateSet-specific AttributeAccess");
    }
}
