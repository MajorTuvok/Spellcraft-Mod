package mt.mcmods.spellcraft.common.events.handlers;

import mt.mcmods.spellcraft.common.capabilities.wandproperties.WandPropertyDefinition;
import mt.mcmods.spellcraft.common.events.WandEvent;
import mt.mcmods.spellcraft.common.recipes.WandPart;
import mt.mcmods.spellcraft.common.recipes.WandRegistryHelper;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

import static mt.mcmods.spellcraft.common.items.SpellcraftItems.WAND_IRON_IRON;

class WandEventHandler {

    @SubscribeEvent
    public void onDefineEvent(WandEvent.DefineEvent event) {
        WandRegistryHelper help = event.getCreationHelper();
        addWandParts(help);
        createWands(help);
    }

    private void addWandParts(WandRegistryHelper help) { //TODO add to config
        help.addCorePart(WandPart.newInstance(Items.IRON_INGOT, new WandPropertyDefinition(80, 60, 140, 100), new Color(114, 114, 114).getRGB()));
        help.addCorePart(WandPart.newInstance(Items.GOLD_INGOT, new WandPropertyDefinition(85, 70, 100, 80), new Color(174, 174, 0).getRGB()));
        help.addCorePart(WandPart.newInstance(Blocks.COBBLESTONE, new WandPropertyDefinition(50, 45, 70, 50), new Color(86, 86, 86).getRGB()));
        help.addCorePart(WandPart.newInstance(Blocks.STONE, new WandPropertyDefinition(55, 50, 80, 70), new Color(104, 104, 104).getRGB()));
        help.addCorePart(WandPart.newInstance(Blocks.LOG, new WandPropertyDefinition(70, 40, 50, 30), new Color(100, 79, 46).getRGB()));

        help.addTipPart(WandPart.newInstance(Items.IRON_INGOT, new WandPropertyDefinition(85, 65, 100, 60), new Color(168, 168, 168).getRGB()));
        help.addTipPart(WandPart.newInstance(Items.GOLD_INGOT, new WandPropertyDefinition(90, 75, 60, 40), new Color(222, 222, 0).getRGB()));
        help.addTipPart(WandPart.newInstance(Blocks.COBBLESTONE, new WandPropertyDefinition(55, 50, 30, 20), new Color(191, 191, 191).getRGB()));
        help.addTipPart(WandPart.newInstance(Blocks.STONE, new WandPropertyDefinition(60, 55, 40, 30), new Color(144, 144, 144).getRGB()));
        help.addTipPart(WandPart.newInstance(Blocks.LOG, new WandPropertyDefinition(75, 45, 10, 0), new Color(145, 113, 66).getRGB()));
    }

    private void createWands(WandRegistryHelper help) {
        WAND_IRON_IRON = help.getWand(StringHelper.createUnlocalizedName("iron", "iron", "wand"), Items.IRON_INGOT, Items.IRON_INGOT);
        //WAND_IRON_IRON.setCustomLocation(ironLocation);
        /*help.getWand(StringHelper.createUnlocalizedName("iron", "gold", "wand"), Items.IRON_INGOT, Items.GOLD_INGOT);
        help.getWand(StringHelper.createUnlocalizedName("cobblestone", "iron", "wand"), Items.IRON_INGOT, Blocks.COBBLESTONE);
        help.getWand(StringHelper.createUnlocalizedName("cobblestone", "gold", "wand"), Items.GOLD_INGOT, Blocks.COBBLESTONE);
        help.getWand(StringHelper.createUnlocalizedName("stone", "iron", "wand"), Items.IRON_INGOT, Blocks.STONE);
        help.getWand(StringHelper.createUnlocalizedName("stone", "gold", "wand"), Items.GOLD_INGOT, Blocks.STONE);
        help.getWand(StringHelper.createUnlocalizedName("gold", "iron", "wand"), Items.GOLD_INGOT, Items.IRON_INGOT);
        help.getWand(StringHelper.createUnlocalizedName("gold", "gold", "wand"), Items.GOLD_INGOT, Items.GOLD_INGOT);
        help.getWand(StringHelper.createUnlocalizedName("log", "iron", "wand"), Items.IRON_INGOT, Blocks.LOG);
        help.getWand(StringHelper.createUnlocalizedName("log", "gold", "wand"), Items.GOLD_INGOT, Blocks.LOG);*/
    }
}
