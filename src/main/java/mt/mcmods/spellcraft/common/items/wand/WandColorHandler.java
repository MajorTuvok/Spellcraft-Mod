package mt.mcmods.spellcraft.common.items.wand;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public enum WandColorHandler implements IItemColor {
    WAND_INSTANCE {
        @Override
        public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {
            switch (tintIndex) {
                case 1:
                    return ((ItemWand) stack.getItem()).getTipColor();
                case 2:
                    return ((ItemWand) stack.getItem()).getCoreColor();
                default:
                    return -1;
            }
        }
    },
    STAFF_INSTANCE {
        @Override
        public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {
            switch (tintIndex) {
                case 1:
                    return ((ItemWand) stack.getItem()).getTipColor();
                case 2:
                    return ((ItemWand) stack.getItem()).getCoreColor();
                default:
                    return -1;
            }
        }
    };

    @Override
    public abstract int colorMultiplier(@Nonnull ItemStack stack, int tintIndex);
}
