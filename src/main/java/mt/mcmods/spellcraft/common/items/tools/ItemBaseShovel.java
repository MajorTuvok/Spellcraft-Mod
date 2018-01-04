package mt.mcmods.spellcraft.common.items.tools;

import mt.mcmods.spellcraft.common.CTabs;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.interfaces.INamed;
import mt.mcmods.spellcraft.common.interfaces.IRenderable;
import net.minecraft.item.ItemSpade;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ItemBaseShovel extends ItemSpade implements INamed, ILoggable, IRenderable {
    private String mName;

    public ItemBaseShovel(ToolMaterial material, String name) {
        super(material);
        this.mName = name;
        setCreativeTab(CTabs.TAB_MAIN);
        setUnlocalizedName(name);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Nullable
    @Override
    public ResourceLocation getLocation() {
        return null;
    }

    @Override
    public boolean registerRenderer() {
        return true;
    }
}
