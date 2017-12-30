package com.mt.mcmods.spellcraft.common;

import com.mt.mcmods.spellcraft.common.items.smeltable.SmeltingConstants;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;

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
