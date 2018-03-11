package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;


public class ViewComponentGroup extends ViewComponent {
    private boolean mDragGroup;
    private boolean mDraggable;
    private PriorityQueue<ViewComponent> mSubComponents;

    public ViewComponentGroup(int xPos, int yPos, int xSize, int ySize) {
        super(xPos, yPos, xSize, ySize);
        mSubComponents = new PriorityQueue<>(ComponentPriorityComparator.INSTANCE);
        mDraggable = true;
        mDragGroup = false;
        setFocusable(true); //In order to allow subclasses to be focused and clicked
        setClickable(true);
    }

    @Nullable
    @Override
    protected ViewComponentGroupDragController createDragController() {
        return new ViewComponentGroupDragController();
    }

    public ViewComponentGroup setDragGroup(boolean dragGroup) {
        mDragGroup = dragGroup;
        return this;
    }

    @Override
    public ViewComponentGroup setPriority(int priority) {
        return (ViewComponentGroup) super.setPriority(priority);
    }

    @Override
    public ViewComponentGroup setXPos(int xPos) {
        return (ViewComponentGroup) super.setXPos(xPos);
    }

    @Override
    public ViewComponentGroup setYPos(int yPos) {
        return (ViewComponentGroup) super.setYPos(yPos);
    }

    @Override
    public ViewComponentGroup setXSize(int xSize) {
        return (ViewComponentGroup) super.setXSize(xSize);
    }

    @Override
    public ViewComponentGroup setYSize(int ySize) {
        return (ViewComponentGroup) super.setYSize(ySize);
    }

    @Override
    public void drawLayer(GuiDrawingDelegate drawingDelegate, int mouseX, int mouseY, DrawLayer layer) {
        drawChildrenLayer(drawingDelegate, mouseX, mouseY, layer);
        super.drawLayer(drawingDelegate, mouseX, mouseY, layer);
    }

    @Override
    protected void onUpdate() {
        for (ViewComponent c :
                mSubComponents) {
            c.onUpdate();
        }
    }

    @Override
    protected void onResize() {
        for (ViewComponent c :
                mSubComponents) {
            c.onResize();
        }
    }

    @Override
    protected void onGainFocus(int mouseX, int mouseY) {
        for (ViewComponent c :
                mSubComponents) {
            if (c.isFocusable() && c.liesWithinComponentRegion(mouseX, mouseY)) {
                c.handleGainFocus(mouseX, mouseY);
            }
        }
    }

