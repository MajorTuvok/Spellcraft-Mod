package mt.mcmods.spellcraft.common.gui.helper;

public final class PlayerInventoryOffsets {
    public static final int DEFAULT_COLUMN_COUNT_INV_BAR = 9;
    public static final int DEFAULT_COLUMN_COUNT_INNER = 9;
    public static final int DEFAULT_ROW_COUNT_INNER = 3;
    public static final int DEFAULT_SLOT_X_SIZE = 18;
    public static final int DEFAULT_SLOT_Y_SIZE = 18;
    private final int mmInnerXInvOffset;
    private final int mmInnerYInvOffset;
    private final int mmInvBarXOffset;
    private final int mmInvBarYOffset;
    private final int mmInnerRowCount;
    private final int mmInnerColumnCount;
    private final int mmInvBarColumnCount;
    private final int mmSlotXSize;
    private final int mmSlotYSize;

    public PlayerInventoryOffsets(int innerXInvOffset, int innerYInvOffset, int invBarXOffset, int invBarYOffset, int innerRowCount, int innerColumnCount, int invBarColumnCount, int slotXSize, int slotYSize) {
        this.mmInnerXInvOffset = innerXInvOffset;
        this.mmInnerYInvOffset = innerYInvOffset;
        this.mmInvBarXOffset = invBarXOffset;
        this.mmInvBarYOffset = invBarYOffset;
        this.mmInnerRowCount = innerRowCount;
        this.mmInnerColumnCount = innerColumnCount;
        this.mmInvBarColumnCount = invBarColumnCount;
        this.mmSlotXSize = slotXSize;
        this.mmSlotYSize = slotYSize;
    }

    public PlayerInventoryOffsets(int innerXInvOffset, int innerYInvOffset, int invBarXOffset, int invBarYOffset) {
        this(innerXInvOffset, innerYInvOffset, invBarXOffset, invBarYOffset,
                DEFAULT_ROW_COUNT_INNER, DEFAULT_COLUMN_COUNT_INNER, DEFAULT_COLUMN_COUNT_INV_BAR, DEFAULT_SLOT_X_SIZE, DEFAULT_SLOT_Y_SIZE);
    }

    public int getInnerXInvOffset() {
        return mmInnerXInvOffset;
    }

    public int getInnerYInvOffset() {
        return mmInnerYInvOffset;
    }

    public int getInvBarXOffset() {
        return mmInvBarXOffset;
    }

    public int getInvBarYOffset() {
        return mmInvBarYOffset;
    }

    public int getInnerRowCount() {
        return mmInnerRowCount;
    }

    public int getInnerColumnCount() {
        return mmInnerColumnCount;
    }

    public int getInvBarColumnCount() {
        return mmInvBarColumnCount;
    }

    public int getSlotXSize() {
        return mmSlotXSize;
    }

    public int getSlotYSize() {
        return mmSlotYSize;
    }

    @Override
    public String toString() {
        return "PlayerInventoryOffsets{" + "mmInnerXInvOffset=" + mmInnerXInvOffset +
                ", mmInnerYInvOffset=" + mmInnerYInvOffset +
                ", mmInvBarXOffset=" + mmInvBarXOffset +
                ", mmInvBarYOffset=" + mmInvBarYOffset +
                ", mmInnerRowCount=" + mmInnerRowCount +
                ", mmInnerColumnCount=" + mmInnerColumnCount +
                ", mmInvBarColumnCount=" + mmInvBarColumnCount +
                ", mmSlotXSize=" + mmSlotXSize +
                ", mmSlotYSize=" + mmSlotYSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerInventoryOffsets)) return false;

        PlayerInventoryOffsets that = (PlayerInventoryOffsets) o;

        if (mmInnerXInvOffset != that.mmInnerXInvOffset) return false;
        if (mmInnerYInvOffset != that.mmInnerYInvOffset) return false;
        if (mmInvBarXOffset != that.mmInvBarXOffset) return false;
        if (mmInvBarYOffset != that.mmInvBarYOffset) return false;
        if (mmInnerRowCount != that.mmInnerRowCount) return false;
        if (mmInnerColumnCount != that.mmInnerColumnCount) return false;
        if (mmInvBarColumnCount != that.mmInvBarColumnCount) return false;
        if (mmSlotXSize != that.mmSlotXSize) return false;
        return mmSlotYSize == that.mmSlotYSize;
    }

    @Override
    public int hashCode() {
        int result = mmInvBarXOffset;
        result = 31 * result + mmInvBarYOffset;
        result = 31 * result + mmInnerRowCount;
        result = 31 * result + mmInnerColumnCount;
        result = 31 * result + mmInvBarColumnCount;
        return result;
    }
}
