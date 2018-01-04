package mt.mcmods.spellcraft;


import mt.mcmods.spellcraft.Client.net.MessageHandlers.ShowParticleHandler;
import mt.mcmods.spellcraft.Client.net.MessageHandlers.SyncEntitySpellpowerHandler;
import mt.mcmods.spellcraft.Client.net.Messages.ParticleActivated;
import mt.mcmods.spellcraft.Client.net.Messages.RequestNewPlayerSpell;
import mt.mcmods.spellcraft.Client.net.Messages.RequestSyncEntitySpellpower;
import mt.mcmods.spellcraft.Server.net.MessageHandlers.ParticleActivatedHandler;
import mt.mcmods.spellcraft.Server.net.MessageHandlers.RequestAddPlayerSpellHandler;
import mt.mcmods.spellcraft.Server.net.MessageHandlers.RequestSyncEntitySpellpowerHandler;
import mt.mcmods.spellcraft.Server.net.Messages.ShowParticle;
import mt.mcmods.spellcraft.Server.net.Messages.SyncEntitySpellpower;
import mt.mcmods.spellcraft.common.Capabilities.SpellcraftCapabilities;
import mt.mcmods.spellcraft.common.ConfigurationManager;
import mt.mcmods.spellcraft.common.Events.EventHandler;
import mt.mcmods.spellcraft.common.RecipeManager;
import mt.mcmods.spellcraft.common.blocks.SpellcraftBlocks;
import mt.mcmods.spellcraft.common.gui.SpellcraftGuiHandler;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.items.SpellcraftItems;
import mt.mcmods.spellcraft.common.materials.Materials;
import mt.mcmods.spellcraft.common.spell.SpellRegistry;
import mt.mcmods.spellcraft.common.spell.components.conditions.SpellcraftConditions;
import mt.mcmods.spellcraft.common.spell.components.executables.SpellcraftExecutables;
import mt.mcmods.spellcraft.common.util.ChannelHolder;
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
    public final SpellcraftItems items;
    public final SpellcraftConditions conditions;
    public final SpellcraftExecutables executables;
    private final Materials materials;

    public CommonProxy() {
        blocks = SpellcraftBlocks.getInstance();
        items = SpellcraftItems.getInstance();
        materials = Materials.getINSTANCE();
        conditions = SpellcraftConditions.getInstance();
        executables = SpellcraftExecutables.getInstance();
    }

    public void preInit(FMLPreInitializationEvent e) {
        CONFIG.register(e);
        EventHandler.registerEvents();
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
        RecipeManager.registerRecipes();
        RecipeManager.registerSmelting();
        blocks.registerWorldGen();
    }

    public void postInit(FMLPostInitializationEvent e) {
        items.postInit();
        blocks.postInit();
        conditions.postInit();
        executables.postInit();
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
