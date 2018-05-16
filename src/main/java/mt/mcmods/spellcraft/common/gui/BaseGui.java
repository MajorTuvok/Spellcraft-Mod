package mt.mcmods.spellcraft.common.gui;


import mt.mcmods.spellcraft.common.gui.helper.GuiMeasurements;
import mt.mcmods.spellcraft.common.gui.helper.SlotDrawingDelegate;
import mt.mcmods.spellcraft.common.interfaces.IGuiRenderProvider;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.io.IOException;


public class BaseGui extends GuiContainer implements ILoggable, IGuiRenderProvider {
    private SlotDrawingDelegate mDelegate;
    private ScaledResolution mScaledResolution;

    public BaseGui(BaseGuiContainer inventorySlotsIn, int xSize, int ySize) {
        super(inventorySlotsIn);
        this.xSize = xSize;
        this.ySize = ySize;
        mDelegate = new SlotDrawingDelegate(inventorySlotsIn.getPlayerInventory(), this, inventorySlotsIn);
        mScaledResolution = new ScaledResolution(getMc());
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

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        super.initGui();
        mDelegate.setMeasurements(getGuiMeasurements());
        mScaledResolution = new ScaledResolution(mc);
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
        revertTranslateForeground();
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
        //this.drawDefaultBackground(); //=>done by drawScreen method
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
        if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
            onShouldCloseScreen();
        } else {
            super.keyTyped(typedChar, keyCode);
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
}
