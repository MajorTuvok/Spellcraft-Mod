package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.helper.GuiResource;
import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiContainerSpellCreation extends GuiContainerSpellCreator {
    private static final PlayerInventoryOffsets OFFSETS = GuiResource.GUI_BLANK_MAX.getSuggestedOffsets();

    public GuiContainerSpellCreation(GuiContainerSpellCreator containerSpellCreator) {
        this(containerSpellCreator.getPlayerInventory(), containerSpellCreator.getTileEntity());
    }

    public GuiContainerSpellCreation(@Nonnull InventoryPlayer playerInv, @Nullable TileEntitySpellCreator entity) {
        super(playerInv, entity, OFFSETS, null);
    }

    @Override
    protected void createInventoryFromCapability(IItemHandler handler) {

    }

    @Override
    protected void createPlayerInventory(InventoryPlayer playerInv, PlayerInventoryOffsets offsets) {

    }
}
