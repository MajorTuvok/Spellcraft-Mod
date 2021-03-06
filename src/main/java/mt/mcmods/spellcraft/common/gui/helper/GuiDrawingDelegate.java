package mt.mcmods.spellcraft.common.gui.helper;

import mt.mcmods.spellcraft.common.interfaces.IGuiRenderProvider;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GuiDrawingDelegate implements ILoggable, IGuiRenderProvider {
    private GuiMeasurements measurements;
    private IGuiRenderProvider renderProvider;
    private boolean bidiFlag;
    private boolean uniFlag;

    public GuiDrawingDelegate(IGuiRenderProvider renderProvider, GuiMeasurements measurements) {
        this.renderProvider = renderProvider;
        this.measurements = measurements;
        bidiFlag = false;
        uniFlag = true;
    }

    //----------------------------Getter and Setter-------------------------------------------------

    public void setBidiFlag(boolean bidiFlag) {
        this.bidiFlag = bidiFlag;
    }

    public void setUniFlag(boolean uniFlag) {
        this.uniFlag = uniFlag;
    }

    public boolean bidiFlag() {
        return bidiFlag;
    }

    public boolean uniFlag() {
        return uniFlag;
    }

    public int getXSize() {
        return getMeasurements().getXSize();
    }

    public int getYSize() {
        return getMeasurements().getYSize();
    }

    public int getGuiLeft() {
        return getMeasurements().getGuiLeft();
    }

    public int getGuiTop() {
        return getMeasurements().getGuiTop();
    }

    public IGuiRenderProvider getRenderProvider() {
        return renderProvider;
    }

    public GuiMeasurements getMeasurements() {
        return measurements;
    }

    public void setMeasurements(GuiMeasurements measurements) {
        this.measurements = measurements;
    }

    //----------------------------Render Provider Methods -----------------------------------------------

    @Override
    public void drawTexturedModalRect(int xPos, int yPos, int textureX, int textureY, int width, int height) {
        getRenderProvider().drawTexturedModalRect(xPos, yPos, textureX, textureY, width, height);
    }

    @Override
    public void drawTexturedModalRect(float xPos, float yPos, int minU, int minV, int maxU, int maxV) {
        getRenderProvider().drawTexturedModalRect(xPos, yPos, minU, minV, maxU, maxV);
    }

    @Override
    public void drawTexturedModalRect(int xPos, int yPos, TextureAtlasSprite textureSprite, int widthIn, int heightIn) {
        getRenderProvider().drawTexturedModalRect(xPos, yPos, textureSprite, widthIn, heightIn);
    }

    @Override
    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        boolean bidi = fontRendererIn.getBidiFlag();
        boolean uni = fontRendererIn.getUnicodeFlag();
        fontRendererIn.setBidiFlag(bidiFlag());
        fontRendererIn.setUnicodeFlag(uniFlag());
        getRenderProvider().drawCenteredString(fontRendererIn, text, x, y, color);
        fontRendererIn.setBidiFlag(bidi);
        fontRendererIn.setUnicodeFlag(uni);
    }

    @Override
    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        boolean bidi = fontRendererIn.getBidiFlag();
        boolean uni = fontRendererIn.getUnicodeFlag();
        fontRendererIn.setBidiFlag(bidiFlag());
        fontRendererIn.setUnicodeFlag(uniFlag());
        getRenderProvider().drawString(fontRendererIn, text, x, y, color);
        fontRendererIn.setBidiFlag(bidi);
        fontRendererIn.setUnicodeFlag(uni);
    }

    @Override
    public void drawHorizontalLine(int startX, int endX, int y, int color) {
        getRenderProvider().drawHorizontalLine(startX, endX, y, color);
    }

    @Override
    public void drawVerticalLine(int x, int startY, int endY, int color) {
        getRenderProvider().drawVerticalLine(x, startY, endY, color);
    }

    @Override
    public void drawRectangle(int left, int top, int right, int bottom, int color) {
        getRenderProvider().drawRectangle(left, top, right, bottom, color);
    }

    @Override
    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        getRenderProvider().drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    @Nonnull
    @Override
    public FontRenderer getFontRenderer() {
        return getRenderProvider().getFontRenderer();
    }

    @Nonnull
    @Override
    public RenderItem getRenderItem() {
        return getRenderProvider().getRenderItem();
    }

    @Override
    public Minecraft getMc() {
        return getRenderProvider().getMc();
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        drawCenteredString(getFontRenderer(), text, x, y, color);
    }

    public void drawString(String text, int x, int y, int color) {
        getRenderProvider().drawString(getFontRenderer(), text, x, y, color);
    }


    //----------------------------Resource Management-------------------------------------------------

    public void bindResource(@Nonnull ResourceProvider provider) {
        bindResource(provider.getResource());
    }

    public void bindResource(@Nonnull ResourceLocation provider) {
        getMc().getTextureManager().bindTexture(provider);
    }


    //----------------------------Background Image-------------------------------------------------

    public void drawGuiBackground(@Nonnull ResourceProvider provider) {
        drawGuiBackground(provider.getResource());
    }

    public void drawGuiBackground(@Nonnull ResourceLocation location) {
        bindResource(location);
        drawGuiBackground();
    }

    public void drawGuiBackground() {
        drawTexturedModalRect(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
    }

    //----------------------------Image Drawing-------------------------------------------------

    public void drawImage(ResourceLocation location, float xRel, float yRel, ResourceImgMeasurements measurements) {
        drawImage(location, xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize());
    }

    public void drawImage(float xRel, float yRel, IResourceInfo info) {
        drawImage(info, xRel, yRel, info);
    }

    public void drawImage(ResourceProvider provider, float xRel, float yRel, ResourceImgMeasurements measurements) {
        drawImage(provider, xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize());
    }

    public void drawImage(ResourceProvider provider, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize) {
        drawImage(provider.getResource(), xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize);
    }

    public void drawImage(ResourceLocation location, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize) {
        bindResource(location);
        drawTexturedImage(xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize);
    }

    public void drawTexturedImage(float xRel, float yRel, ResourceImgMeasurements measurements) {
        drawTexturedImage(xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize());
    }

    public void drawTexturedImage(float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize) {
        drawTexturedModalRect(xRel + getGuiLeft() - 1, yRel + getGuiTop() - 1, imgXStart, imgYStart, imgXSize, imgYSize);
    }

    //----------------------------Partial Drawing-------------------------------------------------

    //--------Textured---------
    public void drawTexturedImagePartially(float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage, float yPercentage) {
        drawTexturedImagePartially(xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize(), xPercentage, yPercentage);
    }

    public void drawTexturedImagePartially(float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage, float yPercentage) {
        drawTexturedImage(xRel, yRel, imgXStart, imgYStart, Math.round(imgXSize * xPercentage), Math.round(imgYSize * yPercentage));
    }

    //--------Untextured---------
    public void drawImagePartially(ResourceProvider provider, float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage, float yPercentage) {
        drawImagePartially(provider.getResource(), xRel, yRel, measurements, xPercentage, yPercentage);
    }

    public void drawImagePartially(ResourceLocation location, float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage, float yPercentage) {
        drawImagePartially(location, xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize(), xPercentage, yPercentage);
    }

    public void drawImagePartially(ResourceProvider provider, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage, float yPercentage) {
        drawImagePartially(provider.getResource(), xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, xPercentage, yPercentage);
    }

    public void drawImagePartially(ResourceLocation location, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage, float yPercentage) {
        drawImage(location, xRel, yRel, imgXStart, imgYStart, Math.round(imgXSize * xPercentage), Math.round(imgYSize * yPercentage));
    }

    //--------Untextured X---------
    public void drawImageXPartially(ResourceProvider provider, float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage) {
        drawImagePartially(provider.getResource(), xRel, yRel, measurements, xPercentage, 1.0f);
    }

    public void drawImageXPartially(ResourceLocation location, float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage) {
        drawImagePartially(location, xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize(), xPercentage, 1.0f);
    }

    public void drawImageXPartially(ResourceProvider provider, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage) {
        drawImagePartially(provider.getResource(), xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, xPercentage, 1.0f);
    }

    public void drawImageXPartially(ResourceLocation location, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage) {
        drawImagePartially(location, xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, xPercentage, 1.0f);
    }

    //--------Untextured Y---------
    public void drawImageYPartially(ResourceProvider provider, float xRel, float yRel, ResourceImgMeasurements measurements, float yPercentage) {
        drawImagePartially(provider.getResource(), xRel, yRel, measurements, 1.0f, yPercentage);
    }

    public void drawImageYPartially(ResourceLocation location, float xRel, float yRel, ResourceImgMeasurements measurements, float yPercentage) {
        drawImagePartially(location, xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize(), 1.0f, yPercentage);
    }

    public void drawImageYPartially(ResourceProvider provider, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float yPercentage) {
        drawImagePartially(provider.getResource(), xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, 1.0f, yPercentage);
    }

    public void drawImageYPartially(ResourceLocation location, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float yPercentage) {
        drawImagePartially(location, xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, 1.0f, yPercentage);
    }

    //----------------------------Partial Drawing (Reverse)-------------------------------------------------

    //--------Textured---------
    public void drawTexturedImagePartiallyReverse(float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage, float yPercentage) {
        drawTexturedImagePartiallyReverse(xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize(), xPercentage, yPercentage);
    }

    public void drawTexturedImagePartiallyReverse(float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage, float yPercentage) {
        int xPart = Math.round(imgXSize * (1 - xPercentage));
        int yPart = Math.round(imgYSize * (1 - yPercentage));
        drawTexturedImage(xRel + xPart, yRel + yPart, imgXStart + xPart, imgYStart + yPart, imgXSize - xPart, imgYSize - yPart);
    }

    //--------Untextured--------
    public void drawImagePartiallyReverse(ResourceProvider provider, float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage, float yPercentage) {
        drawImagePartiallyReverse(provider.getResource(), xRel, yRel, measurements, xPercentage, yPercentage);
    }

    public void drawImagePartiallyReverse(ResourceLocation location, float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage, float yPercentage) {
        drawImagePartiallyReverse(location, xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize(), xPercentage, yPercentage);
    }

    public void drawImagePartiallyReverse(ResourceProvider provider, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage, float yPercentage) {
        drawImagePartiallyReverse(provider.getResource(), xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, xPercentage, yPercentage);
    }

    public void drawImagePartiallyReverse(ResourceLocation location, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage, float yPercentage) {
        int xPart = Math.round(imgXSize * (1 - xPercentage));
        int yPart = Math.round(imgYSize * (1 - yPercentage));
        drawImage(location, xRel + xPart, yRel + yPart, imgXStart + xPart, imgYStart + yPart, imgXSize - xPart, imgYSize - yPart);
    }

    //--------UntexturedX--------
    public void drawImageXPartiallyReverse(ResourceProvider provider, float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage) {
        drawImageXPartiallyReverse(provider.getResource(), xRel, yRel, measurements, xPercentage);
    }

    public void drawImageXPartiallyReverse(ResourceLocation location, float xRel, float yRel, ResourceImgMeasurements measurements, float xPercentage) {
        drawImageXPartiallyReverse(location, xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize(), xPercentage);
    }

    public void drawImageXPartiallyReverse(ResourceProvider provider, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage) {
        drawImageXPartiallyReverse(provider.getResource(), xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, xPercentage);
    }

    public void drawImageXPartiallyReverse(ResourceLocation location, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float xPercentage) {
        drawImagePartiallyReverse(location, xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, xPercentage, 1.0f);
    }

    //--------UntexturedY--------
    public void drawImageYPartiallyReverse(ResourceProvider provider, float xRel, float yRel, ResourceImgMeasurements measurements, float yPercentage) {
        drawImageYPartiallyReverse(provider.getResource(), xRel, yRel, measurements, yPercentage);
    }

    public void drawImageYPartiallyReverse(ResourceLocation location, float xRel, float yRel, ResourceImgMeasurements measurements, float yPercentage) {
        drawImageYPartiallyReverse(location, xRel, yRel, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize(), yPercentage);
    }

    public void drawImageYPartiallyReverse(ResourceProvider provider, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float yPercentage) {
        drawImageYPartiallyReverse(provider.getResource(), xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, yPercentage);
    }

    public void drawImageYPartiallyReverse(ResourceLocation location, float xRel, float yRel, int imgXStart, int imgYStart, int imgXSize, int imgYSize, float yPercentage) {
        drawImagePartiallyReverse(location, xRel, yRel, imgXStart, imgYStart, imgXSize, imgYSize, 1.0f, yPercentage);
    }

    //----------------------------Text------------------------------------------------------------------

    /**
     * Draws a string scaled with the given Scale Factor.
     * Coordinates are relative to the bottom right end of the String.
     *
     * @param text  The String to draw
     * @param x     The x-Coordinate
     * @param y     The y-Coordinate
     * @param color The color to draw with
     * @param scale The scale to scale the String by
     */
    public void drawScaledString(String text, float x, float y, int color, float scale) {
        float reverse = 1.0f / scale;
        float yAdd = 4.0f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        int length = getFontRenderer().getStringWidth(text);
        getFontRenderer().drawString(text, Math.round(x * reverse - length), Math.round((y - yAdd) * reverse), color);
        GlStateManager.popMatrix();
    }

    /**
     * Draws a string scaled with the given Scale Factor.
     * Coordinates are relative to the Top Left end of the String.
     *
     * @param text  The String to draw
     * @param x     The x-Coordinate
     * @param y     The y-Coordinate
     * @param color The color to draw with
     * @param scale The scale to scale the String by
     */
    public void drawScaledStringTopLeft(String text, float x, float y, int color, float scale) {
        float reverse = 1.0f / scale;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        getFontRenderer().drawString(text, Math.round(x * reverse), Math.round(y * reverse), color);
        GlStateManager.popMatrix();
    }


    /**
     * Draws a string scaled with the given Scale Factor.
     * Coordinates are relative to the Center of the String.
     * Effectively behaves as {@link #drawScaledStringTopLeft(String, float, float, int, float)} with the exception of using
     * {@link #drawCenteredString(String, int, int, int)} instead of {@link #drawString(String, int, int, int)}.
     *
     * @param text  The String to draw
     * @param x     The x-Coordinate
     * @param y     The y-Coordinate
     * @param color The color to draw with
     * @param scale The scale to scale the String by
     */
    public void drawScaledStringCentered(String text, float x, float y, int color, float scale) {
        float reverse = 1.0f / scale;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        drawCenteredString(text, Math.round(x * reverse), Math.round(y * reverse), color);
        GlStateManager.popMatrix();
    }

    /**
     * Draws a string scaled with the given Scale Factor.
     * The Horizontal Coordinate is relative to the Center of the String, the vertical Coordinate is the top End of the String.
     * Effectively behaves as {@link #drawScaledStringTopLeft(String, float, float, int, float)} using
     * {@link #drawCenteredString(String, int, int, int)} instead of {@link #drawString(String, int, int, int)} and additionally
     * centering the Text vertically.
     *
     * @param text  The String to draw
     * @param x     The x-Coordinate (left)
     * @param y     The y-Coordinate (top)
     * @param color The color to draw with
     * @param scale The scale to scale the String by
     */
    public void drawScaledStringCenteredHorizontally(String text, float x, float y, int color, float scale) {
        float reverse = 1.0f / scale;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        drawCenteredString(text, Math.round(x * reverse), Math.round((y - Math.round(getFontRenderer().FONT_HEIGHT / 2f)) * reverse), color);
        GlStateManager.popMatrix();
    }

    /**
     * Draws a string scaled with the given Scale Factor.
     * Coordinates are relative to the bottom right end of the String.
     *
     * @param text           The String to draw
     * @param x              The x-Coordinate
     * @param y              The y-Coordinate
     * @param color          The color to draw with
     * @param scale          The scale to scale the String by
     * @param resScaleFactor The ScaleFactor of the current Resolution
     */
    public void drawScaledStringWithResScale(String text, float x, float y, int color, float scale, float resScaleFactor) {
        float reverse = 1.0f / scale;
        float yAdd = 4.0f * resScaleFactor;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        int length = getFontRenderer().getStringWidth(text);
        getFontRenderer().drawString(text, Math.round(x * reverse - length), Math.round((y - yAdd) * reverse), color);
        GlStateManager.popMatrix();
    }

    /**
     * Draws a string scaled with the given Scale Factor.
     * Coordinates are relative to the bottom right end of the String.
     * This Method takes the position of the gui into account and is therefore equal to calling
     * {@link #drawScaledStringWithResScale(String, float, float, int, float, float)} with x+{@link #getGuiLeft()} and x+{@link #getGuiTop()}.
     *
     * @param text           The String to draw
     * @param x              The x-Coordinate
     * @param y              The y-Coordinate
     * @param color          The color to draw with
     * @param scale          The scale to scale the String by
     * @param resScaleFactor The ScaleFactor of the current Resolution
     */
    public void drawScaledStringWithResScaleInGui(String text, float x, float y, int color, float scale, float resScaleFactor) {
        drawScaledStringWithResScale(text, x + getGuiLeft(), y + getGuiTop(), color, scale, resScaleFactor);
    }

    //----------------------------Interfaces ---------------------------------------------------------------
    public interface ResourceProvider {
        @Nonnull
        ResourceLocation getResource();
    }

    public interface ResourceImgMeasurements {
        int getImgXStart();

        int getImgYStart();

        int getImgXSize();

        int getImgYSize();
    }

    public interface IResourceInfo extends ResourceProvider, ResourceImgMeasurements {

    }
}
