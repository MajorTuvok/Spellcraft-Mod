package mt.mcmods.spellcraft.common.gui;

import mt.mcmods.spellcraft.common.capabilities.SpellcraftCapabilities;
import mt.mcmods.spellcraft.common.capabilities.spellpower.ISpellPowerProvider;
import mt.mcmods.spellcraft.common.events.handlers.GameOverlayEventHandler.IRenderPostGameOverlayListener;
import mt.mcmods.spellcraft.common.events.handlers.GameOverlayEventHandler.IRenderTextGameOverlayListener;
import mt.mcmods.spellcraft.common.events.handlers.GameOverlayEventHandler.ISizeChangedListener;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.gui.helper.GuiMeasurements;
import mt.mcmods.spellcraft.common.interfaces.IGuiRenderProvider;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.*;

import static mt.mcmods.spellcraft.common.gui.helper.GuiResource.OVERLAY_SPELLPOWER_BACKGROUND;
import static mt.mcmods.spellcraft.common.gui.helper.GuiResource.OVERLAY_SPELLPOWER_FOREGROUND;

public class GameOverlayGui extends Gui implements
        IRenderPostGameOverlayListener, IRenderTextGameOverlayListener, ISizeChangedListener,
        IGuiRenderProvider, ILoggable {
    public static final GameOverlayGui INSTANCE = new GameOverlayGui();
    private float barRelXPos;
    private float barRelYPos;
    private GuiDrawingDelegate delegate;

    public GameOverlayGui() {
        super();
        delegate = new GuiDrawingDelegate(this, new GuiMeasurements(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, 0, 0));
        this.barRelXPos = 1f;
        this.barRelYPos = 0.5f;
    }

    public float getBarRelXPos() {
        return barRelXPos;
    }

    public void setBarRelXPos(float barRelXPos) {
        this.barRelXPos = MathHelper.clamp(barRelXPos, 0.0f, 1.0f);
    }

    public float getBarRelYPos() {
        return barRelYPos;
    }

    public void setBarRelYPos(float barRelYPos) {
        this.barRelYPos = MathHelper.clamp(barRelYPos, 0.0f, 1.0f);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isDrawingAs(RenderGameOverlayEvent.ElementType elementType) {
        return elementType.ordinal() == RenderGameOverlayEvent.ElementType.EXPERIENCE.ordinal();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawGameOverlay(RenderGameOverlayEvent.ElementType elementType, ScaledResolution resolution, GuiMeasurements measurements, float partialTicks) {
        float barXPos = getBarXPos(resolution);
        float barYPos = getBarYPos(resolution);
        EntityPlayerSP player = getMc().player;
        if (isHoldingWand(player)) {
            ISpellPowerProvider provider = player.getCapability(SpellcraftCapabilities.SPELL_POWER_PROVIDER_CAPABILITY, null);
            delegate.drawImage(OVERLAY_SPELLPOWER_BACKGROUND, barXPos, barYPos, OVERLAY_SPELLPOWER_BACKGROUND);
            if (provider != null) {
                delegate.drawImageYPartiallyReverse(OVERLAY_SPELLPOWER_FOREGROUND, barXPos, barYPos, OVERLAY_SPELLPOWER_FOREGROUND, getPowerFactor(provider));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawTextOverlay(ScaledResolution resolution, GuiMeasurements measurements, float partialTicks) {
        EntityPlayerSP player = getMc().player;
        if (isHoldingWand(player)) {
            ISpellPowerProvider provider = player.getCapability(SpellcraftCapabilities.SPELL_POWER_PROVIDER_CAPABILITY, null);
            if (provider != null) {
                float powerFactor = getPowerFactor(provider);
                int gb = Math.round(255 * powerFactor);
                int r = Math.round(255 * (1 - powerFactor));
                int color = new Color(r, gb, gb).getRGB();
                String text = "" + Math.round(provider.getPower());
                delegate.drawScaledStringWithResScale(text, resolution.getScaledWidth(), getBarYPos(resolution), color, 0.8f, resolution.getScaleFactor());
            }
        }
    }

    @Override
    public void onSizeChanged(GuiMeasurements newScreenMeasurements) {
        delegate.setMeasurements(newScreenMeasurements);
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
        return getMc().fontRenderer;
    }

    @Nonnull
    @Override
    public RenderItem getRenderItem() {
        return this.getMc().getRenderItem();
    }

    @Override
    public Minecraft getMc() {
        return Minecraft.getMinecraft();
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

    private float getBarXPos(GuiMeasurements measurements) {
        return MathHelper.clamp(getBarRelXPos() * measurements.getXSize() - OVERLAY_SPELLPOWER_BACKGROUND.getImgXSize() / 2, 0, measurements.getXSize());
    }

    private float getBarYPos(GuiMeasurements measurements) {
        return MathHelper.clamp(getBarRelYPos() * measurements.getYSize() - OVERLAY_SPELLPOWER_BACKGROUND.getImgYSize() / 2, 0, measurements.getYSize());
    }

    private float getBarXPos(ScaledResolution measurements) {
        return MathHelper.clamp(getBarRelXPos() * measurements.getScaledWidth() - OVERLAY_SPELLPOWER_BACKGROUND.getImgXSize() / 2 * measurements.getScaleFactor(), 0, measurements.getScaledWidth());
    }

    private float getBarYPos(ScaledResolution measurements) {
        return MathHelper.clamp(getBarRelYPos() * measurements.getScaledHeight() - OVERLAY_SPELLPOWER_BACKGROUND.getImgYSize() / 2 * measurements.getScaleFactor(), 0, measurements.getScaledHeight());
    }

    private boolean isHoldingWand(EntityPlayer player) {
        return player.getHeldItemMainhand().getItem() instanceof ItemWand || player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemWand;
    }

    private float getPowerFactor(ISpellPowerProvider provider) {
        return MathHelper.clamp((float) provider.getPower() / provider.getMaxPower(), 0, 1);
    }
}
