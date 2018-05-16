package mt.mcmods.spellcraft;


import mt.mcmods.spellcraft.client.net.MessageHandlers.ShowParticleHandler;
import mt.mcmods.spellcraft.client.net.MessageHandlers.SyncEntitySpellpowerHandler;
import mt.mcmods.spellcraft.client.net.Messages.ParticleActivated;
import mt.mcmods.spellcraft.client.net.Messages.RequestNewPlayerSpell;
import mt.mcmods.spellcraft.client.net.Messages.RequestSyncEntitySpellpower;
import mt.mcmods.spellcraft.common.ConfigurationManager;
import mt.mcmods.spellcraft.common.blocks.SpellcraftBlocks;
import mt.mcmods.spellcraft.common.capabilities.SpellcraftCapabilities;
import mt.mcmods.spellcraft.common.events.handlers.EventHandler;
import mt.mcmods.spellcraft.common.gui.SpellcraftGuiHandler;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.items.SpellcraftItems;
import mt.mcmods.spellcraft.common.materials.Materials;
import mt.mcmods.spellcraft.common.recipes.RecipeManager;
import mt.mcmods.spellcraft.common.spell.SpellRegistry;
import mt.mcmods.spellcraft.common.spell.components.conditions.SpellcraftConditions;
import mt.mcmods.spellcraft.common.spell.components.executables.SpellcraftExecutables;
import mt.mcmods.spellcraft.common.spell.entity.EntitySpellRegistry;
import mt.mcmods.spellcraft.common.util.ChannelHolder;
import mt.mcmods.spellcraft.server.net.MessageHandlers.ParticleActivatedHandler;
import mt.mcmods.spellcraft.server.net.MessageHandlers.RequestAddPlayerSpellHandler;
import mt.mcmods.spellcraft.server.net.MessageHandlers.RequestSyncEntitySpellpowerHandler;
import mt.mcmods.spellcraft.server.net.Messages.ShowParticle;
import mt.mcmods.spellcraft.server.net.Messages.SyncEntitySpellpower;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class CommonProxy implements ILoggable {
    public static final ChannelHolder CHANNEL_HOLDER = new ChannelHolder(MODID);
    public static ConfigurationManager CONFIG = new ConfigurationManager();
    public static IForgeRegistry<Item> ITEM_REGISTRY = null;
    public final SpellcraftBlocks blocks;
    public final SpellcraftConditions conditions;
    public final SpellcraftExecutables executables;
    public final SpellcraftItems items;
    private final Materials materials;

    public CommonProxy() {
        blocks = SpellcraftBlocks.getInstance();
        items = SpellcraftItems.getInstance();
        materials = Materials.getINSTANCE();
        conditions = SpellcraftConditions.getInstance();
        executables = SpellcraftExecutables.getInstance();
        EventHandler.registerEvents();
    }

    public abstract boolean isClient();

    public void preInit(FMLPreInitializationEvent e) {
        CONFIG.register(e);
        materials.onConfigCreated(this);
        items.commonPreInit();
        blocks.commonPreInit();
        conditions.commonPreInit();
        executables.commonPreInit();
        NetworkRegistry.INSTANCE.registerGuiHandler(SpellcraftMod.instance, new SpellcraftGuiHandler());
        registerMessages();
        SpellcraftCapabilities.registerCapabilities();
    }

    public void init(FMLInitializationEvent e) {
        RecipeManager.registerSmelting();
        blocks.registerWorldGen();
        SpellRegistry.addSpellRegistryCallback(EntitySpellRegistry.ENTITY_SPELL_REGISTRY);
    }

    public void postInit(FMLPostInitializationEvent e) {
        items.postInit();
        blocks.postInit();
        conditions.postInit();
        executables.postInit();
        EventHandler.postInit();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        EventHandler.onServerStarting();
        SpellRegistry.onServerStarting(event);
    }

    public void serverStopping(FMLServerStoppingEvent event) {

    }

    public void registerTemplateSprites() {

    }

    protected void registerMessages() {
        CHANNEL_HOLDER.registerMessage(ParticleActivatedHandler.class, ParticleActivated.class, Side.SERVER);
        CHANNEL_HOLDER.registerMessage(ShowParticleHandler.class, ShowParticle.class, Side.CLIENT);
        CHANNEL_HOLDER.registerMessage(RequestSyncEntitySpellpowerHandler.class, RequestSyncEntitySpellpower.class, Side.SERVER);
        CHANNEL_HOLDER.registerMessage(SyncEntitySpellpowerHandler.class, SyncEntitySpellpower.class, Side.CLIENT);
        CHANNEL_HOLDER.registerMessage(RequestAddPlayerSpellHandler.class, RequestNewPlayerSpell.class, Side.SERVER);
    }
}
