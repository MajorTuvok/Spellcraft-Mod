package mt.mcmods.spellcraft.common.interfaces;

import net.minecraft.client.renderer.color.IItemColor;

import javax.annotation.Nonnull;

public interface IItemColorable {
    public @Nonnull
    IItemColor getItemColor();
}
