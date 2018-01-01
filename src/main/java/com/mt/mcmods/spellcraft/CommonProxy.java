package com.mt.mcmods.spellcraft;

import com.mt.mcmods.spellcraft.Client.net.MessageHandlers.ShowParticleHandler;
import com.mt.mcmods.spellcraft.Client.net.MessageHandlers.SyncEntitySpellpowerHandler;
import com.mt.mcmods.spellcraft.Client.net.Messages.ParticleActivated;
import com.mt.mcmods.spellcraft.Client.net.Messages.RequestNewPlayerSpell;
import com.mt.mcmods.spellcraft.Client.net.Messages.RequestSyncEntitySpellpower;
import com.mt.mcmods.spellcraft.Server.net.MessageHandlers.ParticleActivatedHandler;
import com.mt.mcmods.spellcraft.Server.net.MessageHandlers.RequestAddPlayerSpellHandler;
import com.mt.mcmods.spellcraft.Server.net.MessageHandlers.RequestSyncEntitySpellpowerHandler;
import com.mt.mcmods.spellcraft.Server.net.Messages.ShowParticle;
import com.mt.mcmods.spellcraft.Server.net.Messages.SyncEntitySpellpower;
import com.mt.mcmods.spellcraft.common.spell.SpellRegistry;
import com.mt.mcmods.spellcraft.common.Capabilities.SpellcraftCapabilities;
import com.mt.mcmods.spellcraft.common.ConfigurationManager;
import com.mt.mcmods.spellcraft.common.Events.EventHandler;
import com.mt.mcmods.spellcraft.common.RecipeManager;
import com.mt.mcmods.spellcraft.common.blocks.SpellcraftBlocks;
import com.mt.mcmods.spellcraft.common.gui.SpellcraftGuiHandler;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.items.SpellcraftItems;
import com.mt.mcmods.spellcraft.common.materials.Materials;
import com.mt.mcmods.spellcraft.common.spell.conditions.SpellcraftConditions;
import com.mt.mcmods.spellcraft.common.util.ChannelHolder;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public abstract class CommonProxy implements ILoggable {
    public static final ChannelHolder CHANNEL_HOLDER = new ChannelHolder(MODID);
    public static ConfigurationManager CONFIG = new ConfigurationManager();
    public final SpellcraftBlocks blocks;
    public final SpellcraftItems items;
    public final SpellcraftConditions conditions;
    private final Materials materials;

    public CommonProxy() {
        blocks = SpellcraftBlocks.getInstance();
        items = SpellcraftItems.getInstance();
        materials = Materials.getINSTANCE();
        conditions = SpellcraftConditions.getInstance();
    }

    public void preInit(FMLPreInitializationEvent e) {
        CONFIG.register(e);
        EventHandler.registerEvents();
        materials.onConfigCreated(this);
        items.commonPreInit();
        blocks.commonPreInit();
        conditions.commonPreInit();
        NetworkRegistry.INSTANCE.registerGuiHandler(SpellcraftMod.instance, new SpellcraftGuiHandler());
        registerMessages();
        SpellcraftCapabilities.registerCapabilities();
    }

    public void init(FMLInitializationEvent e) {
        RecipeManager.registerRecipes();
        RecipeManager.registerSmelting();
        blocks.registerWorldGen();
    }

    public void postInit(FMLPostInitializationEvent e) {
        items.postInit();
        blocks.postInit();
        conditions.postInit();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        EventHandler.onServerStarting();
        SpellRegistry.onServerStarting(event);
    }

    public void serverStopping(FMLServerStoppingEvent event) {

    }

    public abstract boolean isClient();


    protected void registerMessages() {
        CHANNEL_HOLDER.registerMessage(ParticleActivatedHandler.class, ParticleActivated.class, Side.SERVER);
        CHANNEL_HOLDER.registerMessage(ShowParticleHandler.class, ShowParticle.class, Side.CLIENT);
        CHANNEL_HOLDER.registerMessage(RequestSyncEntitySpellpowerHandler.class, RequestSyncEntitySpellpower.class, Side.SERVER);
        CHANNEL_HOLDER.registerMessage(SyncEntitySpellpowerHandler.class, SyncEntitySpellpower.class, Side.CLIENT);
        CHANNEL_HOLDER.registerMessage(RequestAddPlayerSpellHandler.class, RequestNewPlayerSpell.class, Side.SERVER);
    }

    public void registerTemplateSprites() {

    }
}
