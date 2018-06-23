package mt.mcmods.spellcraft.common.util;

import java.util.Collection;
import java.util.List;

public class CollectionHelper {
    public static <T> T getFromIndex(Collection<T> c, int index) {
        if (c instanceof List) {
            return ((List<T>) c).get(index);
        }
        if (index >= 0 && index < c.size()) {
            int count = 0;
            for (T thing :
                    c) {
                if (++count == index) {
                    return thing;
                }
            }
        }
        return null;
    }
}
