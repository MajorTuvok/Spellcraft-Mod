package mt.mcmods.spellcraft.common.spell.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.spell.SpellBuilder;
import mt.mcmods.spellcraft.common.spell.access.AccessType;
import mt.mcmods.spellcraft.common.spell.access.IAttributeAccess;
import mt.mcmods.spellcraft.common.spell.access.IAttributeProvider;
import mt.mcmods.spellcraft.common.spell.access.IAttributeSet;
import mt.mcmods.spellcraft.common.spell.components.conditions.ISpellCondition;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import mt.mcmods.spellcraft.common.spell.types.SpellTypes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbsSpellComponent<T extends ISpellComponent<T>> extends IForgeRegistryEntry.Impl<T> implements ISpellComponent<T>, Comparable<IForgeRegistryEntry<T>> {
    /**
     * Return all SpellTypes which are compatible with this component. In case of this implementation SpellTypes.getAll() is returned.
     *
     * @return A list of compatible SpellTypes. It is recommended to return an ImmutableList.
     */
    @Override
    public @Nonnull
    List<ISpellType> getSupportedTypes() {
        return SpellTypes.getAll();
    }

    /**
     * @return A new AttributeSet corresponding to this SpellComponent. Return null if none is required.
     */
    @Nullable
    @Override
    public IAttributeSet getAttributes() {
        return null;
    }

    /**
     * Draws the 16*16 wide Icon Image for this SpellComponent.
     * The Icon should only be drawn within (inclusively) x to x+15 and y to y+15.
     *
     * @param drawingDelegate The drawingDelegate to be used for drawing Operations
     * @param x               The x-Position to draw at (left end of the Icon)
     * @param y               The y-Position to draw at (top-End of the Icon)
     */
    @Override
    public void drawIcon(@Nonnull GuiDrawingDelegate drawingDelegate, int x, int y) {

    }

    /**
     * @return The localized name of this component.
     */
    @Nonnull
    @Override
    public String getLocalizedName() {
        return "Missing Name";
    }

    /**
     * @return Whether or not this SpellComponent provides a possibility to configure it's attributes.
     * Returning true here implicitly states that getConfigurationGui/getConfigurationGuiContainer returns Nonnull values.
     */
    @Override
    public boolean hasConfigurableAttributes() {
        return false;
    }

    /**
     * Do not register this Gui with your own Mod. It will be opened by the Framework for you.
     *
     * @param player
     * @param pos        Position of the Block initiating the GuiContainer
     * @param builder    The builder to use as an interface to the Spell in construction
     * @param spellState The SpellState from which configuration was requested
     * @param index      The index of the StateList form which configuration was requested
     * @return A GuiContainer to configure the Corresponding attributes. Null is only permitted if hasConfigurableAttributes returns false.
     */
    @Nullable
    @Override
    public GuiContainer getConfigurationGui(@Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull SpellBuilder builder, @Nonnull String spellState, int index) {
        return null;
    }

    /**
     * @param player
     * @param pos        Position of the Block initiating the Container
     * @param builder    The builder to use as an interface to the Spell in construction
     * @param spellState The SpellState from which configuration was requested
     * @param index      The index of the StateList form which configuration was requested
     * @return A Container to configure the Corresponding attributes. Null is only permitted if hasConfigurableAttributes returns false.
     */
    @Nullable
    @Override
    public Container getConfigurationGuiContainer(@Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull SpellBuilder builder, @Nonnull String spellState, int index) {
        return null;
    }

    /**
     * @return Returns a hashCode of 25 if getRegistryName returns null, otherwise the registryName will be taken into account
     */
    @Override
    public int hashCode() {
        int hash = 25;
        if (getRegistryName() != null) {
            hash = hash * 31 + getRegistryName().toString().hashCode();
        }
        return hash;
    }

    /**
     * @return True if the other Object is an instance of ISpellComponent and the RegistryNames are equal.
     */
    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj != null && obj instanceof ISpellComponent
                && ((this.getRegistryName() != null && ((ISpellComponent) obj).getRegistryName() != null && ((ISpellCondition) obj).getRegistryName().equals(this.getRegistryName()))
                || (this.getRegistryName() == null && ((ISpellComponent) obj).getRegistryName() == null)));
    }

    /**
     * @return The comparison of the two RegistryNames.
     */
    @Override
    public int compareTo(@Nonnull IForgeRegistryEntry<T> o) {
        return Validate.notNull(Validate.notNull(o).getRegistryName()).compareTo(Validate.notNull(this.getRegistryName()));
    }

    /**
     * Returns the Attributes for the given AccessType in the given Provider. Invocation will throw an NullPointerException if getAttributes() returns null.
     * If the given AttributeProvider doesn't have attributes for this SpellConditions RegistryName it will add a new AttributeSet (retrieved via getAttributes()) to the
     * given Provider and return it.
     *
     * @param attributeProvider
     * @param type
     * @return
     */
    public IAttributeSet getAttributes(IAttributeProvider attributeProvider, AccessType type) {
        IAttributeAccess attributeAccess = attributeProvider.getAccess(type);
        if (!attributeAccess.containsAttributeFor(getRegistryName())) {
            IAttributeSet attributeSet = getAttributes();
            attributeAccess.putAttributes(attributeSet);
            return attributeSet;
        }
        return attributeAccess.getAttributes(getRegistryName());
    }
}
