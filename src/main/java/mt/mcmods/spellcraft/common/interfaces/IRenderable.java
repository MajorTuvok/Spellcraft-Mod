package mt.mcmods.spellcraft.common.interfaces;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public interface IRenderable {
    //this Interface just states, that the render process can be done just as with any item or Block
    public @Nullable
    ResourceLocation getLocation();

    public boolean registerRenderer();
}
