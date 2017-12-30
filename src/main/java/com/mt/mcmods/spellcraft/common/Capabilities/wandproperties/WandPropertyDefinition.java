package com.mt.mcmods.spellcraft.common.Capabilities.wandproperties;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;

public class WandPropertyDefinition implements IWandPropertyDefinition {
    public static final float DEFAULT_MIN_EFFICIENCY = 85;
    public static final float DEFAULT_MAX_EFFICIENCY = 95;
    public static final float DEFAULT_MIN_MAX_POWER = 45;
    public static final float DEFAULT_MAX_MAX_POWER = 55;
    public static final float DEFAULT_PERFECT_BORDER_PERCENTAGE = 90;
    private final float maxEfficiency;
    private final float minEfficiency;
    private final float maxMaxPower;
    private final float minMaxPower;
    private final float perfectEfficiencyBorder;
    private final float perfectMaxPowerBorder;

    public WandPropertyDefinition() {
        this(DEFAULT_MAX_EFFICIENCY, DEFAULT_MIN_EFFICIENCY, DEFAULT_MAX_MAX_POWER, DEFAULT_MIN_MAX_POWER);
        ILoggable.Log.warn("Created WandPropertyDefinition from default values! This is probably not what you want!");
    }

    public WandPropertyDefinition(float maxEfficiency, float minEfficiency, float maxMaxPower, float minMaxPower) {
        this.maxEfficiency = maxEfficiency;
        this.minEfficiency = minEfficiency;
        this.maxMaxPower = maxMaxPower;
        this.minMaxPower = minMaxPower;
        this.perfectEfficiencyBorder = getEfficiencyRange() * DEFAULT_PERFECT_BORDER_PERCENTAGE / 100 + getMinEfficiency();
        this.perfectMaxPowerBorder = getMaxPowerRange() * DEFAULT_PERFECT_BORDER_PERCENTAGE / 100 + getMinMaxPower();
    }

    public WandPropertyDefinition(float maxEfficiency, float minEfficiency, float maxMaxPower, float minMaxPower, float perfectEfficiencyBorder, float perfectMaxPowerBorder) {
        this.maxEfficiency = maxEfficiency;
        this.minEfficiency = minEfficiency;
        this.maxMaxPower = maxMaxPower;
        this.minMaxPower = minMaxPower;
        this.perfectEfficiencyBorder = perfectEfficiencyBorder;
        this.perfectMaxPowerBorder = perfectMaxPowerBorder;
    }

    @Override
    public float getMaxEfficiency() {
        return maxEfficiency;
    }

    @Override
    public float getMinEfficiency() {
        return minEfficiency;
    }

    @Override
    public float getMaxMaxPower() {
        return maxMaxPower;
    }

    @Override
    public float getMinMaxPower() {
        return minMaxPower;
    }

    @Override
    public float getEfficiencyRange() {
        return Math.abs(getMaxEfficiency() - getMinEfficiency());
    }

    @Override
    public float getMaxPowerRange() {
        return Math.abs(getMaxMaxPower() - getMinMaxPower());
    }

    @Override
    public float getPerfectEfficiencyBorder() {
        return perfectEfficiencyBorder;
    }

    @Override
    public float getPerfectMaxPowerBorder() {
        return perfectMaxPowerBorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WandPropertyDefinition)) return false;

        WandPropertyDefinition that = (WandPropertyDefinition) o;

        if (Float.compare(that.getMaxEfficiency(), getMaxEfficiency()) != 0) return false;
        if (Float.compare(that.getMinEfficiency(), getMinEfficiency()) != 0) return false;
        if (Float.compare(that.getMaxMaxPower(), getMaxMaxPower()) != 0) return false;
        if (Float.compare(that.getMinMaxPower(), getMinMaxPower()) != 0) return false;
        if (Float.compare(that.getPerfectEfficiencyBorder(), getPerfectEfficiencyBorder()) != 0) return false;
        return Float.compare(that.getPerfectMaxPowerBorder(), getPerfectMaxPowerBorder()) == 0;
    }

    @Override
    public int hashCode() {
        int result = (getMaxEfficiency() != +0.0f ? Float.floatToIntBits(getMaxEfficiency()) : 0);
        result = 31 * result + (getMinEfficiency() != +0.0f ? Float.floatToIntBits(getMinEfficiency()) : 0);
        result = 31 * result + (getMaxMaxPower() != +0.0f ? Float.floatToIntBits(getMaxMaxPower()) : 0);
        result = 31 * result + (getMinMaxPower() != +0.0f ? Float.floatToIntBits(getMinMaxPower()) : 0);
        result = 31 * result + (getPerfectEfficiencyBorder() != +0.0f ? Float.floatToIntBits(getPerfectEfficiencyBorder()) : 0);
        result = 31 * result + (getPerfectMaxPowerBorder() != +0.0f ? Float.floatToIntBits(getPerfectMaxPowerBorder()) : 0);
        return result;
    }
}
