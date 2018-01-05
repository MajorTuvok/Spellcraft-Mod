package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.helper.GuiResource;
import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import net.minecraft.entity.player.InventoryPlayer;

import javax.annotation.Nonnull;

public final class GuiContainerVoid extends BaseGuiContainer {
    private static final PlayerInventoryOffsets OFFSETS = GuiResource.GUI_BLANK_WPI.getSuggestedOffsets();

    public GuiContainerVoid(@Nonnull InventoryPlayer playerInv) {
        super(playerInv, null, OFFSETS, null);
    }


}
