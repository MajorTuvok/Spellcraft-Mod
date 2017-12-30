package com.mt.mcmods.spellcraft.common.Events;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraftforge.common.MinecraftForge;

public class EventHandler {
    public static void registerEvents() {
        ILoggable.Log.info("Registering EventHandlers");
        MinecraftForge.EVENT_BUS.register(new LeftClickEventHandler());
        MinecraftForge.EVENT_BUS.register(new TextureEventHandler());
        MinecraftForge.EVENT_BUS.register(new GameOverlayEventHandler());
        MinecraftForge.EVENT_BUS.register(new SpellPowerEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    public static void onServerStarting(){
        SpellPowerEventHandler.clear();
    }
}
