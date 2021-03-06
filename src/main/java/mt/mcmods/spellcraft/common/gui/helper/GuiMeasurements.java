package mt.mcmods.spellcraft.common.gui.helper;

public class GuiMeasurements {
    private final int guiLeft;
    private final int guiTop;
    private final int xSize;
    private final int ySize;

    public GuiMeasurements(int xSize, int ySize, int guiLeft, int guiTop) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }

    public boolean equals(int xSize, int ySize, int guiLeft, int guiTop) {

        if (getXSize() != xSize) return false;
        if (getYSize() != ySize) return false;
        if (getGuiLeft() != guiLeft) return false;
        return getGuiTop() == guiTop;
    }

    @Override
    public int hashCode() {
        int result = xSize;
        result = 31 * result + ySize;
        result = 31 * result + getGuiLeft();
        result = 31 * result + getGuiTop();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiMeasurements)) return false;

        GuiMeasurements that = (GuiMeasurements) o;

        if (xSize != that.xSize) return false;
        if (ySize != that.ySize) return false;
        if (getGuiLeft() != that.getGuiLeft()) return false;
        return getGuiTop() == that.getGuiTop();
    }

    @Override
    public String toString() {
        return "GuiMeasurements{" + "xSize=" + xSize +
                ", ySize=" + ySize +
                ", guiLeft=" + guiLeft +
                ", guiTop=" + guiTop +
                '}';
    }
}
