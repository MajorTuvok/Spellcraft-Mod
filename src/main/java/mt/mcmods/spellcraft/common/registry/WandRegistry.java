package mt.mcmods.spellcraft.common.registry;

import mt.mcmods.spellcraft.common.RegistryUtils;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public void addWand(@Nonnull ItemStack first, @Nonnull ItemStack second, @Nonnull ItemWand wand) {
        WandRecipe recipe = new WandRecipe(first, second);
        recipeWandMap.putObject(recipe, wand);
        wandRecipeMap.putObject(wand, recipe);
    }

    public boolean hasWand(@Nonnull ItemStack first, @Nonnull ItemStack second) {
        if (first.isEmpty() || second.isEmpty()) {
            return false;
        } else {
            WandRecipe recipe = new WandRecipe(first, second);
            boolean res = recipeWandMap.containsKey(recipe);
            return res;
        }
    }

    public boolean hasRecipe(@Nonnull ItemWand wand) {
        return wandRecipeMap.containsKey(wand);
    }

    public @Nullable
    ItemWand getWand(@Nonnull ItemStack first, @Nonnull ItemStack second) {
        if (first.isEmpty() || second.isEmpty()) {
            return null;
        } else {
            WandRecipe recipe = new WandRecipe(first, second);
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
            int result = getHead().isEmpty() ? 1 : getHead().getItem().hashCode();
            result = 31 * result + (getCore().isEmpty() ? 1 : getCore().getItem().hashCode());
            return result;
        }

        public ItemStack getHead() {
            return head;
        }

        public ItemStack getCore() {
            return core;
        }

        /**
         * Indicates whether some other object is "equal to" this one.
         * <p>
         * The {@code equals} method implements an equivalence relation
         * on non-null object references:
         * <ul>
         * <li>It is <i>reflexive</i>: for any non-null reference value
         * {@code x}, {@code x.equals(x)} should return
         * {@code true}.
         * <li>It is <i>symmetric</i>: for any non-null reference values
         * {@code x} and {@code y}, {@code x.equals(y)}
         * should return {@code true} if and only if
         * {@code y.equals(x)} returns {@code true}.
         * <li>It is <i>transitive</i>: for any non-null reference values
         * {@code x}, {@code y}, and {@code z}, if
         * {@code x.equals(y)} returns {@code true} and
         * {@code y.equals(z)} returns {@code true}, then
         * {@code x.equals(z)} should return {@code true}.
         * <li>It is <i>consistent</i>: for any non-null reference values
         * {@code x} and {@code y}, multiple invocations of
         * {@code x.equals(y)} consistently return {@code true}
         * or consistently return {@code false}, provided no
         * information used in {@code equals} comparisons on the
         * objects is modified.
         * <li>For any non-null reference value {@code x},
         * {@code x.equals(null)} should return {@code false}.
         * </ul>
         * <p>
         * The {@code equals} method for class {@code Object} implements
         * the most discriminating possible equivalence relation on objects;
         * that is, for any non-null reference values {@code x} and
         * {@code y}, this method returns {@code true} if and only
         * if {@code x} and {@code y} refer to the same object
         * ({@code x == y} has the value {@code true}).
         * <p>
         * Note that it is generally necessary to override the {@code hashCode}
         * method whenever this method is overridden, so as to maintain the
         * general contract for the {@code hashCode} method, which states
         * that equal objects must have equal hash codes.
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
                res = recipe.getCore().getItem() == this.getCore().getItem() && recipe.getHead().getItem() == this.getHead().getItem();
            }
            return res;
        }
    }
}
