package mt.mcmods.spellcraft.common.registry;

import mt.mcmods.spellcraft.common.RegistryUtils;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NotThreadSafe
public class WandRegistry {
    public static final WandRegistry INSTANCE = new WandRegistry();
    private RegistryAdvanced<WandRecipe, ItemWand> recipeWandMap;
    private RegistryAdvanced<ItemWand, WandRecipe> wandRecipeMap;

    public WandRegistry() {
        recipeWandMap = new RegistryAdvanced<>();
        wandRecipeMap = new RegistryAdvanced<>();
    }

    public void onRegister(RegistryUtils<Item> utils) {
        for (ItemWand wand :
                wandRecipeMap.getKeySet()) {
            utils.register(wand);
        }
    }

    public void addWand(@Nonnull ItemStack tip, @Nonnull ItemStack core, @Nonnull ItemWand wand) {
        WandRecipe recipe = new WandRecipe(tip, core);
        recipeWandMap.putObject(recipe, wand);
        wandRecipeMap.putObject(wand, recipe);
    }

    public boolean hasWand(@Nonnull ItemStack tip, @Nonnull ItemStack core) {
        if (tip.isEmpty() || core.isEmpty()) {
            return false;
        } else {
            WandRecipe recipe = new WandRecipe(tip, core);
            boolean res = recipeWandMap.containsKey(recipe);
            return res;
        }
    }

    public boolean hasRecipe(@Nonnull ItemWand wand) {
        return wandRecipeMap.containsKey(wand);
    }

    public @Nullable
    ItemWand getWand(@Nonnull ItemStack tip, @Nonnull ItemStack core) {
        if (tip.isEmpty() || core.isEmpty()) {
            return null;
        } else {
            WandRecipe recipe = new WandRecipe(tip, core);
            return recipeWandMap.getObject(recipe);
        }
    }

    public @Nullable
    WandRecipe getRecipe(@Nonnull ItemWand wand) {
        return wandRecipeMap.getObject(wand);
    }

    public Set<Map.Entry<WandRecipe, ItemWand>> getRecipeWandEntries() {
        return recipeWandMap.getEntrySet();
    }

    public Set<Map.Entry<ItemWand, WandRecipe>> getWandRecipeEntries() {
        return wandRecipeMap.getEntrySet();
    }

    public static class WandRecipe {
        private final ItemStack head;
        private final ItemStack core;

        private WandRecipe(@Nonnull ItemStack head, @Nonnull ItemStack core) {
            this.head = head;
            this.core = core;
            if (this.head.isEmpty() || this.core.isEmpty()) {
                throw new IllegalArgumentException("Wands may only be crafted with non Empty stacks");
            }
        }

        @Override
        public int hashCode() {
            int result = getHead().getItem().hashCode(); //direct call possible, because Stacks used may not be empty
            result = 31 * result + getCore().getItem().hashCode();
            return result;
        }

        public ItemStack getHead() {
            return head;
        }

        public ItemStack getCore() {
            return core;
        }

        /**
         * This Equals Method compares the two Recipes by checking whether the Items in both WandRecipes (checking whether it is a WandRecipe first) are equal.
         * It does not take StackSizes into account.
         *
         * @param obj the reference object with which to compare.
         * @return {@code true} if this object is the same as the obj
         * argument; {@code false} otherwise.
         * @see #hashCode()
         * @see HashMap
         */
        @Override
        public boolean equals(Object obj) {
            boolean res = false;
            if (obj instanceof WandRecipe) {
                WandRecipe recipe = (WandRecipe) obj;
                //using equals to ensure compatibility if they ever remove there Item-Singleton principle (Object.equals is effectively the same as ==)
                res = recipe.getCore().getItem().equals(this.getCore().getItem()) && recipe.getHead().getItem().equals(this.getHead().getItem());
            }
            return res;
        }
    }
}
