package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.components.ImageButton;
import mt.mcmods.spellcraft.common.gui.helper.GuiResource;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

import static mt.mcmods.spellcraft.common.LocaleKey.GUI_SPELL_CREATOR_EDIT;
import static mt.mcmods.spellcraft.common.LocaleKey.GUI_SPELL_CREATOR_NO_SPELL;
import static mt.mcmods.spellcraft.common.gui.helper.GuiResource.*;
import static mt.mcmods.spellcraft.common.gui.instances.GuiContainerSpellCreator.*;

public class GuiSpellCreator extends BaseGui {
    private static final int ID_EDIT = 0;
    private static final int ID_INSCRIBE = 1;
    private static final GuiResource USED_BACKGROUND = GUI_BLANK;
    private static final int X_EDIT_SPELL = X_INPUT + OFFSETS.getSlotXSize() + 4;  
    private static final int Y_EDIT_SPELL = 10;
    private static final int Y_EDIT_SPELL_ADD = 6;
    private static final int Y_INSCRIBE_SPELL = Y_OUTPUT - Math.round((BOOK_AND_QUILL.getImgYSize() - OFFSETS.getSlotYSize()) / 2);
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
        mButtonEditSpell = new GuiButton(ID_EDIT, getGuiLeft() + X_EDIT_SPELL, getGuiTop() + Y_EDIT_SPELL, GUI_SPELL_CREATOR_EDIT.get(spellName));
        mButtonEditSpell.width = getFontRenderer().getStringWidth(mButtonEditSpell.displayString) + Y_EDIT_SPELL_ADD;
        mButtonInscribeSpell = new ImageButton(ID_INSCRIBE, getXSize(), Y_INSCRIBE_SPELL, BOOK_AND_QUILL, getDelegate());
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
