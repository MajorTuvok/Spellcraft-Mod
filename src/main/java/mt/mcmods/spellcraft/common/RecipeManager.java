package mt.mcmods.spellcraft.common;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.items.smeltable.SmeltingConstants;

public class RecipeManager implements ILoggable, SmeltingConstants {

    public static void registerRecipes() {
        Log.info("Starting Recipe-Registration");
        //ingots and so on
        //tools
        //armor
        Log.info("successfully registered Recipes");
    }

    public static void registerSmelting() {
        Log.info("Starting smelting Recipe-Registration");
        Log.info("successfully registered smelting Recipes");
    }
}
