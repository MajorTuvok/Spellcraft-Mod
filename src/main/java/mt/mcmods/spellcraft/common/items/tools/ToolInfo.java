package mt.mcmods.spellcraft.common.items.tools;

import javax.annotation.Nonnull;

public enum ToolInfo {
    AXE(5, -3, "axe"),
    SWORD(4, -2.4000000953674316f, ""),
    PICKAXE(1, -2.8f, "pickaxe"),
    SHOVEL(1.5f, -3, "shovel"),
    HOE(0, -3, "");
    private final float mDamageModifier;
    private final String mToolClass;
    private final float mVanillaSpeed;

    ToolInfo(float damageModifier, float vanillaSpeed, String toolClass) {
        mDamageModifier = damageModifier;
        mVanillaSpeed = vanillaSpeed;
        mToolClass = toolClass;
    }

    public float getDamageModifier() {
        return mDamageModifier;
    }

    public float getVanillaSpeed() {
        return mVanillaSpeed;
    }

    public @Nonnull
    String getToolClass() {
        return mToolClass;
    }

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
