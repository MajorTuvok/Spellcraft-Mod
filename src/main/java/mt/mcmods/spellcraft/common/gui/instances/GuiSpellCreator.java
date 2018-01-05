package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.helper.GuiResources;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

import static mt.mcmods.spellcraft.common.gui.helper.GuiResources.GUI_BLANK;
import static mt.mcmods.spellcraft.common.gui.helper.GuiResources.SLOT;

public class GuiSpellCreator extends BaseGui {
    private static final GuiResources USED_BACKGROUND = GUI_BLANK;

    public GuiSpellCreator(GuiContainerSpellCreator inventorySlotsIn) {
        super(inventorySlotsIn, USED_BACKGROUND.getImgXSize(), USED_BACKGROUND.getImgYSize());
    }

    @Override
    public TileEntitySpellCreator getTileEntity() {
        return (TileEntitySpellCreator) super.getTileEntity();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        super.initGui();
        addButton(new GuiButton(0, getGuiLeft() + 42, getGuiTop() + 10, "Button Test"));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     *
     * @param button
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiSpellCreation(new GuiContainerSpellCreation((GuiContainerSpellCreator) inventorySlots)));
        }
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
