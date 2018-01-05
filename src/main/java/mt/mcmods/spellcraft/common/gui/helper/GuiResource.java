package mt.mcmods.spellcraft.common.gui.helper;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate.ResourceInfo;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public enum GuiResource implements ResourceInfo, ILoggable {
    SLOT(StringHelper.createResourceLocation(MODID, "textures", "gui", "elements", "slot.png"), 0, 0, 18, 18),
    SLOT_DARK(StringHelper.createResourceLocation(MODID, "textures", "gui", "elements", "slot_dark.png"), 0, 0, 18, 18),
    GUI_BLANK(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank.png"), 0, 0, 176, 166, new PlayerInventoryOffsets(8, 84, 8, 142)),
    GUI_BLANK_WPI(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_with_player_inv.png"), 0, 0, 176, 166, new PlayerInventoryOffsets(8, 84, 8, 142)),
    GUI_BLANK_BIG(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_big.png"), 0, 0, 176, 222),
    GUI_BLANK_GIANT(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_giant.png"), 0, 0, 256, 222),
    GUI_BLANK_MAX(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_max.png"), 0, 0, 256, 256, new PlayerInventoryOffsets(44, 172, 44, 230)),
    GUI_BLANK_BIG_WPI(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_big_with_player_inv.png"), 0, 0, 176, 222),
    OVERLAY_SPELLPOWER_BACKGROUND(StringHelper.createResourceLocation(MODID, "textures", "gui", "overlay", "spellpower_background.png"), 0, 0, 19, 60),
    OVERLAY_SPELLPOWER_FOREGROUND(StringHelper.createResourceLocation(MODID, "textures", "gui", "overlay", "spellpower_foreground.png"), 0, 0, 19, 60),
    BOOK_AND_QUILL(StringHelper.createResourceLocation(MODID, "textures", "gui", "book_and_quill.png"), 0, 0, 21, 21);
    ResourceLocation location;
    private int imgXStart;
    private int imgYStart;
    private int imgXSize;
    private int imgYSize;
    private PlayerInventoryOffsets suggestedOffsets;

    GuiResource(@Nonnull ResourceLocation location, int imgXStart, int imgYStart, int imgXSize, int imgYSize, PlayerInventoryOffsets playerInventoryOffsets) {
        this.location = location;
        this.imgXStart = imgXStart;
        this.imgYStart = imgYStart;
        this.imgXSize = imgXSize;
        this.imgYSize = imgYSize;
        this.suggestedOffsets = playerInventoryOffsets;
    }

    GuiResource(ResourceLocation location, int imgXStart, int imgYStart, int imgXSize, int imgYSize) {
        this(location, imgXStart, imgYStart, imgXSize, imgYSize, null);
    }

    GuiResource(String location, int imgXStart, int imgYStart, int imgXSize, int imgYSize, PlayerInventoryOffsets playerInventoryOffsets) {
        this(new ResourceLocation(location), imgXStart, imgYStart, imgXSize, imgYSize, playerInventoryOffsets);
    }

    GuiResource(String location, int imgXStart, int imgYStart, int imgXSize, int imgYSize) {
        this(new ResourceLocation(location), imgXStart, imgYStart, imgXSize, imgYSize);
    }

    @Override
    public @Nonnull
    ResourceLocation getResource() {
        return location;
    }

    @Override
    public int getImgXStart() {
        return imgXStart;
    }

    @Override
    public int getImgYStart() {
        return imgYStart;
    }

    @Override
    public int getImgXSize() {
        return imgXSize;
    }

    @Override
    public int getImgYSize() {
        return imgYSize;
    }

    public PlayerInventoryOffsets getSuggestedOffsets() {
        assert suggestedOffsets != null : "attempted to retrieve null Inventory Offsets!";
        return suggestedOffsets;
    }

    @Override
    public String toString() {
        return "GuiResource{" + "location=" + location +
                ", imgXStart=" + imgXStart +
                ", imgYStart=" + imgYStart +
                ", imgXSize=" + imgXSize +
                ", imgYSize=" + imgYSize +
                '}';
    }
}
