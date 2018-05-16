package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.helper.GuiResource;
import mt.mcmods.spellcraft.common.spell.SpellBuilder;
import mt.mcmods.spellcraft.common.spell.types.SpellTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.awt.*;
import java.io.IOException;

public class GuiSpellCreation extends BaseGui {
    private static final float HEIGHT_HEADBAR = 0.1f;
    private static final float HEIGTH_CONTENT = 1 - HEIGHT_HEADBAR;
    private static final GuiResource USED_BACKGROUND = GuiResource.GUI_COLOR;
    private static final float WIDTH_CONDITIONS = 0.25f;
    private static final float WIDTH_NEXT_STATE = 0.15f;
    private static final float WIDTH_EXECUTABLES = 1 - WIDTH_CONDITIONS - WIDTH_NEXT_STATE;
    private GuiSpellCreator mCreator;
    private SpellBuilder mSpellBuilder;
    private GuiSpellStateList mSpellStateList;

    public GuiSpellCreation(GuiSpellCreator creator) {
        super(new GuiContainerSpellCreation((GuiContainerSpellCreator) creator.inventorySlots), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        mCreator = creator;
        try {
            mSpellBuilder = new SpellBuilder(SpellTypes.PLAYER_SPELL_TYPE);
        } catch (InstantiationException e) {
            Log.error("Could not instantiate Spell!", e);
            onShouldCloseScreen();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        mSpellStateList = new GuiSpellStateList(getMc(), 100, 100, getGuiTop(), getGuiLeft(), 10);
    }

    /*
     * Draws the background layer of this container (behind the items).
     *
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        getDelegate().drawGuiBackground(USED_BACKGROUND);
        try {
            mSpellStateList.handleMouseInput(mouseX, mouseY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        mSpellStateList.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void onShouldCloseScreen() {
        Minecraft.getMinecraft().displayGuiScreen(mCreator);
    }

    @Override
    protected void createInventoryViews(BaseGuiContainer inventorySlotsIn) {

    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     *
     * @param button
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        mSpellStateList.actionPerformed(button);
    }

    private void drawTitleBar() {

    }

    private class GuiSpellStateList extends GuiScrollingList {

        public GuiSpellStateList(Minecraft client, int width, int height, int top, int left, int entryHeight) {
            super(client, width, height, top, top + height, left, entryHeight, client.displayWidth, client.displayHeight);
            this.selectedIndex = -1;
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        @Override
        protected int getSize() {
            return mSpellBuilder.getStateCount();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
            if (!doubleClick) {
                this.selectedIndex = index;
            }
        }

        @Override
        protected boolean isSelected(int index) {
            return index == selectedIndex;
        }

        @Override
        protected void drawBackground() {
            getDelegate().drawRectangle(left, top, right, bottom, Color.DARK_GRAY.getRGB());
        }

        @Override
        protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {

        }
    }
}
