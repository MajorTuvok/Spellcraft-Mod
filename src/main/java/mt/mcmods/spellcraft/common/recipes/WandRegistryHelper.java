package mt.mcmods.spellcraft.common.recipes;

import mt.mcmods.spellcraft.common.capabilities.wandproperties.IWandPropertyDefinition;
import mt.mcmods.spellcraft.common.capabilities.wandproperties.WandPropertyDefinition;
import mt.mcmods.spellcraft.common.events.WandEvent.CreateEvent;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import mt.mcmods.spellcraft.common.util.item.ItemStackWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.HashMap;
import java.util.Map;

@NotThreadSafe
public enum WandRegistryHelper {
    INSTANCE;
    private WandPropertyDefinition mBoundDef;
    private Map<ItemStackWrapper, WandPart> mCoreMap;
    private Map<ItemStackWrapper, WandPart> mTipMap;

    WandRegistryHelper() {
        this.mBoundDef = new WandPropertyDefinition(0, 100, Float.MIN_VALUE, Float.MAX_VALUE);
        this.mCoreMap = new HashMap<>();
        this.mTipMap = new HashMap<>();
    }

    @Nonnull
    public WandPropertyDefinition getBoundDefinition() {
        return mBoundDef;
    }

    @Nonnull
    Map<ItemStackWrapper, WandPart> getCoreMap() {
        return mCoreMap;
    }

    @Nonnull
    Map<ItemStackWrapper, WandPart> getTipMap() {
        return mTipMap;
    }

    public boolean hasCorePart(ItemStackWrapper stack) {
        return mCoreMap.containsKey(stack);
    }

    public boolean hasTipPart(ItemStackWrapper stack) {
        return mTipMap.containsKey(stack);
    }

    public void addCorePart(@Nonnull WandPart part) {
        mCoreMap.put(part.getKey(), part);
    }

    public void addTipPart(@Nonnull WandPart part) {
        mTipMap.put(part.getKey(), part);
    }

    public boolean hasCorePart(ItemStack stack) {
        return hasCorePart(new ItemStackWrapper(stack));
    }

    @Nullable
    public ItemWand getWand(@Nonnull String name, @Nonnull Block tipCraftingItem, @Nonnull Item coreCraftingItem) {
        return getWand(name, Item.getItemFromBlock(tipCraftingItem), coreCraftingItem);
    }

    public boolean hasTipPart(ItemStack stack) {
        return hasTipPart(new ItemStackWrapper(stack));
    }

    @Nullable
    public ItemWand getWand(@Nonnull String name, @Nonnull Item tipCraftingItem, @Nonnull Block coreCraftingItem) {
        return getWand(name, tipCraftingItem, Item.getItemFromBlock(coreCraftingItem));
    }

    @Nullable
    public ItemWand getWand(@Nonnull String name, @Nonnull Block tipCraftingItem, @Nonnull Block coreCraftingItem) {
        return getWand(name, Item.getItemFromBlock(tipCraftingItem), Item.getItemFromBlock(coreCraftingItem));
    }

    @Nullable
    public ItemWand getWand(@Nonnull String name, @Nonnull Item tipCraftingItem, @Nonnull Item coreCraftingItem) {
        return getWand(name, new ItemStack(tipCraftingItem), new ItemStack(coreCraftingItem));
    }

    @Nullable
    public ItemWand getWand(@Nonnull String name, @Nonnull ItemStack tipCraftingItem, @Nonnull ItemStack coreCraftingItem) {
        return getWand(name, new ItemStackWrapper(tipCraftingItem), new ItemStackWrapper(coreCraftingItem));
    }

    @Nullable
    public ItemWand getWand(@Nonnull String name, @Nonnull ItemStackWrapper tipCraftingItem, @Nonnull ItemStackWrapper coreCraftingItem) {
        tipCraftingItem.asSingleElementStack();
        coreCraftingItem.asSingleElementStack();
        if (name.isEmpty())
            throw new IllegalArgumentException("Cannot instantiate Wand with null name!");
        if (!mTipMap.containsKey(tipCraftingItem) || tipCraftingItem.get().isEmpty())
            throw new IllegalArgumentException("Cannot instantiate Wand with unknown tipCraftingItem!");
        if (!mCoreMap.containsKey(coreCraftingItem) || coreCraftingItem.get().isEmpty())
            throw new IllegalArgumentException("Cannot instantiate Wand with unknown coreCraftingItem!");
        IWandPropertyDefinition tipDef = mTipMap.get(tipCraftingItem).getWandPropertyDefinition();
        IWandPropertyDefinition coreDef = mCoreMap.get(coreCraftingItem).getWandPropertyDefinition();
        WandPropertyDefinition wandDef = new WandPropertyDefinition(
                (tipDef.getMaxEfficiency() * 2 + coreDef.getMaxEfficiency()) / 3,
                (tipDef.getMinEfficiency() * 2 + coreDef.getMinEfficiency()) / 3,
                (tipDef.getMaxMaxPower() + coreDef.getMaxMaxPower() * 2) / 3,
                (tipDef.getMinMaxPower() + coreDef.getMinMaxPower() * 2) / 3,
                (tipDef.getPerfectEfficiencyBorder() * 2 + coreDef.getPerfectEfficiencyBorder()) / 3,
                (tipDef.getPerfectMaxPowerBorder() + coreDef.getPerfectMaxPowerBorder() * 2) / 3);
        checkForBoundValues(wandDef);
        CreateEvent event = new CreateEvent(WandRegistry.INSTANCE, name, wandDef, tipCraftingItem.get(), coreCraftingItem.get());
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            ItemWand wand = new ItemWand(name, wandDef);
            WandRegistry.INSTANCE.addWand(tipCraftingItem.get(), coreCraftingItem.get(), wand);
            return wand;
        }
        return null;
    }

    WandPart getTipPart(ItemStack stack) {
        return getTipPart(new ItemStackWrapper(stack));
    }

    WandPart getTipPart(ItemStackWrapper stack) {
        return mTipMap.get(stack);
    }

    WandPart getCorePart(ItemStack stack) {
        return getCorePart(new ItemStackWrapper(stack));
    }

    WandPart getCorePart(ItemStackWrapper stack) {
        return mCoreMap.get(stack);
    }

    private void checkForBoundValues(IWandPropertyDefinition definition) {
        if (!mBoundDef.equals(definition)) {
            float minEfficiency = Math.min(definition.getMinEfficiency(), mBoundDef.getMinEfficiency());
            float maxEfficiency = Math.max(definition.getMaxEfficiency(), mBoundDef.getMaxEfficiency());
            float minMaxPower = Math.min(definition.getMinMaxPower(), mBoundDef.getMinMaxPower());
            float maxMaxPower = Math.max(definition.getMaxMaxPower(), mBoundDef.getMaxMaxPower());
            mBoundDef = new WandPropertyDefinition(maxEfficiency, minEfficiency, maxMaxPower, minMaxPower);
        }
    }
}
