package mt.mcmods.spellcraft.common.Capabilities;


import mt.mcmods.spellcraft.common.Capabilities.spellpower.ISpellPowerProvider;
import mt.mcmods.spellcraft.common.Capabilities.spellpower.SpellPowerProviderCapability;
import mt.mcmods.spellcraft.common.Capabilities.wandproperties.IWandProperties;
import mt.mcmods.spellcraft.common.Capabilities.wandproperties.WandPropertiesCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.items.IItemHandler;

public class SpellcraftCapabilities {
    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    @CapabilityInject(ISpellPowerProvider.class)
    public static Capability<ISpellPowerProvider> SPELL_POWER_PROVIDER_CAPABILITY = null;

    @CapabilityInject(IWandProperties.class)
    public static Capability<IWandProperties> WAND_PROPERTIES_CAPABILITY = null;

    public static void registerCapabilities() {
        SpellPowerProviderCapability spellPowerProviderCapability = new SpellPowerProviderCapability();
        WandPropertiesCapability wandPropertiesCapability = new WandPropertiesCapability();
        CapabilityManager.INSTANCE.register(ISpellPowerProvider.class, spellPowerProviderCapability, spellPowerProviderCapability);
        CapabilityManager.INSTANCE.register(IWandProperties.class, wandPropertiesCapability, wandPropertiesCapability);
    }
}
