package com.mt.mcmods.spellcraft.common;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

import static com.mt.mcmods.spellcraft.common.ConfigurationManager.ToolsConfig.*;

//TODO update config!!!
public class ConfigurationManager implements ILoggable {
    private static final String MAIN_CONFIG_NAME = "main.cfg";
    private static final String TOOLS_CONFIG_NAME = "tools.cfg";
    private Configuration mMainConfig;
    private Configuration mToolsConfig;
    private ToolsConfig mTools;

    public ConfigurationManager() {
    }

    public void register(FMLPreInitializationEvent e) {
        Log.info("Loading Configs");
        File suggestedPath = e.getSuggestedConfigurationFile();
        Log.trace("Suggested Config is at: +" + suggestedPath.getAbsolutePath());
        File path = resolveConfigPath(suggestedPath);
        Log.trace("Using Config path: +" + path.getAbsolutePath());
        if (!path.exists()) {
            Log.trace(path.mkdirs() ? "Constructed Config Path" : "Failed to construct Config Path");
        }
        mMainConfig = new Configuration(new File(path, MAIN_CONFIG_NAME));
        mToolsConfig = new Configuration(new File(path, TOOLS_CONFIG_NAME));
        syncConfig();
        Log.debug("Finished initialising configuration");
    }

    public ToolsConfig getToolsConfig() {
        return mTools;
    }

    private void syncConfig() {
        try {
            mMainConfig.load();
            syncMain();
        } catch (Exception e) {
            Log.error("Failed to load Main Config File", e);
        } finally {
            if (mMainConfig.hasChanged()) mMainConfig.save();
        }
        try {
            mToolsConfig.load();
            syncTools();
        } catch (Exception e) {
            Log.error("Failed to load Tools Config File", e);
        } finally {
            if (mToolsConfig.hasChanged()) mToolsConfig.save();
        }
    }

    private void syncMain() {

    }

    private void syncTools() {
        mTools = new ToolsConfig();
        mTools.mmEnergizedOsmiumHarvestLevel = mToolsConfig.getInt(VALUE_HARVEST_LEVEL, CATEGORY_ENERGIZED_OSMIUM, 4, 0, Integer.MAX_VALUE, COMMENT_HARVEST_LEVEL);
        mTools.mmEnergizedOsmiumBaseUses = mToolsConfig.getInt(VALUE_BASE_USES, CATEGORY_ENERGIZED_OSMIUM, 755, 0, Integer.MAX_VALUE, COMMENT_BASE_USES);
        mTools.mmEnergizedOsmiumEnchantability = mToolsConfig.getInt(VALUE_ENCHANTABILITY, CATEGORY_ENERGIZED_OSMIUM, 50, 0, Integer.MAX_VALUE, COMMENT_ENCHANTABILITY);
        mTools.mmEnergizedOsmiumBaseDamage = mToolsConfig.getFloat(VALUE_BASE_DAMAGE, CATEGORY_ENERGIZED_OSMIUM, 5, 0, Float.MAX_VALUE, COMMENT_BASE_DAMAGE);
        mTools.mmEnergizedOsmiumEfficiency = mToolsConfig.getFloat(VALUE_EFFICIENCY, CATEGORY_ENERGIZED_OSMIUM, 15, 0, Float.MAX_VALUE, COMMENT_EFFICIENCY);
        mTools.mmEnergizedOsmiumArmorEnchantability = mToolsConfig.getInt(VALUE_ARMOR_ENCHANTABILITY, CATEGORY_ENERGIZED_OSMIUM, 50, 0, Integer.MAX_VALUE, COMMENT_ENCHANTABILITY);
        mTools.mmEnergizedOsmiumArmorBaseDurability = mToolsConfig.getInt(VALUE_ARMOR_BASE_DURABILITY, CATEGORY_ENERGIZED_OSMIUM, 50, 0, Integer.MAX_VALUE, COMMENT_BASE_DURABILITY);
        mTools.mmEnergizedOsmiumArmorEnchantability = mToolsConfig.getInt(VALUE_ARMOR_ENCHANTABILITY, CATEGORY_ENERGIZED_OSMIUM, 50, 0, Integer.MAX_VALUE, COMMENT_ENCHANTABILITY);
        mTools.mmEnergizedOsmiumArmorToughness = mToolsConfig.getFloat(VALUE_ARMOR_TOUGHNESS, CATEGORY_ENERGIZED_OSMIUM, 5, 0, 20, COMMENT_TOUGHNESS);
        mTools.setEnergizedOsmiumArmorBaseDamageReduction(mToolsConfig.getStringList(VALUE_ARMOR_DAMAGE_REDUCTION, CATEGORY_ENERGIZED_OSMIUM, new String[]{"6", "12", "14", "8"}, COMMENT_DAMAGE_REDUCTION));
    }

    private File resolveConfigPath(File suggestedPath) {
        return new File(suggestedPath.getAbsolutePath().replaceFirst(suggestedPath.getName(), MODID + File.pathSeparator).replaceAll(";", ""));
    }

