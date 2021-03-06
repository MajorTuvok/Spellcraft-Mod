package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate.IResourceInfo;
import mt.mcmods.spellcraft.common.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class HorizontalScrollingList extends AbsScrollingList {
    private static final int MOUSE_SCROLL_REDUCTION = 240;
    private boolean negativeButtonHovered;
    private boolean positiveButtonHovered;
    private boolean sliderClicked;
    private boolean sliderHovered;
    /**
     * The left of the slider relative to the left of the scrollbar
     */
    private int sliderPos;
    /**
     * width of the slider
     */
    private int sliderSize;

    public HorizontalScrollingList(Minecraft client, int width, int height, int top, int left, int slotHeight) {
        super(ScrollingOrientation.VERTICAL, client, width, height, top, left, width - DEFAULT_SCROLLBAR_SIZE - 1, slotHeight);
        positiveButtonHovered = false;
        negativeButtonHovered = false;
        sliderHovered = false;
        sliderClicked = false;
        sliderPos = getButtonWidth();
        sliderSize = getSliderWidth();
    }

    @Override
    protected int getContentHeight() {
        return this.getSize() * this.getSlotHeight() + this.getHeaderSize();
    }

    @Override
    protected float getMaxScrollDistance() {
        return this.getContentHeight() - (getListHeight() - getBorder());
    }

    @Override
    public boolean isMouseOnSlot(int index) {
        int entryRight = getLeft() + getSlotWidth();
        int baseY = getTop() + getBorder() - (int) getScrollDistance();
        int slotTop = baseY + index * this.getSlotHeight() + this.getHeaderSize();
        return GuiUtil.isWithinBounds(getMouseX(), getMouseY(), getLeft(), entryRight, slotTop, slotTop + getSlotHeight());
    }

    /**
     * Called when the mouse is clicked.
     *
     * @param mouseX      Mouse X - Coordinate
     * @param mouseY      Mouse Y - Coordinate
     * @param mouseButton The pressed mouse Button. F.i. 0 is an left click.
     */
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovering()) {
            int listLength = this.getSize();
            int scrollBarRight = getRight();
            int scrollBarLeft = scrollBarRight - getScrollbarSize();
            int entryLeft = getLeft();
            int entryRight = entryLeft + getSlotWidth();
            int mouseListY = mouseY - getTop() - this.getHeaderSize() + (int) getScrollDistance() - getBorder();
            int slotIndex = mouseListY / getSlotHeight();

            if (mouseX >= entryLeft && mouseX <= entryRight && slotIndex >= 0 && mouseListY >= 0 && slotIndex < listLength) {
                if (this.elementClicked(slotIndex, slotIndex == getSelectedIndex() && System.currentTimeMillis() - this.getLastClickTime() < 250L)) {
                    setSelectedIndex(slotIndex);
                }
                updateLastClickTime();
            } else if (mouseX >= entryLeft && mouseX <= entryRight && mouseListY < 0) {
                this.clickHeader(mouseX - entryLeft, mouseY - getTop() + (int) getScrollDistance() - getBorder());
            }

            if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight) {//scrollbarParameters
                if (positiveButtonHovered) { //onPositiveButton
                    scrollUp();
                } else if (negativeButtonHovered) { //onNegativeButton
                    scrollDown();
                } else if (sliderHovered) {
                    onSliderClicked();
                } else {
                    this.setScrollFactor(-1);

                    setScrollDistance(getScrollDistance() + (mouseY - sliderPos - getTop() - getBorder()));
                    updateSliderPos();
                }
            } else {
                this.setScrollFactor(1);
            }

            this.setInitialMouseClickY(mouseY);
        } else {
            this.setInitialMouseClickY(-2);
        }
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around.
     *
     * @param mouseX             Mouse X - Coordinate
     * @param mouseY             Mouse Y - Coordinate
     * @param clickedMouseButton The pressed mouse Button. F.i. 0 is an left click.
     * @param timeSinceLastClick tim in ms since the Mouse button was pressed
     */
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (clickedMouseButton == 0 && sliderClicked) {
            onSliderMovedTo(mouseY);
        }
    }

    /**
     * Called when a mouse button is released.
     *
     * @param mouseX Mouse X - Coordinate
     * @param mouseY Mouse Y - Coordinate
     * @param state  Mouse state ?
     */
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (sliderClicked) {
            onSliderReleased();
        }
    }

    @Override
    protected void onPreDraw(int mouseX, int mouseY) {
        sliderSize = getSliderWidth();
    }

    @Override
    protected void performDrawAdditionalBackground(Tessellator tess) {
        if (this.getClient().world != null) {
            drawListGradientRect(getLeft(), getTop(), getRight(), getBottom(), 0xC0101010, 0xD0101010);
        } else // Draw dark dirt background
        {
            BufferBuilder worldr = tess.getBuffer();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            this.getClient().renderEngine.bindTexture(Gui.OPTIONS_BACKGROUND);
            final float scale = 32.0F;
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(getLeft(), getBottom(), 0.0D).tex(getLeft() / scale, (getBottom() + (int) getScrollDistance()) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(getRight(), getBottom(), 0.0D).tex(getRight() / scale, (getBottom() + (int) getScrollDistance()) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(getRight(), getTop(), 0.0D).tex(getRight() / scale, (getTop() + (int) getScrollDistance()) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(getLeft(), getTop(), 0.0D).tex(getLeft() / scale, (getTop() + (int) getScrollDistance()) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            tess.draw();
        }
    }

    @Override
    public void scrollDown() {
        float dScroll = (float) (getSlotHeight() * 2 / 3);
        this.setInitialMouseClickY(-2.0F);
        setScrollDistance(getScrollDistance() + dScroll);
    }

    @Override
    public void scrollUp() {
        float dScroll = (float) (getSlotHeight() * 2 / 3);
        this.setInitialMouseClickY(-2.0F);
        setScrollDistance(getScrollDistance() - dScroll);
    }

    @Override
    protected void performDraw(Tessellator tess) {
        int baseY = getTop() + getBorder() - (int) getScrollDistance();
        int listLength = this.getSize();
        int entryLeft = getLeft();
        int entryRight = entryLeft + getSlotWidth();

        performDrawHeader(tess, baseY, entryRight);

        performDrawSlots(tess, baseY, listLength, entryRight);

        performDrawScrollbar();
    }

    @Override
    protected void onHandleMouse() {
        int mouseX = getMouseX();
        int mouseY = getMouseY();
        positiveButtonHovered = false;
        negativeButtonHovered = false;
        sliderHovered = false;

        setHovering(GuiUtil.isWithinBounds(mouseX, mouseY, getLeft(), getRight(), getBottom(), getTop()));
        if (isHovering()) {
            //check Button and Slider Hover
            if (mouseX >= getRight() - getScrollbarSize() && mouseX <= getRight()) {
                if (mouseY >= getTop() && mouseY <= getTop() + getButtonWidth()) {
                    positiveButtonHovered = true;
                } else if (mouseY >= getTop() + sliderPos && mouseY <= getTop() + sliderPos + sliderSize) {
                    sliderHovered = true;
                } else if (mouseY >= getBottom() - getButtonWidth() && mouseY <= getBottom()) {
                    negativeButtonHovered = true;
                }
            }

            handleMouseWheelScroll();
        }
    }

    @Override
    protected void updateSliderPos() {
        int buttonHeight = getButtonWidth();
        int sliderEnd = getListHeight() - buttonHeight - sliderSize;
        sliderPos = buttonHeight + Math.round(getScrollDistance() / getMaxScrollDistance() * getSliderScrollWidth());
        sliderPos = MathHelper.clamp(sliderPos, buttonHeight, sliderEnd);
    }

    private int getButtonWidth() {
        return Math.max(
                Math.max(getButtonNegative().getImgXSize(), getButtonPositive().getImgXSize()),
                Math.max(getButtonNegativeHovered().getImgXSize(), getButtonPositiveHovered().getImgXSize()));
    }

    private int getSliderWidth() {
        return Math.max(
                getScrollbarSlider().getImgXSize(),
                getScrollbarSliderHovered().getImgXSize());
    }

    private float getSliderScrollWidth() {
        return getListWidth() - getButtonWidth() * 2 - sliderSize;
    }

    protected void performDrawHeader(Tessellator tess, int baseY, int entryRight) {
        if (this.hasHeader()) {
            this.drawHeader(getLeft(), entryRight, baseY, getHeaderSize(), tess);
        }
    }

    protected void performDrawSlots(Tessellator tess, int baseY, int listLength, int entryRight) {
        for (int slotIdx = 0; slotIdx < listLength; ++slotIdx) {
            int slotTop = baseY + slotIdx * getSlotHeight() + this.getHeaderSize();
            int slotBuffer = getSlotHeight() - getBorder();

            if (slotTop <= getBottom() && slotTop + slotBuffer >= getTop()) {
                this.drawSlot(slotIdx, getLeft(), entryRight, slotTop, slotTop + getSlotHeight(), slotBuffer, tess);
            }
        }
    }

    protected void performDrawScrollbar() {
        int buttonHeight = getButtonWidth();
        int scrollbarLeft = getRight() - getScrollbarSize() - 1;
        int scrollbarTop = getTop() + buttonHeight;
        int scrollbarHeight = Math.round(getSliderScrollWidth() + sliderSize);
        int halfHeight = scrollbarHeight / 2;
        //retrieve Resources
        IResourceInfo positiveRes = positiveButtonHovered ? getButtonPositiveHovered() : getButtonPositive();
        IResourceInfo negativeRes = negativeButtonHovered ? getButtonNegativeHovered() : getButtonNegative();
        IResourceInfo sliderRes = sliderHovered ? getScrollbarSliderHovered() : getScrollbarSlider();
        //draw positive Button
        getDelegate().bindResource(positiveRes);
        getDelegate().drawTexturedModalRect(scrollbarLeft, getTop(), positiveRes.getImgXStart(), positiveRes.getImgYStart(), positiveRes.getImgXSize(), positiveRes.getImgYSize());
        //draw ScrollbarBackground
        getDelegate().bindResource(getScrollbarBackground().getResource());
        getDelegate().drawTexturedModalRect(scrollbarLeft, scrollbarTop, getScrollbarBackground().getImgXStart(), getScrollbarBackground().getImgYStart(), getScrollbarBackground().getImgXSize(), halfHeight);
        getDelegate().drawTexturedModalRect(scrollbarLeft, scrollbarTop + halfHeight, getScrollbarBackground().getImgXStart(), getScrollbarBackground().getImgYStart() + getScrollbarBackground().getImgYSize() - halfHeight, getScrollbarBackground().getImgXSize(), halfHeight);
        //drawSlider
        getDelegate().bindResource(sliderRes.getResource());
        getDelegate().drawTexturedModalRect(scrollbarLeft, getTop() + sliderPos, sliderRes.getImgXStart(), sliderRes.getImgYStart(), sliderRes.getImgXSize(), sliderRes.getImgYSize());
        //draw negative Button
        getDelegate().bindResource(negativeRes.getResource());
        getDelegate().drawTexturedModalRect(scrollbarLeft, getBottom() - buttonHeight - 1, negativeRes.getImgXStart(), negativeRes.getImgYStart(), negativeRes.getImgXSize(), negativeRes.getImgYSize());
    }

    protected void onSliderClicked() {
        sliderClicked = true;
    }

    protected void onSliderMovedTo(int mouseX) {
        setScrollDistance(getScrollDistance() + (mouseX - sliderPos - getLeft()));
        updateSliderPos();
    }

    protected void onSliderReleased() {
        sliderClicked = false;
    }

    protected void handleMouseWheelScroll() {
        //checkScrolling
        int scroll = Mouse.getDWheel();
        if (allowMouseScroll() && scroll != 0) {
            setScrollDistance(getScrollDistance() + (-1 * scroll * getSlotWidth() / MOUSE_SCROLL_REDUCTION));
        }
    }
}
