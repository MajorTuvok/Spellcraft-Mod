package com.mt.mcmods.spellcraft.common.materials;


import com.mt.mcmods.spellcraft.CommonProxy;

public class Materials {
    private static boolean instantiated = false;
    private static final Materials INSTANCE = new Materials();

    public static Materials getINSTANCE() {
        return INSTANCE;
    }

    private Materials() {
        if (instantiated) throw new AssertionError();
        instantiated = true;
    }

    public void onConfigCreated(CommonProxy proxy) {

    }

}
