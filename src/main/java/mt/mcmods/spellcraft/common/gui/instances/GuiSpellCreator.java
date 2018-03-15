package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.components.ButtonAdapterComponent;
import mt.mcmods.spellcraft.common.gui.components.ImageButton;
import mt.mcmods.spellcraft.common.gui.helper.GuiResource;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;
import java.io.IOException;

import static mt.mcmods.spellcraft.common.LocaleKey.*;
import static mt.mcmods.spellcraft.common.gui.helper.GuiResource.*;
import static mt.mcmods.spellcraft.common.gui.instances.GuiContainerSpellCreator.*;

public class GuiSpellCreator extends BaseGui {
    private static final int ID_EDIT = 0;
    private static final int ID_INSCRIBE = 1;
    private static final float SCALE_INFO_MAX_POWER = 0.4f;
    private static final GuiResource USED_BACKGROUND = GUI_BLANK;
    private static final int X_EDIT_SPELL = X_INPUT + OFFSETS.getSlotXSize() + 4;
    private static final int X_INFO_MAX_POWER = X_INPUT + 4;
    private static final int Y_EDIT_SPELL = 10;
    private static final int Y_EDIT_SPELL_ADD = 6;
    private static final int Y_INFO_MAX_POWER = Y_OUTPUT;
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

    private String getSpellName() {
        return GUI_SPELL_CREATOR_NO_SPELL_NAME.get();
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
        showOverallSpellInformation();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resize's, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        super.initGui();
        String spellName = getSpellName();
        mButtonEditSpell = new GuiButton(ID_EDIT, getGuiLeft() + X_EDIT_SPELL, getGuiTop() + Y_EDIT_SPELL, GUI_SPELL_CREATOR_EDIT.get(spellName));
        mButtonEditSpell.width = getFontRenderer().getStringWidth(mButtonEditSpell.displayString) + Y_EDIT_SPELL_ADD;
        mButtonInscribeSpell = new ImageButton(ID_INSCRIBE, getXSize(), Y_INSCRIBE_SPELL, BOOK_AND_QUILL, getDelegate());
        ButtonAdapterComponent editSpellAdapter = new ButtonAdapterComponent(mButtonEditSpell);
        ButtonAdapterComponent inscribeSpellAdapter = new ButtonAdapterComponent(mButtonInscribeSpell);
        addComponent(editSpellAdapter);
        addComponent(inscribeSpellAdapter);
    }

    private String getMaxPowerInfo() {
        return GUI_SPELL_CREATOR_MAX_POWER.get(GUI_SPELL_CREATOR_NO_SPELL.get());
    }

    private String getNumStatesInfo() {
        return GUI_SPELL_CREATOR_MAX_POWER.get(GUI_SPELL_CREATOR_NO_SPELL.get());
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
                Minecraft.getMinecraft().displayGuiScreen(new GuiSpellCreation(this));
                break;
            case ID_INSCRIBE:
                Log.info("Inscribing...");
                break;
            default: {
                Log.warn("Unknown id of " + button.id + "!");
            }
        }
    }

    private void showOverallSpellInformation() {
        String maxPower = getMaxPowerInfo();
        String numStates = getNumStatesInfo();
        getDelegate().drawScaledStringTopLeft(maxPower, X_INFO_MAX_POWER, Y_INFO_MAX_POWER, Color.BLACK.getRGB(), SCALE_INFO_MAX_POWER * getScaleFactor());
        getDelegate().drawScaledStringTopLeft(numStates, X_INFO_MAX_POWER, Y_INFO_MAX_POWER + 8, Color.BLACK.getRGB(), SCALE_INFO_MAX_POWER * getScaleFactor());
    }
}
