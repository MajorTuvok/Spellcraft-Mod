package mt.mcmods.spellcraft.common.gui;

import mt.mcmods.spellcraft.common.gui.components.ViewComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;

public interface IComponentCallback {

    public int getGuiXSize();

    public int getGuiYSize();

    public int getGuiTop();

    public int getGuiLeft();

    public float getLastPartialTicks();

    public int getLastMouseX();

    public int getLastMouseY();

    public int getMouseDx();

    public int getMouseDy();

    public @Nonnull
    Comparator<? extends ViewComponent> getComponentPriorityComparator();

    public long requestNewId();

    public @Nullable
    ViewComponent findViewById(long id);

    public @Nonnull
    BaseGui getGui();

}
