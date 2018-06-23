package mt.mcmods.spellcraft.common.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import javax.annotation.Nonnull;

public interface IGuiRenderProvider {
    @Nonnull
    public FontRenderer getFontRenderer();

    @Nonnull
    public RenderItem getRenderItem();

    public Minecraft getMc();

    public void drawTexturedModalRect(int xPos, int yPos, int textureX, int textureY, int width, int height);

    public void drawTexturedModalRect(float xPos, float yPos, int minU, int minV, int maxU, int maxV);

    public void drawTexturedModalRect(int xPos, int yPos, TextureAtlasSprite textureSprite, int widthIn, int heightIn);

    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color);

    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color);

    public void drawHorizontalLine(int startX, int endX, int y, int color);

    public void drawVerticalLine(int x, int startY, int endY, int color);

    public void drawRectangle(int left, int top, int right, int bottom, int color);

    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor);
}
