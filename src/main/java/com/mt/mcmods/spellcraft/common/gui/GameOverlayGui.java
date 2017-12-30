package com.mt.mcmods.spellcraft.common.gui;

import com.mt.mcmods.spellcraft.common.Capabilities.SpellcraftCapabilities;
import com.mt.mcmods.spellcraft.common.Capabilities.spellpower.ISpellPowerProvider;
import com.mt.mcmods.spellcraft.common.Events.GameOverlayEventHandler;
import com.mt.mcmods.spellcraft.common.gui.helper.GUIMeasurements;
import com.mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import com.mt.mcmods.spellcraft.common.interfaces.IGuiRenderProvider;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.items.wand.ItemWand;
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

import static com.mt.mcmods.spellcraft.common.gui.helper.GuiResources.*;

public class GameOverlayGui extends Gui implements
        GameOverlayEventHandler.IRenderPostGameOverlayListener, GameOverlayEventHandler.IRenderTextGameOverlayListener, GameOverlayEventHandler.ISizeChangedListener,
        IGuiRenderProvider, ILoggable {
    private GuiDrawingDelegate delegate;
    private float barRelXPos;
    private float barRelYPos;
    public static final GameOverlayGui INSTANCE = new GameOverlayGui();

    public GameOverlayGui() {
        super();
        delegate = new GuiDrawingDelegate(this,new GUIMeasurements(Minecraft.getMinecraft().displayWidth,Minecraft.getMinecraft().displayHeight,0,0));
        this.barRelXPos = 1f;
        this.barRelYPos = 0.5f;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isDrawingAs(RenderGameOverlayEvent.ElementType elementType) {
        return elementType.ordinal() == RenderGameOverlayEvent.ElementType.EXPERIENCE.ordinal();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawGameOverlay(RenderGameOverlayEvent.ElementType elementType, ScaledResolution resolution, GUIMeasurements measurements, float partialTicks) {
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
    public void drawTextOverlay(ScaledResolution resolution, GUIMeasurements measurements,float partialTicks) {
        EntityPlayerSP player = getMc().player;
        if (isHoldingWand(player)) {
            ISpellPowerProvider provider = player.getCapability(SpellcraftCapabilities.SPELL_POWER_PROVIDER_CAPABILITY, null);
            if (provider!=null) {
                float powerFactor = getPowerFactor(provider);
                int gb = Math.round(255*powerFactor);
                int r = Math.round(255*(1-powerFactor));
                int color = new Color(r,gb,gb).getRGB();
                String text = ""+Math.round(provider.getPower());
                delegate.drawScaledString(text,resolution.getScaledWidth(),getBarYPos(resolution),color,0.8f,resolution.getScaleFactor());
            }
        }
    }

    @Override
    public void onSizeChanged(GUIMeasurements newScreenMeasurements) {
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

    public float getBarRelXPos() {
        return barRelXPos;
    }

    public void setBarRelXPos(float barRelXPos) {
        this.barRelXPos = MathHelper.clamp(barRelXPos,0.0f,1.0f);
    }

    public float getBarRelYPos() {
        return barRelYPos;
    }

    public void setBarRelYPos(float barRelYPos) {
        this.barRelYPos = MathHelper.clamp(barRelYPos,0.0f,1.0f);
    }

    private float getBarXPos(GUIMeasurements measurements) {
        return MathHelper.clamp( getBarRelXPos()*measurements.getXSize()-OVERLAY_SPELLPOWER_BACKGROUND.getImgXSize()/2,0,measurements.getXSize());
    }

    private float getBarYPos(GUIMeasurements measurements) {
        return MathHelper.clamp( getBarRelYPos()*measurements.getYSize()-OVERLAY_SPELLPOWER_BACKGROUND.getImgYSize()/2,0,measurements.getYSize());
    }

    private float getBarXPos(ScaledResolution measurements) {
        return MathHelper.clamp( getBarRelXPos()*measurements.getScaledWidth()-OVERLAY_SPELLPOWER_BACKGROUND.getImgXSize()/2*measurements.getScaleFactor(),0,measurements.getScaledWidth());
    }

    private float getBarYPos(ScaledResolution measurements) {
        return MathHelper.clamp( getBarRelYPos()*measurements.getScaledHeight()-OVERLAY_SPELLPOWER_BACKGROUND.getImgYSize()/2*measurements.getScaleFactor(),0,measurements.getScaledHeight());
    }

    private boolean isHoldingWand(EntityPlayer player) {
        return player.getHeldItemMainhand().getItem() instanceof ItemWand || player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemWand;
    }

    private float getPowerFactor(ISpellPowerProvider provider) {
        return MathHelper.clamp((float) provider.getPower() / provider.getMaxPower(), 0,1);
    }
}
