package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;

import static mt.mcmods.spellcraft.common.gui.helper.GuiResource.GUI_BLANK_WPI;

public final class GuiVoid extends BaseGui {

    public GuiVoid(BaseGuiContainer inventorySlotsIn) {
        super(inventorySlotsIn, GUI_BLANK_WPI.getImgXSize(), GUI_BLANK_WPI.getImgYSize());
    }

    /**
     * Draws the background layer of this container (behind the items).
     *
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        getDelegate().drawGuiBackground(GUI_BLANK_WPI);
    }
}
