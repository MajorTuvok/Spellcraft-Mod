package mt.mcmods.spellcraft.common.gui;


import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.gui.helper.GuiMeasurements;
import mt.mcmods.spellcraft.common.gui.helper.SlotDrawingDelegate;
import mt.mcmods.spellcraft.common.interfaces.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BaseGui extends GuiContainer implements ILoggable, IGuiRenderProvider, IDelegateProvider<GuiDrawingDelegate> {
    private SlotDrawingDelegate mDelegate;
    private ScaledResolution mScaledResolution;
    private List<IGuiButtonListener> mGuiButtonListeners;
    private List<IGuiDrawScreenListener> mGuiDrawables;
    private List<IGuiInitialisationListener> mGuiInitialisationListeners;
    private List<IGuiKeyboardListener> mGuiKeyboardListeners;
    private List<IGuiMouseListener> mGuiMouseListeners;

    public BaseGui(BaseGuiContainer inventorySlotsIn, int xSize, int ySize) {
        super(inventorySlotsIn);
        this.xSize = xSize;
        this.ySize = ySize;
        mDelegate = new SlotDrawingDelegate(inventorySlotsIn.getPlayerInventory(), this, inventorySlotsIn);
        mScaledResolution = new ScaledResolution(getMc());
        mGuiDrawables = new ArrayList<>();
        mGuiMouseListeners = new ArrayList<>();
        mGuiButtonListeners = new ArrayList<>();
        mGuiKeyboardListeners = new ArrayList<>();
        mGuiInitialisationListeners = new ArrayList<>();
        createInventoryViews(inventorySlotsIn);
    }

    @Override
    @Nonnull
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    @Override
    @Nonnull
    public RenderItem getRenderItem() {
        return itemRender;
    }

    @Override
    @Nonnull
    public Minecraft getMc() {
        return mc == null ? Minecraft.getMinecraft() : mc;
    }

    public TileEntity getTileEntity() {
        return ((BaseGuiContainer) inventorySlots).getTileEntity();
    }

    @Override
    public SlotDrawingDelegate getDelegate() {
        return mDelegate;
    }

    public GuiMeasurements getGuiMeasurements() {
        return new GuiMeasurements(xSize, ySize, guiLeft, guiTop);
    }

    protected float getScaleFactor() {
        return getScaledResolution().getScaleFactor();
    }

    protected ScaledResolution getScaledResolution() {
        return mScaledResolution;
    }

    @Override
    public void drawRectangle(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }

    @Override
    public void drawHorizontalLine(int startX, int endX, int y, int color) {
        super.drawHorizontalLine(startX, endX, y, color);
    }

    @Override
    public void drawVerticalLine(int x, int startY, int endY, int color) {
        super.drawVerticalLine(x, startY, endY, color);
    }

    @Override
    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    @Override
    public void drawCenteredString(FontRenderer fontRendererIn, @Nonnull String text, int x, int y, int color) {
        boolean bidi = fontRendererIn.getBidiFlag();
        boolean uni = fontRendererIn.getUnicodeFlag();
        fontRendererIn.setBidiFlag(true);
        fontRendererIn.setUnicodeFlag(true);
        super.drawCenteredString(fontRendererIn, text, x, y, color);
        fontRendererIn.setBidiFlag(bidi);
        fontRendererIn.setUnicodeFlag(uni);
    }


    @Override
    public void drawString(FontRenderer fontRendererIn, @Nonnull String text, int x, int y, int color) {
        boolean bidi = fontRendererIn.getBidiFlag();
        boolean uni = fontRendererIn.getUnicodeFlag();
        fontRendererIn.setBidiFlag(true);
        fontRendererIn.setUnicodeFlag(true);
        super.drawString(fontRendererIn, text, x, y, color);
        fontRendererIn.setBidiFlag(bidi);
        fontRendererIn.setUnicodeFlag(uni);
    }

    public <T> T register(T thing) {
        if (thing instanceof IGuiDrawScreenListener) {
            mGuiDrawables.add((IGuiDrawScreenListener) thing);
        }

        if (thing instanceof IGuiMouseListener) {
            mGuiMouseListeners.add((IGuiMouseListener) thing);
        }

        if (thing instanceof IGuiButtonListener) {
            mGuiButtonListeners.add((IGuiButtonListener) thing);
        }

        if (thing instanceof IGuiKeyboardListener) {
            mGuiKeyboardListeners.add((IGuiKeyboardListener) thing);
        }

        if (thing instanceof IGuiInitialisationListener) {
            mGuiInitialisationListeners.add((IGuiInitialisationListener) thing);
        }

        return thing;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public final void initGui() {
        super.initGui();
        mDelegate.setMeasurements(getGuiMeasurements());
        mScaledResolution = new ScaledResolution(mc);
        clearListeners();
        onInit();
        for (IGuiInitialisationListener l : mGuiInitialisationListeners) {
            l.onGuiInit(this, getGuiMeasurements(), mScaledResolution);
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     *
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        translateForeground();
        this.renderHoveredToolTip(mouseX, mouseY);
        executeDrawListeners(mouseX, mouseY);
        revertTranslateForeground();
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     *
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (IGuiMouseListener l : mGuiMouseListeners) {
            l.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    protected int getWidth() {
        return width;
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     *
     * @param mouseX
     * @param mouseY
     * @param clickedMouseButton
     * @param timeSinceLastClick
     */
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (IGuiMouseListener l : mGuiMouseListeners) {
            l.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    /**
     * Called when a mouse button is released.
     *
     * @param mouseX
     * @param mouseY
     * @param state
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (IGuiMouseListener l : mGuiMouseListeners) {
            l.mouseReleased(mouseX, mouseY, state);
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     *
     * @param typedChar
     * @param keyCode
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (IGuiKeyboardListener l : mGuiKeyboardListeners) {
            if (!l.keyTyped(typedChar, keyCode)) {
                return;
            }
        }
        if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
            onShouldCloseScreen();
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    protected void onInit() {

    }

    /**
     * Adds a control to this GUI's button list. Any type that subclasses button may be added (particularly, GuiSlider,
     * but not text fields).
     *
     * @param buttonIn The control to add
     * @return The control passed in.
     */
    @Override
    protected <T extends GuiButton> T addButton(T buttonIn) {
        for (IGuiButtonListener l : mGuiButtonListeners) {
            if (!l.onRegisterButton(buttonIn)) {
                return buttonIn;
            }
        }
        return super.addButton(buttonIn);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     *
     * @param button
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        for (IGuiButtonListener l : mGuiButtonListeners) {
            l.actionPerformed(button);
        }
    }

    protected void onShouldCloseScreen() {
        this.mc.player.closeScreen();
    }

    protected void createInventoryViews(BaseGuiContainer inventorySlotsIn) {
        createInnerInventoryView(inventorySlotsIn);
        createInvBarView(inventorySlotsIn);
    }

    protected void translateForeground() {
        GlStateManager.translate(-guiLeft, -guiTop, 0);//drawScreen somehow translates the Foreground by guiLeft and GuiTop...
    }

    protected void revertTranslateForeground() {
        GlStateManager.translate(guiLeft, guiTop, 0);//drawScreen somehow translates the Foreground by guiLeft and GuiTop...
    }

    protected void createInnerInventoryView(BaseGuiContainer inventorySlotsIn) {

    }

    protected void createInvBarView(BaseGuiContainer inventorySlotsIn) {

    }

    protected int getHeight() {
        return height;
    }

    /**
     * Draws the background layer of this container (behind the items).
     *
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground(); //=>not done by drawScreen method
    }

    /**
     * Evaluate the absolute Position for the given relative Position. <p>
     * Relative Positions are represented as a 0-100 double percent value. This reflects the values 0 = 'left End' of the gui, 100 = 'right End' of the gui.
     * The Values for the display Borders is something entirely different.
     *
     * @param relX The relative Position to convert
     * @return The absolute Display Pixel for the given relative Position
     */
    protected int asAbsoluteXPos(double relX) {
        return getGuiLeft() + asAbsoluteXDis(relX);
    }

    /**
     * Evaluate the absolute Position for the given relative Position. <p>
     * Relative Positions are represented as a 0-100 double percent value. This reflects the values 0 = 'left End' of the gui, 100 = 'right End' of the gui.
     * The Values for the display Borders is something entirely different.
     *
     * @param relY The relative Position to convert
     * @return The absolute Display Pixel for the given relative Position
     */
    protected int asAbsoluteYPos(double relY) {
        return getGuiTop() + asAbsoluteYDis(relY);
    }

    protected double asRelativeXPos(int absX) {
        return (absX - getGuiLeft()) * 100D / getXSize();
    }

    protected double asRelativeYPos(int absX) {
        return (absX - getGuiTop()) * 100D / getYSize();
    }

    protected int asAbsoluteXDis(double relX) {
        return (int) Math.round((getXSize() * relX / 100));
    }

    protected int asAbsoluteYDis(double relY) {
        return (int) Math.round((getYSize() * relY / 100));
    }

    private void clearListeners() {
        mGuiDrawables.removeIf(iGuiListener -> !iGuiListener.persistInitialisation());
        mGuiButtonListeners.removeIf(iGuiListener -> !iGuiListener.persistInitialisation());
        mGuiKeyboardListeners.removeIf(iGuiListener -> !iGuiListener.persistInitialisation());
        mGuiMouseListeners.removeIf(iGuiListener -> !iGuiListener.persistInitialisation());
        mGuiInitialisationListeners.removeIf(iGuiListener -> !iGuiListener.persistInitialisation());
    }

    private void executeDrawListeners(int mouseX, int mouseY) {
        for (IGuiDrawScreenListener l : mGuiDrawables) {
            l.drawScreen(mouseX, mouseY);
        }
    }
}
