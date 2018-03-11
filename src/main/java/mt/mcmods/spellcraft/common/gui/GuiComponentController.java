package mt.mcmods.spellcraft.common.gui;

import mt.mcmods.spellcraft.common.gui.components.*;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;


final class GuiComponentController implements ILoggable, Constants {
    private GuiDrawingDelegate mDrawingDelegate;
    private boolean mDragAccepted;
    private BaseGui mGui;
    private int mLastMouseX;
    private int mLastMouseY;
    private long mMaxId;
    private int mMouseDx;
    private int mMouseDy;
    private float mLastPartialTicks;
    private int mStartMouseX;
    private ViewComponentGroup mRoot;
    private int mStartMouseY;

    GuiComponentController(@Nonnull BaseGui gui, @Nonnull GuiDrawingDelegate drawingDelegate) {
        mGui = gui;
        mDrawingDelegate = drawingDelegate;
        ComponentCallback componentCallback = new ComponentCallback();
        mRoot = new ViewComponentGroup(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        mMaxId = ROOT_ID;
        mRoot.onAddToParent(componentCallback, null);
        mLastMouseX = -1;
        mLastMouseY = -1;
        mLastPartialTicks = 0f;
        mDragAccepted = true;
    }

    ViewComponentGroup getRoot() {
        return mRoot;
    }

    void drawLayer(int mouseX, int mouseY, float partialTicks, DrawLayer layer) {
        if (partialTicks >= 0) {
            mLastPartialTicks = partialTicks;
        }
        if (mLastMouseX < 0 || mLastMouseY < 0) {
            updateLastMousePos(mouseX, mouseY);
        }
        if (layer == DrawLayer.LAST) {
            updateLastMousePos(mouseX, mouseY);
        } else if (layer == DrawLayer.FIRST) {
            updateMouseMovement(mouseX, mouseY);
            if (mRoot.isFocusable()) {
                if (mRoot.liesWithinComponentRegion(mouseX, mouseY)) {
                    if (!mRoot.isFocused()) {
                        mRoot.handleGainFocus(mouseX, mouseY);
                    } else {
                        mRoot.handleFocus(mouseX, mouseY);
                    }
                } else {
                    mRoot.handleLoseFocus(mouseX, mouseY);
                }
            }
        }
        if (mRoot.getDragController() != null && mRoot.getDragController().isDragging()) {
            mRoot.getDragController().onDragUpdateDraw(mouseX, mouseY, mMouseDx, mMouseDy, mDrawingDelegate, layer);
        } else {
            mRoot.drawLayer(mDrawingDelegate, mouseX, mouseY, layer);
        }
    }

    long onAdd(ViewComponent component) {
        return mRoot.addSubComponent(component);
    }

    void onUpdate() {
        mRoot.handleUpdate();
    }

    void onResize() {
        mRoot.setXSize(mGui.mc.displayWidth);
        mRoot.setYSize(mGui.mc.displayHeight);
        mRoot.handleResize();
    }

    void onClick(int mouseX, int mouseY, int mouseButton) {
        mRoot.handleClick(mouseX, mouseY, mouseButton);
        mDragAccepted = true;
    }

    void onClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (mDragAccepted) {
            updateMouseMovement(mouseX, mouseY);
            IDragController dragController = mRoot.getDragController();
            if (dragController != null && dragController.isDragMouseButton(clickedMouseButton)) {
                if (dragController.isDragging()) {
                    dragController.onDragMove(mouseX, mouseY, mMouseDx, mMouseDy);
                } else {
                    dragController.onDragStart(mouseX, mouseY, mMouseDx, mMouseDy);
                    mDragAccepted = dragController.isDragging();
                    updateStartMousePos(mouseX, mouseY);
                }
            }
        }
    }

    void onClickReleased(int mouseX, int mouseY, int state) {
        IDragController dragController = mRoot.getDragController();
        if (dragController != null && dragController.isDragging()) {
            dragController.onDragFinished(mouseX, mouseY, mMouseDx, mMouseDx);
        }
        mRoot.handleClickReleased(mouseX, mouseY, state);
    }

    void onKeyTyped(char typedChar, int keycode) {
        mRoot.handleKeyTyped(typedChar, keycode);
    }

    @Nullable
    ViewComponent findViewById(long id) {
        return mRoot.findViewById(id);
    }

    private void updateMouseMovement(int mouseX, int mouseY) {
        if (mDragAccepted) {
            mMouseDx = mouseX - mStartMouseX;
        } else {
            mMouseDx = -1;
        }
        if (mDragAccepted) {
            mMouseDy = mouseY - mStartMouseY;
        } else {
            mMouseDy = -1;
        }
    }

    private void updateLastMousePos(int mouseX, int mouseY) {
        mLastMouseX = mouseX;
        mLastMouseY = mouseY;
    }

    private void updateStartMousePos(int mouseX, int mouseY) {
        if (mDragAccepted) {
            mStartMouseX = mouseX;
            mStartMouseY = mouseY;
        } else {
            mStartMouseX = -1;
            mStartMouseY = -1;
        }
    }

    private final class ComponentCallback implements IComponentCallback {
        @Override
        public int getGuiXSize() {
            return mGui.getXSize();
        }

        @Override
        public int getGuiYSize() {
            return mGui.getYSize();
        }

        @Override
        public int getGuiTop() {
            return mGui.getGuiTop();
        }

        @Override
        public int getGuiLeft() {
            return mGui.getGuiLeft();
        }

        @Override
        public float getLastPartialTicks() {
            return mLastPartialTicks;
        }

        @Override
        public int getLastMouseX() {
            return mLastMouseX;
        }

        @Override
        public int getLastMouseY() {
            return mLastMouseY;
        }

        @Override
        public int getMouseDx() {
            return mMouseDx;
        }

        @Override
        public int getMouseDy() {
            return mMouseDy;
        }

        @Nonnull
        @Override
        public Comparator<? extends ViewComponent> getComponentPriorityComparator() {
            return ComponentPriorityComparator.INSTANCE;
        }

        @Override
        public long requestNewId() {
            return mMaxId++;
        }

        @Override
        public @Nullable
        ViewComponent findViewById(long id) {
            return GuiComponentController.this.findViewById(id);
        }

        @Override
        public @Nonnull
        BaseGui getGui() {
            return mGui;
        }
    }
}
