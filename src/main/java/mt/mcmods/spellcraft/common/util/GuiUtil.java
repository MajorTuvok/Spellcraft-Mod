package mt.mcmods.spellcraft.common.util;

public final class GuiUtil {
    private GuiUtil() {
    }

    public static boolean isWithinBounds(float x, float y, float lowX, float highX, float lowY, float highY) {
        return ((lowX < highX && lowX <= x && x <= highX) || (lowX >= highX && highX <= x && x <= lowX)) &&
                ((lowY < highY && lowY <= y && y <= highY) || (lowY >= highY && highY <= y && y <= lowY));
    }
}
