package com.mt.mcmods.spellcraft;


public class ServerProxy extends CommonProxy {
    public ServerProxy() {
        super();
    }

    @Override
    public boolean isClient() {
        return false;
    }
}
