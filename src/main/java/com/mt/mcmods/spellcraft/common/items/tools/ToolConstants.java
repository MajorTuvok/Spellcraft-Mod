package com.mt.mcmods.spellcraft.common.items.tools;

public interface ToolConstants {
    public static final float AXE_DAMAGE_MODIFIER = 5;
    public static final float SWORD_DAMAGE_MODIFIER = 4;
    public static final float PICKAXE_DAMAGE_MODIFIER = 1.0f;
    public static final float SHOVEL_DAMAGE_MODIFIER = 1.5f;
    public static final float HOE_DAMAGE_MODIFIER = 0;

    public static final float AXE_VANILLA_SPEED = -3.0f;
    public static final float SWORD_VANILLA_SPEED = -2.4000000953674316f;
    public static final float PICKAXE_VANILLA_SPEED = -2.8f;
    public static final float SHOVEL_VANILLA_SPEED = -3.0f;
    public static final float HOE_VANILLA_SPEED = -3; //the hoe attack speed is calculated from the damage vs. Entity...
    //damage vs. Entity -3 equals hoe speed - damage is always 1!
}
