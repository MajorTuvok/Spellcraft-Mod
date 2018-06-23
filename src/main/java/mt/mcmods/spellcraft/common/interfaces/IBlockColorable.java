package mt.mcmods.spellcraft.common.interfaces;

import net.minecraft.client.renderer.color.IBlockColor;

import javax.annotation.Nonnull;

public interface IBlockColorable {
    @Nonnull
    public IBlockColor getBlockColor();
}
