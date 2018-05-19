package mt.mcmods.spellcraft.common.gui.components;

import net.minecraft.client.Minecraft;

public class HorizontalScrollingList extends AbsScrollingList {

    public HorizontalScrollingList(Minecraft client, int width, int height, int top, int left, int entrySize) {
        super(client, width, height, top, left, entrySize);
    }

    @Override
    public boolean isMouseOnSlot(int index) {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void scrollDown() {

    }

    @Override
    public void scrollUp() {

    }

    protected void applyScrollLimits() {

    }
}
