package mt.mcmods.spellcraft.common.spell.access;

import mt.mcmods.spellcraft.common.spell.components.ISpellComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;

public interface IAttributeProvider extends INBTSerializable<NBTTagCompound> {
    /**
     * @return The AttributeAccess common to the whole Spell
     * @throws IllegalStateException if an internalError occurred
     */
    public @Nonnull
    IAttributeAccess getGlobalAttributes();

    /**
     * @return The AttributeAccess common to the current SpellState
     * @throws IllegalStateException if an internalError occurred
     */
    public @Nonnull
    IAttributeAccess getStateAttributes();

    /**
     * @return The AttributeAccess common only to the currently active StateSet.
     * @throws IllegalStateException if an internalError occurred
     */
    public @Nonnull
    IAttributeAccess getLocalAttributes();

    /**
     * Adds a new Instance provided by this callable to every Access where it fits (e.g. getSupportedTypes).
     *
     * @param callable The callable Object to use as Factory for AttributeSets
     * @return False if an Exception was thrown or if no AttributeSet was added. True otherwise.
     * @throws IllegalStateException if an internalError occurred
     */
    public boolean addCorrespondingToTypes(Callable<? extends IAttributeSet> callable);


    /**
     * Adds a new Instance provided by this component to every Access where it fits (e.g. getSupportedTypes).
     *
     * @param component The component Object to use as Factory for AttributeSets
     * @return False if an Exception was thrown, components createAttributes Method returned null or if no AttributeSet was added . True otherwise.
     * @throws IllegalStateException if an internalError occurred
     */
    public <T extends ISpellComponent<T>> boolean addCorrespondingToTypes(ISpellComponent<T> component);

    /**
     * @param type The AccessType to look for
     * @return The AttributeAccess for the given AccessType.
     * @throws NullPointerException  if type was null
     * @throws IllegalStateException if an internalError occurred
     */
    public @Nonnull
    IAttributeAccess getAccess(AccessType type);

    /**
     * Tries to find all Access-possibilities for the given ResourceLocation.
     *
     * @param location The Key for the Attributes to find
     * @return A NonNullList containing up to all three Attribute-Access's. May be empty if no matching Access is found.
     * @throws IllegalStateException if an internalError occurred
     */
    public @Nonnull
    NonNullList<IAttributeAccess> findAccessFor(ResourceLocation location);
}
