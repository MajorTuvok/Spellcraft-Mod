package mt.mcmods.spellcraft.common.recipes;

import mt.mcmods.spellcraft.common.capabilities.wandproperties.IWandPropertyDefinition;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import mt.mcmods.spellcraft.common.util.item.ItemStackWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class WandPart {
    private int color;
    private ItemStackWrapper mKey;
    private IWandPropertyDefinition mWandPropertyDefinition;

    private WandPart(ItemStackWrapper key, IWandPropertyDefinition wandPropertyDefinition, int color) {
        mKey = key;
        mWandPropertyDefinition = wandPropertyDefinition;
        this.color = color;
    }

    public static WandPart newInstance(Block key, IWandPropertyDefinition wandPropertyDefinition) {
        return newInstance(Item.getItemFromBlock(key), wandPropertyDefinition);
    }

    public static WandPart newInstance(Item key, IWandPropertyDefinition wandPropertyDefinition) {
        return newInstance(new ItemStack(key), wandPropertyDefinition);
    }

    public static WandPart newInstance(ItemStack key, IWandPropertyDefinition wandPropertyDefinition) {
        return newInstance(new ItemStackWrapper(key), wandPropertyDefinition);
    }

    public static WandPart newInstance(ItemStackWrapper key, IWandPropertyDefinition wandPropertyDefinition) {
        return newInstance(key, wandPropertyDefinition, ItemWand.DEFAULT_COLOR);
    }

    public static WandPart newInstance(Block key, IWandPropertyDefinition wandPropertyDefinition, int color) {
        return newInstance(Item.getItemFromBlock(key), wandPropertyDefinition, color);
    }

    public static WandPart newInstance(Item key, IWandPropertyDefinition wandPropertyDefinition, int color) {
        return newInstance(new ItemStack(key), wandPropertyDefinition, color);
    }

    public static WandPart newInstance(ItemStack key, IWandPropertyDefinition wandPropertyDefinition, int color) {
        return newInstance(new ItemStackWrapper(key), wandPropertyDefinition, color);
    }

    public static WandPart newInstance(ItemStackWrapper key, IWandPropertyDefinition wandPropertyDefinition, int color) {
        return new WandPart(key, wandPropertyDefinition, color);
    }

    public ItemStackWrapper getKey() {
        return mKey;
    }

    public WandPart setKey(ItemStackWrapper key) {
        mKey = key;
        return this;
    }

    public IWandPropertyDefinition getWandPropertyDefinition() {
        return mWandPropertyDefinition;
    }

    public WandPart setWandPropertyDefinition(IWandPropertyDefinition wandPropertyDefinition) {
        mWandPropertyDefinition = wandPropertyDefinition;
        return this;
    }

    public int getColor() {
        return color;
    }

    public WandPart setColor(int color) {
        this.color = color;
        return this;
    }
}
