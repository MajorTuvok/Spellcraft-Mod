package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.Capabilities.wandproperties.WandPropertyDefinition;
import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;

import java.awt.*;

import static mt.mcmods.spellcraft.common.LocaleKey.*;
import static mt.mcmods.spellcraft.common.gui.helper.GuiResources.GUI_BLANK;
import static mt.mcmods.spellcraft.common.gui.helper.GuiResources.SLOT;
import static mt.mcmods.spellcraft.common.gui.instances.GuiContainerWandCraftingTable.*;

public class GuiWandCraftingTable extends BaseGui {
    private TileEntityWandCraftingTable entityWandCraftingTable;
    private static final int CRAFTING_OFFSET = 4;
    private static final int INFO_OFFSET = Math.round((float) (CRAFTING_Y_2 - CRAFTING_Y_1) / 2);
    private static final float TEXT_SCALE = 0.4f;
    private static final int TEXT_START_X = CRAFTING_X_2 + OFFSETS.getSlotXSize() + CRAFTING_OFFSET;
    private static final int TEXT_START_Y_1 = CRAFTING_Y_0;
    private static final int TEXT_START_Y_2 = CRAFTING_Y_1;

    public GuiWandCraftingTable(InventoryPlayer inventoryPlayer, GuiContainerWandCraftingTable inventorySlotsIn) {
        this(inventoryPlayer, inventorySlotsIn, inventorySlotsIn.getEntity());
    }

    public GuiWandCraftingTable(InventoryPlayer inventoryPlayer, GuiContainerWandCraftingTable inventorySlotsIn, TileEntityWandCraftingTable entity) {
        super(inventoryPlayer, inventorySlotsIn, GUI_BLANK.getImgXSize(), GUI_BLANK.getImgYSize());
        guiTop = 10;
        entityWandCraftingTable = entity;
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
        getDelegate().drawGuiBackground(GUI_BLANK);
        getDelegate().drawAllSlotsWithResource(SLOT);
        drawInformation();
    }

    private void drawInformation() {
        if (!entityWandCraftingTable.getWandCraftingStack().isEmpty()) {
            Item wandItem = entityWandCraftingTable.getWandCraftingStack().getItem();
            if (wandItem instanceof ItemWand) {
                WandPropertyDefinition definition = ((ItemWand) wandItem).getDefinition();
                revertTranslateForeground();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableRescaleNormal();
                getDelegate().drawScaledStringTopLeft(GUI_WAND_CRAFTING_TABLE_WAND_PROPS_TITLE.get(getLocalizedName(wandItem)),
                        TEXT_START_X, TEXT_START_Y_1, Color.BLACK.getRGB(), TEXT_SCALE * getScaledResolution().getScaleFactor() * 1.25f);
                getDelegate().drawScaledStringTopLeft(GUI_WAND_CRAFTING_TABLE_WAND_PROPS_INFO_STANDARD_0.get(
                        Math.round(definition.getMinEfficiency()), Math.round(definition.getMaxEfficiency())),
                        TEXT_START_X, TEXT_START_Y_2, Color.BLACK.getRGB(), TEXT_SCALE * getScaledResolution().getScaleFactor());
                getDelegate().drawScaledStringTopLeft(GUI_WAND_CRAFTING_TABLE_WAND_PROPS_INFO_STANDARD_1.get(
                        Math.round(definition.getMinMaxPower()), Math.round(definition.getMaxMaxPower())),
                        TEXT_START_X, TEXT_START_Y_2 + INFO_OFFSET, Color.BLACK.getRGB(), TEXT_SCALE * getScaledResolution().getScaleFactor());
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableDepth();
                GlStateManager.enableLighting();
                translateForeground();
            }
        }
    }
}