    public static class ToolsConfig {
        static final String COMMENT_HARVEST_LEVEL = "This is the Harvest Level, determining the type of blocks which can be mined with this. 0=wood level, 3=diamond level";
        static final String COMMENT_BASE_USES = "This is how often a Tool can be used until it breaks.";
        static final String COMMENT_ENCHANTABILITY = "Enchantability Factor determines the number (amd how good they are) of Enchantments, which can be put on this item";
        static final String COMMENT_EFFICIENCY = "The Efficiency value for this Tool. Diamond has a value of 8.";
        static final String COMMENT_BASE_DAMAGE = "Remember, this is just the Base Damage. Swords will add 3 additional Damage to this(axes 5). Diamond has value of 3.";
        static final String COMMENT_DAMAGE_REDUCTION = "Damage Reduction as ints in order:  boots, leggings, chestplate, helmet. For example Diamond has values of 3,6,8,3.";
        static final String COMMENT_BASE_DURABILITY = "The Basic Durability value for armor of this Type. Exact Values are calculated as: \n" +
                "durability * 11 for the helmet \n" +
                "durability * 16 for the chestplate \n" +
                "durability * 15 for the leggings \n" +
                "durability * 13 for the boots. \nAs a Reference: Diamond has a value of 33, iron of 15.";
        static final String COMMENT_TOUGHNESS = "Armor Toughness determines a resistance factor which influences how well it shields against armor bypassing attacks";
        static final String CATEGORY_ENERGIZED_OSMIUM = "energized_osmium";
        static final String VALUE_HARVEST_LEVEL = "harvest level";
        static final String VALUE_BASE_USES = "base uses";
        static final String VALUE_ENCHANTABILITY = "enchantability";
        static final String VALUE_BASE_DAMAGE = "base damage";
        static final String VALUE_EFFICIENCY = "efficiency";
        static final String VALUE_ARMOR_ENCHANTABILITY = "armor enchantability";
        static final String VALUE_ARMOR_DAMAGE_REDUCTION = "armor damage reduction";
        static final String VALUE_ARMOR_BASE_DURABILITY = "armor base durability";
        static final String VALUE_ARMOR_TOUGHNESS = "armor toughness";

        private int mmEnergizedOsmiumHarvestLevel;
        private int mmEnergizedOsmiumBaseUses;
        private int mmEnergizedOsmiumEnchantability;
        private float mmEnergizedOsmiumBaseDamage;
        private float mmEnergizedOsmiumEfficiency;

        private int mmEnergizedOsmiumArmorEnchantability;
        private int mmEnergizedOsmiumArmorBaseDurability;
        private int[] mmEnergizedOsmiumArmorBaseDamageReduction;
        private float mmEnergizedOsmiumArmorToughness;

        public ToolsConfig() {
        }

        public int getEnergizedOsmiumHarvestLevel() {
            return mmEnergizedOsmiumHarvestLevel;
        }

        public int getEnergizedOsmiumBaseUses() {
            return mmEnergizedOsmiumBaseUses;
        }

        public int getEnergizedOsmiumEnchantability() {
            return mmEnergizedOsmiumEnchantability;
        }

        public float getEnergizedOsmiumBaseDamage() {
            return mmEnergizedOsmiumBaseDamage;
        }

        public float getEnergizedOsmiumEfficiency() {
            return mmEnergizedOsmiumEfficiency;
        }

        public int getEnergizedOsmiumArmorEnchantability() {
            return mmEnergizedOsmiumArmorEnchantability;
        }

        public int getEnergizedOsmiumArmorBaseDurability() {
            return mmEnergizedOsmiumArmorBaseDurability;
        }

        public int[] getEnergizedOsmiumArmorBaseDamageReduction() {
            return mmEnergizedOsmiumArmorBaseDamageReduction;
        }

        void setEnergizedOsmiumArmorBaseDamageReduction(String[] energizedOsmiumArmorBaseDamageReduction) {
            int[] baseReduction = new int[4];
            int count = 0;
            for (String s :
                    energizedOsmiumArmorBaseDamageReduction) {
                if (count > 3) {
                    Log.warn("Malformed Config File: attempted to use to much values for EnergizedOsmiumDamageReduction, ignoring rest.");
                    break;
                }
                try {
                    baseReduction[count] = Integer.parseInt(s);
                    if (baseReduction[count] < 0) {
                        Log.warn("Malformed Config File: attempted to use int EnergizedOsmiumDamageReduction at index " + count + " below 0. Using Value 0.");
                        baseReduction[count] = 0;
                    }
                } catch (NumberFormatException e) {
                    Log.error("Malformed Config File: Unable to parse int from EnergizedOsmiumDamageReduction at index " + count + ". Using value 0.", e);
                    baseReduction[count] = 0;
                }
                count++;
            }
            if (count < 4) {
                Log.warn("Malformed Config File: attempted to use not enough values for EnergizedOsmiumDamageReduction setting Rest to 0.");
                for (; count < 4; count++) {
                    baseReduction[count] = 0;
                }
            }
            this.mmEnergizedOsmiumArmorBaseDamageReduction = baseReduction;
        }

        public float getEnergizedOsmiumArmorToughness() {
            return mmEnergizedOsmiumArmorToughness;
        }
    }
}
