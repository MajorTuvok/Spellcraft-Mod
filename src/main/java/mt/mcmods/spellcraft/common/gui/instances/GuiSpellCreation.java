package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.components.ListControllerStub;
import mt.mcmods.spellcraft.common.gui.components.SimpleTextAdapter;
import mt.mcmods.spellcraft.common.gui.components.VerticalScrollingList;
import mt.mcmods.spellcraft.common.gui.helper.GuiResource;
import mt.mcmods.spellcraft.common.spell.SpellState;
import mt.mcmods.spellcraft.common.spell.entity.PlayerSpellBuilder;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiSpellCreation extends BaseGui {
    private static final ResourceLocation BUTTON_IMAGE = new ResourceLocation("textures/gui/widgets.png");
    private static final int DEFAULT_SELECTED_INDEX = 0;
    private static final int SCROLLBAR_SIZE = 10;
    private static final float HEIGHT_HEADBAR = 10;
    private static final int HEIGHT_ROW = 20;
    private static final float HEIGTH_CONTENT = 100 - HEIGHT_HEADBAR;
    private static final int TITLE_COLOR = 0x99AAAAAA;
    private static final GuiResource USED_BACKGROUND = GuiResource.GUI_COLOR;
    private static final int SIZE = 512;
    private static final float WIDTH_CONDITIONS = 25;
    private static final float WIDTH_NEXT_STATE = 10;
    private static final float WIDTH_STATES = 12.5f;
    private static final float WIDTH_EXECUTABLES = 100 - WIDTH_CONDITIONS - WIDTH_NEXT_STATE - WIDTH_STATES;
    private static final float WIDTH_STATE_LIST = 100 - WIDTH_STATES;
    private GuiSpellCreator mCreator;
    private int mHeadBarBorder;
    private SimpleTextAdapter mCurrentStateAdapter;
    private VerticalScrollingList mCurrentStateList;
    private SimpleTextAdapter mSpellStateAdapter;
    private VerticalScrollingList mSpellStateList;
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
        mSpellStateAdapter = new SimpleTextAdapter(Collections.emptyList());
    }

    @Override
    public TileEntitySpellCreator getTileEntity() {
        return (TileEntitySpellCreator) super.getTileEntity();
    }

    private String getSelectedState() {
        return mSpellStateAdapter.getData(mSpellStateList.getSelectedIndex());
    }

    @Override
    public void initGui() {
        super.initGui();
        mHeadBarBorder = asAbsoluteYPos(HEIGHT_HEADBAR);
        createSpellStateList();
        createCurrentStateList();
    }

    @Override
    protected void onShouldCloseScreen() {
        Minecraft.getMinecraft().displayGuiScreen(mCreator);
    }

    @Override
    protected void createInventoryViews(BaseGuiContainer inventorySlotsIn) {

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
        getDelegate().drawGuiBackground(USED_BACKGROUND);
        mSpellStateList.drawScreen(mouseX, mouseY, partialTicks);
        mCurrentStateList.drawScreen(mouseX, mouseY, partialTicks);
        drawTitleBar();
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     *
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mSpellStateList.onMouseClick(mouseX, mouseY, mouseButton);
        mCurrentStateList.onMouseClick(mouseX, mouseY, mouseButton);
    }

    private void createSpellStateList() {
        mSpellStateList = new VerticalScrollingList(getMc(), asAbsoluteXDis(WIDTH_STATES), asAbsoluteYDis(HEIGTH_CONTENT) + 1, mHeadBarBorder, getGuiLeft() + 1, HEIGHT_ROW);
        mSpellStateList.setSelectedIndex(0);
        mSpellStateList.setScrollbarSize(10);
        List<String> spellNames = mSpellBuilder.getStateNames();

        mSpellStateAdapter = new SimpleTextAdapter(spellNames);
        int buttonLeft = mSpellStateList.getLeft() + mSpellStateList.getSlotWidth();

        mSpellStateList.init(mSpellStateAdapter, new ListControllerStub());
    }

    private void createCurrentStateList() {
        mCurrentStateList = new VerticalScrollingList(getMc(), asAbsoluteXDis(WIDTH_STATE_LIST), asAbsoluteYDis(HEIGTH_CONTENT) + 1, mHeadBarBorder, getGuiLeft() + asAbsoluteXDis(WIDTH_STATES) + 1, HEIGHT_ROW);
        mCurrentStateList.setSelectedIndex(DEFAULT_SELECTED_INDEX);
        mCurrentStateList.setScrollbarSize(SCROLLBAR_SIZE);
        String state = getSelectedState();
        List<String> names = new ArrayList<>();
        if (mSpellBuilder.hasState(state)) {
            List<SpellState.StateList> stateLists = mSpellBuilder.getStateLists(state);
            for (SpellState.StateList list :
                    stateLists) {
                names.add("a state list");
            }
        } else {
            Log.warn("Failed to retrieve State!");
        }
        for (int i = names.size(); i <= 50; i++) {
            names.add("" + i);
        }
        mCurrentStateAdapter = new SimpleTextAdapter(names);
        int buttonLeft = mCurrentStateList.getLeft() + mCurrentStateList.getSlotWidth();
        mCurrentStateList.init(mCurrentStateAdapter, new ListControllerStub());
    }

    private void drawTitleBar() {
        drawRectangle(getGuiLeft(), getGuiTop(), getGuiLeft() + getXSize(), mHeadBarBorder, TITLE_COLOR);
        String name = getTileEntity().getSpellName();
        getDelegate().drawScaledStringCenteredHorizontally(name, getGuiLeft() + Math.round(getXSize() / 2), mHeadBarBorder - Math.round((mHeadBarBorder - getGuiTop()) / 2), Color.BLACK.getRGB(), 0.8f * getScaleFactor());
    }

}
