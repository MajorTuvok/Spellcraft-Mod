package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;

public class DragControllerAdapter implements IDragController {
    private boolean mDragging;
    private int mStartX;
    private int mStartY;

    public DragControllerAdapter() {
        mDragging = false;
        mStartX = -1;
        mStartY = -1;
    }

    @Override
    public boolean isDragging() {
        return mDragging;
    }

    public void setDragging(boolean dragging) {
        mDragging = dragging;
    }

    @Override
    public void onDragStart(int mouseX, int mouseY, int dPosX, int dPosY) {

    }

    @Override
    public void onDragUpdateDraw(int mouseX, int mouseY, int dToStartX, int dToSTartY, GuiDrawingDelegate drawingDelegate, DrawLayer layer) {

    }

    @Override
    public void onDragMove(int mouseX, int mouseY, int dToStartX, int dToStartY) {
        if (getStartX() >= 0 && getStartY() >= 0) {
            handleSuggestedPosition(getStartX() + dToStartX, getStartY() + dToStartY);
        }
    }

    @Override
    public void onDragFinished(int mouseX, int mouseY, int dPosX, int dPosY) {
        setDragging(false);
    }

    protected int getStartX() {
        return mStartX;
    }

    public void setStartX(int startX) {
        mStartX = startX;
    }

    protected int getStartY() {
        return mStartY;
    }

    public void setStartY(int startY) {
        mStartY = startY;
    }

    protected void handleSuggestedPosition(int evaluatedX, int evaluatedY) {

    }
}
