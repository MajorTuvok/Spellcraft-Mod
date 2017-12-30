package com.mt.mcmods.spellcraft;

import com.mt.mcmods.spellcraft.common.util.ChannelHolder;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.util.Random;

@Mod(modid = SpellcraftMod.MODID,
        useMetadata = true)
public class SpellcraftMod implements ILoggable {
    public static final Random rand = new Random();
    public static ChannelHolder CHANNEL_HOLDER = null;
    @Mod.Instance(MODID)
    public static SpellcraftMod instance = new SpellcraftMod();
    @SidedProxy(clientSide = "com.mt.mcmods.spellcraft.ClientProxy", serverSide = "com.mt.mcmods.spellcraft.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        CHANNEL_HOLDER = CommonProxy.CHANNEL_HOLDER;
        Log.info("Entered PreInitialisation Phase");
        proxy.preInit(e);
        Log.info("Finished PreInitialisation Phase");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        Log.info("Entered Initialisation Phase");
        proxy.init(e);
        Log.info("Finished Initialisation Phase");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        Log.info("Entered PostInitialisation Phase");
        proxy.postInit(e);
        Log.info("Finished PostInitialisation Phase");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        proxy.serverStopping(event);
    }

}
