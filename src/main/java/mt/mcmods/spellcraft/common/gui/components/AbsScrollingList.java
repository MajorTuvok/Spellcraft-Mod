package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate.IResourceInfo;
import mt.mcmods.spellcraft.common.gui.helper.GuiResource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbsScrollingList extends Gui {
    public enum ScrollingOrientation {
        HORIZONTAL {
            @Nonnull
            @Override
            public GuiResource getDefaultPositiveButton() {
                return GuiResource.GUI_BUTTON_TRIANGLE_RIGHT;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultPositiveButtonHovered() {
                return GuiResource.GUI_BUTTON_TRIANGLE_RIGHT_HOVERED;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultNegativeButton() {
                return GuiResource.GUI_BUTTON_TRIANGLE_LEFT;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultNegativeButtonHovered() {
                return GuiResource.GUI_BUTTON_TRIANGLE_LEFT_HOVERED;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultScrollbarBackground() {
                return GuiResource.GUI_SCROLLBAR_BACKGROUND_HORIZONTAL;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultScrollbarSlider() {
                return GuiResource.GUI_SCROLLBAR_SLIDER_HORIZONTAL;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultScrollbarSliderHovered() {
                return GuiResource.GUI_SCROLLBAR_SLIDER_HOVERED_HORIZONTAL;
            }
        },
        VERTICAL {
            @Nonnull
            @Override
            public GuiResource getDefaultPositiveButton() {
                return GuiResource.GUI_BUTTON_TRIANGLE_TOP;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultPositiveButtonHovered() {
                return GuiResource.GUI_BUTTON_TRIANGLE_TOP_HOVERED;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultNegativeButton() {
                return GuiResource.GUI_BUTTON_TRIANGLE_BOTTOM;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultNegativeButtonHovered() {
                return GuiResource.GUI_BUTTON_TRIANGLE_BOTTOM_HOVERED;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultScrollbarBackground() {
                return GuiResource.GUI_SCROLLBAR_BACKGROUND_VERTICAL;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultScrollbarSlider() {
                return GuiResource.GUI_SCROLLBAR_SLIDER_VERTICAL;
            }

            @Nonnull
            @Override
            public GuiResource getDefaultScrollbarSliderHovered() {
                return GuiResource.GUI_SCROLLBAR_SLIDER_HOVERED_VERTICAL;
            }
        };

        @Nonnull
        public abstract GuiResource getDefaultPositiveButton();

        @Nonnull
        public abstract GuiResource getDefaultPositiveButtonHovered();

        @Nonnull
        public abstract GuiResource getDefaultNegativeButton();

        @Nonnull
        public abstract GuiResource getDefaultNegativeButtonHovered();

        @Nonnull
        public abstract GuiResource getDefaultScrollbarBackground();

        @Nonnull
        public abstract GuiResource getDefaultScrollbarSlider();

        @Nonnull
        public abstract GuiResource getDefaultScrollbarSliderHovered();
    }

    protected static final int DEFAULT_SCROLLBAR_SIZE = 6;
    private final int bottom;
    private final Minecraft client;
    private final int left;
    private final int listHeight;
    private final int listWidth;
    private final ScrollingOrientation mOrientation;
    private final int right;
    private final int top;
    private boolean allowMouseScroll;
    private IResourceInfo buttonNegative;
    private IResourceInfo buttonNegativeHovered;
    private IResourceInfo buttonPositive;
    private IResourceInfo buttonPositiveHovered;
    private int lastMouseWheel;
    private boolean captureMouse;
    private float initialMouseClickY;
    private long lastClickTime;
    private int border;
    private boolean mDrawBackground;
    private boolean hasHeader;
    private int headerSize;
    private IListController mListController;
    private boolean isHovering;
    private List<IOnScrollListener> mOnScrollListeners;
    private ISlotAdapter mSlotAdapter;
    private int mouseX;
    private int mouseY;
    private int padding;
    private IResourceInfo scrollbarBackground;
    private int scrollbarSize;
    private float scrollDistance;
    private float scrollFactor;
    private IResourceInfo scrollbarSlider;
    private IResourceInfo scrollbarSliderHovered;
    private int selectedIndex;
    private int slotHeight;
    private int slotWidth;
    private int suggestedButtonHeight;
    private int suggestedButtonWidth;

    public AbsScrollingList(ScrollingOrientation orientation, Minecraft client, int width, int height, int top, int left, int slotWidth, int slotHeight) {
        this(orientation, client, width, height, top, left, slotWidth, slotHeight, true);
    }

    public AbsScrollingList(ScrollingOrientation orientation, Minecraft client, int width, int height, int top, int left, int slotWidth, int slotHeight, boolean captureMouse) {
        this.client = client;
        this.top = top;
        this.bottom = top + height;
        this.left = left;
        this.right = width + this.left;
        this.listWidth = width;
        this.listHeight = height;
        this.slotHeight = slotHeight;
        this.captureMouse = captureMouse;
        this.isHovering = false;
        this.border = 4;
        this.selectedIndex = -1;
        this.initialMouseClickY = -2.0F;
        this.lastClickTime = 0L;
        this.mSlotAdapter = null;
        this.mListController = null;
        this.mDrawBackground = false;
        this.padding = 0;
        this.allowMouseScroll = true;
        this.mOrientation = orientation;
        this.suggestedButtonWidth = scrollbarSize;
        this.suggestedButtonHeight = 10;
        this.mOnScrollListeners = new ArrayList<>();
        this.scrollbarBackground = mOrientation.getDefaultScrollbarBackground();
        this.scrollbarSlider = mOrientation.getDefaultScrollbarSlider();
        this.scrollbarSliderHovered = mOrientation.getDefaultScrollbarSliderHovered();
        this.buttonPositive = mOrientation.getDefaultPositiveButton();
        this.buttonNegative = mOrientation.getDefaultNegativeButton();
        this.buttonPositiveHovered = mOrientation.getDefaultPositiveButtonHovered();
        this.buttonNegativeHovered = mOrientation.getDefaultNegativeButtonHovered();
        this.scrollbarSize = Math.max(width - slotWidth, getScrollbarBackground().getImgXSize());
        this.slotWidth = Math.min(slotWidth, width - scrollbarSize);
    }


    protected static void drawListGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        GuiUtils.drawGradientRect(0, left, top, right, bottom, color1, color2);
    }

    public void setAllowMouseScroll(boolean allowMouseScroll) {
        this.allowMouseScroll = allowMouseScroll;
    }

    public void setDrawBackground(boolean drawBackground) {
        mDrawBackground = drawBackground;
    }

    protected void setCaptureMouse(boolean captureMouse) {
        this.captureMouse = captureMouse;
    }

    protected void setHovering(boolean hovering) {
        isHovering = hovering;
    }

    public int getSuggestedButtonWidth() {
        return suggestedButtonWidth;
    }

    public void setSuggestedButtonWidth(int suggestedButtonWidth) {
        this.suggestedButtonWidth = suggestedButtonWidth;
    }

    public int getSuggestedButtonHeight() {
        return suggestedButtonHeight;
    }

    public void setSuggestedButtonHeight(int suggestedButtonHeight) {
        this.suggestedButtonHeight = suggestedButtonHeight;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getScrollbarSize() {
        return scrollbarSize;
    }

    public void setScrollbarSize(int scrollbarSize) {
        this.scrollbarSize = scrollbarSize;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getListWidth() {
        return listWidth;
    }

    public int getListHeight() {
        return listHeight;
    }

    public int getSlotWidth() {
        return slotWidth;
    }

    public void setSlotWidth(int slotWidth) {
        this.slotWidth = slotWidth;
    }

    public int getSlotHeight() {
        return slotHeight;
    }

    public void setSlotHeight(int slotHeight) {
        this.slotHeight = slotHeight;
    }

    public Minecraft getClient() {
        return client;
    }

    public int getMouseX() {
        return mouseX;
    }

    protected void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    protected void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public float getInitialMouseClickY() {
        return initialMouseClickY;
    }

    protected void setInitialMouseClickY(float initialMouseClickY) {
        this.initialMouseClickY = initialMouseClickY;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public IListController getListController() {
        return mListController;
    }

    public ISlotAdapter getSlotAdapter() {
        return mSlotAdapter;
    }

    public boolean isHovering() {
        return isHovering;
    }

    public float getScrollDistance() {
        return scrollDistance;
    }

    public long getLastClickTime() {
        return lastClickTime;
    }

    protected void setScrollDistance(float scrollDistance) {
        setScrollDistance(scrollDistance, true);
    }

    public IResourceInfo getScrollbarBackground() {
        return scrollbarBackground;
    }

    public void setScrollbarBackground(IResourceInfo scrollbarBackground) {
        this.scrollbarBackground = scrollbarBackground;
    }

    public IResourceInfo getScrollbarSlider() {
        return scrollbarSlider;
    }

    public void setScrollbarSlider(IResourceInfo scrollbarSlider) {
        this.scrollbarSlider = scrollbarSlider;
    }

    public float getScrollFactor() {
        return scrollFactor;
    }

    protected void setScrollFactor(float scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    public IResourceInfo getScrollbarSliderHovered() {
        return scrollbarSliderHovered;
    }

    public void setScrollbarSliderHovered(IResourceInfo scrollbarSliderHovered) {
        this.scrollbarSliderHovered = scrollbarSliderHovered;
    }

    protected int getSize() {
        return getSlotAdapter().getSize();
    }

    public IResourceInfo getButtonPositive() {
        return buttonPositive;
    }

    public void setButtonPositive(IResourceInfo buttonPositive) {
        this.buttonPositive = buttonPositive;
    }

    public IResourceInfo getButtonNegative() {
        return buttonNegative;
    }

    public void setButtonNegative(IResourceInfo buttonNegative) {
        this.buttonNegative = buttonNegative;
    }

    public IResourceInfo getButtonPositiveHovered() {
        return buttonPositiveHovered;
    }

    public void setButtonPositiveHovered(IResourceInfo buttonPositiveHovered) {
        this.buttonPositiveHovered = buttonPositiveHovered;
    }

    public IResourceInfo getButtonNegativeHovered() {
        return buttonNegativeHovered;
    }

    public void setButtonNegativeHovered(IResourceInfo buttonNegativeHovered) {
        this.buttonNegativeHovered = buttonNegativeHovered;
    }

    public ScrollingOrientation getOrientation() {
        return mOrientation;
    }

    protected int getScreenWidth() {
        return getClient().displayWidth;
    }

    protected int getScreenHeight() {
        return getClient().displayHeight;
    }

    protected abstract int getContentHeight();

    protected abstract float getMaxScrollDistance();

    public void addOnScrollListener(IOnScrollListener listener) {
        mOnScrollListeners.add(listener);
    }

    public boolean removeOnScrollListener(IOnScrollListener listener) {
        return mOnScrollListeners.remove(listener);
    }

    public boolean allowMouseScroll() {
        return allowMouseScroll;
    }

    public boolean drawAdditionalBackground() {
        return mDrawBackground;
    }

    public void updateLastClickTime() {
        this.lastClickTime = System.currentTimeMillis();
    }

    public boolean hasHeader() {
        return hasHeader && (getListController() == null || getListController().hasHeader());
    }

    public boolean isSelected(int index) {
        return index == getSelectedIndex();
    }

    public abstract boolean isMouseOnSlot(int index);

    public void init(@Nonnull ISlotAdapter slotAdapter, IListController controller) {
        mSlotAdapter = Objects.requireNonNull(slotAdapter);
        mListController = Objects.requireNonNull(controller);
    }

    public boolean captureMouse() {
        return captureMouse;
    }

    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        onPreDraw(mouseX, mouseY);

        setMouseX(mouseX);
        setMouseY(mouseY);
        ScaledResolution res = new ScaledResolution(getClient());

        onHandleMouse();

        this.applyScrollLimits();
        updateSliderPos();

        Tessellator tess = Tessellator.getInstance();

        double scaleW = getClient().displayWidth / res.getScaledWidth_double();
        double scaleH = getClient().displayHeight / res.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (getLeft() * scaleW), (int) (getClient().displayHeight - (getBottom() * scaleH)),
                (int) (getListWidth() * scaleW), (int) (getListHeight() * scaleH));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.drawBackground();

        if (drawAdditionalBackground()) {
            performDrawAdditionalBackground(tess);
        }

        performDraw(tess);

        GlStateManager.disableDepth();

        this.drawForeground();

        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void onMouseClick(int mouseX, int mouseY, int mouseButton) {

    }

    protected void setScrollDistance(float scrollDistance, boolean applyLimits) {
        float oldPos = this.scrollDistance;
        this.scrollDistance = scrollDistance;
        if (applyLimits) this.applyScrollLimits();
        notifyScrollPosChanged(oldPos);
    }

    protected abstract void onPreDraw(int mouseX, int mouseY);

    protected abstract void performDrawAdditionalBackground(Tessellator tess);

    public abstract void scrollDown();

    public abstract void scrollUp();

    protected abstract void performDraw(Tessellator tess);

    protected abstract void onHandleMouse();

    protected void setHeaderInfo(boolean hasHeader, int headerHeight) {
        this.hasHeader = hasHeader;
        this.headerSize = headerHeight;
        if (!hasHeader) this.headerSize = 0;
    }

    protected abstract void updateSliderPos();

    protected boolean elementClicked(int index, boolean doubleClick) {
        if (getSlotAdapter() != null) {
            return getSlotAdapter().onSlotClicked(index, doubleClick);
        }
        return false;
    }

    protected boolean canHighlightIndex(int index) {
        return getSlotAdapter() != null && getSlotAdapter().canHighlightIndex(index);
    }

    protected void drawBackground() {
        if (getListController() != null) getListController().drawBackground(getMouseX(), getMouseY());
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawSlot(int slotIdx, int left, int right, int top, int bottom, int slotBuffer, Tessellator tess) {
        ISlotAdapter slotAdapter = getSlotAdapter();
        if (slotAdapter != null) {
            slotAdapter.drawSlot(slotIdx, left, right, top, bottom, isMouseOnSlot(slotIdx), isSelected(slotIdx), slotBuffer, tess);
        }
    }

    protected void clickHeader(int x, int y) {
        if (getSlotAdapter() != null) getSlotAdapter().onHeaderClicked(x, y);
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawHeader(int left, int right, int top, int bottom, Tessellator tess) {
        if (getSlotAdapter() != null) {
            getSlotAdapter().drawHeader(tess, left, right, top, bottom, getMouseX(), getMouseY());
        }
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawForeground() {
        if (getListController() != null) getListController().drawForeground(getMouseX(), getMouseY());
    }

    protected void applyScrollLimits() {
        int listSize = Math.round(getMaxScrollDistance());

        if (listSize < 0) {
            listSize /= 2;
        }

        if (getScrollDistance() > (float) listSize) {
            setScrollDistance((float) listSize, false);
        }

        if (getScrollDistance() < 0.0F) {
            setScrollDistance(0, false);
        }
    }

    protected void notifyScrollPosChanged(float oldValue) {
        for (IOnScrollListener listener :
                mOnScrollListeners) {
            listener.onNewScrollValue(oldValue, getScrollDistance());
        }
    }

    public interface ISlotAdapter {
        public int getSize();

        public void drawSlot(int index, int left, int right, int top, int bottom, boolean hoveredOver, boolean selected, int slotBuffer, Tessellator tess);

        public void drawHeader(Tessellator tessellator, int left, int right, int top, int bottom, int mouseX, int mouseY);

        /**
         * Called when a slot was clicked
         *
         * @param index       The clicked index
         * @param doubleClick whether this was a double click
         * @return If this Index should now be selected
         */
        public default boolean onSlotClicked(int index, boolean doubleClick) {
            return true;
        }

        public default void onHeaderClicked(int x, int y) {

        }

        public default boolean canHighlightIndex(int index) {
            return true;
        }
    }

    public interface IListController {

        public default boolean hasHeader() {
            return true;
        }

        public void drawBackground(int mouseX, int mouseY);

        public default void drawForeground(int mouseX, int mouseY) {

        }
    }

    public interface IOnScrollListener {
        public void onNewScrollValue(float oldScrollPos, float newScrollPos);
    }
}
