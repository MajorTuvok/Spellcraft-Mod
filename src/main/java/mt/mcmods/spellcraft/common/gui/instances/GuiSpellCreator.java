package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.components.ImageButton;
import mt.mcmods.spellcraft.common.gui.helper.GuiResources;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

import static mt.mcmods.spellcraft.common.LocaleKey.GUI_SPELL_CREATOR_EDIT;
import static mt.mcmods.spellcraft.common.LocaleKey.GUI_SPELL_CREATOR_NO_SPELL;
import static mt.mcmods.spellcraft.common.gui.helper.GuiResources.*;
import static mt.mcmods.spellcraft.common.gui.instances.GuiContainerSpellCreator.*;

public class GuiSpellCreator extends BaseGui {
    private static final int ID_EDIT = 0;
    private static final int ID_INSCRIBE = 1;
    private static final GuiResources USED_BACKGROUND = GUI_BLANK;
    private static final int X_SPELL_NAME = X_INPUT + OFFSETS.getSlotXSize() + 4;
    private static final int Y_INSCRIBE = Y_OUTPUT - Math.round((BOOK_AND_QUILL.getImgYSize() - OFFSETS.getSlotYSize()) / 2);
    private static final int Y_SPELL_NAME = 10;
    private GuiButton mButtonEditSpell;
    private GuiButton mButtonInscribeSpell;
    public GuiSpellCreator(GuiContainerSpellCreator inventorySlotsIn) {
        super(inventorySlotsIn, USED_BACKGROUND.getImgXSize(), USED_BACKGROUND.getImgYSize());
        mButtonEditSpell = null;
        mButtonInscribeSpell = null;
    }

    @Override
    public TileEntitySpellCreator getTileEntity() {
        return (TileEntitySpellCreator) super.getTileEntity();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resize's, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        super.initGui();
        String spellName = GUI_SPELL_CREATOR_NO_SPELL.get();
        mButtonEditSpell = new GuiButton(ID_EDIT, getGuiLeft() + X_SPELL_NAME, getGuiTop() + Y_SPELL_NAME, GUI_SPELL_CREATOR_EDIT.get(spellName));
        mButtonEditSpell.width = getFontRenderer().getStringWidth(mButtonEditSpell.displayString) + 6;
        mButtonInscribeSpell = new ImageButton(ID_INSCRIBE, getXSize(), Y_INSCRIBE, BOOK_AND_QUILL, getDelegate());
        //Log.info("Coords: e{"+mButtonEditSpell.x+", "+mButtonEditSpell.y+", "+mButtonEditSpell.width+", "+mButtonEditSpell.height);
        addButton(mButtonEditSpell);
        addButton(mButtonInscribeSpell);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     *
     * @param button
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case ID_EDIT:
                Minecraft.getMinecraft().displayGuiScreen(new GuiSpellCreation(new GuiContainerSpellCreation((GuiContainerSpellCreator) inventorySlots)));
                break;
            case ID_INSCRIBE:
                Log.info("Inscribing...");
                break;
            default: {
                Log.warn("Unknown id of " + button.id + "!");
            }
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
