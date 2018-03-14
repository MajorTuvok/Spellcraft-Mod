package mt.mcmods.spellcraft.common.recipes;

import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class StaffRecipe extends WandRecipe {
    private final ItemStack mCrystal;
    private final String mCrystalName;

    public StaffRecipe(@Nonnull ItemStack head, @Nonnull ItemStack core, @Nonnull ItemStack crystal) {
        super(head, core);
        if (crystal.isEmpty()) {
            throw new IllegalArgumentException("Staffs may only be crafted with non Empty stacks!");
        }
        this.mCrystal = crystal;
        String crystalName = StringHelper.getOreIdentityName(head);
        if (crystalName == null || crystalName.isEmpty()) {
            throw new IllegalArgumentException("Wand Registry items must have at least Registry names! (" + crystal + ")");
        }
        mCrystalName = crystalName;
    }

    public @Nonnull
    ItemStack getCrystal() {
        return mCrystal;
    }

    public @Nonnull
    String getCrystalName() {
        return mCrystalName;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getCrystalName().hashCode();
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
        boolean res = super.equals(obj);
        if (res && obj instanceof StaffRecipe) {
            StaffRecipe recipe = (StaffRecipe) obj;
            res = recipe.getCrystalName().equals(this.getCrystalName());
        }
        return res;
    }
}