    @Override
    protected void onFocus(int mouseX, int mouseY) {
        for (ViewComponent c :
                mSubComponents) {
            if (c.isFocusable()) {
                if (c.liesWithinComponentRegion(mouseX, mouseY)) {
                    if (c.isFocused()) {
                        c.handleFocus(mouseX, mouseY);
                    } else {
                        c.handleGainFocus(mouseX, mouseY);
                    }
                } else if (c.isFocused()) {
                    c.handleLoseFocus(mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void onLoseFocus(int mouseX, int mouseY) {
        for (ViewComponent c :
                mSubComponents) {
            if (c.isFocused()) {
                c.handleLoseFocus(mouseX, mouseY);
            }
        }
    }

    @Override
    protected void onClick(int mouseX, int mouseY, int mouseButton) {
        for (ViewComponent c :
                mSubComponents) {
            if (c.isClickable() && c.isFocused() && c.liesWithinComponentRegion(mouseX, mouseY)) {
                c.onClick(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    protected void onClickReleased(int mouseX, int mouseY, int state) {
        for (ViewComponent c :
                mSubComponents) {
            if (c.isClickable() && c.isFocused() && c.liesWithinComponentRegion(mouseX, mouseY)) {
                c.onClickReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    protected void onKeyTyped(char typedChar, int keycode) {
        for (ViewComponent c :
                mSubComponents) {
            c.onKeyTyped(typedChar, keycode);
        }
    }

    public boolean isDraggable() {
        return mDraggable;
    }

    public ViewComponentGroup setDraggable(boolean draggable) {
        mDraggable = draggable;
        return this;
    }

    public boolean isDraggingGroup() {
        return mDragGroup;
    }

    public @Nullable
    ViewComponent findViewById(long id) {
        List<ViewComponentGroup> groups = new ArrayList<>(mSubComponents.size());
        for (ViewComponent c :
                mSubComponents) {
            if (c.getId() == id) return c;
            if (c instanceof ViewComponentGroup) groups.add((ViewComponentGroup) c);
        }
        for (ViewComponentGroup c :
                groups) {
            ViewComponent res = c.findViewById(id);
            if (res != null) return res;
        }
        return null;
    }

    public final long addSubComponent(ViewComponent component) {
        mSubComponents.add(component);
        return component.onAddToParent(getComponentCallback(), this);
    }

    protected void drawChildrenLayer(GuiDrawingDelegate drawingDelegate, int mouseX, int mouseY, DrawLayer layer) {
        for (ViewComponent c :
                mSubComponents) {
            c.drawLayer(drawingDelegate, mouseX, mouseY, layer);
        }
    }

    protected boolean isDragMouseButton(int mouseButton) {
        return mouseButton == MOUSE_BUTTON_LEFT;
    }

    protected class ViewComponentGroupDragController extends DragControllerAdapter {
        private ViewComponent mmDraggedComponent;

        public ViewComponentGroupDragController() {
            super();
            mmDraggedComponent = null;
        }

        @Override
        public void onDragStart(int mouseX, int mouseY, int dPosX, int dPosY) {
            if (isDraggable()) {
                if (!isDraggingGroup()) {
                    for (ViewComponent c :
                            mSubComponents) {
                        if (c.isClickable() && c.getDragController() != null && c.liesWithinComponentRegion(mouseX, mouseY)) {
                            c.getDragController().onDragStart(mouseX, mouseY, dPosX, dPosY);
                            setDragging(c.getDragController().isDragging());
                            if (isDragging()) {
                                mmDraggedComponent = c;
                                return;
                            }
                        }
                    }
                } else {
                    setDragging(true);
                }
            }
        }

        @Override
        public void onDragUpdateDraw(int mouseX, int mouseY, int dToStartX, int dToSTartY, GuiDrawingDelegate drawingDelegate, DrawLayer layer) {
            for (ViewComponent c :
                    mSubComponents) {
                if (c == mmDraggedComponent && c.getDragController() != null) {
                    mmDraggedComponent.getDragController().onDragUpdateDraw(mouseX, mouseY, dToStartX, dToSTartY, drawingDelegate, layer);
                } else {
                    c.drawLayer(drawingDelegate, mouseX, mouseY, layer);
                }
            }

        }

        @Override
        public void onDragMove(int mouseX, int mouseY, int dToStartX, int dToStartY) {
            if (!isDraggingGroup()) {
                if (mmDraggedComponent != null && mmDraggedComponent.getDragController() != null) {
                    mmDraggedComponent.getDragController().onDragMove(mouseX, mouseY, dToStartX, dToStartY);
                }
            } else {
                super.onDragMove(mouseX, mouseY, dToStartX, dToStartY);
            }
        }

        @Override
        public void onDragFinished(int mouseX, int mouseY, int dPosX, int dPosY) {
            if (!isDraggingGroup()) {
                if (mmDraggedComponent != null && mmDraggedComponent.getDragController() != null) {
                    mmDraggedComponent.getDragController().onDragFinished(mouseX, mouseY, dPosX, dPosY);
                    mmDraggedComponent = null;
                }
            }
            setDragging(false);
        }

        @Override
        public boolean isDragMouseButton(int mouseButton) {
            if (isDraggable()) {
                for (ViewComponent c :
                        mSubComponents) {
                    if (c.getDragController() != null && c.getDragController().isDragMouseButton(mouseButton)) {
                        return true;
                    }
                }
                return isDraggingGroup() && isDragMouseButton(mouseButton);
            }
            return false;
        }

        @Override
        protected void handleSuggestedPosition(int evaluatedX, int evaluatedY) {
            setXPos(evaluatedX);
            setYPos(evaluatedY);
        }
    }
}
