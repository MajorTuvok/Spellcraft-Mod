package com.mt.mcmods.spellcraft.common.items.tools;

import com.mt.mcmods.spellcraft.common.CTabs;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.interfaces.INamed;
import com.mt.mcmods.spellcraft.common.interfaces.IRenderable;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ItemBaseAxe extends ItemAxe implements ILoggable, INamed, IRenderable, ToolConstants {
    private String mName;

    public ItemBaseAxe(ToolMaterial material, String itemName) {
        super(material, material.getAttackDamage() + AXE_DAMAGE_MODIFIER, AXE_VANILLA_SPEED); //vanilla mc uses these values
        this.setCreativeTab(CTabs.TAB_MAIN);
        mName = itemName;
        this.setUnlocalizedName(mName);
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
