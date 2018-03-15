package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import net.minecraft.client.Minecraft;

import static mt.mcmods.spellcraft.common.gui.helper.GuiResource.GUI_BLANK_MAX;

public class GuiSpellCreation extends BaseGui {
    private GuiSpellCreator mCreator;

    public GuiSpellCreation(GuiSpellCreator creator) {
        super(new GuiContainerSpellCreation((GuiContainerSpellCreator) creator.inventorySlots), GUI_BLANK_MAX.getImgXSize(), GUI_BLANK_MAX.getImgYSize());
        mCreator = creator;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
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
        getDelegate().drawGuiBackground(GUI_BLANK_MAX);
    }

    @Override
    protected void onShouldCloseScreen() {
        Minecraft.getMinecraft().displayGuiScreen(mCreator);
    }

    @Override
    protected void createInventoryViews(BaseGuiContainer inventorySlotsIn) {

    }
}
