package com.mt.mcmods.spellcraft.common.items;


import com.mt.mcmods.spellcraft.common.BaseContainer;
import com.mt.mcmods.spellcraft.common.items.wand.ItemWand;
import com.mt.mcmods.spellcraft.common.Capabilities.wandproperties.WandPropertyDefinition;
import com.mt.mcmods.spellcraft.common.items.wand.WandRegistry;
import com.mt.mcmods.spellcraft.common.items.wand.WandRegistryHelper;
import com.mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class SpellcraftItems extends BaseContainer<Item> {
    private static boolean createdItems = false;
    public static ItemWand WAND_IRON_IRON;

    @Override
    @SubscribeEvent
    public void onRegistryEvent(RegistryEvent.Register<Item> e) {
        createItems();
        super.onRegistryEvent(e);
        Log.info("Registering Items");
        WandRegistry.INSTANCE.onRegister(getUtils());
        Log.trace("Created Tools successfully. Registering");
        Log.debug("Succeeded creating tools");
    }

    @Override
    public void commonPreInit() {
        createItems();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientInit() {
        super.clientInit();
    }

    private static void createItems() {
        if (!createdItems) {
            createdItems = true;
            addWandParts();
            createWands();
        }
    }

    private static void createWands() {
        WandRegistryHelper help = WandRegistryHelper.INSTANCE;
        ResourceLocation ironLocation = new ResourceLocation(StringHelper.createResourceLocation(MODID, "wands","wand_iron"));
        ResourceLocation goldLocation = new ResourceLocation(StringHelper.createResourceLocation(MODID, "wands","wand_gold"));
        WAND_IRON_IRON = help.getWand(StringHelper.createUnlocalizedName("iron","iron","wand"),Items.IRON_INGOT,Items.IRON_INGOT);
        WAND_IRON_IRON.setCustomLocation(ironLocation);
        help.getWand(StringHelper.createUnlocalizedName("iron","gold","wand"),Items.IRON_INGOT,Items.GOLD_INGOT).setCustomLocation(goldLocation);
        help.getWand(StringHelper.createUnlocalizedName("cobblestone","iron","wand"),Items.IRON_INGOT,Blocks.COBBLESTONE).setCustomLocation(ironLocation);
        help.getWand(StringHelper.createUnlocalizedName("cobblestone","gold","wand"),Items.GOLD_INGOT,Blocks.COBBLESTONE).setCustomLocation(goldLocation);
        help.getWand(StringHelper.createUnlocalizedName("stone","iron","wand"),Items.IRON_INGOT,Blocks.STONE).setCustomLocation(ironLocation);
        help.getWand(StringHelper.createUnlocalizedName("stone","gold","wand"),Items.GOLD_INGOT,Blocks.STONE).setCustomLocation(goldLocation);
        help.getWand(StringHelper.createUnlocalizedName("gold","iron","wand"),Items.GOLD_INGOT,Items.IRON_INGOT).setCustomLocation(ironLocation);
        help.getWand(StringHelper.createUnlocalizedName("gold","gold","wand"),Items.GOLD_INGOT,Items.GOLD_INGOT).setCustomLocation(goldLocation);
        help.getWand(StringHelper.createUnlocalizedName("log","iron","wand"),Items.IRON_INGOT,Blocks.LOG).setCustomLocation(ironLocation);
        help.getWand(StringHelper.createUnlocalizedName("log","gold","wand"),Items.GOLD_INGOT,Blocks.LOG).setCustomLocation(goldLocation);
    }

    private static void addWandParts() {
        WandRegistryHelper help = WandRegistryHelper.INSTANCE;
        help.addCorePart(Items.IRON_INGOT,new WandPropertyDefinition(80,60,140,100));
        help.addCorePart(Items.GOLD_INGOT,new WandPropertyDefinition(85,70,100,80));
        help.addCorePart(Blocks.COBBLESTONE,new WandPropertyDefinition(50,45,70,50));
        help.addCorePart(Blocks.STONE,new WandPropertyDefinition(55,50,80,70));
        help.addCorePart(Blocks.LOG,new WandPropertyDefinition(70,40,50,30));

        help.addTipPart(Items.IRON_INGOT,new WandPropertyDefinition(85,65,100,60));
        help.addTipPart(Items.GOLD_INGOT,new WandPropertyDefinition(90,75,60,40));
    }
}
