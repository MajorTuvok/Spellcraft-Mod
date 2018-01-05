package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.helper.GuiResources;

import static mt.mcmods.spellcraft.common.gui.helper.GuiResources.GUI_BLANK;
import static mt.mcmods.spellcraft.common.gui.helper.GuiResources.SLOT;

public class GUISpellCreator extends BaseGui {
    private static final GuiResources USED_BACKGROUND = GUI_BLANK;

    public GUISpellCreator(GUIContainerSpellCreator inventorySlotsIn) {
        this(inventorySlotsIn, USED_BACKGROUND.getImgXSize(), USED_BACKGROUND.getImgYSize());
    }

    private GUISpellCreator(BaseGuiContainer inventorySlotsIn, int xSize, int ySize) {
        super(inventorySlotsIn, xSize, ySize);
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
        getDelegate().drawGuiBackground(USED_BACKGROUND);
        getDelegate().drawAllSlotsWithResource(SLOT);
    }
}
