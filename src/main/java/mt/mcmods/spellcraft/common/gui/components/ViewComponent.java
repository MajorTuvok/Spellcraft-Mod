package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.IComponentCallback;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ViewComponent implements Constants {
    private boolean mClickable;
    private IComponentCallback mComponentCallback;
    private boolean mFocusable;
    private boolean mFocused;
    private long mId;
    private List<IOnClickListener> mOnClickListeners;
    private ViewComponentGroup mParent;
    private int mPriority;
    private int xPos;
    private int xSize;
    private int yPos;
    private int ySize;

    public ViewComponent(int xPos, int yPos, int xSize, int ySize) {
        this.mFocused = false;
        this.mClickable = false;
        this.mFocusable = false;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xSize = xSize;
        this.ySize = ySize;
        this.mPriority = 0;
        this.mId = 0L;
        this.mComponentCallback = null;
        this.mParent = null;
        this.mOnClickListeners = new LinkedList<>();
    }

    protected static boolean isBetween(int a, int a0, int a1) {
        return a >= a0 && a <= a1;
    }

    protected static boolean isPosOnComponent(ViewComponent c, int x, int y) {
        return isBetween(x, c.getXPos(), c.getXPos() + c.getXSize()) && isBetween(y, c.getYPos(), c.getYPos() + c.getYSize());
    }

    public IComponentCallback getComponentCallback() {
        return mComponentCallback;
    }

    public @Nullable
    ViewComponentGroup getParent() {
        return mParent;
    }

    public int getPriority() {
        return mPriority;
    }

    public ViewComponent setPriority(int priority) {
        mPriority = priority;
        return this;
    }

    public @Nullable
    IDragController getDragController() {
        return null;
    }

    public boolean isFocused() {
        return mFocused;
    }

    public boolean isFocusable() {
        return mFocusable;
    }

    public ViewComponent setFocusable(boolean focusable) {
        mFocusable = focusable;
        return this;
    }

    public boolean isClickable() {
        return mClickable;
    }

    public ViewComponent setClickable(boolean clickable) {
        mClickable = clickable;
        return this;
    }

    public long getId() {
        return mId;
    }

    public int getXPos() {
        return xPos;
    }

    public ViewComponent setXPos(int xPos) {
        this.xPos = xPos;
        return this;
    }

    public int getYPos() {
        return yPos;
    }

    public ViewComponent setYPos(int yPos) {
        this.yPos = yPos;
        return this;
    }

    public int getXSize() {
        return xSize;
    }

    public ViewComponent setXSize(int xSize) {
        if (xSize < 0) {
            throw new RuntimeException("Size may never be negative! You cannot set an xSize of " + xSize + "!");
        }
        this.xSize = xSize;
        return this;
    }

    public int getYSize() {
        return ySize;
    }

    public ViewComponent setYSize(int ySize) {
        if (ySize < 0) {
            throw new RuntimeException("Size may never be negative! You cannot set an ySize of " + ySize + "!");
        }
        this.ySize = ySize;
        return this;
    }

    public <T extends IOnClickListener> T addOnClickListener(T listener) {
        mOnClickListeners.add(listener);
        return listener;
    }

    public <T extends IOnClickListener> T removeOnClickListener(T listener) {
        mOnClickListeners.remove(listener);
        return listener;
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    public boolean liesWithinComponentRegion(int x, int y) {
        return isPosOnComponent(this, x, y);
    }

    public final long onAddToParent(@Nonnull IComponentCallback componentCallback, @Nullable ViewComponentGroup parent) {
        mComponentCallback = componentCallback;
        mParent = parent;
        mId = componentCallback.requestNewId();
        onAdd();
        return mId;
    }

    public final void handleUpdate() {
        onUpdate();
    }

    public final void handleResize() {
        onResize();
    }

    public final void handleGainFocus(int mouseX, int mouseY) {
        this.mFocused = true;
        onGainFocus(mouseX, mouseY);
    }

    public final void handleFocus(int mouseX, int mouseY) {
        onFocus(mouseX, mouseY);
    }

    public final void handleLoseFocus(int mouseX, int mouseY) {
        this.mFocused = false;
        onLoseFocus(mouseX, mouseY);
    }

    public final void handleClick(int mouseX, int mouseY, int mouseButton) {
        onClick(mouseX, mouseY, mouseButton);
    }

    public final void handleClickReleased(int mouseX, int mouseY, int state) {
        onClickReleased(mouseX, mouseY, state);
    }

    public final void handleKeyTyped(char typedChar, int keycode) {
        onKeyTyped(typedChar, keycode);
    }

    public void drawLayer(GuiDrawingDelegate drawingDelegate, int mouseX, int mouseY, DrawLayer layer) {
        normalizeGlState(layer);
        layer.drawComponentLayer(drawingDelegate, this, mouseX, mouseY);
        resetGlState(layer);
    }

    public void playClickSound(SoundHandler soundHandler, float suggestedPitch) {
        soundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, suggestedPitch));
    }

    @Override
    public String toString() {
        return "ViewComponent{" +
                "xPos=" + xPos +
                ", xSize=" + xSize +
                ", yPos=" + yPos +
                ", ySize=" + ySize +
                ", id=" + mId +
                ", priority=" + mPriority +
                ", focused=" + mFocused +
                '}';
    }

    protected void normalizeGlState(DrawLayer layer) {
        layer.normalizeGLState(getComponentCallback().getGuiXSize(), getComponentCallback().getGuiYSize());
    }

    protected void onAdd() {

    }

    protected void onUpdate() {

    }

    protected void onResize() {

    }

    protected void onGainFocus(int mouseX, int mouseY) {

    }

    protected void onFocus(int mouseX, int mouseY) {

    }

    protected void onLoseFocus(int mouseX, int mouseY) {

    }

    protected void onClick(int mouseX, int mouseY, int mouseButton) {

    }

    protected void onClickReleased(int mouseX, int mouseY, int state) {

    }

    protected void onKeyTyped(char typedChar, int keycode) {

    }

    protected void drawBackgroundLayer(GuiDrawingDelegate drawingDelegate, int mouseX, int mouseY) {

    }

    protected void drawForegroundLayer(GuiDrawingDelegate drawingDelegate, int mouseX, int mouseY) {

    }

    protected void drawFirstLayer(GuiDrawingDelegate drawingDelegate, int mouseX, int mouseY) {

    }

    protected void drawLastLayer(GuiDrawingDelegate drawingDelegate, int mouseX, int mouseY) {

    }

    protected void resetGlState(DrawLayer layer) {
        layer.resetGLState();
    }
}
