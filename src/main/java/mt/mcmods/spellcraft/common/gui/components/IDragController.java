package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;

public interface IDragController extends Constants {

    public boolean isDragging();

    public void onDragStart(int mouseX, int mouseY, int dPosX, int dPosY);

    public void onDragUpdateDraw(int mouseX, int mouseY, int dToStartX, int dToSTartY, GuiDrawingDelegate drawingDelegate, DrawLayer layer);

    public void onDragMove(int mouseX, int mouseY, int dToStartX, int dToStartY);

    public void onDragFinished(int mouseX, int mouseY, int dPosX, int dPosY);

    public default boolean isDragMouseButton(int mouseButton) {
        return MOUSE_BUTTON_LEFT == mouseButton;
    }
}
