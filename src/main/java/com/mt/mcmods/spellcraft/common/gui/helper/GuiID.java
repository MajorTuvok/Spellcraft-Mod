package com.mt.mcmods.spellcraft.common.gui.helper;

import javax.annotation.Nullable;

public enum GuiID {
    GUIWandCraftingTable;

    public static @Nullable GuiID getFromId(int id) {
        if (hasGuiIDForId(id)) {
            return values()[id];
        }
        return null;
    }

    public static boolean hasGuiIDForId(int id) {
        return id>=0 && id<values().length;
    }
}

