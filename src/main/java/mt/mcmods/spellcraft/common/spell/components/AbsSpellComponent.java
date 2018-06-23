package mt.mcmods.spellcraft.common.spell.components;

import jline.internal.Log;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.spell.access.IAttributeProvider;
import mt.mcmods.spellcraft.common.spell.components.conditions.ISpellCondition;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class AbsSpellComponent<T extends ISpellComponent<T>> extends IForgeRegistryEntry.Impl<T> implements ISpellComponent<T>, Comparable<IForgeRegistryEntry<T>> {
    /**
     * @return Returns a hashCode of 25 if getRegistryName returns null, otherwise the registryName will be taken into account
     */
    public static int standardHashCode(ISpellComponent<? extends ISpellComponent<?>> component) {
        int hash = 25;
        if (component.getRegistryName() != null) {
            hash = hash * 31 + component.getRegistryName().toString().hashCode();
        }
        return hash;
    }

    public static boolean standardEquals(@Nonnull ISpellComponent<? extends ISpellComponent<?>> component, @Nonnull Object obj) {
        return obj == component || (obj instanceof ISpellComponent
                && ((component.getRegistryName() != null && ((ISpellComponent) obj).getRegistryName() != null && ((ISpellCondition) obj).getRegistryName().equals(component.getRegistryName()))
                || (component.getRegistryName() == null && ((ISpellComponent) obj).getRegistryName() == null)));
    }

    public static int standardCompare(@Nonnull ISpellComponent<? extends ISpellComponent<?>> component, @Nonnull IForgeRegistryEntry<? extends IForgeRegistryEntry<?>> o) {
        return Objects.requireNonNull(Objects.requireNonNull(o).getRegistryName()).compareTo(Objects.requireNonNull(Objects.requireNonNull(component).getRegistryName()));
    }

    /**
     * @return The localized name of this component.
     */
    @Nonnull
    @Override
    public String getLocalizedName(IAttributeProvider provider) {
        return "Missing Name";
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
    @SideOnly(Side.CLIENT)
    public void drawIcon(@Nonnull GuiDrawingDelegate drawingDelegate, boolean hovered, boolean selected, int x, int y) {
        Log.warn("Failed to override ISpellComponent.drawIcon(GuiDrawingDelegate,int,int) in class {}, though it is apparently used!", this.getClass().getSimpleName());
    }

    @Override
    public int hashCode() {
        return standardHashCode(this);
    }

    /**
     * @return True if the other Object is an instance of ISpellComponent and the RegistryNames are equal.
     */
    @Override
    public boolean equals(Object obj) {
        return standardEquals(this, obj);
    }

    /**
     * @return The comparison of the two RegistryNames.
     */
    @Override
    public int compareTo(@Nonnull IForgeRegistryEntry<T> o) {
        return standardCompare(this, o);
    }
}
