package mt.mcmods.spellcraft;


import mt.mcmods.spellcraft.common.spell.SpellRegistry;
import mt.mcmods.spellcraft.common.spell.entity.EntitySpellRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ServerProxy extends CommonProxy {
    public ServerProxy() {
        super();
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        SpellRegistry.addSpellRegistryCallback(EntitySpellRegistry.ENTITY_SPELL_REGISTRY);
    }
}
