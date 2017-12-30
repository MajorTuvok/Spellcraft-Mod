package com.mt.mcmods.spellcraft.common.util.item;

import com.mt.mcmods.spellcraft.common.items.wand.ItemWand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ItemHelper {
    public static boolean areItemStacksEqual(ItemStack first, ItemStack second) {
        return areStacksItemStackEqual(first, second) || areStacksOreDictEqual(first, second);
    }

    public static boolean areStacksItemStackEqual(ItemStack first, ItemStack second) {
        return ItemStack.areItemsEqual(first, second) && ItemStack.areItemStacksEqual(first, second);
    }

    public static boolean areStacksOreDictEqual(ItemStack first, ItemStack second) {
        return areOreDictIdsEqual(first, second) && areStackAttributesEqual(first, second);
    }

    public static boolean areOreDictIdsEqual(ItemStack first, ItemStack second) {
        int[] firstIds = OreDictionary.getOreIDs(first);
        int[] secondIds = OreDictionary.getOreIDs(second);
        ArrayList<Integer> list = new ArrayList<>(firstIds.length);
        for (int i :
                firstIds) {
            list.add(i);
        }
        for (int i :
                secondIds) {
            if (list.contains(i)) {
                return true;
            }
        }
        return false;
    }

    public static boolean areStackAttributesEqual(ItemStack first, ItemStack second) {
        return ItemStack.areItemStackTagsEqual(first, second);
    }

    public static int getStackHash(ItemStack stack) {
        return stack.getDisplayName().hashCode() << 8 | stack.getCount();
    }

    public static boolean isWand(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemWand;
    }

    public static boolean isEmptyOrNull(ItemStack stack) {
        return stack == null || stack.isEmpty();
    }

    public static boolean areItemsAssignable(ItemStack first, ItemStack second) {
        return !isEmptyOrNull(second) && areItemsAssignable(first, second.getItem());
    }

    public static boolean areItemsAssignable(ItemStack first, @Nonnull Item second) {
        return !isEmptyOrNull(first) && areItemsAssignable(first.getItem(), second);
    }

    public static boolean areItemsAssignable(@Nonnull Item first, @Nonnull Item second) {
        return first.getClass().isAssignableFrom(second.getClass());
    }

    public static @Nonnull
    ItemStack decreaseStackSize(ItemStack stack, int amount) {
        if (stack == null || stack.isEmpty() || stack.getCount() <= amount) {
            return ItemStack.EMPTY;
        } else {
            stack.setCount(stack.getCount() - amount);
            return stack;
        }
    }
}
