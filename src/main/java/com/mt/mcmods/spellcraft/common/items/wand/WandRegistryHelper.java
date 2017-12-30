package com.mt.mcmods.spellcraft.common.items.wand;

import com.mt.mcmods.spellcraft.common.Capabilities.wandproperties.IWandPropertyDefinition;
import com.mt.mcmods.spellcraft.common.Capabilities.wandproperties.WandPropertyDefinition;
import com.mt.mcmods.spellcraft.common.util.item.ItemStackWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class WandRegistryHelper {
    private HashMap<ItemStackWrapper,WandPropertyDefinition> coreMap;
    private HashMap<ItemStackWrapper,WandPropertyDefinition> tipMap;
    private WandPropertyDefinition boundDef;
    public static final WandRegistryHelper INSTANCE = new WandRegistryHelper();

    public WandRegistryHelper() {
        this.coreMap = new HashMap<>();
        this.tipMap = new HashMap<>();
        this.boundDef = new WandPropertyDefinition(0,100,0,Float.MAX_VALUE);
    }

    public WandPropertyDefinition getBoundDefinition() {
        return boundDef;
    }

    public void addCorePart(@Nonnull Block coreCraftingItem, @Nonnull WandPropertyDefinition definition) {
        addCorePart(Item.getItemFromBlock(coreCraftingItem),definition);
    }

    public void addTipPart(@Nonnull Block tipCraftingItem, @Nonnull WandPropertyDefinition definition) {
        addTipPart(Item.getItemFromBlock(tipCraftingItem),definition);
    }

    public void addCorePart(@Nonnull Item coreCraftingItem, @Nonnull WandPropertyDefinition definition) {
        addCorePart(coreCraftingItem.getDefaultInstance(),definition);
    }

    public void addTipPart(@Nonnull Item tipCraftingItem, @Nonnull WandPropertyDefinition definition) {
        addTipPart(tipCraftingItem.getDefaultInstance(),definition);
    }

    public void addCorePart(@Nonnull ItemStack coreCraftingItem, @Nonnull WandPropertyDefinition definition) {
        addCorePart(new ItemStackWrapper(coreCraftingItem),definition);
    }

    public void addTipPart(@Nonnull ItemStack tipCraftingItem, @Nonnull WandPropertyDefinition definition) {
        addTipPart(new ItemStackWrapper(tipCraftingItem),definition);
    }

    public void addCorePart(@Nonnull ItemStackWrapper coreCraftingItem, @Nonnull WandPropertyDefinition definition) {
        if (coreCraftingItem.get().isEmpty()) throw new IllegalArgumentException("Cannot add empty crafting Stack");
        coreMap.put(coreCraftingItem,definition);
    }

    public void addTipPart(@Nonnull ItemStackWrapper tipCraftingItem, @Nonnull WandPropertyDefinition definition) {
        if (tipCraftingItem.get().isEmpty()) throw new IllegalArgumentException("Cannot add empty crafting Stack");
        tipMap.put(tipCraftingItem,definition);
    }

    public ItemWand getWand(@Nonnull String name, @Nonnull Block tipCraftingItem, @Nonnull Item coreCraftingItem) {
        return getWand(name,Item.getItemFromBlock(tipCraftingItem),coreCraftingItem);
    }

    public ItemWand getWand(@Nonnull String name, @Nonnull Item tipCraftingItem, @Nonnull Block coreCraftingItem) {
        return getWand(name,tipCraftingItem,Item.getItemFromBlock(coreCraftingItem));
    }

    public ItemWand getWand(@Nonnull String name, @Nonnull Block tipCraftingItem, @Nonnull Block coreCraftingItem) {
        return getWand(name,Item.getItemFromBlock(tipCraftingItem),Item.getItemFromBlock(coreCraftingItem));
    }

    public ItemWand getWand(@Nonnull String name, @Nonnull Item tipCraftingItem, @Nonnull Item coreCraftingItem) {
        return getWand(name,tipCraftingItem.getDefaultInstance(),coreCraftingItem.getDefaultInstance());
    }

    public ItemWand getWand(@Nonnull String name, @Nonnull ItemStack tipCraftingItem, @Nonnull ItemStack coreCraftingItem) {
        return getWand(name,new ItemStackWrapper(tipCraftingItem),new ItemStackWrapper(coreCraftingItem));
    }

    public ItemWand getWand(@Nonnull String name, @Nonnull ItemStackWrapper tipCraftingItem, @Nonnull ItemStackWrapper coreCraftingItem) {
        if (name.isEmpty())
            throw new IllegalArgumentException("Cannot instantiate Wand with null name!");
        if (!tipMap.containsKey(tipCraftingItem) || tipCraftingItem.get().isEmpty())
            throw new IllegalArgumentException("Cannot instantiate Wand with unknown tipCraftingItem!");
        if (!coreMap.containsKey(coreCraftingItem) || coreCraftingItem.get().isEmpty())
            throw new IllegalArgumentException("Cannot instantiate Wand with unknown tipCraftingItem!");
        WandPropertyDefinition tipDef = tipMap.get(tipCraftingItem);
        WandPropertyDefinition coreDef = coreMap.get(coreCraftingItem);
        WandPropertyDefinition wandDef = new WandPropertyDefinition(
                (tipDef.getMaxEfficiency()*2+coreDef.getMaxEfficiency())/3,
                (tipDef.getMinEfficiency()*2+coreDef.getMinEfficiency())/3,
                (tipDef.getMaxMaxPower()+coreDef.getMaxMaxPower()*2)/3,
                (tipDef.getMinMaxPower()+coreDef.getMinMaxPower()*2)/3,
                (tipDef.getPerfectEfficiencyBorder()*2+coreDef.getPerfectEfficiencyBorder())/3,
                (tipDef.getPerfectMaxPowerBorder()+coreDef.getPerfectMaxPowerBorder()*2)/3);
        checkForBoundValues(wandDef);
        ItemWand wand = new ItemWand(name,wandDef);
        WandRegistry.INSTANCE.addWand(tipCraftingItem.get(),coreCraftingItem.get(),wand);
        return wand;
    }

    private void checkForBoundValues(IWandPropertyDefinition definition) {
        if (!boundDef.equals(definition)) {
            float minEfficiency = Math.min(definition.getMinEfficiency(),boundDef.getMinEfficiency());
            float maxEfficiency = Math.max(definition.getMaxEfficiency(),boundDef.getMaxEfficiency());
            float minMaxPower = Math.min(definition.getMinMaxPower(),boundDef.getMinMaxPower());
            float maxMaxPower = Math.max(definition.getMaxMaxPower(),boundDef.getMaxMaxPower());
            boundDef = new WandPropertyDefinition(maxEfficiency,minEfficiency,maxMaxPower,minMaxPower);
        }
    }
}
