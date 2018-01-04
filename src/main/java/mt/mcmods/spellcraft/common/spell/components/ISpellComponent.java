package mt.mcmods.spellcraft.common.spell.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.spell.SpellBuilder;
import mt.mcmods.spellcraft.common.spell.access.IAttributeSet;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface ISpellComponent<T extends ISpellComponent<T>> extends IForgeRegistryEntry<T> {
    /**
     * Return all SpellTypes which are compatible with this component
     *
     * @return A list of compatible SpellTypes. It is recommended to return an ImmutableList.
     */
    public @Nonnull
    List<ISpellType> getSupportedTypes();

    /**
     * @return A new AttributeSet corresponding to this SpellComponent. Return null if none is required.
     */
    public @Nullable
    IAttributeSet getAttributes();

    /**
     * @return The localized name of this component.
     */
    public @Nonnull
    String getLocalizedName();

    /**
     * Draws the 16*16 wide Icon Image for this SpellComponent.
     * The Icon should only be drawn within (inclusively) x to x+15 and y to y+15.
     *
     * @param drawingDelegate The drawingDelegate to be used for drawing Operations
     * @param x               The x-Position to draw at (left end of the Icon)
     * @param y               The y-Position to draw at (top-End of the Icon)
     */
    public void drawIcon(@Nonnull GuiDrawingDelegate drawingDelegate, int x, int y);

    /**
     * @return Whether or not this SpellComponent provides a possibility to configure it's attributes.
     * Returning true here implicitly states that getConfigurationGui/getConfigurationGuiContainer returns Nonnull values.
     */
    public boolean hasConfigurableAttributes();

    /**
     * Do not register this Gui with your own Mod. It will be opened by the Framework for you.
     *
     * @param pos        Position of the Block initiating the GuiContainer
     * @param builder    The builder to use as an interface to the Spell in construction
     * @param spellState The SpellState from which configuration was requested
     * @param index      The index of the StateList form which configuration was requested
     * @return A GuiContainer to configure the Corresponding attributes. Null is only permitted if hasConfigurableAttributes returns false.
     */
    public @Nullable
    GuiContainer getConfigurationGui(@Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull SpellBuilder builder, @Nonnull String spellState, int index);

    /**
     * @param pos        Position of the Block initiating the Container
     * @param builder    The builder to use as an interface to the Spell in construction
     * @param spellState The SpellState from which configuration was requested
     * @param index      The index of the StateList form which configuration was requested
     * @return A Container to configure the Corresponding attributes. Null is only permitted if hasConfigurableAttributes returns false.
     */
    public @Nullable
    Container getConfigurationGuiContainer(@Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull SpellBuilder builder, @Nonnull String spellState, int index);
}
