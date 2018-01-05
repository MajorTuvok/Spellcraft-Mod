package mt.mcmods.spellcraft.common.gui.helper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum GuiID {
    GUIVoid(0),
    GUIWandCraftingTable(1),
    GUISpellCreator(2),
    GUISpellCreation(3);
    private static final Map<Integer, GuiID> idMap = new HashMap<>();
    private Integer id;

    static {
        for (GuiID idObj :
                values()) {
            assert !idMap.containsKey(idObj.getId());
            idMap.put(idObj.getId(), idObj);
        }
    }

    GuiID(Integer id) {
        this.id = id;
    }

    public static @Nullable
    GuiID getFromId(int id) {
        return idMap.get(id);
    }

    public Integer getId() {
        return id;
    }
}

