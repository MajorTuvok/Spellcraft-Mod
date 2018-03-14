package mt.mcmods.spellcraft.common.recipes;

import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class WandRecipe {
    private final ItemStack mCore;
    private final String mCoreName;
    private final ItemStack mHead;
    private final String mHeadName;

    WandRecipe(@Nonnull ItemStack head, @Nonnull ItemStack core) {
        if (head.isEmpty() || core.isEmpty()) {
            throw new IllegalArgumentException("Wands may only be crafted with non Empty stacks!");
        }
        this.mHead = head;
        this.mCore = core;
        this.mHeadName = StringHelper.getOreIdentityName(head);
        this.mCoreName = StringHelper.getOreIdentityName(core);
        if (mCoreName == null || mHeadName == null) {
            throw new IllegalArgumentException("Wand Registry items must have at least Registry names! (" + head + ", " + core + ")");
        }
    }

    public ItemStack getHead() {
        return mHead;
    }

    public ItemStack getCore() {
        return mCore;
    }

    protected String getHeadName() {
        return mHeadName;
    }

    protected String getCoreName() {
        return mCoreName;
    }

    @Override
    public int hashCode() {
        int result = mHeadName.hashCode(); //direct call possible, because Stacks used may not be empty
        result = 31 * result + mCoreName.hashCode();
        return result;
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
            res = recipe.getCoreName().equals(this.getCoreName()) && recipe.getHeadName().equals(this.getHeadName());
        }
        return res;
    }
}
