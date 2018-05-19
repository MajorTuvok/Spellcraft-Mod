package mt.mcmods.spellcraft.common.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class AbsScrollingList {
    protected final int bottom;
    protected final int left;
    protected final int listHeight;
    protected final int listWidth;
    protected final int right;
    protected final int screenHeight;
    protected final int screenWidth;
    protected final int slotSize;
    protected final int top;
    private final Minecraft client;
    protected boolean captureMouse = true;
    protected int mouseX;
    protected int mouseY;
    protected int selectedIndex = -1;
    private int border;
    private boolean hasHeader;
    private int headerSize;
    private boolean highlightSelected = false;
    private float initialMouseClickY = -2.0F;
    private boolean isHovering;
    private long lastClickTime = 0L;
    private ListAdapter mListAdapter;
    private SlotAdapter mSlotAdapter;
    private float scrollDistance;
    private int scrollDownActionId;
    private float scrollFactor;
    private int scrollUpActionId;


    public AbsScrollingList(Minecraft client, int width, int height, int top, int left, int entrySize) {
        this(client, width, height, top, left, entrySize, client.displayWidth, client.displayHeight, true);
    }

    public AbsScrollingList(Minecraft client, int width, int height, int top, int left, int entrySize, int screenWidth, int screenHeight, boolean captureMouse) {
        this.client = client;
        this.listWidth = width;
        this.listHeight = height;
        this.top = top;
        this.bottom = top + height;
        this.slotSize = entrySize;
        this.left = left;
        this.right = width + this.left;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.captureMouse = true;
        this.isHovering = false;
        this.border = 4;
        mSlotAdapter = null;
        mListAdapter = null;
    }

    protected static void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        GuiUtils.drawGradientRect(0, left, top, right, bottom, color1, color2);
    }

    public void setCaptureMouse(boolean captureMouse) {
        this.captureMouse = captureMouse;
    }

    public void setHighlightSelected(boolean highlightSelected) {
        this.highlightSelected = highlightSelected;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public Minecraft getClient() {
        return client;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public boolean isHovering() {
        return isHovering;
    }

    public void setHovering(boolean hovering) {
        isHovering = hovering;
    }

    public SlotAdapter getSlotAdapter() {
        return mSlotAdapter;
    }

    public ListAdapter getListAdapter() {
        return mListAdapter;
    }

    public long getLastClickTime() {
        return lastClickTime;
    }

    public float getScrollFactor() {
        return scrollFactor;
    }

    public void setScrollFactor(float scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    public float getInitialMouseClickY() {
        return initialMouseClickY;
    }

    public void setInitialMouseClickY(float initialMouseClickY) {
        this.initialMouseClickY = initialMouseClickY;
    }

    protected float getScrollDistance() {
        return scrollDistance;
    }

    protected void setScrollDistance(float scrollDistance) {
        this.scrollDistance = scrollDistance;
    }

    protected int getSize() {
        return getSlotAdapter().getSize();
    }

    protected int getContentHeight() {
        return this.getSize() * this.slotSize + this.headerSize;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public boolean highlightSelected() {
        return highlightSelected;
    }

    public void updateLastClickTime() {
        this.lastClickTime = System.currentTimeMillis();
    }

    public abstract boolean isMouseOnSlot(int index);

    public void init(@Nonnull SlotAdapter slotAdapter, @Nonnull ListAdapter listAdapter) {
        mSlotAdapter = Objects.requireNonNull(slotAdapter);
        mListAdapter = Objects.requireNonNull(listAdapter);
        scrollUpActionId = mListAdapter.getUpButtonId();
        scrollDownActionId = mListAdapter.getDownButtonId();
    }

    public boolean captureMouse() {
        return captureMouse;
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == this.scrollUpActionId) {
                scrollUp();
            } else if (button.id == this.scrollDownActionId) {
                scrollDown();
            }
        }
    }

    public void handleMouseInput(int mouseX, int mouseY) {
        boolean isHovering = mouseX >= this.left && mouseX <= this.left + this.listWidth &&
                mouseY >= this.top && mouseY <= this.bottom;
        if (!isHovering) {
            return;
        }

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            this.scrollDistance += (float) ((-1 * scroll / 120.0F) * this.slotSize / 2);
        }
    }

    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

    public abstract void scrollDown();

    public abstract void scrollUp();

    protected void setHeaderInfo(boolean hasHeader, int headerHeight) {
        this.hasHeader = hasHeader;
        this.headerSize = headerHeight;
        if (!hasHeader) this.headerSize = 0;
    }

    protected void elementClicked(int index, boolean doubleClick) {
        if (getSlotAdapter() != null) getSlotAdapter().onSlotClicked(index, doubleClick);
    }

    /**
     * better name would be canHighlightIndex
     */
    protected boolean isSelected(int index) {
        return getSlotAdapter() != null && getSlotAdapter().canHighlightIndex(index);
    }

    protected void drawBackground() {
        if (getListAdapter() != null) getListAdapter().drawBackground(mouseX, mouseY);
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        SlotAdapter slotAdapter = getSlotAdapter();
        if (slotAdapter != null) {
            slotAdapter.drawSlot(slotIdx, entryRight, slotTop, false, slotBuffer, tess);
        }
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawHeader(int entryRight, int y, Tessellator tess) {
        if (getSlotAdapter() != null) getSlotAdapter().drawHeader(tess, entryRight, y, mouseX, mouseY);
    }

    protected void clickHeader(int x, int y) {
        if (getSlotAdapter() != null) getSlotAdapter().onHeaderClicked(x, y);
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawScreen(int mouseX, int mouseY) {
        if (getListAdapter() != null) getListAdapter().drawForeground(mouseX, mouseY);
    }


    public interface SlotAdapter {
        public int getSize();

        public void drawSlot(int index, int right, int top, boolean hoveredOver, int slotBuffer, Tessellator tess);

        public void drawHeader(Tessellator tessellator, int right, int top, int mousex, int mouseY);

        public void onSlotClicked(int index, boolean doubleClick);

        public void onHeaderClicked(int x, int y);

        public default boolean canHighlightIndex(int index) {
            return true;
        }
    }

    public interface ListAdapter {
        public int getUpButtonId();

        public int getDownButtonId();

        public void drawBackground(int mouseX, int mouseY);

        public void drawForeground(int mouseX, int mouseY);
    }
}
