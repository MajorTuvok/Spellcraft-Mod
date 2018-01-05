package mt.mcmods.spellcraft.common.gui.helper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum GuiID {
    GUI_VOID(0),
    GUI_WAND_CRAFTING_TABLE(1),
    GUI_SPELL_CREATOR(2),
    GUI_SPELL_CREATION(3);
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

