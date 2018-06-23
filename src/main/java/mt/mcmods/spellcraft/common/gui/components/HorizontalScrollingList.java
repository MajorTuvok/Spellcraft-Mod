package mt.mcmods.spellcraft.common.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class HorizontalScrollingList extends AbsScrollingList {

    public HorizontalScrollingList(Minecraft client, int width, int height, int top, int left, int entrySize) {
        super(ScrollingOrientation.HORIZONTAL, client, width, height, top, left, entrySize, height - DEFAULT_SCROLLBAR_SIZE - 1);
    }


    @Override
    protected int getContentHeight() {
        return 0;
    }

    @Override
    public boolean isMouseOnSlot(int index) {
        return false;
    }

    @Override
    protected float getMaxScrollDistance() {
        return 0;
    }

    @Override
    protected void onPreDraw(int mouseX, int mouseY) {

    }

    @Override
    protected void performDrawAdditionalBackground(Tessellator tess) {

    }

    @Override
    public void scrollDown() {

    }

    @Override
    public void scrollUp() {

    }

    @Override
    protected void performDraw(Tessellator tess) {

    }

    @Override
    protected void onHandleMouse() {

    }

    @Override
    protected void updateSliderPos() {

    }
}
