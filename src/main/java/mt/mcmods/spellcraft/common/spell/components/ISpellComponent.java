package mt.mcmods.spellcraft.common.spell.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.spell.SpellBuilder;
import mt.mcmods.spellcraft.common.spell.access.AccessType;
import mt.mcmods.spellcraft.common.spell.access.IAttributeAccess;
import mt.mcmods.spellcraft.common.spell.access.IAttributeProvider;
import mt.mcmods.spellcraft.common.spell.access.IAttributeSet;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import mt.mcmods.spellcraft.common.spell.types.SpellTypes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public interface ISpellComponent<T extends ISpellComponent<T>> extends IForgeRegistryEntry<T> {
    /**
     * Return all SpellTypes which are compatible with this component
     *
     * @return A list of compatible SpellTypes. It is recommended to return an ImmutableList.
     */
    public @Nonnull
    default Collection<ISpellType> getSupportedTypes() {
        return SpellTypes.getAll(); //SpellTypes.getAll already returns an unmodifiableList...
    }

    /**
     * @return A new AttributeSet corresponding to this SpellComponent. Return null if none is required.
     */
    public @Nullable
    default IAttributeSet createAttributes() {
        return null;
    }

    /**
     * @param attributeProvider The AttributeProvider to query for attributes
     * @return The localized name of this component.
     */
    public @Nonnull
    String getLocalizedName(IAttributeProvider attributeProvider);

    /**
     * @param toolTips            The list to which toolTips should be added.
     * @param attributeProvider   Attribute Provider which enables access to this Components attributes
     * @param extendedInformation Whether or not only all Information available or only a brief summary (like f.e. press sneak to show everything) should be shown.
     * @return Void Optional if some Error prevented showing all Tooltips. True if extended Information was shown, false if not.
     */
    public @Nonnull
    @SideOnly(Side.CLIENT)
    default Optional<Boolean> addTooltipInformation(NonNullList<String> toolTips, IAttributeProvider attributeProvider, boolean extendedInformation) {
        return Optional.of(extendedInformation);
    }

    /**
     * Draws the 16*16 wide Icon Image for this SpellComponent.
     * The Icon should only be drawn within (inclusively) x to x+15 and y to y+15.
     *
     * @param drawingDelegate The drawingDelegate to be used for drawing Operations
     * @param x               The x-Position to draw at (left end of the Icon)
     * @param y               The y-Position to draw at (top-End of the Icon)
     */
    @SideOnly(Side.CLIENT)
    public void drawIcon(@Nonnull GuiDrawingDelegate drawingDelegate, int x, int y);

    /**
     * @return Whether or not this SpellComponent provides a possibility to configure it's attributes.
     * Returning true here implicitly states that getConfigurationGui/getConfigurationGuiContainer returns Nonnull values.
     */
    public default boolean hasConfigurableAttributes() {
        return createAttributes() == null;
    }

    /**
     * Do not register this Gui with your own Mod. It will be opened by the Framework for you.
     *
     * @param pos        Position of the Block initiating the GuiContainer
     * @param builder    The builder to use as an interface to the Spell in construction
     * @param spellState The SpellState from which configuration was requested
     * @param index      The index of the StateList form which configuration was requested
     * @return A GuiContainer to configure the Corresponding attributes. Null is only permitted if hasConfigurableAttributes returns false.
     */
    public
    default GuiContainer getConfigurationGui(@Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull SpellBuilder builder, @Nonnull String spellState, int index) {
        return null;
    }

    /**
     * Do not register this Gui with your own Mod. It will be opened by the Framework for you.
     *
     * @param pos        Position of the Block initiating the Container
     * @param builder    The builder to use as an interface to the Spell in construction
     * @param spellState The SpellState from which configuration was requested
     * @param index      The index of the StateList form which configuration was requested
     * @return A Container to configure the Corresponding attributes. Null is only permitted if hasConfigurableAttributes returns false.
     */
    public
    default Container getConfigurationGuiContainer(@Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull SpellBuilder builder, @Nonnull String spellState, int index) {
        return null;
    }

    /**
     * Returns the Attributes for the given AccessType in the given Provider. Invocation will throw an NullPointerException if createAttributes() returns null.
     * If the given AttributeProvider doesn't have attributes for this SpellConditions RegistryName it will add a new AttributeSet (retrieved via createAttributes()) to the
     * given Provider and return it.
     * Override this Method, if you want your attributes to be keyed by something else then this Components RegistryName.
     *
     * @param attributeProvider The AttributeProvider to retrieve from
     * @param type              The accessType to use
     * @return Either an freshly created AttributeSet (if none was available), or an existing one (if one was available).
     */
    public default IAttributeSet getOrCreateAttributes(IAttributeProvider attributeProvider, AccessType type) {
        IAttributeAccess attributeAccess = attributeProvider.getAccess(type);
        if (!attributeAccess.containsAttributeFor(getRegistryName())) {
            IAttributeSet attributeSet = createAttributes();
            Objects.requireNonNull(attributeSet);
            attributeAccess.putAttributes(attributeSet);
            return attributeSet;
        }
        return attributeAccess.getAttributes(getRegistryName());
    }
}
