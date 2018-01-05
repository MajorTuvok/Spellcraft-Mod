package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.helper.GuiResources;
import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GUIContainerSpellCreator extends BaseGuiContainer {
    private static final PlayerInventoryOffsets OFFSETS = GuiResources.GUI_BLANK.getSuggestedOffsets();

    public GUIContainerSpellCreator(@Nonnull InventoryPlayer playerInv, @Nonnull TileEntitySpellCreator entity, @Nullable EnumFacing facing) {
        super(playerInv, entity, OFFSETS, facing);
    }

    @Override
    protected void createInventoryFromCapability(IItemHandler handler) {
        addSlotToContainer(new SlotItemHandler(handler, 0, 10, 10));
        addSlotToContainer(new SlotItemHandler(handler, 1, 26, 10));
    }

    @Override
    public TileEntitySpellCreator getTileEntity() {
        return (TileEntitySpellCreator) super.getTileEntity();
    }
}
