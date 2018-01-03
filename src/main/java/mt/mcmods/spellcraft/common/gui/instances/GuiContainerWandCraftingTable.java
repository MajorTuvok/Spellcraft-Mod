package mt.mcmods.spellcraft.common.gui.instances;

import mt.mcmods.spellcraft.common.gui.BaseGuiContainer;
import mt.mcmods.spellcraft.common.gui.helper.PlayerInventoryOffsets;
import mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiContainerWandCraftingTable extends BaseGuiContainer {
    private static final int CRAFTING_Y_POS = 10;
    private static final int MIN_SLOT_DIS = 2;
    static final int CRAFTING_Y_0 = CRAFTING_Y_POS;
    static final PlayerInventoryOffsets OFFSETS = new PlayerInventoryOffsets(10, 84, 10, 142);  //TODO align properly
    private static final int CRAFTING_X_POS = OFFSETS.getInnerXInvOffset();
    private static final int CRAFTING_X_0 = CRAFTING_X_POS;
    private static final int CRAFTING_X_1 = CRAFTING_X_POS + Math.round(OFFSETS.getSlotXSize() * 0.5f) + MIN_SLOT_DIS;
    static final int CRAFTING_Y_1 = CRAFTING_Y_POS + OFFSETS.getSlotYSize() + MIN_SLOT_DIS;
    private static final int RESULT_Y_POS = CRAFTING_Y_1;
    static final int CRAFTING_Y_2 = CRAFTING_Y_POS + OFFSETS.getSlotYSize() * 2 + MIN_SLOT_DIS * 2;
    static final int CRAFTING_X_2 = CRAFTING_X_POS + OFFSETS.getSlotXSize() + MIN_SLOT_DIS * 2;
    private static final int RESULT_X_POS = CRAFTING_X_POS + OFFSETS.getSlotXSize() * 8;

    public GuiContainerWandCraftingTable(@Nonnull InventoryPlayer playerInv, @Nonnull TileEntityWandCraftingTable entity, @Nullable EnumFacing facing) {
        super(playerInv, entity, OFFSETS, facing);
    }

    @Override
    protected void createInventoryFromCapability(IItemHandler handler) {
        addSlotToContainer(new SlotItemHandler(handler, TileEntityWandCraftingTable.INVENTORY_STACK_TIP, CRAFTING_X_1, CRAFTING_Y_0));
        addSlotToContainer(new SlotItemHandler(handler, TileEntityWandCraftingTable.INVENTORY_STACK_WOOD, CRAFTING_X_0, CRAFTING_Y_1));
        addSlotToContainer(new SlotItemHandler(handler, TileEntityWandCraftingTable.INVENTORY_STACK_CORE, CRAFTING_X_2, CRAFTING_Y_1));
        addSlotToContainer(new SlotItemHandler(handler, TileEntityWandCraftingTable.INVENTORY_STACK_CRYSTAL, CRAFTING_X_1, CRAFTING_Y_2));
        addSlotToContainer(new SlotItemHandler(handler, TileEntityWandCraftingTable.INVENTORY_STACK_WAND, RESULT_X_POS, RESULT_Y_POS));
    }

    @Override
    public TileEntityWandCraftingTable getEntity() {
        return (TileEntityWandCraftingTable) super.getEntity();
    }
}
