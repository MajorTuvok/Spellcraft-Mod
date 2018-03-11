package mt.mcmods.spellcraft.common.gui;

import mt.mcmods.spellcraft.common.gui.components.DrawLayer;
import mt.mcmods.spellcraft.common.gui.components.SlotComponent;
import mt.mcmods.spellcraft.common.gui.components.ViewComponent;
import mt.mcmods.spellcraft.common.gui.components.ViewComponentGroup;
import mt.mcmods.spellcraft.common.gui.helper.GuiMeasurements;
import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import mt.mcmods.spellcraft.common.gui.helper.SlotDrawingDelegate;
import mt.mcmods.spellcraft.common.interfaces.IGuiRenderProvider;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
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
import javax.annotation.Nullable;
import java.io.IOException;


public class BaseGui extends GuiContainer implements ILoggable, IGuiRenderProvider {
    private GuiComponentController mComponentController;
    private SlotDrawingDelegate mDelegate;
    private ViewComponentGroup mInnerInventory;
    private ViewComponentGroup mInventoryBar;
    private ScaledResolution mScaledResolution;

    public BaseGui(BaseGuiContainer inventorySlotsIn, int xSize, int ySize) {
        super(inventorySlotsIn);
        this.xSize = xSize;
        this.ySize = ySize;
        mDelegate = new SlotDrawingDelegate(inventorySlotsIn.getPlayerInventory(), this, inventorySlotsIn);
        mComponentController = new GuiComponentController(this, mDelegate);
        mScaledResolution = new ScaledResolution(getMc());
        mInnerInventory = null;
        mInventoryBar = null;
        createInventoryViews(inventorySlotsIn);
    }

    @Override
    @Nonnull
    public FontRenderer getFontRenderer() {
        return fontRenderer;
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

    protected void setInnerInventory(@Nullable ViewComponentGroup innerInventory) {
        mInnerInventory = innerInventory;
    }

    @Nullable
    protected ViewComponentGroup getInnerInventory() {
        return mInnerInventory;
    }

    protected void setInventoryBar(@Nullable ViewComponentGroup inventoryBar) {
        mInventoryBar = inventoryBar;
    }

    protected ScaledResolution getScaledResolution() {
        return mScaledResolution;
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
    public void drawRectangle(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }

    @Nullable
    protected ViewComponentGroup getInventoryBar() {
        return mInventoryBar;
    }

    /**
     * Adds a control to this GUI's button list. Any type that subclasses button may be added (particularly, GuiSlider,
     * but not text fields).
     *
     * @param buttonIn The control to add
     * @return The control passed in.
     */
    @Override
    @Nonnull
    public <T extends GuiButton> T addButton(T buttonIn) {
        return super.addButton(buttonIn);
    }

    protected float getScaleFactor() {
        return getScaledResolution().getScaleFactor();
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
        mComponentController.onResize();
    }

    /**
     * Draws the screen and all the components in it.
     *
     * @param mouseX
     * @param mouseY
     * @param partialTicks
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mComponentController.drawLayer(mouseX, mouseY, partialTicks, DrawLayer.FIRST);
        super.drawScreen(mouseX, mouseY, partialTicks);
        mComponentController.drawLayer(mouseX, mouseY, partialTicks, DrawLayer.LAST);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     *
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        mComponentController.drawLayer(mouseX, mouseY, -1, DrawLayer.FOREGROUND);
        DrawLayer.FOREGROUND.normalizeGLState(getXSize(), getYSize());
        this.renderHoveredToolTip(mouseX, mouseY);
        DrawLayer.FOREGROUND.resetGLState();
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
        this.drawDefaultBackground(); //=>done by drawScreen method
        mComponentController.drawLayer(mouseX, mouseY, partialTicks, DrawLayer.BACKGROUND);
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
        mComponentController.onClick(mouseX, mouseY, mouseButton);
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
        mComponentController.onClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
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
        mComponentController.onClickReleased(mouseX, mouseY, state);
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
        super.keyTyped(typedChar, keyCode);
        mComponentController.onKeyTyped(typedChar, keyCode);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
        mComponentController.onUpdate();
    }

    protected void createInventoryViews(BaseGuiContainer inventorySlotsIn) {
        createInnerInventoryView(inventorySlotsIn);
        createInvBarView(inventorySlotsIn);
    }

    protected @Nullable
    ViewComponent findViewById(long id) {
        return mComponentController.findViewById(id);
    }

    protected long addComponent(ViewComponent component) {
        return mComponentController.onAdd(component);
    }

    protected void translateForeground() {
        GlStateManager.translate(-guiLeft, -guiTop, 0);//drawScreen somehow translates the Foreground by guiLeft and GuiTop...
    }

    protected void revertTranslateForeground() {
        GlStateManager.translate(guiLeft, guiTop, 0);//drawScreen somehow translates the Foreground by guiLeft and GuiTop...
    }

    private void createInnerInventoryView(BaseGuiContainer inventorySlotsIn) {
        PlayerInventoryOffsets offsets = inventorySlotsIn.getInventoryOffsets();
        ViewComponentGroup innerInventory =
                new ViewComponentGroup(offsets.getInnerXInvOffset(), offsets.getInnerYInvOffset(), offsets.getInnerColumnCount() * offsets.getSlotXSize(), offsets.getInnerRowCount() * offsets.getSlotYSize())
                        .setPriority(-100)
                        .setDraggable(false);
        addComponent(innerInventory);
        for (int i = 0; i < offsets.getInnerColumnCount() * offsets.getInnerRowCount(); ++i) {
            ViewComponent component = new SlotComponent(inventorySlotsIn.inventorySlots.get(i)).setPriority(i - 100);
            innerInventory.addSubComponent(component);
        }
        setInnerInventory(innerInventory);
    }

    private void createInvBarView(BaseGuiContainer inventorySlotsIn) {
        PlayerInventoryOffsets offsets = inventorySlotsIn.getInventoryOffsets();
        ViewComponentGroup invBar =
                new ViewComponentGroup(offsets.getInvBarXOffset(), offsets.getInvBarYOffset(), offsets.getInvBarColumnCount() * offsets.getSlotXSize(), offsets.getSlotYSize())
                        .setPriority(-99)
                        .setDraggable(false);
        addComponent(invBar);
        for (int i = offsets.getInnerColumnCount() * offsets.getInnerRowCount(); i < inventorySlotsIn.inventorySlots.size(); ++i) {
            ViewComponent component = new SlotComponent(inventorySlotsIn.inventorySlots.get(i)).setPriority(i - 100);
            invBar.addSubComponent(component);
        }
        setInventoryBar(invBar);
    }

}
