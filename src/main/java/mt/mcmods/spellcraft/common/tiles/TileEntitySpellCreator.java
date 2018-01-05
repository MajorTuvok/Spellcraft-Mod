package mt.mcmods.spellcraft.common.tiles;

public class TileEntitySpellCreator extends BaseTileEntityWithInventory {
    public static final int INVENTORY_SIZE = 2;
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    public TileEntitySpellCreator() {
        super(INVENTORY_SIZE);
    }
}
