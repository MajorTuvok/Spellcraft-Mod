package com.mt.mcmods.spellcraft.common.Events;

import com.mt.mcmods.spellcraft.common.gui.GameOverlayGui;
import com.mt.mcmods.spellcraft.common.gui.helper.GUIMeasurements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedHashSet;

public class GameOverlayEventHandler {
    private static final LinkedHashSet<IRenderPostGameOverlayListener> postGameOverlayListeners = new LinkedHashSet<>();
    private static final LinkedHashSet<IRenderTextGameOverlayListener> textGameOverlayListeners = new LinkedHashSet<>();
    private static final LinkedHashSet<ISizeChangedListener> sizeChangedListeners = new LinkedHashSet<>();
    private GUIMeasurements measurements;

    static {
        registerGameOverlayListener(GameOverlayGui.INSTANCE);
        registerTextOverlayListener(GameOverlayGui.INSTANCE);
        registerSizeChangedListener(GameOverlayGui.INSTANCE);
    }

    public static void registerGameOverlayListener(IRenderPostGameOverlayListener listener) {
        postGameOverlayListeners.add(listener);
    }

    public static void registerTextOverlayListener(IRenderTextGameOverlayListener listener) {
        textGameOverlayListeners.add(listener);
    }

    public static void registerSizeChangedListener(ISizeChangedListener listener) {
        sizeChangedListeners.add(listener);
    }

    public static void deregisterGameOverlayListener(IRenderPostGameOverlayListener listener) {
        postGameOverlayListeners.remove(listener);
    }

    public static void deregisterTextOverlayListener(IRenderTextGameOverlayListener listener) {
        textGameOverlayListeners.remove(listener);
    }

    public static void deregisterSizeChangedListener(ISizeChangedListener listener) {
        sizeChangedListeners.remove(listener);
    }

    public GameOverlayEventHandler() {
        this(new GUIMeasurements(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, 0, 0));
    }

    private GameOverlayEventHandler(GUIMeasurements measurements) {
        this.measurements = measurements;
        checkMeasurements();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPostDraw(RenderGameOverlayEvent.Post event) {
        RenderGameOverlayEvent.ElementType type = event.getType();
        ScaledResolution resolution = event.getResolution();
        checkMeasurements();
        float partialTicks = event.getPartialTicks();
        for (IRenderPostGameOverlayListener listener : postGameOverlayListeners) {
            if (listener.isDrawingAs(type)) {
                listener.drawGameOverlay(type, resolution, measurements, partialTicks);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextDraw(RenderGameOverlayEvent.Text event) {
        ScaledResolution resolution = event.getResolution();
        float partialTicks = event.getPartialTicks();
        checkMeasurements();
        for (IRenderTextGameOverlayListener listener : textGameOverlayListeners) {
            listener.drawTextOverlay(resolution, measurements, partialTicks);
        }
    }

    private void checkMeasurements() {
        if (!measurements.equals(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, 0, 0)) {
            measurements = new GUIMeasurements(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, 0, 0);
            for (ISizeChangedListener listener : sizeChangedListeners) {
                listener.onSizeChanged(measurements);
            }
        }
    }

    public interface IRenderPostGameOverlayListener {
        @SideOnly(Side.CLIENT)
        public boolean isDrawingAs(RenderGameOverlayEvent.ElementType elementType);

        @SideOnly(Side.CLIENT)
        public void drawGameOverlay(RenderGameOverlayEvent.ElementType elementType, ScaledResolution resolution, GUIMeasurements measurements, float partialTicks);
    }

    public interface IRenderTextGameOverlayListener {
        @SideOnly(Side.CLIENT)
        public void drawTextOverlay(ScaledResolution resolution, GUIMeasurements measurements, float partialTicks);
    }

    public interface ISizeChangedListener {
        public void onSizeChanged(GUIMeasurements newScreenMeasurements);
    }
}
