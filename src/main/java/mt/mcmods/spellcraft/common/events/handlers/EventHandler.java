package mt.mcmods.spellcraft.common.events.handlers;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraftforge.common.MinecraftForge;

public class EventHandler {
    public static void registerEvents() {
        ILoggable.Log.info("Registering EventHandlers");
        MinecraftForge.EVENT_BUS.register(new LeftClickEventHandler());
        MinecraftForge.EVENT_BUS.register(new SpellPowerEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new WandEventHandler());
    }

    public static void registerClientEvents() {
        MinecraftForge.EVENT_BUS.register(new TextureEventHandler());
        MinecraftForge.EVENT_BUS.register(new GameOverlayEventHandler());
    }

    public static void postInit() {

    }

    public static void onServerStarting() {
        SpellPowerEventHandler.clear();
    }
}
