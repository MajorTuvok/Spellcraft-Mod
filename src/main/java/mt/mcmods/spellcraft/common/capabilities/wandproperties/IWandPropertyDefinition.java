package mt.mcmods.spellcraft.common.capabilities.wandproperties;

public interface IWandPropertyDefinition {
    public float getMaxEfficiency();

    public float getMinEfficiency();

    public float getMaxMaxPower();

    public float getMinMaxPower();

    public float getEfficiencyRange();

    public float getMaxPowerRange();

    public float getPerfectEfficiencyBorder();

    public float getPerfectMaxPowerBorder();
}
