package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.helper.GuiResources;
import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import net.minecraft.entity.player.InventoryPlayer;

import javax.annotation.Nonnull;

public class VoidGuiContainer extends BaseGuiContainer {
    private static final PlayerInventoryOffsets OFFSETS = GuiResources.GUI_BLANK_WPI.getSuggestedOffsets();

    public VoidGuiContainer(@Nonnull InventoryPlayer playerInv) {
        super(playerInv, null, OFFSETS, null);
    }


}
