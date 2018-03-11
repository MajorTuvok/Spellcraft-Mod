package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGui;
import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static mt.mcmods.spellcraft.common.gui.helper.GuiResource.GUI_BLANK_MAX;

public class GuiSpellCreation extends BaseGui {
    public GuiSpellCreation(GuiContainerSpellCreator inventorySlotsIn) {
        super(inventorySlotsIn, GUI_BLANK_MAX.getImgXSize(), GUI_BLANK_MAX.getImgYSize());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiOpenEvent(GuiOpenEvent event) {
        if (event.getGui() == null && Minecraft.getMinecraft().currentScreen instanceof GuiSpellCreation) {
            event.setGui(new GuiSpellCreator(new GuiContainerSpellCreator((GuiContainerSpellCreator) inventorySlots)));
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        MinecraftForge.EVENT_BUS.unregister(this);
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
    protected void createInventoryViews(BaseGuiContainer inventorySlotsIn) {

    }
}
