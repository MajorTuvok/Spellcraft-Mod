package mt.mcmods.spellcraft.common.config;

import mt.mcmods.spellcraft.SpellcraftMod;
import net.minecraftforge.common.config.Config.Ignore;

@net.minecraftforge.common.config.Config(modid = SpellcraftMod.MODID, category = Config.CATEGORY_ROOT)
public class Config {
    @Ignore
    static final String CATEGORY_ROOT = "general";

}
