package com.mt.mcmods.spellcraft.common.items.armor;

import com.mt.mcmods.spellcraft.common.CTabs;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.interfaces.INamed;
import com.mt.mcmods.spellcraft.common.interfaces.IRenderable;
import com.mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BaseArmor extends ItemArmor implements ILoggable, INamed, IRenderable {
    private String mItemName;

    protected BaseArmor(@Nonnull String itemName, ArmorMaterial armorMaterial, EntityEquipmentSlot type) {
        super(armorMaterial, getRenderIndex(type), type);
        this.mItemName = itemName;
        setUnlocalizedName(mItemName + getArmorName(type));
        setCreativeTab(CTabs.TAB_MAIN);
    }

    protected static String getArmorName(EntityEquipmentSlot type) {
        switch (type) {
            case HEAD:
                return "_helmet";
            case CHEST:
                return "_chestplate";
            case LEGS:
                return "_leggings";
            case FEET:
                return "_boots";
            default:
                Log.warn("Attempted to getWand Armor Name for unidentified Equipment Slot!");
                return "";
        }
    }

    private static int getRenderIndex(EntityEquipmentSlot type) {
        return type.equals(EntityEquipmentSlot.LEGS) ? 1 : 0;
    }

    @Override
    public String getName() {
        return mItemName + getArmorName(armorType);
    }

    /**
     * Called by RenderBiped and RenderPlayer to determine the armor texture that
     * should be use for the currently equipped item.
     * This will only be called on instances of ItemArmor.
     * <p>
     * Returning null from this function will use the default value.
     *
     * @param stack  ItemStack for the equipped armor
     * @param entity The entity wearing the armor
     * @param slot   The slot the armor is in
     * @param type   The subtype, can be null or "overlay"
     * @return Path of texture to bind, or null to use default
     */
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return StringHelper.createResourceLocation(MODID, "textures", "armor",
                StringHelper.createUnlocalizedName(mItemName, "layer", (slot.equals(EntityEquipmentSlot.LEGS) ? "1" : "0"))) + ".png";
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
