package mt.mcmods.spellcraft.common.gui.components;


import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import net.minecraft.client.gui.GuiButton;

import javax.annotation.Nullable;

public class ButtonAdapterComponent extends ViewComponent {
    private final GuiButton mButton;
    private boolean mAddButton;
    private boolean mDraggable;

    public ButtonAdapterComponent(GuiButton button) {
        super(button.x, button.y, button.width, button.height);
        setDraggable(false);
        mButton = button;
        mAddButton = true;
    }

    public ButtonAdapterComponent setAddButton(boolean addButton) {
        mAddButton = addButton;
        return this;
    }

    public boolean isDraggable() {
        return mDraggable;
    }

    public ButtonAdapterComponent setDraggable(boolean draggable) {
        mDraggable = draggable;
        setFocusable(isDraggable());
        setClickable(isDraggable());
        return this;
    }

    public GuiButton getButton() {
        return mButton;
    }

    public boolean addButton() {
        return mAddButton;
    }

    @Nullable
    @Override
    protected IDragController createDragController() {
        return new ButtonDragController();
    }

    @Override
    protected void onAdd() {
        if (addButton()) {
            getComponentCallback().getGui().addButton(mButton);
        }
    }

    protected class ButtonDragController extends DragControllerAdapter {
        @Override
        public void onDragStart(int mouseX, int mouseY, int dPosX, int dPosY) {
            setDragging(isDraggable());
            if (isDragging()) {
                setStartX(getXPos());
                setStartY(getYPos());
            }
        }

        @Override
        public void onDragUpdateDraw(int mouseX, int mouseY, int dToStartX, int dToSTartY, GuiDrawingDelegate drawingDelegate, DrawLayer layer) {
            //not required, because the Button is drawn completely by the gui
        }

        @Override
        protected void handleSuggestedPosition(int evaluatedX, int evaluatedY) {
            setButtonPos(evaluatedX, evaluatedY);
            setXPos(evaluatedX);
            setYPos(evaluatedY);
        }

        private void setButtonPos(int x, int y) {
            mButton.x = x;
            mButton.y = y;
        }
    }
}
