package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.components.VerticalScrollingList;
import mt.mcmods.spellcraft.common.gui.helper.GuiResource;
import mt.mcmods.spellcraft.common.spell.entity.PlayerSpellBuilder;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

public class GuiSpellCreation extends BaseGui {
    private static final ResourceLocation BUTTON_IMAGE = new ResourceLocation("textures/gui/widgets.png");
    private static final float HEIGHT_HEADBAR = 10;
    private static final int HEIGHT_ROW = 20;
    private static final float HEIGTH_CONTENT = 100 - HEIGHT_HEADBAR;
    private static final GuiResource USED_BACKGROUND = GuiResource.GUI_COLOR;
    private static final int SIZE = 512;
    private static final float WIDTH_CONDITIONS = 25;
    private static final float WIDTH_NEXT_STATE = 10;
    private static final float WIDTH_STATES = 10;
    private static final float WIDTH_EXECUTABLES = 100 - WIDTH_CONDITIONS - WIDTH_NEXT_STATE - WIDTH_STATES;
    private GuiSpellCreator mCreator;
    private int mHeadBarBorder;
    private GuiSpellStateList mSpellStateList;
    private PlayerSpellBuilder mSpellBuilder;

    public GuiSpellCreation(GuiSpellCreator creator) {
        super(new GuiContainerSpellCreation((GuiContainerSpellCreator) creator.inventorySlots), SIZE, Math.round(SIZE * 2f / 3f));
        mCreator = creator;
        try {
            mSpellBuilder = new PlayerSpellBuilder(getTileEntity().getSpellCompound());
        } catch (InstantiationException e) {
            Log.error("Could not instantiate Spell!", e);
            onShouldCloseScreen();
        }
        mHeadBarBorder = getGuiTop();
    }

    @Override
    public TileEntitySpellCreator getTileEntity() {
        return (TileEntitySpellCreator) super.getTileEntity();
    }

    @Override
    public void initGui() {
        super.initGui();
        mHeadBarBorder = asAbsoluteYPos(HEIGHT_HEADBAR);
        mSpellStateList = new GuiSpellStateList(getMc(), asAbsoluteXDis(WIDTH_STATES), asAbsoluteYDis(HEIGTH_CONTENT), getGuiLeft() + 1, mHeadBarBorder, HEIGHT_ROW);
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
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        mSpellStateList.handleMouseInput(mouseX, mouseY);
        getDelegate().drawGuiBackground(USED_BACKGROUND);
        drawTitleBar();
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
        drawRectangle(getGuiLeft(), getGuiTop(), getGuiLeft() + getXSize(), mHeadBarBorder, 0x99AAAAAA);
        String name = getTileEntity().getSpellName();
        getDelegate().drawScaledStringCentered(name, getGuiLeft() + Math.round(getXSize() / 2), mHeadBarBorder - Math.round((mHeadBarBorder - getGuiTop()) / 2), Color.BLACK.getRGB(), 0.8f * getScaleFactor());
    }

    private class GuiSpellStateList extends VerticalScrollingList {
        public GuiSpellStateList(Minecraft client, int width, int height, int left, int top, int entryHeight) {
            super(client, width, height, top, left, entryHeight);
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
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected boolean isSelected(int index) {
            return index == selectedIndex;
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {

        }

        @Override
        protected void drawBackground() {
            getDelegate().drawImage(GuiResource.BACKGROUND_PLATING, this.left - getGuiLeft(), this.top - getGuiTop(), 0, 0, listWidth, listHeight);
        }

        @Override
        protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
            int i = 0;
            if (isMouseOnSlot(slotIdx)) {
                i += 20;
            }
            int halfWidth = (entryRight - left) / 2;
            //int relEntryRight = entryRight-getGuiLeft();//relEntryRight-(listWidth-getBorder()*2)
            getDelegate().drawImage(BUTTON_IMAGE, left - getGuiLeft(), slotTop - getGuiTop(), 0, 66 + i, halfWidth, slotSize); //first Half
            getDelegate().drawImage(BUTTON_IMAGE, left - getGuiLeft() + halfWidth, slotTop - getGuiTop(), 200 - halfWidth, 66 + i, halfWidth, slotSize); //secondHalf
            String name = mSpellBuilder.getStateNames().get(slotIdx);
            getDelegate().drawScaledStringCentered(name, entryRight - halfWidth, slotTop + Math.round(slotSize / 2), Color.BLACK.getRGB(), 0.4f * getScaleFactor());
        }
    }
}
