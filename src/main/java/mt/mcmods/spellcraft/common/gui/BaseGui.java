package mt.mcmods.spellcraft.common.gui;

import mt.mcmods.spellcraft.common.gui.helper.GUIMeasurements;
import mt.mcmods.spellcraft.common.gui.helper.SlotDrawingDelegate;
import mt.mcmods.spellcraft.common.interfaces.IGuiRenderProvider;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;

import javax.annotation.Nonnull;

public class BaseGui extends GuiContainer implements ILoggable, IGuiRenderProvider {
    private SlotDrawingDelegate delegate;

    public BaseGui(InventoryPlayer inventoryPlayer, BaseGuiContainer inventorySlotsIn, int xSize, int ySize) {
        super(inventorySlotsIn);
        this.xSize = xSize;
        this.ySize = ySize;
        delegate = new SlotDrawingDelegate(inventoryPlayer, this, inventorySlotsIn);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        super.initGui();
        delegate.setMeasurements(getGuiMeasurements());
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

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
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

    @Override
    public @Nonnull
    FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    @Nonnull
    @Override
    public RenderItem getRenderItem() {
        return itemRender;
    }

    @Override
    public Minecraft getMc() {
        return mc;
    }

    public GUIMeasurements getGuiMeasurements() {
        return new GUIMeasurements(xSize, ySize, guiLeft, guiTop);
    }

    public SlotDrawingDelegate getDelegate() {
        return delegate;
    }

    protected void translateForeground() {
        GlStateManager.translate(-guiLeft, -guiTop, 0);//drawScreen somehow translates the Forground by guiLeft and GuiTop...
    }

    protected void revertTranslateForeground() {
        GlStateManager.translate(guiLeft, guiTop, 0);//drawScreen somehow translates the Forground by guiLeft and GuiTop...
    }

}
