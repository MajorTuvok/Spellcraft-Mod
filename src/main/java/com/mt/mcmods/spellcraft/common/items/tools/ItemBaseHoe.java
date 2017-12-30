package com.mt.mcmods.spellcraft.common.items.tools;

import com.mt.mcmods.spellcraft.common.CTabs;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.interfaces.INamed;
import com.mt.mcmods.spellcraft.common.interfaces.IRenderable;
import net.minecraft.item.ItemHoe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ItemBaseHoe extends ItemHoe implements ILoggable, INamed, IRenderable, ToolConstants {
    private String mName;

    public ItemBaseHoe(ToolMaterial material, String name) {
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
